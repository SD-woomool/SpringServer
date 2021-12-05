package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.constants.Constants;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.UserInfo;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.AccountService;
import app.joycourse.www.prod.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private OauthConfig oauthConfig;
    private AccountService service;
    private JwtService jwtService;

    @GetMapping("/logout")
    public Response<Object> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.JWT_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new Response<>(null);
    }

    @GetMapping("/{provider}/login")
    public void login(@PathVariable(value = "provider") String provider, HttpServletResponse response) throws IOException {
        String redirectUri = "/";
        if (oauthConfig.getProviders().containsKey(provider)) {
            OauthConfig.Provider providerConfig = oauthConfig.getProviders().get(provider);
            redirectUri = String.format(providerConfig.getLoginUri(), providerConfig.getClientId(), providerConfig.getRedirectUri());
        }
        response.sendRedirect(redirectUri);
    }

    @GetMapping("/{provider}/callback")
    public Response<UserInfo> callback(@PathVariable(value = "provider") String provider, @RequestParam String code, @RequestParam(required = false) String state, HttpServletResponse response) throws CustomException {
        if (!oauthConfig.getProviders().containsKey(provider)) {
            throw new CustomException(CustomException.CustomError.PROVIDER_WRONG);
        }

        String accessToken = service.getAccessToken(provider, code, state);
        if (Objects.isNull(accessToken)) {
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        String email = service.getEmail(provider, accessToken);
        if (Objects.isNull(email)) {
            throw new CustomException(CustomException.CustomError.BAD_REQUEST);
        }

        // check already signed or new account
        Optional<User> user = service.getUserByEmail(email);
        boolean signed = user.isPresent();
        Map<String, Object> payloads = new HashMap<>();
        String nickname = null, profileImageUrl = null;

        if (signed) {
            nickname = user.get().getNickname();
            profileImageUrl = user.get().getProfileImageUrl();

            payloads.put("id", user.get().getId());
        } else {
            payloads.put("email", email);
        }

        String jwtToken = jwtService.createToken(payloads);
        Cookie cookie = new Cookie(Constants.JWT_COOKIE_NAME, jwtToken);
        cookie.setPath("/");
        cookie.setMaxAge(Constants.JWT_COOKIE_EXPIRED);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new Response<>(new UserInfo(signed, email, nickname, profileImageUrl));
    }
}
