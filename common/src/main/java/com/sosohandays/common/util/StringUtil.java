package com.sosohandays.common.util;

public class StringUtil {

    /**
     * null 또는 빈값인지 확인
     * @param str 확인대상
     * @return 여부
     */
    public static boolean isNullOrEmpty(String str) {
        boolean returnVal = false;
        if (null == str || str.trim().isEmpty()) {
            returnVal = true;
        }

        return returnVal;
    }
}
