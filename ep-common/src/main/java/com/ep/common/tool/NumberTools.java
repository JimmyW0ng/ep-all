package com.ep.common.tool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * NumberUtil 数字工具类
 */


public class NumberTools {

    //活期利息
    public static final BigDecimal CURRENT_DEPOSIT_INTEREST = new BigDecimal("3.56");
    private static final BigDecimal YEAR_DAYS = new BigDecimal(36500);
    private static final BigDecimal MONTH_LEN = new BigDecimal(1200);
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final BigDecimal THOUSAND = new BigDecimal(1000);

    public static BigDecimal parseBigDecimal(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseBigDecimal((String) o);
        }
        if ("java.lang.Short".equals(className)) {
            return parseBigDecimal((Short) o);
        }
        if ("java.lang.Integer".equals(className)) {
            return parseBigDecimal((Integer) o);
        }
        if ("java.lang.Long".equals(className)) {
            return parseBigDecimal((Long) o);
        }
        if ("java.lang.Float".equals(className)) {
            return parseBigDecimal((Float) o);
        }
        if ("java.lang.Double".equals(className)) {
            return parseBigDecimal((Double) o);
        }
        if ("java.math.BigDecimal".equals(className)) {
            return ((BigDecimal) o);
        }

        return null;

    }

    public static BigDecimal parseBigDecimal(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new BigDecimal(s.trim());
    }

    public static BigDecimal parseBigDecimal(Short s) {

        if (s == null) {
            return null;
        }
        return new BigDecimal(s.shortValue());
    }

    public static BigDecimal parseBigDecimal(Integer i) {

        if (i == null) {
            return null;
        }
        return new BigDecimal(i.intValue());
    }

    public static BigDecimal parseBigDecimal(Long l) {

        if (l == null) {
            return null;
        }
        return new BigDecimal(l.longValue());
    }

    public static BigDecimal parseBigDecimal(Float f) {

        if (f == null) {
            return null;
        }
        return new BigDecimal(f.floatValue());
    }

    public static BigDecimal parseBigDecimal(Double d) {

        if (d == null) {
            return null;
        }
        return new BigDecimal(d.doubleValue());
    }

    public static BigDecimal parseBigDecimal(BigDecimal bd) {

        return bd;
    }


    public static Double parseDouble(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseDouble((String) o);
        }
        if ("java.lang.Short".equals(className)) {
            return parseDouble((Short) o);
        }

        if ("java.lang.Integer".equals(className)) {
            return parseDouble((Integer) o);
        }
        if ("java.lang.Long".equals(className)) {
            return parseDouble((Long) o);
        }
        if ("java.lang.Float".equals(className)) {
            return parseDouble((Float) o);
        }
        if ("java.lang.Double".equals(className)) {
            return ((Double) o);
        }
        if ("java.math.BigDecimal".equals(className)) {
            return parseDouble((BigDecimal) o);
        }

        return null;

    }

    public static Double parseDouble(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new Double(s.trim());
    }

    public static Double parseDouble(Short s) {

        if (s == null) {
            return null;
        }
        return new Double(s.shortValue());
    }

    public static Double parseDouble(Integer i) {

        if (i == null) {
            return null;
        }
        return new Double(i.intValue());
    }

    public static Double parseDouble(Long l) {

        if (l == null) {
            return null;
        }
        return new Double(l.longValue());
    }

    public static Double parseDouble(Float f) {

        if (f == null) {
            return null;
        }
        return new Double(f.floatValue());
    }

    public static Double parseDouble(Double d) {

        return d;
    }

    public static Double parseDouble(BigDecimal bd) {

        if (bd == null) {
            return null;
        }
        return new Double(bd.doubleValue());
    }


    public static Float parseFloat(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseFloat((String) o);
        }
        if ("java.lang.Short".equals(className)) {
            return parseFloat((Short) o);
        }
        if ("java.lang.Integer".equals(className)) {
            return parseFloat((Integer) o);
        }
        if ("java.lang.Long".equals(className)) {
            return parseFloat((Long) o);
        }
        if ("java.lang.Float".equals(className)) {
            return ((Float) o);
        }
        if ("java.lang.Double".equals(className)) {
            return parseFloat((Double) o);
        }
        if ("java.math.BigDecimal".equals(className)) {
            return parseFloat((BigDecimal) o);
        }

        return null;

    }

    public static Float parseFloat(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new Float(s.trim());
    }

    public static Float parseFloat(Short s) {

        if (s == null) {
            return null;
        }
        return new Float(s.shortValue());
    }

    public static Float parseFloat(Integer i) {

        if (i == null) {
            return null;
        }
        return new Float(i.intValue());
    }

    public static Float parseFloat(Long l) {

        if (l == null) {
            return null;
        }
        return new Float(l.longValue());
    }

    public static Float parseFloat(Float f) {

        return f;
    }

    public static Float parseFloat(Double d) {

        if (d == null) {
            return null;
        }
        return new Float(d.doubleValue());
    }

    public static Float parseFloat(BigDecimal bd) {

        if (bd == null) {
            return null;
        }
        return new Float(bd.floatValue());
    }


    public static Integer parseInteger(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseInteger((String) o);
        }

        if ("java.lang.Short".equals(className)) {
            return parseInteger((Short) o);
        }
        if ("java.lang.Integer".equals(className)) {
            return ((Integer) o);
        }

        if ("java.lang.Long".equals(className)) {
            return parseInteger((Long) o);
        }
        if ("java.lang.Float".equals(className)) {
            return parseInteger((Float) o);
        }
        if ("java.lang.Double".equals(className)) {
            return parseInteger((Double) o);
        }

        if ("java.math.BigDecimal".equals(className)) {
            return parseInteger((BigDecimal) o);
        }
        return null;

    }

    public static Integer parseInteger(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new Integer(s.trim());
    }

    public static Integer parseInteger(Short s) {

        if (s == null) {
            return null;
        }
        return new Integer(s.shortValue());
    }

    public static Integer parseInteger(Integer i) {

        return i;
    }

    public static Integer parseInteger(Long l) {

        if (l == null) {
            return null;
        }
        return new Integer(l.intValue());
    }

    public static Integer parseInteger(Float f) {

        if (f == null) {
            return null;
        }
        return new Integer(f.intValue());
    }

    public static Integer parseInteger(Double d) {

        if (d == null) {
            return null;
        }
        return new Integer(d.intValue());
    }

    public static Integer parseInteger(BigDecimal bd) {

        if (bd == null) {
            return null;
        }
        return new Integer(bd.intValue());
    }


    public static Long parseLong(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseLong((String) o);
        }
        if ("java.lang.Short".equals(className)) {
            return parseLong((Short) o);
        }
        if ("java.lang.Integer".equals(className)) {
            return parseLong((Integer) o);
        }
        if ("java.lang.Long".equals(className)) {
            return (Long) o;
        }
        if ("java.lang.Float".equals(className)) {
            return parseLong((Float) o);
        }
        if ("java.lang.Double".equals(className)) {
            return parseLong((Double) o);
        }

        if ("java.math.BigDecimal".equals(className)) {
            return parseLong((BigDecimal) o);
        }

        return null;

    }

    public static Long parseLong(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new Long(Long.parseLong(s.trim()));
    }

    public static Long parseLong(Short s) {

        if (s == null) {
            return null;
        }
        return new Long(s.longValue());
    }

    public static Long parseLong(Integer i) {

        if (i == null) {
            return null;
        }
        return new Long(i.longValue());
    }

    public static Long parseLong(Long l) {

        return l;
    }

    public static Long parseLong(Float f) {

        if (f == null) {
            return null;
        }
        return new Long(f.longValue());
    }

    public static Long parseLong(Double d) {

        if (d == null) {
            return null;
        }
        return new Long(d.longValue());
    }

    public static Long parseLong(BigDecimal bd) {

        if (bd == null) {
            return null;
        }
        return new Long(bd.longValue());
    }

    public static Short parseShort(Object o) {
        if (o == null) {
            return null;
        }
        String className = o.getClass().getName();

        if ("java.lang.String".equals(className)) {
            return parseShort((String) o);
        }
        if ("java.lang.Short".equals(className)) {
            return ((Short) o);
        }
        if ("java.lang.Integer".equals(className)) {
            return parseShort((Integer) o);
        }
        if ("java.lang.Long".equals(className)) {
            return parseShort((Long) o);
        }
        if ("java.lang.Float".equals(className)) {
            return parseShort((Float) o);
        }

        if ("java.lang.Double".equals(className)) {
            return parseShort((Double) o);
        }

        if ("java.lang.BigDecimal".equals(className)) {
            return parseShort((BigDecimal) o);
        }

        return null;

    }

    public static Short parseShort(String s) {
        if (StringTools.isBlank(s)) {
            return null;
        }
        return new Short(s.trim());
    }

    public static Short parseShort(Short s) {

        return s;
    }

    public static Short parseShort(Integer i) {

        if (i == null) {
            return null;
        }
        return new Short(i.shortValue());
    }

    public static Short parseShort(Long l) {

        if (l == null) {
            return null;
        }
        return new Short(l.shortValue());
    }

    public static Short parseShort(Float f) {

        if (f == null) {
            return null;
        }
        return new Short(f.shortValue());
    }

    public static Short parseShort(Double d) {

        if (d == null) {
            return null;
        }
        return new Short(d.shortValue());
    }

    public static Short parseShort(BigDecimal bd) {

        if (bd == null) {
            return null;
        }
        return new Short(bd.shortValue());
    }

    /**
     * Double类型数据转换成double类型数据
     *
     * @param d -Double类型数据
     * @return double类型数据
     */
    public static double doubleValue(Double d) {
        if (d == null)
            return 0.0;
        else
            return d.doubleValue();
    }

    /**
     * Integer类型数据转换成int类型数据
     *
     * @param i -Integer类型数据
     * @return int类型数据
     */
    public static int intValue(Integer i) {
        if (i == null)
            return 0;
        else
            return i.intValue();
    }

    /**
     * Short类型数据转换成short类型数据
     *
     * @param s -Short类型数据
     * @return short类型数据
     */
    public static short shortValue(Short s) {
        short sh = 0;
        if (s != null)
            return s.shortValue();
        else
            return sh;
    }

    /**
     * Long类型数据转换成long类型数据
     *
     * @param l
     * @return
     */
    public static long longValue(Long l) {
        if (l != null)
            return l.longValue();
        else
            return 0;
    }

    /**
     * Float类型数据转换成float类型数据
     *
     * @param f
     * @return
     */
    public static float floatValue(Float f) {
        if (f != null)
            return f.floatValue();
        else
            return 0;
    }

    /**
     * 判断对象是否为空或零
     *
     * @param obj -对象
     * @return boolean  true:为空或零 false 不为空或零
     */
    public static boolean isNullOrZero(Object obj) {
        if (obj == null)
            return true;
        String str = obj.toString();
        if (str.equals("")) {
            return true;
        } else {
            BigDecimal bd = new BigDecimal(str).setScale(10);
            BigDecimal zero = new BigDecimal("0.0").setScale(10);
            return bd.compareTo(zero) == 0;
        }
    }

    /**
     * 比较Long类型的值是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean compareLong(Long a, Long b) {
        boolean result = false;
        if (a == null || b == null)
            return result;

        if (a.longValue() == b.longValue())
            result = true;
        return result;
    }

    public static boolean compareBigDecimal(BigDecimal a, BigDecimal b) {
        if (a == null || b == null)
            return false;
        return a.compareTo(b) > 0;
    }

    /**
     * Double,Float空值处理
     *
     * @param d            Object(Double,Float)
     * @param defaultValue double
     * @return double
     */
    public static double getDoubleValue(Object d, double defaultValue) {
        if (d == null) {
            return defaultValue;
        } else {
            if (d instanceof Double)
                return ((Double) d).doubleValue();
            else if (d instanceof Float)
                return Double.parseDouble(d.toString());
            else
                return defaultValue;
        }
    }

    /**
     * Long,Integer空值处理
     *
     * @param d
     * @param defaultValue int
     * @return int
     */
    public static int getIntValue(Object d, int defaultValue) {
        if (d == null) {
            return defaultValue;
        } else {
            if (d instanceof Long)
                return ((Long) d).intValue();
            else if (d instanceof Integer)
                return ((Integer) d).intValue();
            else
                return defaultValue;
        }
    }

    /**
     * 判断String 是否可以转换为Integer
     *
     * @param str
     * @return
     */
    public static boolean isIntegerToString(String str) {
        boolean bean = false;
        try {
            Integer.parseInt(str);
            bean = true;

        } catch (NumberFormatException e) {
            return bean;
        }
        return bean;
    }

    /**
     * 判断String 是否可以转换为Long
     *
     * @param str
     * @return
     */
    public static boolean isLongToString(String str) {
        boolean bean = false;
        try {
            Long.parseLong(str);
            bean = true;

        } catch (NumberFormatException e) {
            return bean;
        }
        return bean;
    }

    /**
     * 格式化数字
     */
    public static BigDecimal formatDouble(Double d) {
        Format format = new DecimalFormat("#0.00");
        return new BigDecimal(format.format(d));
    }

    public static boolean eqCompareInteger(Integer a, Integer b) {
        if (a == null || b == null) {
            return false;
        }
        return a.intValue() == b.intValue();
    }

    public static boolean nEqCompareInteger(Integer a, Integer b) {
        if (a == null || b == null) {
            return false;
        }
        return !(a.intValue() == b.intValue());
    }

    public static boolean eqCompareLong(Long a, Long b) {
        if (a == null || b == null) {
            return false;
        }
        int i = a.compareTo(b);
        if (i == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean nEqCompareLong(Long a, Long b) {
        return !eqCompareLong(a, b);
    }

    /**
     * 格式化金额
     *
     * @param price
     * @return
     */
    public static String getFormatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(price);
    }

    /**
     * 格式化金额省略小数位的0
     *
     * @param price
     * @return
     */
    public static String getFormatPriceCutEndZero(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("###,###.##").format(price);
    }

    /**
     * 格式化金额
     *
     * @param price
     * @return symbol+###,###.##
     */
    public static String getFormatPriceWithSymbol(String symbol, BigDecimal price) {
        if (price == null) {
            return "";
        }
        return symbol + new DecimalFormat("###,###.##").format(price);
    }

    /**
     * 格式化金额
     *
     * @param price
     * @return
     * @Deprecated 此方法内#占位符，当匹配为0时，返回空，例如0.01格式化为.01
     */
    @Deprecated
    public static String getFormatPriceDefaultZero(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("###,##0.00").format(price);
    }

    /**
     * 格式化金额，中间无分隔符“,”
     */
    public static String getFormatPriceNoSep(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("######.##").format(price);
    }

    /**
     * 格式化金额，中间无分隔符“,”,结尾默认为00
     */
    public static String getFormatPriceNoSepDefaultZero(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("######.00").format(price);
    }

    /**
     * 格式化金额
     *
     * @param price
     * @return
     */
    public static String getFormatPriceRound(BigDecimal price) {
        if (price == null) {
            return "";
        }
        String formatPrice = new DecimalFormat("###.00").format(price);
        if (formatPrice.startsWith(".")) {
            return "0" + formatPrice;
        }
        return formatPrice;
    }

    /**
     * 格式化金额
     *
     * @param price 5000
     * @return ¥5000.00
     */
    public static String formatCurrency(BigDecimal price) {
        if (price == null) {
            return "¥0.0";
        }
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.CHINA);
        String format = currency.format(price);
        String result = "";
        if (price.compareTo(BigDecimal.ZERO) >= 0) {
            result = "¥" + format.substring(1);
        } else {
            result = "¥-" + format.substring(2);
        }
        return result;

    }

    /**
     * 格式化金额 ,不带 人民币符号
     *
     * @param price 5000
     * @return 5000.00 author: pengyong 下午4:26:42
     */
    public static String formatCurrencyNoUnit(BigDecimal price) {
        String formatCurrency = formatCurrency(price);
        String format = formatCurrency.substring(1);
        return format;

    }

    /**
     * 获得金额整数部分，默认为零
     *
     * @param price
     * @return
     */
    public static String getIntegerDefaultZero(BigDecimal price) {
        String _price = getInteger(price);
        if (StringTools.isNotBlank(_price)) {
            return _price;
        }
        return "0";
    }

    /**
     * 获得金额小数部分，默认为零
     *
     * @param price
     * @return
     */
    public static String getDecimalDefaultZero(BigDecimal price) {
        String _price = getDecimal(price);
        if (StringTools.isNotBlank(_price)) {
            return _price;
        }
        return ".00";
    }

    /**
     * 获得整数部分
     *
     * @param price
     * @return
     */
    public static String getInteger(BigDecimal price) {
        if (price == null) {
            return "";
        }
        //String p = getFormatPriceDefaultZero(price);
        String p = formatCurrencyNoUnit(price);
        if (p.lastIndexOf(".") > 0) {
            return p.substring(0, p.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 获得小数部分
     *
     * @param price
     * @return
     */
    public static String getDecimal(BigDecimal price) {
        if (price == null) {
            return "";
        }
        //String decimal = getFormatPriceDefaultZero(price);
        String decimal = formatCurrencyNoUnit(price);
        if (decimal.lastIndexOf(".") >= 0) {
            return decimal.substring(decimal.lastIndexOf("."), decimal.length());
        }
        return "";
    }

    /**
     * 格式化百分比
     *
     * @param percent 0.12
     * @return 12 author: pengyong 下午4:29:09
     */
    public static String getFormartPercentage(BigDecimal percent) {
        if (percent == null) {
            return "";
        }
        NumberFormat formart = NumberFormat.getPercentInstance();
        String format = formart.format(percent);
        return format.substring(0, format.length() - 1);
    }

    public static double doubleAdd(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 格式化金额
     *
     * @param price
     * @return
     */
    public static String getTempFormatPrice(Integer price) {
        if (price == null) {
            return "";
        }
        return new DecimalFormat("###,###.##").format(price);
    }

    /**
     * 格式化金额,取亿
     *
     * @param price
     * @return author: zhanghao
     */
    public static String getFormatPriceHundredMillion(String price) {
        String priceStrNoSeq = price.replace(",", "");

        if (StringTools.isBlank(priceStrNoSeq)) {
            return "0";
        }

        BigDecimal priceBig = new BigDecimal(priceStrNoSeq);


        String priceStr = getFormatPriceNoSepDefaultZero(priceBig);

        if (priceStr.indexOf(".") < 8) {
            return "0";
        } else {
            return priceStr.substring(0, priceStr.indexOf(".") - 8);
        }

    }

    /**
     * 格式化金额,取万
     *
     * @param price
     * @return author: zhanghao
     */
    public static String getFormatPriceTenThousand(String price) {
        String priceStrNoSeq = price.replace(",", "");
        if (StringTools.isBlank(priceStrNoSeq)) {
            return "0";
        }
        BigDecimal priceBig = new BigDecimal(priceStrNoSeq);
        String priceStr = getFormatPriceNoSepDefaultZero(priceBig);
        if (priceStr.indexOf(".") < 4) {
            return "0";
        } else {
            return priceStr.substring(0, priceStr.indexOf(".") - 4);
        }

    }

    /**
     * @param big1 被运算的数
     * @param str  运算表达式：例如 *=0.005 返回 bigDecimal * 0.005
     * @return
     * @Description:TODO
     * @author: wangyanji
     * @time:2016年3月17日 上午11:47:01
     */
    public static BigDecimal arithmeticByString(BigDecimal big1, String str) {
        String[] array = str.split("=");
        BigDecimal big2 = new BigDecimal(array[1]);
        if ("*".equals(array[0])) {
            return big1.multiply(big2);
        }
        return null;
    }

    /**
     * 金额相加
     */
    public static BigDecimal addDecimal(BigDecimal... addEle) {
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < addEle.length; i++) {
            if (addEle[i] == null) {
                addEle[i] = BigDecimal.ZERO;
            }
            result = result.add(addEle[i]);
        }
        return result;
    }

    public static BigDecimal subtractDecimal(BigDecimal firstEle, BigDecimal secondEle) {
        if (firstEle == null) {
            firstEle = BigDecimal.ZERO;
        }
        if (secondEle == null) {
            secondEle = BigDecimal.ZERO;
        }
        return firstEle.subtract(secondEle);

    }

}