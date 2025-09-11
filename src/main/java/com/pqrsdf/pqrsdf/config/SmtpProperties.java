package com.pqrsdf.pqrsdf.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class SmtpProperties {

    private String host;
    private int port;
    private String username;
    private String password;

    public SmtpProperties(Dotenv dotenv) {
        this.host = dotenv.get("SMTP_HOST");
        this.port = Integer.parseInt(dotenv.get("SMTP_PORT"));
        this.username = dotenv.get("SMTP_USERNAME");
        this.password = dotenv.get("SMTP_PASSWORD");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
