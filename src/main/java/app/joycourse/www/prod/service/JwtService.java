package app.joycourse.www.prod.service;

import app.joycourse.www.prod.constants.Constants;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


@Service
public class JwtService {

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    String secretKey = Constants.getJwtSecretKey();

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


}
