package com.pqrsdf.pqrsdf.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshRequest;
import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.dto.auth.logoutRequest;
import com.pqrsdf.pqrsdf.exceptions.DniAlreadyExistsException;
import com.pqrsdf.pqrsdf.exceptions.EmailAlreadyExistsException;
import com.pqrsdf.pqrsdf.service.ResetPasswordService;
import com.pqrsdf.pqrsdf.service.TokenService;
import com.pqrsdf.pqrsdf.service.UserDetailServiceImpl;
import com.pqrsdf.pqrsdf.service.UsuarioService;
import com.pqrsdf.pqrsdf.utils.JwtUtils;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@Tag(name = "Autenticacion", description = "Gestion de Sesiones y Usuarios")
public class AuthController {

    private final UsuarioService usuariosService;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final ResetPasswordService resetPasswordService;

    public AuthController(UsuarioService usuarioService, UserDetailServiceImpl userDetailServiceImpl,
            JwtUtils jwtUtils, TokenService tokenService, ResetPasswordService resetPasswordService) {
        this.usuariosService = usuarioService;
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest entity, HttpServletResponse response) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.login(entity));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Credenciales incorrectas", null, null, null, false));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> Logout(@RequestBody logoutRequest entity, HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        String jwtToken = entity.tokenjwt();
        if (jwtToken != null) {
            tokenService.revokeToken(jwtToken, jwtUtils.extracExpirationTime(jwtToken));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AuthResponse(null, "Sesion cerrada", null, null, null, false));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, "No se pudo cerrar la sesion", null, null, null, false));
    }

    @PostMapping("/renew")
    public ResponseEntity<Map<String, String>> renewToken(@RequestBody RefreshRequest authHeader) {
        try {
            if (authHeader == null || !authHeader.authHeader().startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token inválido"));
            }

            String token = authHeader.authHeader().substring(7);

            DecodedJWT decodedJWT = jwtUtils.decodeTokenAllowExpired(token);
            String username = jwtUtils.extractUsername(decodedJWT);

            UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), null, userDetails.getAuthorities());

            String newToken = jwtUtils.createToken(authentication);

            return ResponseEntity.ok(Map.of("jwt", newToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No se pudo renovar el token"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest entity) {
        try {
            usuariosService.createNewUser(entity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Mensaje("Usuario registrado con éxito"));
        } catch (EmailAlreadyExistsException ex) {
            return ResponseEntityUtil.handleDuplicatedDataError(ex.getMessage());
        } catch (DniAlreadyExistsException ex) {
            return ResponseEntityUtil.handleDuplicatedDataError(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntityUtil.handleInternalError(ex);
        }
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        try {
            resetPasswordService.createPasswordResetToken(email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Mensaje("Instrucciones para restablecer la contraseña enviadas al correo electrónico"));
        } catch (Exception ex) {
            return ResponseEntityUtil.handleInternalError(ex);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");
            resetPasswordService.resetPassword(token, newPassword);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Mensaje("Contraseña restablecida con éxito"));
        } catch (Exception ex) {
            return ResponseEntityUtil.handleInternalError(ex);
        }
    }
}
