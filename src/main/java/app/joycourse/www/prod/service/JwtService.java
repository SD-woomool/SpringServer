package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.JwtConfig;
import app.joycourse.www.prod.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService{
    private JwtConfig jwtConfig;

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    String secretKey = Constants.getJwtSecretKey();

    @Resource
    ObjectMapper objectMapper;

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

    public String createToken(String data, long ttlMillis){
        if(ttlMillis == 0){
            throw new RuntimeException("토큰만료기간을 0이상을 설정하세요");
        }

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, this.signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setSubject(data).signWith(signatureAlgorithm, signingKey);
        long nowMillis = System.currentTimeMillis();
        builder.setExpiration(new Date(nowMillis + ttlMillis));
        return builder.compact();
    }

    public Map<String, Object> getPayload(String token){
        try{
            String payloadData = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            return objectMapper.readValue(payloadData, new TypeReference<>() {});
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException("INVALID_PAYLOAD");
        }
    }


}

