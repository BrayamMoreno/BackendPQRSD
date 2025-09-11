package com.pqrsdf.pqrsdf.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {

    private final SmtpProperties smtpProperties;

    public MailConfig(SmtpProperties smtpProperties) {
        this.smtpProperties = smtpProperties;
    }

    @Bean
    public JavaMailSender JavaMailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpProperties.getHost());
        mailSender.setPort(smtpProperties.getPort());
        mailSender.setUsername(smtpProperties.getUsername());
        mailSender.setPassword(smtpProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
