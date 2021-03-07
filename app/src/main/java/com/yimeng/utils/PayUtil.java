package com.yimeng.utils;

import android.content.Context;
import android.content.Intent;

import com.alipay.sdk.app.H5PayActivity;
import com.yimeng.config.Constants;
import com.yimeng.haidou.shop.AlipayWechatPayActivity;

/**
 * 支付工具
 *
 * @author xp
 * @describe 支付工具.
 * @date 2017/12/26.
 */

public class PayUtil {

    public static void toAlipayH5Pay(Context context, String orderNo) {
        String url = "";
        Intent intent = new Intent(context, H5PayActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 支付宝-订单支付
     *
     * @param context  Context
     * @param mOrderNo 订单编号
     */
    public static void toAlipayNativePayOrder(Context context, String mOrderNo, String mScore) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mOrderNo", mOrderNo);
        intent.putExtra("mScore", mScore);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_ALIPAY);
        context.startActivity(intent);
    }

    /**
     * 支付宝-充值支付
     *
     * @param context     Context
     * @param mRechargeNo 充值编号
     */
    public static void toAlipayNativePayRecharge(Context context, String mRechargeNo) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mRechargeNo", mRechargeNo);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_ALIPAY);
        context.startActivity(intent);
    }

    /**
     * 微信-订单支付
     *
     * @param context  Context
     * @param mOrderNo 订单编号
     */
    public static void toWechatNativePayOrder(Context context, String mOrderNo, String mScore) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mOrderNo", mOrderNo);
        intent.putExtra("mScore", mScore);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_WECHAT);
        context.startActivity(intent);
    }

    /**
     * 微信-充值支付
     *
     * @param context     Context
     * @param mRechargeNo 充值编号
     */
    public static void toWechatNativePayRecharge(Context context, String mRechargeNo) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mRechargeNo", mRechargeNo);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_WECHAT);
        context.startActivity(intent);
    }
}
