package com.yimeng.haidou.mine;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.yimeng.alipay.AuthResult;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.dialog.RechargeDialogUtils;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelWallet;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 提现
 *
 * @author xp
 * @describe 提现.
 * @date 2017/3/29.
 */

public class WithdrawDeposit2Activity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.moneyET)
    EditText moneyET;
    @Bind(R.id.withdraw_service_charge_tv)
    TextView withdraw_service_charge_tv;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_wechat_money)
    TextView tvWechatMoney;
    @Bind(R.id.tv_alipay_money)
    TextView tvAlipayMoney;
    @Bind(R.id.cb_wechat)
    AppCompatCheckBox cbWechat;
    @Bind(R.id.tv_bind_wechat)
    TextView tvBindWechat;
    @Bind(R.id.cb_alipay)
    AppCompatCheckBox cbAlipay;
    @Bind(R.id.tv_bind_alipay)
    TextView tvBindAlipay;
    @Bind(R.id.tv_wechat_info)
    TextView tv_wechat_info;
    @Bind(R.id.tv_alipay_info)
    TextView tv_alipay_info;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;
    @Bind(R.id.tv_can_withdraw)
    TextView tv_can_withdraw;

    IWXAPI wxApi;
    private OkHttpCommon mOkHttpCommon;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * 我的钱包信息
     */
    private ModelWallet mModelWallet;
    /**
     * 是否提现到微信
     */
    private int mWithdrawType = -1;

    /**
     * 微信支付宝是否授权
     */
    private boolean wechatIsAuth = false, alipayIsAuth = false;
    /**
     * 微信余额,支付宝余额
     */
//    private int wechatMoney = 0, alipayMoney = 0;

    /**
     * mine： 我的钱包
     * shop：店铺钱包
     */
    private String walletType = "mine";


    /**
     * 店铺余额转用户余额
     */
    private AlertDialog shopMoney2WalletDialog;

    /**
     * 余额： 分
     */
    private int moneyAll;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_withdraw_deposit2;
    }

    @Override
    protected void init() {
        mOkHttpCommon = new OkHttpCommon();

        if (getIntent().getExtras() != null) {
            walletType = getIntent().getExtras().getString("walletType", "mine");
        }

        if (walletType.equals("mine")) {
            tvRecharge.setText("充值");
            toolBar.setTitle("我的钱包");
        } else if (walletType.equals("shop")) {
            tvRecharge.setText("转账到用户余额");
            toolBar.setTitle("商家账户");
            tv_can_withdraw.setVisibility(View.VISIBLE);
        }

        Member member = CommonUtils.getMember();

        if (!TextUtils.isEmpty(member.getNameMM())) {
            tv_wechat_info.setText("到微信余额(" + member.getNameMM() + ")");
            tvBindWechat.setText("换绑");
            wechatIsAuth = true;
        } else {
            tvBindWechat.setText("去绑定");
        }

        if (!TextUtils.isEmpty(member.getNameMMphone())) {
            tv_alipay_info.setText("到支付宝余额(" + member.getNameMMphone() + ")");
            tvBindAlipay.setText("换绑");
            alipayIsAuth = true;
        } else {
            tvBindAlipay.setText("去绑定");
        }
        tvMoney.setText(SpannableStringUtils.getBuilder("余额\n")
                .append("¥0.00").setProportion(0.7f)
                .create());
        tvWechatMoney.setText(SpannableStringUtils.getBuilder("微信余额\n")
                .append("¥0.00").setProportion(0.7f)
                .create());
        tvAlipayMoney.setText(SpannableStringUtils.getBuilder("支付宝余额\n")
                .append("¥0.00").setProportion(0.7f)
                .create());
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {

//                Intent intent = new Intent(WithdrawDeposit2Activity.this, WithdrawDepositRecordsActivity.class);
//                intent.putExtra("type", "balance");
//                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShop", walletType.equals("shop"));
                ActivityUtils.getInstance().jumpActivity(BalanceLogsActivity.class, bundle);
            }
        });
    }

    @Override
    protected void loadData() {
        SimpleDialog.showLoadingHintDialog(this, 1);
        HttpParameterUtil.getInstance().requestMyWallet(mHandler);
    }

    @OnClick({R.id.submitBTN, R.id.ll_wechat_type, R.id.ll_alipay_type, R.id.tv_bind_wechat,
            R.id.tv_bind_alipay, R.id.tv_recharge})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_type:   // 提现到微信
                if (wechatIsAuth) {
                    mWithdrawType = 1;
                    changeViewStatus();
                } else {
                    authWechat();
                }
                break;
            case R.id.ll_alipay_type:   // 提现到支付宝
                if (alipayIsAuth) {
                    mWithdrawType = 2;
                    changeViewStatus();
                } else {
                    authAlipay();
                }
                break;
            case R.id.tv_bind_wechat:   // 授权微信
                authWechat();
                break;
            case R.id.tv_bind_alipay:   // 授权支付宝
                authAlipay();
                break;
            case R.id.submitBTN:
                if (mWithdrawType == -1) {
                    ToastUtils.showToast("请选择提现方式");
                    return;
                }
                String applyAmt = moneyET.getText().toString();
                if (TextUtils.isEmpty(applyAmt)) {
                    ToastUtils.showToast("请输入提现金额");
                    return;
                }

                if (UnitUtil.getDouble(applyAmt) <= 0) {
                    ToastUtils.showToast("提现金额不能小于0元");
                    return;
                }
                applyAmt = (int) (UnitUtil.getDouble(applyAmt) * 100.0) + "";

