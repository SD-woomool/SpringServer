package app.joycourse.www.prod.entity.auth;

import app.joycourse.www.prod.exception.CustomException;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
public class AuthPrincipal implements OAuth2User {
    private final String uid;
    private final String email;

    private AuthPrincipal(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    @Override
    public String getName() {
        return uid;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public static AuthPrincipal create(OAuth2User oAuth2User, Provider provider) {
        Map<String, Object> oAuthAttributes = oAuth2User.getAttributes();

        String uid, email;

        switch (provider) {
            case KAKAO:
                uid = Optional.ofNullable(oAuthAttributes.get("id"))
                        .map(Object::toString)
                        .orElse(null);
                email = Optional.ofNullable(oAuthAttributes.get("kakao_account"))
                        .map(map -> ((Map<String, Object>) map).get("email"))
                        .map(Object::toString)
                        .orElse(null);
                break;
            case NAVER:
                uid = Optional.ofNullable(oAuthAttributes.get("response"))
                        .map(map -> ((Map<String, Object>) map).get("id"))
                        .map(Object::toString)
                        .orElse(null);
                email = Optional.ofNullable(oAuthAttributes.get("response"))
                        .map(map -> ((Map<String, Object>) map).get("email"))
                        .map(Object::toString)
                        .orElse(null);
                break;
            case GOOGLE:
                uid = Optional.ofNullable(oAuthAttributes.get("sub"))
                        .map(Object::toString)
                        .orElse(null);
                email = Optional.ofNullable(oAuthAttributes.get("email"))
                        .map(Object::toString)
                        .orElse(null);
                break;
            default:
                throw new CustomException("Unsupported Provider");
        }

        if (Objects.isNull(uid) || Objects.isNull(email)) {
            throw new CustomException("Invalid uid or email");
        }

        // provider간의 uid 겹치는 것을 방지하기 위해, provder로 접두사를 붙인다.
        uid = provider.name() + "_" + uid;

        return new AuthPrincipal(uid, email);
    }
}
