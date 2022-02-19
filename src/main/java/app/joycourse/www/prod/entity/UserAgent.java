package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAgent {
    WEB_MAC("PC", "MAC", UserAgent.WEB),
    WEB_WINDOW("PC", "WINDOW", UserAgent.WEB),
    WEB_LINUX("PC", "LINUX", UserAgent.WEB),
    WEB_OTHER("PC", "OTHER", UserAgent.WEB),
    WEB_IPAD("MOBILE", "IOS", UserAgent.WEB),
    WEB_IPHONE("MOBILE", "IOS", UserAgent.WEB),
    WEB_ANDROID("MOBILE", "ANDROID", UserAgent.WEB),
    APP_IPHONE("MOBILE", "IOS", UserAgent.APP),
    APP_IPAD("MOBILE", "IOS", UserAgent.APP),
    APP_ANDROID("MOBILE", "ANDROID", UserAgent.APP),
    ;

    public static final String WEB = "WEB";
    public static final String APP = "APP";

    private final String platform;
    private final String os;
    private final String type;

    public Boolean isWeb() {
        return this.type.equals(UserAgent.WEB);
    }

    public Boolean isApp() {
        return this.type.equals(UserAgent.APP);
    }
}
