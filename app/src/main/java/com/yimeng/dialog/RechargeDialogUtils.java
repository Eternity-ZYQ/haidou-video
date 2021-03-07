package com.yimeng.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.yimeng.alipay.PayResult;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CashierInputFilter;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
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
 *  Time   : 2019/5/22 10:47 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 充值对话框
 *          注： 请在调用时添加`wechatPay`eventbus注解,
 * </pre>
 */
public class RechargeDialogUtils {

    private Activity mActivity;
    /**
     * 充值弹窗
     */
    private AlertDialog rechargeDialog;
    /**
     * 微信支付
     */
    private IWXAPI msgApi;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(true,EventTags.WECHAT_PAY_RESULT);
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(false,EventTags.WECHAT_PAY_RESULT);
                }

                dismissDialog();
            }
        }
    };

    public RechargeDialogUtils(Activity context) {
        this.mActivity = context;
        initView();
        msgApi = WXAPIFactory.createWXAPI(mActivity, Constants.WX_APPID);
    }

    private void initView() {

        final View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_recharge_to_wallet, null);
        final CheckBox cb_wechat = dialogView.findViewById(R.id.cb_wechat);
        final CheckBox cb_alipay = dialogView.findViewById(R.id.cb_alipay);
        // 开始充值
        final EditText et_input = dialogView.findViewById(R.id.et_input);
        InputFilter[] inputFilters = new InputFilter[]{new CashierInputFilter()};
        et_input.setFilters(inputFilters);

        dialogView.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_wechat.setChecked(true);
                cb_alipay.setChecked(false);
            }
        });

        dialogView.findViewById(R.id.ll_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_wechat.setChecked(false);
                cb_alipay.setChecked(true);
            }
        });

        dialogView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        dialogView.findViewById(R.id.btn_dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputMoney = et_input.getText().toString().trim();
                if (UnitUtil.getDouble(inputMoney) <= 0) {
                    ToastUtils.showToast("请输入金额");
                    return;
                }

                inputMoney = (long) (UnitUtil.getDouble(inputMoney) * 100.0) + "";
                String rechargeType = cb_wechat.isChecked() ? "weixin" : "zhifubao";
                doNet(rechargeType, inputMoney);
            }
        });
        rechargeDialog = new AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .create();
        rechargeDialog.setCanceledOnTouchOutside(false);
    }

    public void showDialog(){
        if(!rechargeDialog.isShowing()) {
            rechargeDialog.show();
        }
    }

    public void dismissDialog(){
        rechargeDialog.dismiss();
        rechargeDialog = null;
        mActivity = null;
    }

    private void doNet(final String rechargeType, String amt){
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rechargeType", rechargeType);
        params.put("amt", amt);
        params.put("rechargeName", "chongzhi");
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_RECHARE_ADD, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("充值失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "充值失败，请稍后重试！"));
                    return;
                }

                String data = jsonObject.get("data").getAsString();
                if(rechargeType.equals("weixin")) {
                    toWeChat(data);
                }else{
                    toAlipay(data);
                }
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void toAlipay(String data) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rechargeNo", data);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_RECHARE_ALIPAY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("充值失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "充值失败，请稍后重试！"));
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

    /**
     * 微信支付
     */
    private void toWeChat(String data) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("rechargeNo", data);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_RECHARE_WXPARAM, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("充值失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "充值失败，请稍后重试！"));
                    return;
                }
                dismissDialog();

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
                msgApi.sendReq(request);
            }
        });
    }

}
