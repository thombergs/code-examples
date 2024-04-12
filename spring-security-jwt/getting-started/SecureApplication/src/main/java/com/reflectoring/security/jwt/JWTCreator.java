package com.reflectoring.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;

public class JWTCreator {

    public static String createJwt() {
        // Recommended to be stored in Secret
        String secret = "5JzoMbk6E5qIqHSuBTgeQCARtUsxAkBiHwdjXOSW8kWdXzYmP3X51C0";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .claim("id", "abc123")
                .claim("role", "admin")
                /*.addClaims(Map.of("id", "abc123",
                        "role", "admin"))*/
                .setIssuer("TestApplication")
                .setIssuedAt(java.util.Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }

    public static Jws<Claims> parseJwt(String jwtString) {
        // Recommended to be stored in Secret
        String secret = "5JzoMbk6E5qIqHSuBTgeQCARtUsxAkBiHwdjXOSW8kWdXzYmP3X51C0";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }
}
