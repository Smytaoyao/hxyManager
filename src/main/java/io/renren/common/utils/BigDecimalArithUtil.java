package io.renren.common.utils;

import java.math.BigDecimal;

/**
 * User: zhoudengjun
 * Date: 2018/4/26
 * Time: 15:57
 * Description: 浮点数高精度加减乘除
 */
public class BigDecimalArithUtil {
    private static final int DIV_SCALE = 2;//除法精度（除不尽时保留几位小数）

    /** 小数精确加法  */
    public static double add(double d1,double d2)
    {
        BigDecimal bd1 = BigDecimal.valueOf(d1);
        BigDecimal bd2 = BigDecimal.valueOf(d2);
        return bd1.add(bd2).doubleValue();
    }

    /** 小数精确减法  */
    public static double sub(double d1,double d2)
    {
        BigDecimal bd1 = BigDecimal.valueOf(d1);
        BigDecimal bd2 = BigDecimal.valueOf(d2);
        return bd1.subtract(bd2).doubleValue();
    }

    /** 小数精确乘法  */
    public static double mul(double d1,double d2)
    {
        BigDecimal bd1 = BigDecimal.valueOf(d1);
        BigDecimal bd2 = BigDecimal.valueOf(d2);
        return bd1.multiply(bd2).doubleValue();
    }

    /** 小数精确除法   */
    public static double div(double d1,double d2)
    {
        BigDecimal bd1 = BigDecimal.valueOf(d1);
        BigDecimal bd2 = BigDecimal.valueOf(d2);
        /*
         * 当除不尽时，以四舍五入的方式（关于除不尽后的值的处理方式有很多种）保留小数点后2位小数
         */
        return bd1.divide(bd2, DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
