package com.pqrsdf.pqrsdf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.minio.MinioClient;

@Configuration
public class MinioConfig {
    
    private String endpoint, accessKey, secretKey;

    public MinioConfig(Dotenv dotenv) {
        // Load configuration from environment variables or application properties
        this.endpoint = dotenv.get("MINIO_ENDPOINT");
        this.accessKey = dotenv.get("MINIO_ACCESS_KEY");
        this.secretKey = dotenv.get("MINIO_SECRET");
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
