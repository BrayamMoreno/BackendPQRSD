package com.pqrsdf.pqrsdf.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.utils.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserDetailServiceImpl(UsuarioService usuarioService, PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) {
        Usuario usuario = usuarioService.getEntityByCorreo(correo);
        if (usuario == null) {
            throw new BadCredentialsException("Usuario no encontrado");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Rol principal
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()));

        // Permisos del rol
        usuario.getRol().getPermisos().forEach(permiso -> {
            authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_" + permiso.getAccion()));
        });

        // ✅ Usa el constructor correcto
        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                usuario.getIsEnable(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }

    public Authentication authentication(String correo, String password) {
        UserDetails userDetails = loadUserByUsername(correo);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Correo o contraseña incorrectos");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Usuario usuario = usuarioService.getEntityByCorreo(loginRequest.correo());
        if (usuario == null) {
            throw new BadCredentialsException("Usuario no encontrado");
        }

        Authentication authentication = this.authentication(loginRequest.correo(), loginRequest.contrasena());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(
                usuario.getCorreo(),
                "Usuario autenticado correctamente",
                accessToken,
                usuario,
                usuario.getPersona(),
                true);
    }

}
