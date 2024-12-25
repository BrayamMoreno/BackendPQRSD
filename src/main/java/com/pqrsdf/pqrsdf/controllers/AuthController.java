package com.pqrsdf.pqrsdf.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshRequest;
import com.pqrsdf.pqrsdf.dto.auth.logoutRequest;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.service.TokenService;
import com.pqrsdf.pqrsdf.service.UserDetailServiceImpl;
import com.pqrsdf.pqrsdf.service.UsuariosService;
import com.pqrsdf.pqrsdf.utils.JwtUtils;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@Tag(name = "Autenticacion", description = "Gestion de Sesiones y Usuarios")
public class AuthController {

    private final UsuariosService usuariosService;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;

    public AuthController(UsuariosService usuarioService, UserDetailServiceImpl userDetailServiceImpl,
            JwtUtils jwtUtils, TokenService tokenService) {
        this.usuariosService = usuarioService;
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest entity) {
        try {
            Usuario usuario = usuariosService.getEntityByUsername(entity.username());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse(null, "Usuario y/o contraseña incorrectos", null, false));
            }
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.login(entity));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> Logout(@RequestBody logoutRequest entity,HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            tokenService.revokeToken(jwtToken, jwtUtils.extracExpirationTime(jwtToken));
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(null, "Sesion cerrada", null, false));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(null, "No se pudo cerrar la sesion", null, false));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.refreshSession(request));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

}
