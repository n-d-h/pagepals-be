package com.pagepal.capstone.configurations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration.accessToken}")
    private String accessTokenExpirationTime;
    @Value("${jwt.expiration.refreshToken}")
    private String refreshTokenExpirationTime;
    private static final String DATA_FIELD = "username";

    public String extractDataFromToken(String token) {
        var claims = extractAllClaims(token);
        var username = claims.get(DATA_FIELD).toString();
        return username;
//        var userMap = claims.get("User", Map.class);
//        return userMap.get(DATA_FIELD).toString();
    }

    public String extractUserEmail(String token) {
        var claims = extractAllClaims(token);
        var userMap = claims.get("User", Map.class);
        return userMap.get(DATA_FIELD).toString();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateTokenUtils(new HashMap<>(), userDetails, accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateTokenUtils(new HashMap<>(), userDetails, refreshTokenExpirationTime);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String userEmail = extractDataFromToken(token);
            return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        var claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private String generateTokenUtils(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            String expirationTimeType
    ) {
        extraClaims.put("username", userDetails.getUsername());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTimeType)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) throws SecurityException {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
