package app.joycourse.www.prod.util;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtil {
    public static String parseDeviceId(HttpServletRequest request) {
        String rawDeviceId = request.getHeader(HttpHeaders.USER_AGENT) + "&&" + request.getRemoteHost();
        return HashUtil.sha256(rawDeviceId);
    }
}
