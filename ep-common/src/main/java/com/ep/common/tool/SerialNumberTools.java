package com.ep.common.tool;

import org.apache.commons.lang3.RandomUtils;

public class SerialNumberTools {
    /**
     * 商户订单号前缀
     */
    public static String PREFIX_OUT_TRADE_NO = "OT";

    /**
     * 商户退款单号前缀
     */
    public static String PREFIX_OUT_REFUND_NO = "OR";

    /**
     * 生成商户订单号,32位
     *
     * @return
     */
    public static String generateOutTradeNo(Long orderId) {
        String orderIdStr = StringTools.addZeroForNum(orderId.toString(), 12);
        return PREFIX_OUT_TRADE_NO + orderIdStr + generateNumber(4);
    }

    /**
     * 生成商户退款单号,32位
     *
     * @return
     */
    public static String generateOutRefundNo(Long orderId) {
        String orderIdStr = StringTools.addZeroForNum(orderId.toString(), 12);
        return PREFIX_OUT_REFUND_NO + orderIdStr + generateNumber(4);
    }

    /**
     * @param randomLen 后缀随机数字长度
     * @return
     */
    private static String generateNumber(int randomLen) {
        StringBuffer stringBuffer = new StringBuffer();
        int random = RandomUtils.nextInt((int) Math.pow(10, randomLen - 1), (int) Math.pow(10, randomLen) - 1);
        //时间yyyyMMddHHmmss14位+后缀随机数字
        stringBuffer.append(generateDateToString())
                .append(random);
        return stringBuffer.toString();
    }

    private static String generateDateToString() {
        //14位
        return DateTools.formatDatetoString(
                DateTools.getCurrentDate(), DateTools.TIME_PATTERN_SESSION);
    }

}