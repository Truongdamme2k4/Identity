package com.identity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {
    @Value("${jwt.signerKey}")
    private static String secretKey;

    public static String getJtiFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getId(); // jti nằm ở đây
    }
}
