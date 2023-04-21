package com.ktds.dsquare.util;

import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    private static final String NUMBERS = "0123456789";
    private static final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "~!@#$%^&*-_=+";
    private static final char[][] charList = {
            NUMBERS.toCharArray(),
            UPPER_ALPHABETS.toCharArray(),
            LOWER_ALPHABETS.toCharArray(),
            SPECIAL_CHARACTERS.toCharArray()
    };

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < length; ++l) {
            int i = random.nextInt(charList.length), j = random.nextInt(charList[i].length);
            sb.append(charList[i][j]);
        }
        return sb.toString();
    }

    public static String generateRandomNumber(int digits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits; ++i)
            sb.append(random.nextInt(10));

        return sb.toString();
    }

}
