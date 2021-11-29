package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.JwtConfig;
import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class JwtService {
    private JwtConfig jwtConfig;

    public String createToken(Map<String, Object> payloads) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", jwtConfig.getType());
        headers.put("alg", jwtConfig.getAlgorithm());

        Date ext = new Date();
        ext.setTime(ext.getTime() + jwtConfig.getExpiredTime());

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user")
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    public Map<String, Object> verifyToken(String jwtToken) {
        Map<String, Object> claimMap = null;
        try {
            claimMap = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) { // 토큰이 만료되었을 경우
            return null;
        }
        return claimMap;
    }
}