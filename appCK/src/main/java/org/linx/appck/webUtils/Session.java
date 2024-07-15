package org.linx.appck.webUtils;

public class Session {
    private static String email;
    private static String jwtToken;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }

    public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String jwtToken) {
        Session.jwtToken = jwtToken;
    }

    public static void clear() {
        email = null;
        jwtToken = null;
    }
}
