package com.ep.common.tool;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:52 2018/4/23
 */
public class ValidCodeTools {

    public static String generateDigitValidCode(int length) {
        if (length == 0) {
            length = 4;
        }
        return RandomStringUtils.random(length, "0123456789");
    }
}
