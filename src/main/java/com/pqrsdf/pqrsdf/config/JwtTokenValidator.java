package com.pqrsdf.pqrsdf.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pqrsdf.pqrsdf.service.TokenService;
import com.pqrsdf.pqrsdf.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    public JwtTokenValidator(JwtUtils jwtUtils, TokenService tokenService) {
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Quitar "Bearer " del token

            if (tokenService.isTokenRevoked(jwtToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token Revoked");
                return;
            }

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                String permisos = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
                Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(permisos);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        jwtUtils.extractUsername(decodedJWT), null, authorities);

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
            } catch (TokenExpiredException e) {
                // Deja pasar solicitudes al endpoint de renovación
                if (request.getRequestURI().equals("/api/auth/refresh-token")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // Respuesta para Access Token expirado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Token Expired");
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
