package com.ep.common.tool.weixin;


import java.util.ArrayList;
import java.util.Collections;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:47 2018/4/23/023
 */
public class TokenUtil {
//    private static String systemToken = null;
//
//    public TokenUtil() {
//    }
//
//    public static String get() {
//        if(systemToken == null) {
//            systemToken = Configuration.getProperty("weixin4j.token", "weixin4j");
//        }
//
//        return systemToken;
//    }

    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        ArrayList params = new ArrayList();
        params.add(token);
        params.add(timestamp);
        params.add(nonce);
        Collections.sort(params);
        String temp = SHA1Tools.encode((String) params.get(0) + (String) params.get(1) + (String) params.get(2));
        return temp.equals(signature);
    }
}
