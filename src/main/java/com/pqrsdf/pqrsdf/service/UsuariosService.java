package com.pqrsdf.pqrsdf.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Roles;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.PersonasRepository;
import com.pqrsdf.pqrsdf.repository.RolesRepository;
import com.pqrsdf.pqrsdf.repository.UsuariosRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuariosService extends GenericService<Usuario, Long>{
    
    private final UsuariosRepository usuarioRepository;
    private final PersonasRepository personasRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuariosService(UsuariosRepository repository, PersonasRepository personasRepository,
                            RolesRepository rolesRepository, PasswordEncoder passwordEncoder){
        super(repository);
        this.usuarioRepository = repository;
        this.personasRepository = personasRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario getEntityByCorreo(String Correo){
        return usuarioRepository.findByCorreo(Correo).orElse(null);
    }


    @Transactional
    public Usuario createNewUser(RegisterRequest entity){
        
        usuarioRepository.findByCorreo(entity.correo()).ifPresent(u -> {
            throw new RuntimeException("El correo ya se encuentra registrado");
        });
        
        
        Personas persona = Personas.builder()
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
        
        Roles rol = rolesRepository.findById(new Long(1)).orElse(null);

        Usuario usuario = Usuario.builder()
                            .correo(entity.correo())
                            .contrasena(passwordEncoder.encode(entity.contraseña()))
                            .persona_id(persona.getId())
                            .rol(rol)
        .build();
        
        return usuarioRepository.save(usuario);
    }
}
