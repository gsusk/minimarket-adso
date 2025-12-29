package org.adso.minimarket.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {

    private final SecretKey accessTokenSecret;
    private final SecretKey refreshTokenSecret;
    private final long expirationSeconds;

    public JwtService(
            @Value("${application.security.access_token}") String accessTokenSecret,
            @Value("${application.security.refresh_token}") String refreshTokenSecret
    ) {
        this.accessTokenSecret = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        this.refreshTokenSecret = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        this.expirationSeconds = 60 * 2;
    }

    public String generateAccessToken(UserDetails user) {
        return createToken(user, accessTokenSecret, expirationSeconds);
    }

    public String generateRefreshToken(UserDetails user) {
        return createToken(user, refreshTokenSecret, expirationSeconds * 3);
    }

    public String extractUsername(String token) {
        return extractClaim(token, accessTokenSecret, Claims::getSubject);
    }

    public boolean isAccessTokenValid(String token, UserDetails user) {
        return isTokenValid(token, user, accessTokenSecret);
    }

    public String extractRefreshUsername(String token) {
        return extractClaim(token, refreshTokenSecret, Claims::getSubject);
    }

    public boolean isRefreshTokenValid(String token, UserDetails user) {
        return isTokenValid(token, user, refreshTokenSecret);
    }

    private String createToken(UserDetails user, SecretKey key, long duration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(duration)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private boolean isTokenValid(String token, UserDetails user, SecretKey key) {
        try {
            final String username = extractClaim(token, key, Claims::getSubject);
            return (username.equals(user.getUsername()) && !isTokenExpired(token, key));
        } catch (Exception e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, SecretKey key, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        Date expiration = extractClaim(token, key, Claims::getExpiration);
        return expiration.before(new Date());
    }
}