//                if (mWithdrawType == 1 && UnitUtil.getInt(applyAmt) > wechatMoney) {
//                    // 微信
//                    if (wechatMoney == 0) {
//                        ToastUtils.showToast("微信金额不足");
//                        return;
//                    }
//                    applyAmt = String.valueOf(wechatMoney);
//                    moneyET.setText(String.valueOf(UnitUtil.getInt(applyAmt) / 100));
//                }
//                if (mWithdrawType == 2 && UnitUtil.getInt(applyAmt) > alipayMoney) {
//                    // 支付宝
//                    if (alipayMoney == 0) {
//                        ToastUtils.showToast("支付宝金额不足");
//                        return;
//                    }
//                    applyAmt = String.valueOf(alipayMoney);
//                    moneyET.setText(String.valueOf(UnitUtil.getInt(applyAmt) / 100));
//                }

                SimpleDialog.showLoadingHintDialog(this, 4);

                HttpParameterUtil.getInstance().requestAddWithdrawApply(walletType, applyAmt, mWithdrawType == 1, mHandler);
                break;
            case R.id.tv_recharge:  // 充值/转账到用户余额
                if (walletType.equals("mine")) {
                    // 充值
                    new RechargeDialogUtils(WithdrawDeposit2Activity.this).showDialog();
                } else if (walletType.equals("shop")) {
                    // 转账到用户余额
                    shopMoneyToMineWalletDialog();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 店铺余额转到个人余额
     */
    private void shopMoneyToMineWalletDialog() {
        if (shopMoney2WalletDialog == null) {
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_shop_money_to_wallet, null);
            shopMoney2WalletDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create();

            final EditText et_input = dialogView.findViewById(R.id.et_input);
            dialogView.findViewById(R.id.tv_all).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 全部转出
                    et_input.setText(String.valueOf(moneyAll / 100.0));
                }
            });

            dialogView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shopMoney2WalletDialog.dismiss();
                }
            });

            dialogView.findViewById(R.id.btn_dialog_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 开始转账

                    String inputMoney = et_input.getText().toString().trim();
                    if (UnitUtil.getDouble(inputMoney) <= 0) {
                        ToastUtils.showToast("请输入金额");
                        return;
                    }

                    shopMoneyToMineWallet(inputMoney);

                }
            });
        }
        shopMoney2WalletDialog.show();
    }

    /**
     * 店铺余额转用户余额
     *
     * @param money
     */
    private void shopMoneyToMineWallet(String money) {
        int dMoney = (int)(UnitUtil.getDouble(money) * 100);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("amt", String.valueOf(dMoney));
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SHOPMONEY_TO_MINEMONEY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("转入失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "转入失败，请稍后重试"));
                    return;
                }

                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "转入成功！"));
                shopMoney2WalletDialog.dismiss();
                loadData();
            }
        });
    }

    private void changeViewStatus() {
        cbWechat.setChecked(mWithdrawType == 1);
        cbAlipay.setChecked(mWithdrawType == 2);
    }

    /**
     * 获取微信用户信息
     */
    private void authWechat() {
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = getPackageName() + (Math.random() * 100);
        wxApi.sendReq(req);
    }

    /**
     * 获取支付宝用户信息
     */
    private void authAlipay() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_ALIPAY_AUTH, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("绑定失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "绑定失败!"));
                    return;
                }
                toAlipayAuth(jsonObject.get("data").getAsString());
            }
        });


    }

    /**
     * 调起支付宝
     *
     * @param signInfo
     */
    private void toAlipayAuth(final String signInfo) {
        if (TextUtils.isEmpty(signInfo)) {
            ToastUtils.showToast("绑定失败!");
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AuthTask authTask = new AuthTask(WithdrawDeposit2Activity.this);
                Map<String, String> resultMap = authTask.authV2(signInfo, true);

                Message msg = Message.obtain();
                msg.what = 111111;
                msg.obj = resultMap;
                mHandler.sendMessage(msg);
            }
        };
        Thread authThread = new Thread(runnable);
        authThread.start();
    }

    @Subscriber(tag = EventTags.WECHAT_AUTH_RESULT)
    public void wecharAuthResult(String code) {
        if (!TextUtils.isEmpty(code)) {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
            HashMap<String, String> params = new HashMap<>();
            params.put("appid", Constants.WX_APPID);
            params.put("secret", Constants.WX_SECRET);
            params.put("grant_type", "authorization_code");
            params.put("code", code);
            mOkHttpCommon.getLoadData(this, url, params, new CallbackCommon() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtils.showToast("绑定失败!");
                }

                @Override
                public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                    LogUtils.json(jsonObject.toString());

                    if (jsonObject.has("errcode")) {
                        // 授权失败
                        ToastUtils.showToast("绑定失败(code:" + jsonObject.get("errcode").getAsString() + ")");
                    } else {
                        // 授权成功
                        getWechatMemberInfo(jsonObject.get("access_token").getAsString(),
                                jsonObject.get("openid").getAsString());
                    }
                }
            });
        }
    }

    /**
     * 获取微信用户信息
     */
    private void getWechatMemberInfo(String access_token, String openId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("openid", openId);
        mOkHttpCommon.getLoadData(this, "https://api.weixin.qq.com/sns/userinfo", params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("绑定失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                LogUtils.json(jsonObject.toString());
                if (jsonObject.has("errcode")) {
                    // 授权失败
                    ToastUtils.showToast("绑定失败");
                } else {
                    ToastUtils.showToast("绑定成功");
                    String nickname = jsonObject.get("nickname").getAsString();
                    tv_wechat_info.setText("到微信余额(" + nickname + ")");
                    tvBindWechat.setText("换绑");
                    mWithdrawType = 1;
                    changeViewStatus();
                    wechatIsAuth = true;
                    saveAuthInfo(true, nickname, jsonObject.get("openid").getAsString());
                }
            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param isWechat 是否为微信信息
     * @param nickname 昵称
     * @param openid   openid
     */
    public void saveAuthInfo(boolean isWechat, String nickname, String openid) {
        HashMap<String, String> params = CommonUtils.createParams();
        if (isWechat) {
            params.put("nameMM", nickname);
            params.put("openid", openid);
        } else {
            params.put("nameMMphone", nickname);
            params.put("unionid", openid);
        }
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SAVE_WECHAR_ALIPAY_AUTH_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

            }
        });
    }

    /**
     * 处理数据
     */
    private void disposeData(final Message msg) {
        SimpleDialog.cancelLoadingHintDialog();
        switch (msg.what) {
            case ConstantHandler.WHAT_MY_WALLET_SUCCESS:    // 用户账号
                mModelWallet = (ModelWallet) msg.obj;
                setMoneyInfo();
                break;
            case ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交提现申请成功!");
                finish();
                break;
            case ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_FAIL:
                ToastUtils.showToast((String) msg.obj);
                SimpleDialog.cancelLoadingHintDialog();
                break;
            case 111111:    // 支付宝授权
                AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);

                String resultStatus = authResult.getResultStatus();
                LogUtils.d(authResult.toString());
                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    getAlipayMemberInfo(authResult.getAuthCode());
                } else {
                    // 其他状态值则为授权失败
                    ToastUtils.showToast("绑定失败");

                }
                break;
            default:
                break;
        }
    }

    private void setMoneyInfo() {
        if (mModelWallet == null) return;
//        wechatMoney = UnitUtil.getInt(mModelWallet.getYongjin());
//        alipayMoney = UnitUtil.getInt(mModelWallet.getBaodanbi());
//        tvMoney.setText(SpannableStringUtils.getBuilder("余额\n")
//                .append(UnitUtil.getMoney((wechatMoney + alipayMoney) + "")).setProportion(0.7f)
//                .create());
//        tvWechatMoney.setText(SpannableStringUtils.getBuilder("微信余额\n")
//                .append(UnitUtil.getMoney(wechatMoney + "")).setProportion(0.7f)
//                .create());
//        tvAlipayMoney.setText(SpannableStringUtils.getBuilder("支付宝余额\n")
//                .append(UnitUtil.getMoney(alipayMoney + "")).setProportion(0.7f)
//                .create());

        if (walletType.equals("shop")) {
            int shopMoneyAll = UnitUtil.getInt(mModelWallet.getBaodanbi());
            tvMoney.setText(SpannableStringUtils.getBuilder("商家余额\n")
                    .append(UnitUtil.getMoney(shopMoneyAll + "")).setProportion(0.7f)
                    .create());

            //可提现金额(分)
            String balanceAmtFreeze = mModelWallet.getBalanceAmtFreeze();//冻结金额
            moneyAll = shopMoneyAll - UnitUtil.getInt(balanceAmtFreeze);

            tv_can_withdraw.setMovementMethod(LinkMovementMethod.getInstance());
            tv_can_withdraw.setText(
                    SpannableStringUtils.getBuilder("其中")
                            .append(UnitUtil.getMoney(String.valueOf(moneyAll))).setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .append("可转出至用户余额或提现")
                            .append("查看原因").setForegroundColor(getResources().getColor(R.color.c_link_color)).setClickSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View view) {
                            SimpleDialog.showSimpleRemarkWithTitleDialog(WithdrawDeposit2Activity.this, "可转出或可提现金额", "买家收货后卖家收益结算到账，即可提现或转账到自己的用户余额，用于消费抵扣");
                        }
                    })
                            .create()
            );

        } else {
            moneyAll = UnitUtil.getInt(mModelWallet.getBalance());
            tvMoney.setText(SpannableStringUtils.getBuilder("用户余额\n")
                    .append(UnitUtil.getMoney(moneyAll + "")).setProportion(0.7f)
                    .create());
        }

    }


    /**
     * 获取支付宝用户信息
     *
     * @param authCode
     */
    private void getAlipayMemberInfo(String authCode) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("authCode", authCode);
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_ALIPAY_MEMBER_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("绑定失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "绑定失败"));
                    return;
                }
                ToastUtils.showToast("绑定成功");
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                String nickname = data.get("nickname").isJsonNull() ? data.get("userId").getAsString() : data.get("nickname").getAsString();
                tv_alipay_info.setText("到支付宝余额(" + nickname + ")");
                tvBindAlipay.setText("换绑");
                mWithdrawType = 2;
                changeViewStatus();
                alipayIsAuth = true;
                saveAuthInfo(false, nickname, data.get("userId").getAsString());
            }
        });
    }

    private static class MyHandler extends Handler {

        private WeakReference<WithdrawDeposit2Activity> mActivity;

        public MyHandler(WithdrawDeposit2Activity mActivity) {
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

    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void onPayResult(boolean flag) {
        if (flag) {
            ToastUtils.showToast("充值成功");
            loadData();
        } else {
            ToastUtils.showToast("充值失败");
        }
    }
}
