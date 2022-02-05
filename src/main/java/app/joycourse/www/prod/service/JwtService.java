package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.JwtConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;


    @Resource
    private final ObjectMapper objectMapper;


    private SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.forName(jwtConfig.getAlgorithm());
    }

    public String createToken(String data, long ttlMillis) {
        if (ttlMillis == 0) {
            throw new RuntimeException("토큰만료기간을 0이상을 설정하세요");
        }

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtConfig.getSecretKey());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, getAlgorithm().getJcaName());

        JwtBuilder builder = Jwts.builder().setSubject(data).signWith(getAlgorithm(), signingKey);
        long nowMillis = System.currentTimeMillis();
        builder.setExpiration(new Date(nowMillis + ttlMillis));
        return builder.compact();
    }

    public Map<String, Object> getPayload(String token) {
        try {
            String payloadData = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            return objectMapper.readValue(payloadData, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("INVALID_PAYLOAD");
        }
    }
}

