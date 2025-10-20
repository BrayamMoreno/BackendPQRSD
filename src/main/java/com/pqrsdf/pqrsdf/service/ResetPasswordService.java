package com.pqrsdf.pqrsdf.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

@Service
public class ResetPasswordService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ResetPasswordService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void createPasswordResetToken(String userEmail) {
        String token = UUID.randomUUID().toString();
        Usuario usuario = usuarioRepository.findByCorreo(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));

        usuarioRepository.save(usuario);
        emailService.sendEmailResetPassword(usuario.getCorreo(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token: " + token));

        if (usuario.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired: " + token);
        }

        usuario.setContrasena(passwordEncoder.encode(newPassword));

        usuario.setResetToken(null);
        usuario.setResetTokenExpiration(null);

        usuarioRepository.save(usuario);

    }

}
