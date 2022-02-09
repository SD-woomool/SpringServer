package app.joycourse.www.prod.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Provider {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NAVER("NAVER");

    private final String provider;
}
