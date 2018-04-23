package com.ep.common.tool.weixin;

import java.security.MessageDigest;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 11:11 2018/4/23/023
 */
public class SHA1Tools {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public SHA1Tools() {
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);

        for (int j = 0; j < len; ++j) {
            buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);
            buf.append(HEX_DIGITS[bytes[j] & 15]);
        }

        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest e = MessageDigest.getInstance("SHA1");
                e.update(str.getBytes());
                return getFormattedText(e.digest());
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }
    }
}
