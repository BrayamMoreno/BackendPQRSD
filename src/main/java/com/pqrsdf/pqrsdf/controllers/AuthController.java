package com.pqrsdf.pqrsdf.controllers;

import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshRequest;
import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.dto.auth.logoutRequest;
import com.pqrsdf.pqrsdf.exceptions.DniAlreadyExistsException;
import com.pqrsdf.pqrsdf.exceptions.EmailAlreadyExistsException;
import com.pqrsdf.pqrsdf.models.Usuario;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    public AuthController(UsuarioService usuarioService, UserDetailServiceImpl userDetailServiceImpl,
            JwtUtils jwtUtils, TokenService tokenService) {
        this.usuariosService = usuarioService;
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
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

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.refreshSession(request));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
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

}
