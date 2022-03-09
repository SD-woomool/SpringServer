package app.joycourse.www.prod.util;

import app.joycourse.www.prod.exception.CustomException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {
    private static String hashing(MessageDigest md, String msg) {
        md.update(msg.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(md.digest())).replaceAll("/", "_");
    }

    public static String sha256(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return hashing(md, msg);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is existed. NoSuchAlgorithmException is never created.
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
    }

    public static String sha224(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            return hashing(md, msg);
        }  catch (NoSuchAlgorithmException e) {
            // SHA-224 is existed. NoSuchAlgorithmException is never created.
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
    }
}
