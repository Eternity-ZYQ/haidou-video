package com.yimeng.haidou.mine;

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

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.PayUtil;
import com.yimeng.utils.TextViewUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 充值
 *
 * @author xp
 * @describe 充值.
 * @date 2017/3/29.
 */

public class PayActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.moneyET)
    EditText moneyET;
    @Bind(R.id.wxCheckSDV)
    ImageView wxCheckSDV;
    @Bind(R.id.alipayCheckSDV)
    ImageView alipayCheckSDV;

    /**
     * 充值编号
     */
    private String mRechargeNo;
    /**
     * 支付方式
     */
    private String mPayType;
    /**
     * 微信支付
     */
    private IWXAPI msgApi;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void init() {
        initData();
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                ActivityUtils.getInstance().jumpActivity(PayRecordsActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {

    }
    private long oldTime = 0;

    @OnClick({R.id.submitBTN, R.id.wechatRL, R.id.alipayRL})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                if (new Date().getTime() - oldTime > 2000) {
                    oldTime = new Date().getTime();

                    String money = moneyET.getText().toString();

                    if (money.isEmpty()) {
                        ToastUtils.showToast("请输入充值金额");
                        return;
                    }

                    Pattern pattern = Pattern.compile("[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*");
                    Matcher match = pattern.matcher(money);
                    if (!match.matches()) {
                        ToastUtils.showToast("充值金额输入格式有误");
                        return;
                    }

                    money = (long) (UnitUtil.getDouble(money) * 100.0) + "";
                    String rechargeType = TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_WECHAT) ? "weixin" : "zhifubao";
                    HttpParameterUtil.getInstance().requestRechargeAdd(rechargeType, money, mHandler);
                }
                break;
            case R.id.wechatRL:
                mPayType = Constants.PAYMENT_TYPE_WECHAT;
                setPaymentType();
                break;
            case R.id.alipayRL:
                mPayType = Constants.PAYMENT_TYPE_ALIPAY;
                setPaymentType();
                break;
            default:
                break;
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<PayActivity> mActivity;

        public MyHandler(PayActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mActivity.get() != null) {
                mActivity.get().disposeData(msg);
            }
        }
    }

    /**
     * 处理数据
     */
    private void disposeData(final Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_RECHARE_ADD_SUCCESS:
                mRechargeNo = (String) msg.obj;// 充值编号
                if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_WECHAT)) {
                    HttpParameterUtil.getInstance().requestWeixinParam(mRechargeNo, mHandler);
                } else {
                    PayUtil.toAlipayNativePayRecharge(this, mRechargeNo);
                }
                break;
            case ConstantHandler.WHAT_RECHARE_ADD_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS:
                // 获取充值信息成功
                final ModelWXPay modelWXPay = (ModelWXPay) msg.obj;
                PayReq request = new PayReq();
                request.appId = modelWXPay.getAppid();
                request.partnerId = modelWXPay.getPartnerid();
                request.prepayId = modelWXPay.getPrepayid();
                request.packageValue = modelWXPay.getPackage1();
                request.nonceStr = modelWXPay.getNoncestr();
                request.timeStamp = modelWXPay.getTimestamp();
                request.sign = modelWXPay.getSign();
                msgApi.sendReq(request);
                break;
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_CANCEL_PAY_SUCCESS:
                break;
            case ConstantHandler.WHAT_CANCEL_PAY_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    protected void initData() {
        mPayType = Constants.PAYMENT_TYPE_WECHAT;
        setPaymentType();

        TextViewUtil.setLimitTwoDecimalPlaces(moneyET);

        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.WX_APPID);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAYMENT_SUCCESS);
        filter.addAction(Constants.ACTION_PAYMENT_FAIL);
        registerReceiver(mPaymentReceiver, filter);
    }

    private void setPaymentType() {
        if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_ALIPAY)) {
            alipayCheckSDV.setImageResource(R.mipmap.select_circle);
            wxCheckSDV.setImageResource(R.mipmap.noselect_circle);
        } else {
            alipayCheckSDV.setImageResource(R.mipmap.noselect_circle);
            wxCheckSDV.setImageResource(R.mipmap.select_circle);
        }
    }

    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
                ToastUtils.showToast("支付成功");
                ActivityUtils.getInstance().jumpSubmitResult(2);
                finish();
            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
                ToastUtils.showToast("支付失败");
                HttpParameterUtil.getInstance().requestCancelPay(mRechargeNo, mHandler);
            }
        }
    };
}
