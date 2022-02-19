package app.joycourse.www.prod.util;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DeviceUtil {
    public static String parseDeviceId(HttpServletRequest request) {
        String rawDeviceId = request.getHeader(HttpHeaders.USER_AGENT) + "&&" + request.getRemoteHost();
        return new String(Base64.getEncoder().encode(rawDeviceId.getBytes(StandardCharsets.UTF_8)));
    }
}
