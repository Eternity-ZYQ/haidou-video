package com.yimeng.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Author : huiGer
 * Time   : 2018/11/6 0006 下午 07:18.
 * Desc   : 单位工具
 */

public class UnitUtil {

    /**
     * 得到int
     *
     * @param num String
     * @return
     */
    public static int getInt(String num) {
        int n;
        try {
            num = num == null ? "" : num;
            n = Integer.parseInt(num.trim());
        } catch (NumberFormatException e) {
            n = (int) getFloat(num);
        }
        return n;
    }

    /**
     * 得到float
     *
     * @param num String
     * @return
     */
    public static float getFloat(String num) {
        float n;
        try {
            n = Float.parseFloat(num);
        } catch (NumberFormatException e) {
            n = 0;
        }
        return n;
    }

    /**
     * 得到Long
     *
     * @param num String
     * @return
     */
    public static long getLong(String num) {
        long n;
        try {
            n = Long.parseLong(num);
        } catch (NumberFormatException e) {
            n = 0;
        }
        return n;
    }

    /**
     * 得到俩位小数点数据
     *
     * @param num 3.1415926
     * @return 3.14
     */
    public static String get2DecimalPointData(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    /**
     * 获取人民币格式
     *
     * @param num 31415926
     * @return 31, 415, 926
     */
    public static String getRMBFormat(double num) {
        if (num < 1) {
            return "¥" + get2DecimalPointData(num);
        }
        DecimalFormat df = new DecimalFormat("¥,###.00");
        return df.format(num);
    }

    /**
     * 小数点后面字数变小
     *
     * @param money
     * @return
     */
    public static SpannableString formatRMB(String money) {
        SpannableString ss = new SpannableString(money);
        if (money.contains(".")) {
            ss.setSpan(new RelativeSizeSpan(0.6f), money.indexOf("."), money.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    /**
     * 默认单位为分, 转为单位为元
     *
     * @param money
     * @return
     */
    public static String getMoney(String money) {
        return getMoney(money, true);
    }

    /**
     * @param money   默认单位为分, 转为单位为元
     * @param hasUnit 是否包含人民币符号
     * @return
     */
    public static String getMoney(String money, boolean hasUnit) {
        double dMoney = getDouble(money) / 100.0;
        return (hasUnit ? "¥" : "") + get2DecimalPointData(dMoney);
    }

    /**
     * 金额超过万，万单位
     * @param money 分
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatMoney(String money, boolean hasUnit) {
        double dMoney = getDouble(money) / 100.0;
        if (dMoney > 10000) {
            return (hasUnit ? "¥" : "") + String.format("%.2f", dMoney / 10000.0) + "万";
        }
        return (hasUnit ? "¥" : "") + String.format("%.2f", dMoney);
    }

    /**
     * 得到Double
     *
     * @param num String
     * @return
     */
    public static double getDouble(String num) {
        double n;
        try {
            n = Double.parseDouble(num);
        } catch (NumberFormatException e) {
            n = 0;
        }
        return n;
    }

    /**
     * 现金财富,现金2倍
     *
     * @param money 单位(分)
     * @return 现金财富
     */
    public static int getJFMoney(String money) {
        return getInt(money) / 100 * 2;
    }

    /**
     * 送money现金财富
     *
     * @param ctx
     * @param money
     * @return
     */
    public static String getJFMoneyStr(Context ctx, String money) {
//        return ctx.getString(R.string.sendXGift, getJFMoney(money));
        return "";
    }

    /**
     * 获取金额
     *
     * @param money 原始金额 单位：元
     * @return 返回金额 单位：分
     */
    public static String commitMoney(String money) {
        double num = getDouble(money);
        num *= 100;
        BigDecimal bigDecimal = new BigDecimal(num);
        String result = BigDecimalUtil.round(bigDecimal.toPlainString(), 0);
        return result;
    }


}

