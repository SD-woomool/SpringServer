package app.joycourse.www.prod.entity.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Provider {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NAVER("NAVER");

    private final String provider;
}
