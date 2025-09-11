package com.pqrsdf.pqrsdf.service;

import java.io.File;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Usuario;

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

    public void sendEmailAdjuntos(Persona persona, String numeroRadicado, List<String> addresses, String subject, String body, List<File> attachments) {

        String nombre = persona.getNombre().concat(" ").concat(persona.getApellido());

        for (String address : addresses) {
            try {

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                Context context = new Context();
                context.setVariable("nombre", nombre);
                context.setVariable("radicado", numeroRadicado);
                context.setVariable("body", body);

                String html = templateEngine.process("email-template", context);

                helper.setTo(address);
                helper.setSubject(subject);
                helper.setText(html, true);

                for (File file : attachments) {
                    helper.addAttachment(file.getName(), file);
                }

                javaMailSender.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
