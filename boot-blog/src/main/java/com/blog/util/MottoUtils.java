package com.blog.util;

public class MottoUtils {
    public static final Long MOTTO_COUNT = 30L;

    public static boolean check(String motto) {
        String[] parts = motto.split(" ");
        if (parts.length > MOTTO_COUNT) {
            return false;
        }
        for (String part : parts) {
            if (!part.matches("^[\u4e00-\u9fa5，]{1,12}$")) {
                return false;
            }
        }
        return true;
    }
}
