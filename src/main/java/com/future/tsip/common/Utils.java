package com.future.tsip.common;

public class Utils {

    static final char[] PASSWORD_CHAR = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };

    // 口令可以使用的字符a-z,A-Z,0-9,-,_
    public static String enPassword(String s) {
        if (s == null || s.trim().length() == 0) {
            return "";
        }
        String s2 = "";
        int tsize = PASSWORD_CHAR.length;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean match = false;
            for (int j = 0; j < tsize; j++) {
                if (c == PASSWORD_CHAR[j]) {
                    int k = j + i * 2 + 3;
                    if (k >= tsize) {
                        k = k - tsize;
                    }
                    s2 = s2 + PASSWORD_CHAR[k];
                    match = true;
                    break;
                }
            }
            if (!match) {
                s2 = s2 + "*";
            }
        }
        return s2;
    }

    public static String dePassword(String s) {
        if (s == null || s.trim().length() == 0) {
            return "";
        }
        String s2 = "";
        int tsize = PASSWORD_CHAR.length;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean match = false;
            for (int j = 0; j < tsize; j++) {
                if (c == PASSWORD_CHAR[j]) {
                    int k = j - i * 2 - 3;
                    if (k < 0) {
                        k = k + tsize;
                    }
                    s2 = s2 + PASSWORD_CHAR[k];
                    match = true;
                    break;
                }
            }
            if (!match) {
                s2 = s2 + "*";
            }
        }
        return s2;
    }

    public static boolean checkPassword(String password) { // 校验字符是否合法
        if (password == null || password.trim().length() == 0) {
            return false;
        }
        int tsize = PASSWORD_CHAR.length;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            boolean match = false;
            for (int j = 0; j < tsize; j++) {
                if (c == PASSWORD_CHAR[j]) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

}
