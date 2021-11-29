package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.constants.Constants;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.UserInfo;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"provider=naver", "code=cPFGokPnRVbHfWgd4J"})
@AutoConfigureMockMvc
@Transactional
public class AccountControllerTests {
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OauthConfig oauthConfig;

    @Value("${provider}")
    private String provider;

    @Value("${code}")
    private String code;

    private final UserInfo signedUserInfo = new UserInfo(true, "email@emai.com", "nickname", null);
    private final UserInfo firstLoginUserInfo = new UserInfo(false, "email@emai.com", null, null);


    @Nested
    @DisplayName("Oauth Redirect Url Test")
    class OauthRedirectTest {
        @Test
        @DisplayName("Success")
        public void oauthLoginRedirectUrlSuccess() throws Exception {
            OauthConfig.Provider providerConfig = oauthConfig.getProviders().get(provider);
            String redirectUri = String.format(providerConfig.getLoginUri(), providerConfig.getClientId(), providerConfig.getRedirectUri());

            //given - provider: naver
            // when
            mockMvc.perform(get("/accounts/" + provider + "/login"))
                    // then
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(redirectUri));
        }

        @Test
        @DisplayName("Fail by invalid provider")
        public void oauthLoginRedirectUrlFailByInvalidProvider() throws Exception {
            // given - provider: INVALID_PROVIDER
            // when
            mockMvc.perform(get("/accounts/INVALID_PROVIDER/login"))
                    // then
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }
    }


    @Nested
    @DisplayName("Oauth Callback Test")
    class OauthCallbackTest {
        @Test
        @DisplayName("Fail by no parameter")
        public void oauthLoginCallbackFailByNoParameter() throws Exception {
            CustomException.CustomError error = CustomException.CustomError.MISSING_PARAMETERS;
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(error.getMessage(), error.getStatus()));

            // given - provider: naver, no parameter
            // when
            mockMvc.perform(get("/accounts/" + provider + "/callback"))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent));
        }

        @Test
        @DisplayName("Fail by invalid provider")
        public void oauthLoginCallbackFailByInvalidProvider() throws Exception {
            CustomException.CustomError error = CustomException.CustomError.PROVIDER_WRONG;
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(error.getMessage(), error.getStatus()));

            // given - provider: INVALID_PROVIDER, parameter: code=code
            // when
            mockMvc.perform(get("/accounts/INVALID_PROVIDER/callback").param("code", code))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent));
        }

        @Test
        @DisplayName("Fail by invalid code")
        public void oauthLoginCallbackFailByInvalidCode() throws Exception {
            CustomException.CustomError error = CustomException.CustomError.BAD_REQUEST;
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(error.getMessage(), error.getStatus()));
            final String INVALID_CODE = "INVALID_CODE";

            given(accountService.getAccessToken(provider, INVALID_CODE, null)).willReturn(null);

            // given - provider: naver, parameter: code=INVALID_CODE
            // when
            mockMvc.perform(get("/accounts/" + provider + "/callback").param("code", INVALID_CODE))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent));
        }

        @Test
        @DisplayName("Fail by getting email")
        public void oauthLoginCallbackFailByGettingEmail() throws Exception {
            CustomException.CustomError error = CustomException.CustomError.BAD_REQUEST;
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(error.getMessage(), error.getStatus()));
            final String ACCESS_TOKEN = "ACCESS_TOKEN";

            given(accountService.getAccessToken(provider, code, null)).willReturn(ACCESS_TOKEN);
            given(accountService.getEmail(provider, ACCESS_TOKEN)).willReturn(null);

            // given - provider: naver, parameter: code=code
            // when
            mockMvc.perform(get("/accounts/" + provider + "/callback").param("code", code))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent));
        }

        @Test
        @DisplayName("Success when first login")
        public void oauthLoginCallbackSuccessFirstLogin() throws Exception {
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(firstLoginUserInfo));
            final String ACCESS_TOKEN = "ACCESS_TOKEN";

            given(accountService.getAccessToken(provider, code, null)).willReturn(ACCESS_TOKEN);
            given(accountService.getEmail(provider, ACCESS_TOKEN)).willReturn(firstLoginUserInfo.getEmail());
            given(accountService.getUserByEmail(firstLoginUserInfo.getEmail())).willReturn(Optional.empty());

            // given - provider: naver, parameter: code=code
            // when
            mockMvc.perform(get("/accounts/" + provider + "/callback").param("code", code))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent))
                    .andExpect(cookie().exists(Constants.JWT_COOKIE_NAME))
                    .andExpect(cookie().httpOnly(Constants.JWT_COOKIE_NAME, true))
                    .andExpect(cookie().secure(Constants.JWT_COOKIE_NAME, true));

        }

        @Test
        @DisplayName("Success when already signed up")
        public void oauthLoginCallbackSuccess() throws Exception {
            final String expectedResponseContent = objectMapper.writeValueAsString(new Response<>(signedUserInfo));
            final String ACCESS_TOKEN = "ACCESS_TOKEN";

            given(accountService.getAccessToken(provider, code, null)).willReturn(ACCESS_TOKEN);
            given(accountService.getEmail(provider, ACCESS_TOKEN)).willReturn(signedUserInfo.getEmail());
            User user = new User();
            user.setEmail(signedUserInfo.getEmail());
            user.setNickname(signedUserInfo.getNickname());
            user.setProfileImageUrl(signedUserInfo.getProfileImageUrl());
            given(accountService.getUserByEmail(signedUserInfo.getEmail())).willReturn(Optional.of(user));

            // given - provider: naver, parameter: code=code
            // when
            mockMvc.perform(get("/accounts/" + provider + "/callback").param("code", code))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedResponseContent))
                    .andExpect(cookie().exists(Constants.JWT_COOKIE_NAME))
                    .andExpect(cookie().httpOnly(Constants.JWT_COOKIE_NAME, true))
                    .andExpect(cookie().secure(Constants.JWT_COOKIE_NAME, true));
        }
    }

}
