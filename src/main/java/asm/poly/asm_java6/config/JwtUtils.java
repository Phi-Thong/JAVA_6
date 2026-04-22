package asm.poly.asm_java6.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
public class JwtUtils {

    private final Key key;
    private final long expirationMs;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs:3600000}") long expirationMs) {

        if (secret == null || secret.length() < 32) {
            // bảo vệ dev: HS256 cần key đủ dài (ít nhất 256 bit = 32 bytes)
            throw new IllegalArgumentException("jwt.secret must be at least 32 characters long");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Generate token with additional claims: roles (List<String>), name, picture
     */
    public String generateToken(Long id, String username, List<String> roles, String name, String picture) {
        long now = System.currentTimeMillis();
        JwtBuilder b = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256);
        if (id != null) {
            b.claim("userId", id); // Thêm id vào payload
        }
        if (roles != null && !roles.isEmpty()) {
            b.claim("roles", roles);
        }
        if (name != null) {
            b.claim("name", name);
        }
        if (picture != null) {
            b.claim("picture", picture);
        }
        return b.compact();
    }

    /**
     * Validate token signature and expiry.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // parse lỗi: expired, malformed, unsupported, signature invalid, etc.
            return false;
        }
    }

    /**
     * Extract username (subject).
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * Extract roles claim as List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) return List.of();

        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            List<?> raw = (List<?>) rolesObj;
            List<String> roles = new ArrayList<>();
            for (Object o : raw) {
                if (o != null) roles.add(o.toString());
            }
            return roles;
        }
        // if roles stored as comma-separated string or single role:
        if (rolesObj instanceof String) {
            String s = (String) rolesObj;
            String[] parts = s.split(",");
            List<String> roles = new ArrayList<>();
            for (String p : parts) {
                if (!p.isBlank()) roles.add(p.trim());
            }
            return roles;
        }
        return List.of();
    }

    public String getNameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims == null ? null : claims.get("name", String.class);
    }

    public String getPictureFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims == null ? null : claims.get("picture", String.class);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) return null;
        // Nếu bạn lưu userId là số trong claim "userId"
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        if (userIdObj instanceof String) {
            try {
                return Long.valueOf((String) userIdObj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
