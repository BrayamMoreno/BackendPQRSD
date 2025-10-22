package com.pqrsdf.pqrsdf.utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class JwtUtils {

    private String privateKey;
    private String userGenerator;

    private int accessTokenExpiration = 15 * 60 * 1000; // 15 min

    public JwtUtils(Dotenv dotenv) {
        this.privateKey = dotenv.get("JWT_PRIVATE_KEY_GENERATOR");
        this.userGenerator = dotenv.get("JWT_PRIVATE_USER_GENERATOR");
    }

    public String createToken(Authentication authentication) {
        Algorithm Algoritthm = Algorithm.HMAC256(this.privateKey);

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(authentication.getName())
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(Algoritthm);
        return jwtToken;
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT;
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Token Expirado", null);
        } catch (JWTVerificationException eJwt) {
            throw new JWTVerificationException("Token Invalido");
        } catch (Exception e) {
            throw new InternalError("Error: ".concat(e.getMessage()));
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    public LocalDateTime extracExpirationTime(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        Date expiredAt = decodedJWT.getExpiresAt();

        LocalDateTime expirationTime = LocalDateTime.ofInstant(expiredAt.toInstant(),
                expiredAt.toInstant().atZone(java.time.ZoneId.systemDefault()).getZone());
        return expirationTime;
    }

    public DecodedJWT decodeTokenAllowExpired(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (TokenExpiredException e) {
            return JWT.decode(token);
        } catch (JWTVerificationException eJwt) {
            throw new JWTVerificationException("Token inválido o manipulado");
        }
    }

}
