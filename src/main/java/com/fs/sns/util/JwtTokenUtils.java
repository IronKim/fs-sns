package com.fs.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String getUserName(String token, String key) { // 토큰에서 사용자 이름을 추출하는 메서드
        return extractClaims(token, key).get("userName", String.class);
    }

    public static boolean isExpired(String token, String key) { // 토큰이 만료되었는지 확인하는 메서드
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) { // 토큰에서 클레임을 추출하는 메서드
        return Jwts.parserBuilder().setSigningKey(getKey(key)).build().parseClaimsJws(token).getBody();
    }

    public static String generateToken(String userName, String key, long expiredTimeMs) { // 토큰을 생성하는 메서드
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) { // 키를 Key 객체로 변환하는 메서드
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8); // 키를 바이트 배열로 변환
        return Keys.hmacShaKeyFor(keyBytes); // 바이트 배열로 변환된 키를 Key 객체로 변환
    }

}
