package app.joycourse.www.prod.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserPrincipal implements OAuth2User {
    private final String name;
    private final Map<String, Object> attributes;

    private UserPrincipal(String name, Map<String, Object> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public static UserPrincipal create(OAuth2User oAuth2User, Provider provider) {
        Map<String, Object> attributes = new HashMap<>();
        String name = "userId";
        String userId = null;
        switch (provider) {
            case KAKAO:
                userId = Optional.ofNullable(oAuth2User.getAttributes().get("id"))
                        .map(Object::toString)
                        .orElse(null);
                break;
            case NAVER:
                userId = Optional.ofNullable(oAuth2User.getAttributes().get("response"))
                        .map(map -> ((Map<String, Object>) map).get("id").toString())
                        .orElse(null);
                break;
            case GOOGLE:
                userId = Optional.ofNullable(oAuth2User.getAttributes().get("sub"))
                        .map(Object::toString)
                        .orElse(null);
                break;
        }
        attributes.put(name, userId);
        return new UserPrincipal(name, attributes);
    }
}
