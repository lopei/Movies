package com.anotap.whatagreatmovie.util;

/**
 * class that does operations with text
 */
public class TextUtil {
    private static final String PATTERN_LOGIN = "^[a-zA-Z0-9_]+$";

    public static boolean isValidLogin(String login) {
        return login.matches(PATTERN_LOGIN);
    }
}
