package com.pqrsdf.pqrsdf.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.dto.auth.RegisterRequest;
import com.pqrsdf.pqrsdf.exceptions.DniAlreadyExistsException;
import com.pqrsdf.pqrsdf.exceptions.EmailAlreadyExistsException;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Rol;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.GeneroRepository;
import com.pqrsdf.pqrsdf.repository.MunicipioRepository;
import com.pqrsdf.pqrsdf.repository.PersonaRepository;
import com.pqrsdf.pqrsdf.repository.RolRepository;
import com.pqrsdf.pqrsdf.repository.TipoDocRepository;
import com.pqrsdf.pqrsdf.repository.TipoPersonaRepository;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService extends GenericService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personasRepository;
    private final RolRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final TipoPersonaRepository tipoPersonaRepository;
    private final GeneroRepository generoRepository;
    private final TipoDocRepository tipoDocRepository;
    private final MunicipioRepository municipioRepository;

    public UsuarioService(UsuarioRepository repository, PersonaRepository personasRepository,
            RolRepository rolesRepository, PasswordEncoder passwordEncoder,
            TipoPersonaRepository tipoPersonaRepository,
            GeneroRepository generoRepository, TipoDocRepository tipoDocRepository,
            MunicipioRepository municipioRepository) {
        super(repository);
        this.usuarioRepository = repository;
        this.personasRepository = personasRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.tipoPersonaRepository = tipoPersonaRepository;
        this.generoRepository = generoRepository;
        this.tipoDocRepository = tipoDocRepository;
        this.municipioRepository = municipioRepository;
    }

    public Usuario getEntityByCorreo(String Correo) {
        return usuarioRepository.findByCorreo(Correo).orElse(null);
    }

    @Transactional
    public Usuario createEntity(Usuario usuario) {

        if(usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya se encuentra registrado");
        }

        if(personasRepository.findByDni(usuario.getPersona().getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya se encuentra registrado");
        }

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario createNewUser(RegisterRequest entity) {

        usuarioRepository.findByCorreo(entity.correo()).ifPresent(u -> {
            throw new EmailAlreadyExistsException("El correo ya se encuentra registrado");
        });

        personasRepository.findByDni(entity.dni()).ifPresent(p -> {
            throw new DniAlreadyExistsException("El Numero de documento ya se encuentra registrado");
        });

        Persona persona = Persona.builder()
                .nombre(entity.nombre())
                .apellido(entity.apellido())
                .tipoDoc(tipoDocRepository.findById(entity.tipoDocumento())
                        .orElseThrow(() -> new RuntimeException(
                                "Tipo de documento no encontrado")))
                .dni(entity.dni())
                .tipoPersona(tipoPersonaRepository.findById(entity.tipoPersona())
                        .orElseThrow(() -> new RuntimeException(
                                "Tipo de persona no encontrado")))
                .telefono(entity.telefono())
                .fechaNacimiento(entity.fechaNacimiento())
                .direccion(entity.direccion())
                .municipio(municipioRepository.findById(entity.municipioId())
                        .orElseThrow(() -> new RuntimeException("Municipio no encontrado")))
                .genero(generoRepository.findById(entity.genero())
                        .orElseThrow(() -> new RuntimeException("Género no encontrado")))
                .tratamientoDatos(entity.tratamientoDatos())
                .build();

        persona = personasRepository.save(persona);

        Rol rol = rolesRepository.findById(5L).orElse(null);

        Usuario usuario = Usuario.builder()
                .correo(entity.correo())
                .contrasena(passwordEncoder.encode(entity.contraseña()))
                .persona(persona)
                .isEnable(true)
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> findAll(Pageable pageable, Specification<Usuario> spec) {
        return usuarioRepository.findAll(spec, pageable);
    }

    public void disableAccount(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setIsEnable(false);
        usuarioRepository.save(usuario);
    }
}
