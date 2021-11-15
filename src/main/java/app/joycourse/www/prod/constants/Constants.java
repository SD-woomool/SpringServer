package app.joycourse.www.prod.constants;

import lombok.Getter;

@Getter
public class Constants {
    private static final String jwtSecretKey = "gaskekjaidsdsf";
    private static final long ttlMillis = 3000;
    private static final String cookieName = "lkjsdf";

    public static String getJwtSecretKey(){
        return jwtSecretKey;
    }

    public static long getTtlMillis(){
        return ttlMillis;
    }

    public static String getCookieName(){
        return cookieName;
    }


}
