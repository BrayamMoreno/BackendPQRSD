package com.pqrsdf.pqrsdf.service;

import java.util.ArrayList;
import java.util.List;

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

import com.auth0.jwt.interfaces.DecodedJWT;

import com.pqrsdf.pqrsdf.dto.auth.AuthResponse;
import com.pqrsdf.pqrsdf.dto.auth.LoginRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshRequest;
import com.pqrsdf.pqrsdf.dto.auth.RefreshResponse;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.PersonaRepository;
import com.pqrsdf.pqrsdf.utils.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
   
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PersonaRepository personasRepository;

    public UserDetailServiceImpl(UsuarioService usuarioService, PasswordEncoder passwordEncoder,
                                JwtUtils jwtUtils, PersonaRepository personasRepository){
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.personasRepository = personasRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String Correo){
        Usuario usuario = usuarioService.getEntityByCorreo(Correo);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(usuario.getRol().getNombre())));
        
        usuario.getRol().getPermisos().forEach(permiso -> {
            if (permiso.getLeer()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_READ"));
            if (permiso.getAgregar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_CREATE"));
            if (permiso.getModificar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_UPDATE"));
            if (permiso.getEliminar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_DELETE"));
        });

        return new User(
            usuario.getCorreo(),
            usuario.getContrasena(),
            usuario.getIsEnable(),
            usuario.getAccountNoExpired(),
            usuario.getCredentialNoExpired(),
            usuario.getAccountNoLocked(),
            authorities
        );
    }

    public Authentication authentication(String correo, String password){
        UserDetails userDetails = loadUserByUsername(correo);

        if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("");
        }
        return new UsernamePasswordAuthenticationToken(correo, password, userDetails.getAuthorities());
    }

    public AuthResponse login(LoginRequest loginRequest){

        Usuario usuario = usuarioService.getEntityByCorreo(loginRequest.correo());

        if(usuario == null){
            throw new BadCredentialsException("");
        }

        Authentication authentication = this.authentication(loginRequest.correo(), loginRequest.contrasena());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String AccesToken = jwtUtils.createToken(authentication);

        return new AuthResponse(usuario.getCorreo(),
                        "User Logged",
                                AccesToken,
                                usuario,
                                usuario.getPersona(),
                                true);
    }

    public RefreshResponse refreshSession (RefreshRequest request){

        DecodedJWT jwt = jwtUtils.validateToken(request.expiredJwt());

        String username = jwtUtils.extractUsername(jwt);
        UserDetails userDetails = loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(), null, userDetails.getAuthorities()
        );

        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        return new RefreshResponse(refreshToken);
    }
}
