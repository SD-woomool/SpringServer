package app.joycourse.www.prod.entity;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public enum UserAgent {
    WEB, IPHONE, ANDROID;

    public static UserAgent convertHttpUserAgent(HttpServletRequest request) {
        switch (request.getHeader(HttpHeaders.USER_AGENT).toUpperCase()) {
            case "IPHONE":
                return UserAgent.IPHONE;
            case "ANDROID":
                return UserAgent.ANDROID;
            default:
                return UserAgent.WEB;
        }
    }
}
