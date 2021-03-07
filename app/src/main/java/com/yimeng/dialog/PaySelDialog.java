package com.yimeng.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alipay.sdk.app.PayTask;
import com.yimeng.alipay.PayResult;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.enums.PayType;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/29 3:37 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 支付方式选择弹窗
 * </pre>
 */
public class PaySelDialog extends CenterPopupView {
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(true, mEventBusTag);
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(false, mEventBusTag);
                }
            }
        }
    };
    private Activity mActivity;
    private String mRewardNo;
    private PayType[] mPayTypes;
    private String mEventBusTag;
    /**
     * 微信支付
     */
    private IWXAPI msgApi;

    public PaySelDialog(@NonNull Context context, @NonNull String rewardNo, String callbackTag, PayType... payTypes) {
        super(context);
        this.mActivity = (Activity) context;
        this.mRewardNo = rewardNo;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.WX_APPID);
        mEventBusTag = callbackTag;

        if(payTypes == null ||payTypes.length == 0) {
            mPayTypes = new PayType[]{PayType.ALIPAY, PayType.WECHAT};
        }else {
            mPayTypes = payTypes;
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pay_sel;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView ivYuePay = findViewById(R.id.iv_yue_pay);
        ImageView ivWechatPay = findViewById(R.id.iv_wechat_pay);
        ImageView ivAliPay = findViewById(R.id.iv_alipay);

        for (PayType type : mPayTypes) {
            switch (type) {
                case YU_E:
                    ivYuePay.setVisibility(VISIBLE);
                    break;
                case ALIPAY:
                    ivAliPay.setVisibility(VISIBLE);
                    break;
                case WECHAT:
                    ivWechatPay.setVisibility(VISIBLE);
                    break;
                default:
            }
        }
        ivYuePay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(R.string.pay_loading);
                toYuepay();
                dismiss();
            }
        });
        ivWechatPay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                ToastUtils.showToast(R.string.pay_loading);
                toWeChat();
                dismiss();
            }
        });
        ivAliPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(R.string.pay_loading);
                toAlipay();
                dismiss();
            }
        });

    }

    /**
     * 余额支付
     */
    private void toYuepay(){
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rewardNo", mRewardNo);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.GET_REWARD_YUE_PARAMS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("支付失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "支付失败，请稍后重试！"));
                    return;
                }
                EventBus.getDefault().post(true, mEventBusTag);
            }
        });
    }

    /**
     * 微信支付
     */
    private void toWeChat() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rewardNo", mRewardNo);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.GET_REWARD_WECHAT_PARAMS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("支付失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "支付失败，请稍后重试！"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                String appid = data.get("appid").getAsString(); // appid
                String partnerid = data.get("partnerid").getAsString(); // 商户号
                String prepayid = data.get("prepayid").getAsString(); // 预支付交易会话ID
                String package1 = "Sign=WXPay"; // 扩展字段
                String noncestr = data.get("noncestr").getAsString(); // 随机字符串
                String timestamp = data.get("timestamp").getAsString(); // 时间戳
                String sign = data.get("sign").getAsString(); // 签名

                PayReq request = new PayReq();
                request.appId = appid;
                request.partnerId = partnerid;
                request.prepayId = prepayid;
                request.packageValue = package1;
                request.nonceStr = noncestr;
                request.timeStamp = timestamp;
                request.sign = sign;
                request.extData = mEventBusTag;
                msgApi.sendReq(request);
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void toAlipay() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rewardNo", mRewardNo);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.GET_REWARD_ALIPAY_PARAMS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("支付失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "支付失败，请稍后重试！"));
                    return;
                }

                final String data = jsonObject.get("data").getAsString();
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(mActivity);
                        Map<String, String> result = alipay.payV2(data, true);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        });
    }


}
