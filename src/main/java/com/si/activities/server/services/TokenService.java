package com.si.activities.server.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.si.activities.server.domain.User;

@Service
public class TokenService {

  @Value("${api.security.secret}")
  private String secret;

  private Instant generateExpiresAt() {
    return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
  }

  public String generateToken(User user) {
    Algorithm algorithm = Algorithm.HMAC256(secret);

    try {
      return JWT.create()
          .withIssuer("si-activities-api")
          .withSubject(user.getNickname())
          .withExpiresAt(generateExpiresAt())
          .sign(algorithm);
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token", e);
    }
  }

  public String validateToken(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    
    try {
      return JWT.require(algorithm)
          .withIssuer("si-activities-api")
          .build()
          .verify(token).getSubject();
    } catch (JWTVerificationException e) {
      return null;  
    }
  }
}