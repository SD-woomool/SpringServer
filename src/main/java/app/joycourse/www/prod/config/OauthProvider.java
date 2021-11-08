package app.joycourse.www.prod.config;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthProvider {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;

    public OauthProvider(OauthProperties.User user, OauthProperties.Provider provider){
        this(user.getClientId(), user.getClientSecret(), user.getRedirectUri(), provider.getTokenUri(), provider.getUserInfoUri());
    }
    // 여기 위에랑 밑에 문법이 이해가 안감, builder는 컨스트럭터를 말하는 건가?
    @Builder
    public OauthProvider(String clientId, String clientSecret, String redirectUri, String tokenUri, String userInfoUri){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }

}
