package com.pqrsdf.pqrsdf.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Rol;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.PersonaRepository;
import com.pqrsdf.pqrsdf.repository.RolRepository;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService extends GenericService<Usuario, Long>{
    
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personasRepository;
    private final RolRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(UsuarioRepository repository, PersonaRepository personasRepository,
                            RolRepository rolesRepository, PasswordEncoder passwordEncoder){
        super(repository);
        this.usuarioRepository = repository;
        this.personasRepository = personasRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario getEntityByCorreo(String Correo){
        return usuarioRepository.findByCorreo(Correo).orElse(null);
    }

/**
    @Transactional
    public Usuario createNewUser(RegisterRequest entity){
        
        usuarioRepository.findByCorreo(entity.correo()).ifPresent(u -> {
            throw new RuntimeException("El correo ya se encuentra registrado");
        });
        
        
        Persona persona = Persona.builder()
                            .nombre(entity.nombre())
                            .apellido(entity.apellido())
                            .tipoDoc(entity.tipoDocumento())
                            .dni(entity.dni())
                            .tipoPersona(entity.tipoPersona())
                            .telefono(entity.telefono())
                            .direccion(entity.direccion())
                            .municipioId(entity.municipioId())
                            .genero(entity.genero())
        .build();
        
        persona = personasRepository.save(persona);
        
        Rol rol = rolesRepository.findById(new Long(1)).orElse(null);

        Usuario usuario = Usuario.builder()
                            .correo(entity.correo())
                            .contrasena(passwordEncoder.encode(entity.contraseña()))
                            .personaId(persona.getId())
                            .rol(rol)
        .build();
        
        return usuarioRepository.save(usuario);
    }
    */
}
