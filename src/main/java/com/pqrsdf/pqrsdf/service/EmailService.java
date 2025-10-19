package com.pqrsdf.pqrsdf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pqrsdf.pqrsdf.models.Persona;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailAdjuntos(
            Persona persona,
            String numeroRadicado,
            List<String> addresses,
            String subject,
            List<File> attachments) {

        String nombre = persona.getNombre().concat(" ").concat(persona.getApellido());

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Generar contenido HTML con Thymeleaf
            Context context = new Context();
            context.setVariable("nombre", nombre);
            context.setVariable("radicado", numeroRadicado);
            String html = templateEngine.process("email-template", context);

            helper.setTo(addresses.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(html, true);

            // ✅ Agregar logo embebido (inline)
            ClassPathResource logo = new ClassPathResource("static/Logo.webp");
            helper.addInline("logoSTTG", logo);

            // ✅ Agregar archivos adjuntos
            for (File file : attachments) {
                helper.addAttachment(file.getName(), file);
            }

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo a " + addresses, e);
        }

    }
}
