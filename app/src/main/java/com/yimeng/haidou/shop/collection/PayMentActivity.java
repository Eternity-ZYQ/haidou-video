package com.yimeng.haidou.shop.collection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alipay.sdk.app.PayTask;
import com.yimeng.alipay.PayResult;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.entity.QrCodeResult;
import com.yimeng.entity.ResultBase;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.EditTextUtil;
import com.yimeng.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.lxj.xpopup.core.BasePopupView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * 扫码进行付款
 *
 * @author yangjie
 */
public class PayMentActivity extends BaseActivity {

    @Bind(R.id.edMoney)
    EditText edMoney;
    @Bind(R.id.checkbox_wechat_pay_normal)
    ImageView checkboxWechatPayNormal;
    @Bind(R.id.checkbox_alipay_normal)
    ImageView checkboxAlipayNormal;
    private double money = 0;
    private String shopNo;
    private OkHttpCommon mOkHttpCommon;

    //支付方式
    private int payType = 2; //-1 未设置   2微信支付，3支付宝支付，

    private MyHandler mHandler = new MyHandler(this);

    /**
     * 微信支付
     */
    private IWXAPI msgApi;

    /**
     * 支付对话框
     */
    private BasePopupView mPayLoadingDialog;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_pay_ment;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String money = intent.getStringExtra(Constants.INTENT_PARAM_MONEY);
        shopNo = intent.getStringExtra(Constants.INTENT_PARAM_SHOPNO);
        if (money != null && !money.isEmpty()) {
            edMoney.setText(money);
            edMoney.setEnabled(false);
            this.money = Double.parseDouble(money);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAYMENT_SUCCESS);
        filter.addAction(Constants.ACTION_PAYMENT_FAIL);
        registerReceiver(mPaymentReceiver, filter);
        mOkHttpCommon = new OkHttpCommon();

        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.WX_APPID);

    }

    @Override
    protected void initListener() {
//限制输入位数
        EditTextUtil.keepTwoDecimals(edMoney, 2,7);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPaymentReceiver);
    }

    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void onPayResult(boolean flag) {
        if (flag) {
            SimpleDialog.showLoadingHintDialog(this, 1);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPayLoadingDialog.dismiss();
                    finish();
                    ToastUtils.showToast("支付成功");
                }
            }, 1000);

        } else {
            ToastUtils.showToast("支付失败");
            mPayLoadingDialog.dismiss();
        }
    }

    /**
     * 支付对话框
     */
    private void showPayLoading() {
        if (mPayLoadingDialog == null) {
            mPayLoadingDialog = SimpleDialog.createDialog(this, getString(R.string.pay_loading), false, true, true);
        }
        mPayLoadingDialog.show();
    }


    @OnClick({R.id.toolBar_icon, R.id.btPayment, R.id.rl_wechat_pay, R.id.rl_alipay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolBar_icon:
                finish();
                break;
            case R.id.btPayment:

                String strMoney = edMoney.getText().toString();
                if (!strMoney.isEmpty()) {
                    money = Double.parseDouble(strMoney);
                }

                if (money == 0) {
                    ToastUtils.showToast("请输入金额");
                    return;
                }

                showPayLoading();


//                SimpleDialog.showLoading(this, "正在加载...");
                Map<String, String> params = new HashMap<>();
                params.put("token", CommonUtils.getToken());
                params.put("shopNo", shopNo);
                params.put("amt", (int) (money * 100) + "");


                String reqUrl = ConstantsUrl.API_HOST + "reward/makePaymentToMerchant/";


                mOkHttpCommon.postLoadData(this, reqUrl, params, new CallbackCommon() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.showToast(R.string.text_network_connected_error);
                    }

                    @Override
                    public void onResponse(Call call, JsonObject jsonObject) throws IOException {

                        LogUtils.v("支付请求结果：" + jsonObject.toString());
                        Gson gson = new Gson();
                        QrCodeResult qrCodeResult = gson.fromJson(jsonObject, QrCodeResult.class);
                        if (qrCodeResult.getStatus() != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                            mPayLoadingDialog.dismiss();

                            return;
                        }


                        requestWeChatPay(qrCodeResult.getData().getRewardNo());

                    }
                });
                break;
            case R.id.rl_wechat_pay:
                payType = 2;
                //微信支付
                checkboxWechatPayNormal.setImageResource(R.mipmap.select_circle);
                checkboxAlipayNormal.setImageResource(R.mipmap.noselect_circle);
                break;
            case R.id.rl_alipay:
                payType = 3;
                //支付宝支付
                checkboxAlipayNormal.setImageResource(R.mipmap.select_circle);
                checkboxWechatPayNormal.setImageResource(R.mipmap.noselect_circle);
                break;
            default:
                break;

        }
    }

    public void requestWeChatPay(String rewardNo) {
        Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> observableEmitter) throws Exception {
                Map<String, String> params = new HashMap<>();
                params.put("token", CommonUtils.getToken());
                params.put("rewardNo", rewardNo);


                String reqUrl;
                if (payType == 2) {
                    reqUrl = ConstantsUrl.API_HOST + "reward/weixinParam/";
                } else {
                    reqUrl = ConstantsUrl.API_HOST + "reward/zhifubaoParam";
                }


                mOkHttpCommon.postLoadData(PayMentActivity.this, reqUrl, params, new CallbackCommon() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        observableEmitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                        observableEmitter.onNext(jsonObject);
                    }
                });

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JsonObject jsonObject) {
                LogUtils.v("支付请求结果：" + jsonObject.toString());
                Gson gson = new Gson();


                if (payType == 2) {
                    ResultBase<ModelWXPay> payResultBase = gson.fromJson(jsonObject, new TypeToken<ResultBase<ModelWXPay>>() {
                    }.getType());

                    if (payResultBase.getStatus() != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                        if (mPayLoadingDialog != null) {
                            mPayLoadingDialog.dismiss();
                        }
                        return;
                    }

                    PayReq request = new PayReq();
                    ModelWXPay modelWxPay = payResultBase.getData();
                    request.appId = modelWxPay.getAppid();
                    request.partnerId = modelWxPay.getPartnerid();
                    request.prepayId = modelWxPay.getPrepayid();
//                    request.packageValue = modelWxPay.getPackage1();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = modelWxPay.getNoncestr();
                    request.timeStamp = modelWxPay.getTimestamp();
                    request.sign = modelWxPay.getSign();
                    LogUtils.v("modelWxPay：" + modelWxPay.toString());

                    msgApi.sendReq(request);

                } else {
                    ResultBase<String> payResultBase = gson.fromJson(jsonObject, new TypeToken<ResultBase<String>>() {
                    }.getType());

                    if (payResultBase.getStatus() != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                        return;
                    }

                    aliPay(payResultBase.getData());
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
                if (mPayLoadingDialog != null) {
                    mPayLoadingDialog.dismiss();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void aliPay(String payParam) {
        Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> e) throws Exception {
                PayTask alipay = new PayTask(PayMentActivity.this);

                LogUtils.v("myurlParameter：" + payParam);

                e.onNext(alipay.payV2(payParam, true));

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Map<String, String> result) {
                PayResult payResult = new PayResult(result);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ToastUtils.showToast("支付成功");

                    mPayLoadingDialog.dismiss();
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtils.showToast(result.get("memo"));
                    mPayLoadingDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                mPayLoadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static class MyHandler extends Handler {

        private WeakReference<PayMentActivity> mImpl;

        public MyHandler(PayMentActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }


    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
//                ToastUtils.showToast("支付成功");
//                finish();
//            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
//                ToastUtils.showToast("支付失败");
//            }
        }
    };


}