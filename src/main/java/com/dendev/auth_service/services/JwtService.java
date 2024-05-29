package com.dendev.auth_service.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtService {
    @Value("${JWT_KEY}")
    private String secretKey;

    @Value("${JWT_EXP_TIME}")
    private long expirationTime;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return Jwts.builder()
                           .setSubject(username)
                           .setExpiration(Date.from(Instant.now().plus(expirationTime, ChronoUnit.MILLIS)))
                           .signWith(key())
                           .compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String getUsernameFromToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer")) {
            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                                     .setSigningKey(key())
                                     .build()
                                     .parseClaimsJws(token);

                return claims.getBody().getSubject();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}