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
import com.pqrsdf.pqrsdf.utils.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
   
    private final UsuariosService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PersonasService personasService;

    public UserDetailServiceImpl(UsuariosService usuarioService, PasswordEncoder passwordEncoder,
                                JwtUtils jwtUtils, PersonasService personasService){
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.personasService = personasService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Usuario usuario = usuarioService.getEntityByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(usuario.getRol().getNombre())));
        
        usuario.getRol().getPermisos().forEach(permiso -> {
            if (permiso.isLeer()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_READ"));
            if (permiso.isAgregar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_CREATE"));
            if (permiso.isModificar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_UPDATE"));
            if (permiso.isEliminar()) authorities.add(new SimpleGrantedAuthority(permiso.getTabla() + "_DELETE"));
        });

        return new User(
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.isEnabled(),
            usuario.isAccountNoExpired(),
            usuario.isCredentialNoExpired(),
            usuario.isAccountNoLocked(),
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

        Usuario usuario = usuarioService.getEntityByUsername(loginRequest.username());

        if(usuario == null){
            throw new BadCredentialsException("");
        }

        Authentication authentication = this.authentication(loginRequest.username(), loginRequest.password());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String AccesToken = jwtUtils.createToken(authentication);

        return new AuthResponse(usuario.getUsername(), "User Logged", AccesToken, true);
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
