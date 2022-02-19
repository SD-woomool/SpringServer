package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.entity.UserAgent;
import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.util.DeviceUtil;
import app.joycourse.www.prod.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManageAuthService {
    private final TokenService tokenService;
    private static final String ACCESS_TOKEN_COOKIE_NAME = "actc";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "rctc";
    private static final int ACCESS_TOKEN_MAX_AGE = 1800; // 30m
    private static final int REFRESH_TOKEN_MAX_AGE = 10800; // 3h

    private Cookie makeCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public void refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        String deviceId = DeviceUtil.parseDeviceId(request);
        String uid = parseRefreshToken(request);

        if (userAgent.isWeb()) {
            // web은 access token cookie에 저장
            String accessToken = tokenService.issueAccessToken(uid, deviceId);
            response.addCookie(makeCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_TOKEN_MAX_AGE));
        }
    }

    public void deliverAuth(HttpServletRequest request, HttpServletResponse response, Auth auth) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        String deviceId = DeviceUtil.parseDeviceId(request);

        if (userAgent.isWeb()) {
            // web은 access token, refresh token 둘다 cookie에 저장
            String accessToken = tokenService.issueAccessToken(auth.getUid(), deviceId);
            String refreshToken = tokenService.issueRefreshToken(auth.getUid(), deviceId);

            response.addCookie(makeCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_TOKEN_MAX_AGE));
            response.addCookie(makeCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, REFRESH_TOKEN_MAX_AGE));
        }
        // app app은 header에 전달..?
    }

    public void clearAuth(HttpServletRequest request, HttpServletResponse response) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        if (userAgent.isWeb()) {
            response.addCookie(makeCookie(ACCESS_TOKEN_COOKIE_NAME, "", 0));
            response.addCookie(makeCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
        }
    }

    public String parseAccessToken(HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        String deviceId = DeviceUtil.parseDeviceId(request);
        String encryptedAccessToken = null;

        if (userAgent.isWeb()) {
            // web은 access token, refresh token 둘다 cookie에 저장
            encryptedAccessToken = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(ACCESS_TOKEN_COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (Objects.isNull(encryptedAccessToken)) {
            throw new CustomException(CustomException.CustomError.GET_TOKEN_ERROR);
        }

        return tokenService.verifyAccessToken(encryptedAccessToken, deviceId);
    }

    public String parseRefreshToken(HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        String deviceId = DeviceUtil.parseDeviceId(request);
        String encryptedRefreshToken = null;

        if (userAgent.isWeb()) {
            // web은 access token, refresh token 둘다 cookie에 저장
            encryptedRefreshToken = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (Objects.isNull(encryptedRefreshToken)) {
            throw new CustomException(CustomException.CustomError.GET_TOKEN_ERROR);
        }

        return tokenService.verifyRefreshToken(encryptedRefreshToken, deviceId);
    }
}
