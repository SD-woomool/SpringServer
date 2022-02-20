package app.joycourse.www.prod.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {
    private static String hashing(MessageDigest md, String msg) {
        md.update(msg.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(md.digest()));
    }

    public static String sha256(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return hashing(md, msg);
        } catch (Exception e) {
            return null;
        }
    }

    public static String sha224(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            return hashing(md, msg);
        } catch (Exception e) {
            return null;
        }
    }
}
