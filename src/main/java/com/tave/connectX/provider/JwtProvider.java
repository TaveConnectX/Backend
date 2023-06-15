package com.tave.connectX.provider;

import com.tave.connectX.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private Key secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(JwtConfig.jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String buildToken(com.tave.connectX.entity.User user) {
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("typ", "JWT");
        jwtHeader.put("alg", "HS256");
        jwtHeader.put("regDate", System.currentTimeMillis());

        Map<String, Object> claim = new HashMap<>();
        claim.put("userIdx", user.getUserIdx());
        claim.put("name", user.getName());
        claim.put("role", user.getRole());

        return Jwts.builder()
                .setSubject(user.getName())
                .setHeader(jwtHeader)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, JwtConfig.jwtSecretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
