package com.loginwebservice.loginwebservice.Email;

public class AuthCodeUtils {
    public static final int UPPER_ALPHABET_TYPE = 0;
    public static final int LOWER_ALPHABET_TYPE = 1;
    public static final int NUMBER_TYPE = 2;

    public static final int AUTH_CODE_LENGTH = 6;
    public static String createAuthCode(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
            int codeType = (int)(Math.random() * 10) % 3;
            builder.append(convertBy(codeType));
        }
        return builder.toString();
    }

    public static String convertBy(final int codeType) {
        return switch (codeType) {
            case UPPER_ALPHABET_TYPE ->
                    String.valueOf((char)('A' + (int) (Math.random() * 26)));
            case LOWER_ALPHABET_TYPE ->
                //알파벳 대문자 타입
                    String.valueOf((char) ('a' + (int) (Math.random() * 26)));
            default -> String.valueOf((int) (Math.random() * 10));
        };
    }
}
