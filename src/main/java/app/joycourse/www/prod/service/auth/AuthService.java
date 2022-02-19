package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.entity.auth.AuthPrincipal;
import app.joycourse.www.prod.entity.auth.Provider;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public Auth saveAuth(Authentication authentication) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Provider provider = Provider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());
        AuthPrincipal authPrincipal = AuthPrincipal.create(authToken.getPrincipal(), provider);

        String uid = authPrincipal.getUid();
        String email = authPrincipal.getEmail();

        // email로 기존에 회원가입 되어 있는 유저가 있는지 확인한다.
        Optional<Auth> optionalAuth = authRepository.findByEmail(email);
        if (optionalAuth.isPresent()) {
            // provider가 다른 경우 error를 던진다. 추후에 계정 통합도 고려해봐야할듯..?
            if (!provider.equals(optionalAuth.get().getProvider())) {
                throw new CustomException(CustomException.CustomError.INVALID_PROVIDER);
            }
        }

        // auth가 없으면 새로 만든다.
        Auth auth = optionalAuth.orElseGet(() -> {
            Auth newAuth = new Auth();
            newAuth.setUid(uid);
            newAuth.setEmail(email);
            newAuth.setProvider(provider);
            return newAuth;
        });

        // 기존에 있더라도 lastLoginAt을 변경하기 위해 저장, 신규 유저도 저장
        authRepository.save(auth);

        return auth;
    }
}
