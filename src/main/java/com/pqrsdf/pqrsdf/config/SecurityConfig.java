package com.pqrsdf.pqrsdf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pqrsdf.pqrsdf.components.AuditLoggingFilter;
import com.pqrsdf.pqrsdf.repository.AuditLogRepository;
import com.pqrsdf.pqrsdf.service.TokenService;
import com.pqrsdf.pqrsdf.utils.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig extends SecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    public SecurityConfig(JwtUtils jwtUtils, TokenService tokenService) {
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                               AuditLoggingFilter auditLoggingFilter) throws Exception {
    return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/api/generos").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/municipios/municipios_departamento").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/departamentos").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/tipos_documentos").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/tipos_personas").permitAll()
                    .requestMatchers(
                            "/api/auth/login",
                            "/api/auth/logout",
                            "/api/auth/register",
                            "/api/auth/renew",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/webjars/**",
                            "/api/auth/forgot-password/**",
                            "/api/auth/reset-password")
                    .permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Registra el validador JWT antes del filtro estándar de autenticación
            .addFilterBefore(new JwtTokenValidator(jwtUtils, tokenService),
                    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

            // Registra tu PermissionFilter después del JWT
            .addFilterAfter(new PermissionFilter(),
                    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

            //Registra el filtro de auditoría al final
            .addFilterAfter(auditLoggingFilter,
                    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

            .build();
}


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditLoggingFilter auditLoggingFilter(AuditLogRepository auditLogRepository) {
        return new AuditLoggingFilter(auditLogRepository);
    }
}
