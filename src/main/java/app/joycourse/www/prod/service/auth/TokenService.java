package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.config.KeyConfig;
import app.joycourse.www.prod.entity.auth.RefreshToken;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.RefreshTokenRepository;
import app.joycourse.www.prod.util.AES256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private static final int REFRESH_TOKEN_MAX_AGE = 10800; // 3h

    private static final String delimiter = "/";
    private final KeyConfig keyConfig;
    private final RefreshTokenRepository refreshTokenRepository;

    public String verifyAccessToken(String encryptedAccessToken, String deviceId) {
        String rawToken = AES256Util.decrypt(keyConfig.getAccessTokenKey(), encryptedAccessToken);

        if (Objects.isNull(rawToken)) {
            log.error("[verifyAccessToken] token is not valid {}->{}", encryptedAccessToken, rawToken);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }

        String[] info = rawToken.split(delimiter);
        if (info.length != 2) {
            log.error("[verifyAccessToken] token is not valid {}->{}", encryptedAccessToken, rawToken);
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        // device id가 다른 경우 token이 hijacking 당한걸꺼이다. (CSRF)
        if (!deviceId.equals(info[1])) {
            log.error("[verifyAccessToken] wrong access. may be CSRF attack. deviceId(request/current): ({}/{})", info[1], deviceId);
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        // 정상 인경우 uid를 반환
        return info[0];
    }

    public String issueAccessToken(String uid, String deviceId) {
        // uid + device id + time -> encrypt
        String rawToken = uid + delimiter + deviceId;
        String encryptedToken = AES256Util.encrypt(keyConfig.getAccessTokenKey(), rawToken);

        if (Objects.isNull(encryptedToken)) {
            log.error("[issueAccessToken] fail to encrypt token {}->{}", rawToken, encryptedToken);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
        return encryptedToken;
    }

    public String verifyRefreshToken(String encryptedRefreshToken, String deviceId) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(encryptedRefreshToken);
        if (optionalRefreshToken.isEmpty()) {
            log.error("[verifyRefreshToken] refresh token is not existed: {}", encryptedRefreshToken);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }

        // device id가 다른 경우 token이 hijacking 당한걸꺼이다. (CSRF)
        RefreshToken refreshToken = optionalRefreshToken.get();
        if (!refreshToken.getDeviceId().equals(deviceId)) {
            log.error("[verifyRefreshToken] wrong access. may be CSRF attack. deviceId(request/current): ({}/{})", refreshToken.getDeviceId(), deviceId);
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        return refreshToken.getUid();
    }

    public String issueRefreshToken(String uid, String deviceId) {
        // random int + localhost IP + current time -> encrypt
        // save to repository
        try {
            Random random = new Random();
            InetAddress localHost = InetAddress.getLocalHost();

            String rawToken = random.nextInt() + delimiter + localHost.getHostAddress() + delimiter + LocalDateTime.now();
            String encryptedToken = AES256Util.encrypt(keyConfig.getRefreshTokenKey(), rawToken);
            assert encryptedToken != null;

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(encryptedToken);
            refreshToken.setUid(uid);
            refreshToken.setDeviceId(deviceId);

            // TODO: redis로 변경하고 시간 지정해야함.
            refreshTokenRepository.save(refreshToken);
            return encryptedToken;
        } catch (Exception e) {
            log.error("[issueRefreshToken] fail to issue refresh token\n{}", e.getMessage());
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
    }
}
