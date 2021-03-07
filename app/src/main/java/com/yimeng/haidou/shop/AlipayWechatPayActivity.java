package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.yimeng.alipay.PayResult;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 支付中心
 *
 * @author xp
 * @describe 支付中心(暂支持alipay).
 * @date 2017/11/21.
 */

public class AlipayWechatPayActivity extends AppCompatActivity {

    private IWXAPI msgApi;

    /**
     * 支付类型
     * ConstantOther.PAYMENT_TYPE_ALIPAY
     * ConstantOther.PAYMENT_TYPE_WECHAT
     */
    private String mPayType;
    /**
     * 充值编号
     */
    private String mRechargeNo;
    /**
     * 订单编号
     */
    private String mOrderNo;
    /**
     * 麻豆
     */
    private String mScore;


    private String mVipType;

    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @SuppressLint("SetTextI18n")
    protected void initData() {

        mScore = getIntent().getStringExtra("mScore");
        mPayType = getIntent().getStringExtra("mPayType");
        mOrderNo = getIntent().getStringExtra("mOrderNo");
        mRechargeNo = getIntent().getStringExtra("mRechargeNo");

        mVipType = getIntent().getStringExtra("mVip");

        SimpleDialog.showLoadingHintDialog(this, 4);

        if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_ALIPAY)) {
            if (null == mRechargeNo) {
                if("vip".equals(mVipType)){
                    HttpParameterUtil.getInstance().requestVipOrderAlipayParam(mOrderNo, mScore, mHandler);
                }else{
                    HttpParameterUtil.getInstance().requestOrderAlipayParam(mOrderNo, mScore, mHandler);
                }
            } else {
                HttpParameterUtil.getInstance().requestAlipayParam(mRechargeNo, mHandler);
            }
        } else if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_WECHAT)) {
            msgApi = WXAPIFactory.createWXAPI(this, null);
            msgApi.registerApp(Constants.WX_APPID);

            if (null == mRechargeNo) {
                HttpParameterUtil.getInstance().requestWechatPayParams(mOrderNo, mScore, mHandler);
            } else {
                HttpParameterUtil.getInstance().requestWeixinParam(mRechargeNo, mHandler);
            }
        }

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<AlipayWechatPayActivity> mImpl;

        public MyHandler(AlipayWechatPayActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        if (isFinishing()) {
            return;
        }

        switch (msg.what) {
            case ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS:
                final String urlParameter = (String) msg.obj;

                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(AlipayWechatPayActivity.this);
                        LogUtils.v("urlParameter：" + urlParameter);

                        Map<String, String> result = alipay.payV2(urlParameter, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
            case ConstantHandler.WHAT_ALIPAY_PARAMS_FAIL:
                ToastUtils.showToast((String) msg.obj);
                SimpleDialog.cancelLoadingHintDialog();
                finish();
                break;
            case SDK_PAY_FLAG:
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    sendBroadcast(new Intent().setAction(Constants.ACTION_PAYMENT_SUCCESS));
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    sendBroadcast(new Intent().setAction(Constants.ACTION_PAYMENT_FAIL));
                }
                finish();
                break;
            default:
                break;
        }
    }

    private void toWeixinPay(ModelWXPay modelWXPay) {
        PayReq request = new PayReq();
        request.appId = modelWXPay.getAppid();
        request.partnerId = modelWXPay.getPartnerid();
        request.prepayId = modelWXPay.getPrepayid();
        request.packageValue = modelWXPay.getPackage1();
        request.nonceStr = modelWXPay.getNoncestr();
        request.timeStamp = modelWXPay.getTimestamp();
        request.sign = modelWXPay.getSign();
        msgApi.sendReq(request);
    }
}
