package com.pqrsdf.pqrsdf.controllers;

import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshRequest;
import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.dto.auth.logoutRequest;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.service.TokenService;
import com.pqrsdf.pqrsdf.service.UserDetailServiceImpl;
import com.pqrsdf.pqrsdf.service.UsuariosService;
import com.pqrsdf.pqrsdf.utils.JwtUtils;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public ResponseEntity<?> Login(@RequestBody LoginRequest entity, HttpServletResponse response) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.login(entity));
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Credenciales incorrectas", null, false));
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest entity) throws Exception {
        try {
            Usuario usuario = usuariosService.createNewUser(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Usuario registrado con exito"));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/test")
    public String getMethodName(@RequestParam(required = false) String param) {
        if (param == null) {
            throw new DataIntegrityViolationException("");
        }
        return "Valor recibido: " + param;
    }

    

}
