package com.ep.common.tool;

import org.apache.commons.lang3.RandomUtils;

public class SerialNumberTools {
    /**
     * 商户订单号前缀
     */
    public static String PREFIX_OUT_TRADE_NO = "OT";

    /**
     * 生成商户订单号,32位
     *
     * @return
     */
    public static String generateOutTradeNo() {
        return generateNumber(PREFIX_OUT_TRADE_NO, 3);
    }

    /**
     * @param prefix    前缀
     * @param randomLen 后缀随机数字长度
     * @return
     */
    private static String generateNumber(String prefix, int randomLen) {
        StringBuffer stringBuffer = new StringBuffer();
        int random = RandomUtils.nextInt((int) Math.pow(10, randomLen - 1), (int) Math.pow(10, randomLen) - 1);
        //前缀+17位+后缀随机数字
        stringBuffer.append(prefix)
                .append(generateDateToString())
                .append(random);
        return stringBuffer.toString();
    }

    private static String generateDateToString() {
        return DateTools.formatDatetoString(
                DateTools.getCurrentDate(), DateTools.TIME_PATTERN_MILLISECOND);
    }

}