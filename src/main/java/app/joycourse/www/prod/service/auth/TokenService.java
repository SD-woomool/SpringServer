package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.config.AuthConfig;
import app.joycourse.www.prod.entity.auth.RefreshToken;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.RefreshTokenRepository;
import app.joycourse.www.prod.util.AES256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private static final String delimiter = "&&";
    private final AuthConfig authConfig;
    private final RefreshTokenRepository refreshTokenRepository;

    public String verifyAccessToken(String encryptedAccessToken, String deviceId) {
        String rawToken = AES256Util.decrypt(authConfig.getAccessTokenKey(), encryptedAccessToken);

        if (Objects.isNull(rawToken)) {
            log.error("[verifyAccessToken] token is not valid {}->{}", encryptedAccessToken, rawToken);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }

        String[] info = rawToken.split(delimiter);
        if (info.length != 3) {
            log.error("[verifyAccessToken] token is not valid {}->{}", encryptedAccessToken, rawToken);
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        // device id가 다른 경우 token이 hijacking 당한걸꺼이다. (CSRF)
        if (!deviceId.equals(info[1])) {
            log.error("[verifyAccessToken] wrong access. may be CSRF attack. deviceId(request/current): ({}/{})", info[1], deviceId);
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        Timestamp expireTimeStamp = new Timestamp(Long.parseLong(info[2]));
        Timestamp nowTimeStamp = Timestamp.valueOf(LocalDateTime.now());

        // 만료 시간이 지난 경우
        if (expireTimeStamp.getTime() < nowTimeStamp.getTime()) {
            throw new CustomException(CustomException.CustomError.ACCESS_TOKEN_EXPIRED);
        }

        // 정상 인경우 uid를 반환
        return info[0];
    }

    public String issueAccessToken(String uid, String deviceId) {
        // uid + device id + time -> encrypt
        String rawToken = uid + delimiter + deviceId + delimiter + (Timestamp.valueOf(LocalDateTime.now()).getTime() + authConfig.getAccessTokenMaxAge() * 1000L);
        String encryptedToken = AES256Util.encrypt(authConfig.getAccessTokenKey(), rawToken);

        if (Objects.isNull(encryptedToken)) {
            log.error("[issueAccessToken] fail to encrypt token {}->{}", rawToken, encryptedToken);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
        return encryptedToken;
    }

    public String verifyRefreshToken(String encryptedRefreshToken, String deviceId) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(encryptedRefreshToken);
        if (optionalRefreshToken.isEmpty()) {
            log.error("[verifyRefreshToken] refresh token is not existed: {}", encryptedRefreshToken);
            throw new CustomException(CustomException.CustomError.REFRESH_TOKEN_EXPIRED);
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
            String encryptedToken = AES256Util.encrypt(authConfig.getRefreshTokenKey(), rawToken);
            assert encryptedToken != null;

            // 기존 토큰 삭제
            Optional<RefreshToken> existedRefreshToken = refreshTokenRepository.findByUidAndDeviceId(uid, deviceId);
            existedRefreshToken.ifPresent(refreshTokenRepository::delete);

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(encryptedToken);
            refreshToken.setUid(uid);
            refreshToken.setDeviceId(deviceId);
            refreshTokenRepository.save(refreshToken);

            return encryptedToken;
        } catch (Exception e) {
            log.error("[issueRefreshToken] fail to issue refresh token\n{}", e.getMessage());
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
    }
}
