package com.pagepal.capstone.configurations.jwt;

import com.pagepal.capstone.configurations.exception.custom.TokenInvalidException;
import com.pagepal.capstone.dtos.account.AccountGoogleDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
//@PropertySource("classpath:application-common.properties")
public class JwtService {
    //@Value("${jwt.secretKey}")
    private final String secretKey="28482B4D6251655468576D597133743677397A24432646294A404E635266556A";
    //@Value("${jwt.expiration.accessToken}")
    private final String accessTokenExpirationTime="86400000";
    //@Value("${jwt.expiration.refreshToken}")
    private final String refreshTokenExpirationTime="2592000000";
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

    private Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims =Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new TokenInvalidException("Error: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AccountGoogleDto parseJwtToken(String jwtToken){
//        jwtToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImY1ZjRiZjQ2ZTUyYjMxZDliNjI0OWY3MzA5YWQwMzM4NDAwNjgwY2QiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI4ODUzNTIxNzU0MDktOGJibTZmdWE4amJnMjJ0MXBuYmkyOWN2ZDNpOWdjcmQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI4ODUzNTIxNzU0MDktOGJibTZmdWE4amJnMjJ0MXBuYmkyOWN2ZDNpOWdjcmQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTU4NTA2OTUwMjcyMTc3OTAxMDkiLCJoZCI6ImZwdC5lZHUudm4iLCJlbWFpbCI6ImFuaG52aHNlMTYyMDgyQGZwdC5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6InY3Z0pvZVhaUHZMRFE3b09udnRZVmciLCJuYW1lIjoiTmd1eWVuIFZ1IEhhaSBBbmggKEsxNiBIQ00pIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0paLXB5b2xHLUZrcDJVeUQxTTZYU0RUaW0tZkVNSlNMR0RDSy1JbHg4VWJLVT1zOTYtYyIsImdpdmVuX25hbWUiOiJOZ3V5ZW4gVnUgSGFpIEFuaCIsImZhbWlseV9uYW1lIjoiKEsxNiBIQ00pIiwibG9jYWxlIjoiZW4tR0IiLCJpYXQiOjE2OTg3MDk2NjYsImV4cCI6MTY5ODcxMzI2Nn0.ipS4R2FySTkBX4VKX83epqz8TQJha2_JnW1xck6JsFWIoWxrm6eLPhf1F_r32X-C1ed1xjVByM8kTrNm_UvVZVc4SG8YDHXBrHbCYbJvlJB0c0ByIR_52egN4kO0ddNs9S57fwlBOajMpxL_Ug0XJEmE3_diHZnkAv2rtFMPWpde5SgU-dVmMnh4M_jR1_jOhaSKCyBDEShtTCmpWg1eNhxZZIRzEp84R0j7JmBjJWv1R6m2SyjA_HnN2XR1EShznCuF6I0PyM7ZsTuZF4xuEDTSwVLOpN9FYyyeVMzpOaUPZp7pPJV3E94k4yxLN14kJpNn_m9KwEJRbTLYsNVAKA";

        // Split the JWT into its parts
        String[] parts = jwtToken.split("\\.");

        String header = new String(Base64.decodeBase64(parts[0]));

        String payload = new String(Base64.decodeBase64(parts[1]));

        JSONObject headerJson = new JSONObject(header);
        JSONObject payloadJson = new JSONObject(payload);

        String name = payloadJson.getString("name");
        String email = payloadJson.getString("email");

        // apply it to GoogleUserDTO
        AccountGoogleDto googleUserDTO = new AccountGoogleDto();
        googleUserDTO.setName(name);
        googleUserDTO.setEmail(email);

        return googleUserDTO;
    }
}
