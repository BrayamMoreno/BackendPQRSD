package com.pqrsdf.pqrsdf.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class TokenService {
    
    private final Map<String, LocalDateTime> revokedTokens = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void revokeToken(String token, LocalDateTime expirationTime){
        revokedTokens.put(token, expirationTime);
    }

    public boolean isTokenRevoked(String token){
        return revokedTokens.containsKey(token);
    }

    @PostConstruct
    public void scheduleTokenCleanup(){
        scheduler.scheduleAtFixedRate(() -> {
            LocalDateTime now = LocalDateTime.now();
            revokedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
        }, 0 ,1 , TimeUnit.HOURS);
    }
}
