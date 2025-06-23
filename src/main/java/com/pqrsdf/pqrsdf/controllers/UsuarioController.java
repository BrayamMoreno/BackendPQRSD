package com.pqrsdf.pqrsdf.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.UserRequest;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.service.RolService;
import com.pqrsdf.pqrsdf.service.UsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/usuarios")
@Tag(name = "Gestion de Usuarios")
public class UsuarioController extends GenericController<Usuario, Long>{

    private final PasswordEncoder passwordEncoder;
    private final RolService rolesService;

    public UsuarioController(UsuarioService service, PasswordEncoder passwordEncoder,
                            RolService rolesService){
        super(service);
        this.passwordEncoder = passwordEncoder;
        this.rolesService = rolesService;
    }


    /**
    @PostMapping("/user")
    public ResponseEntity<?> createEntity(@RequestBody UserRequest request){
        Usuario usuario = new Usuario();
        usuario.setUsername(request.correo());
        usuario.setContraseña(passwordEncoder.encode(request.contraseña()));
        usuario.setPersona_id(request.personaId());
        usuario.setRol(rolesService.getById(request.rolId()));
        return super.createEntity(usuario);
    }
    */
}
