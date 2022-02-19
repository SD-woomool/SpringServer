package app.joycourse.www.prod.util;

import app.joycourse.www.prod.entity.UserAgent;

public class UserAgentUtil {
    public static UserAgent parse(String userAgentString) {
        UserAgent agent = UserAgent.WEB_OTHER;

        userAgentString = userAgentString.toLowerCase();

        // 현재는 전부다 web으로 간주한다. PC, Mobile도 현재는 구분 X
        if (userAgentString.contains("windows")) {
            agent = UserAgent.WEB_WINDOW;
        } else if (userAgentString.contains("macintosh")) {
            agent = UserAgent.WEB_MAC;
        }

        return agent;
    }
}
