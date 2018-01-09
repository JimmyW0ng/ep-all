package com.ep.common.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {

    /**
     * 验证密码
     *
     * @param password 长度6-16位至少包含数字和字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPassword(String password) {
        String regex = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}";
        return Pattern.matches(regex, password);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，
     *              xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>
     *               移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *               </p>
     *               <p>
     *               联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *               </p>
     *               <p>
     *               电信的号段：133、153、180（未启用）、189
     *               178(4G)、147(上网卡)、176(4G)、145(上网卡)、177(4G)、170（虚拟运营商）171
     *               </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[103678])[0-9]{8}";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证手机号码（不支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>
     *               移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *               </p>
     *               <p>
     *               联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *               </p>
     *               <p>
     *               电信的号段：133、153、180（未启用）、189
     *               178(4G)、147(上网卡)、176(4G)、145(上网卡)、177(4G)、170（虚拟运营商）171
     *               </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobileLocal(String mobile) {
        String regex = "^(13[0-9]|15[012356789]|18[0-9]|14[57]|17[013678])[0-9]{8}";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p>
     *              <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
     *              的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
     *              </p>
     *              <p>
     *              <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。
     *              </p>
     *              <p>
     *              <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
     *              </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     *            http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域名
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com
     * </pre>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile(
                "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
                Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern
        // p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",
        // Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 匹配银行卡号(简单匹配 15-19位纯数字)
     *
     * @param cardNumber 银行卡卡号
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkCardNumber(String cardNumber) {
        String regex = "[1-9][0-9]{14,18}";
        return Pattern.matches(regex, cardNumber);
    }

    /**
     * 匹配qq(5-15位数字)
     *
     * @param qq qq
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkQQ(String qq) {
        String regex = "[1-9][0-9]{4,14}";
        return Pattern.matches(regex, qq);
    }

    /**
     * 匹配用户昵称(2-16位)
     *
     * @param userName 用户昵称
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkUserName(String userName) {
        String regex = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2dA-Za-z]+$";
        return Pattern.matches(regex, userName);
    }

    /**
     * 匹配银行联行号(12位数字)
     *
     * @param cardBankCnaps 联行号
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkCardBankCnaps(String cardBankCnaps) {
        String regex = "^[\\d]{12}$";
        return Pattern.matches(regex, cardBankCnaps);
    }

    /**
     * 匹配批量开户结果文件
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean checkOpenAccountFileRes(String fileName) {
        String regex = "^(\\d){4}\\-APPZX(\\d){4}RES-(\\d){6}-(\\d){8}$";
        return Pattern.matches(regex, fileName);
    }

    /**
     * 匹配响应结果信息
     *
     * @param str
     * @return
     */
    public static String findMessage(String str) {
        String regex = "<message>(.+?)</message>";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);//忽略大小写
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}