package com.github.rand0m86.util;

public abstract class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(String text) {
        return null == text || text.length() == 0;
    }
}
