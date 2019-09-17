package com.chengzi.app.utils;

public class CharacterUtils {

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isEn(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.BASIC_LATIN;
//        return source.matches("^[a-zA-Z0-9\\s_(),.-]*");
    }

    //  是否可见字符
    public static boolean isEnChar(char c) {
        int i = c;
        return isEn(c) && ((i >= 0x30 && i <= 0x39) || (i >= 0x41 && i <= 0x5A) || (i >= 0x61 && i <= 0x7A));
    }
}
