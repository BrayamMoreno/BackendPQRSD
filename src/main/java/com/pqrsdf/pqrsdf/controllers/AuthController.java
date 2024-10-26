package com.pqrsdf.pqrsdf.controllers;

import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.service.UserDetailServiceImpl;
import com.pqrsdf.pqrsdf.service.UsuarioService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Autenticacion", description = "Gestion de Sesiones y Usuarios")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UserDetailServiceImpl userDetailServiceImpl;

    public AuthController(UsuarioService usuarioService, UserDetailServiceImpl userDetailServiceImpl){
        this.usuarioService = usuarioService;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }
   
    @PostMapping("/Login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest entity){
        try{
            Usuario usuario = usuarioService.getEntityByUsername(entity.username());
            if(usuario == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(null, "Usuario y/o contraseña incorrectos", null, false));
            }
            return ResponseEntity.status(HttpStatus.OK).body(userDetailServiceImpl.login(entity));
        }catch(Exception e){
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping("/logout")
    public String Logout(){
        return null;
    }

}
