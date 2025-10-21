package com.pqrsdf.pqrsdf.service;

import java.io.File;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pqrsdf.pqrsdf.models.Persona;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final String urlResert;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.urlResert = Dotenv.load().get("URL_RESET_PASSWORD");
    }

    public void sendEmailAdjuntos(Persona persona, String numeroRadicado, List<String> addresses, String subject,
            List<File> attachments) {

        String nombre = persona.getNombre().concat(" ").concat(persona.getApellido());

        for (String address : addresses) {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                Context context = new Context();
                context.setVariable("nombre", nombre);
                context.setVariable("radicado", numeroRadicado);

                String html = templateEngine.process("email-template", context);

                helper.setTo(address);
                helper.setSubject(subject);
                helper.setText(html, true);

                for (File file : attachments) {
                    helper.addAttachment(file.getName(), file);
                }

                javaMailSender.send(message);

            } catch (Exception e) {
                throw new RuntimeException("Error al enviar correo a " + address, e);
            }
        }
    }

    public void sendEmailResetPassword(String to, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String resetLink = urlResert.concat(token);

            Context context = new Context();
            context.setVariable("resetLink", resetLink);

            String html = templateEngine.process("reset-password-template", context);

            helper.setTo(to);
            helper.setSubject("Restablecimiento de contraseña - Secretaría de Tránsito y Transporte de Girardot");
            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo de restablecimiento de contraseña a " + to, e);
        }
    }
}
