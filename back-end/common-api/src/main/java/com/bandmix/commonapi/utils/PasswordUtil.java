package com.bandmix.commonapi.utils;

import java.util.Random;

public class PasswordUtil {

    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    private static char nextChar(Random rnd) {
        switch (rnd.nextInt(4)) {
            case 0:
                return (char) ('a' + rnd.nextInt(26));
            case 1:
                return (char) ('A' + rnd.nextInt(26));
            case 2:
                return (char) ('0' + rnd.nextInt(10));
            default:
                return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
        }
    }

    public static String randomPassword() {
        char[] chars = new char[6];
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            chars[i] = nextChar(rnd);
        }
        return new String(chars);
    }

}
