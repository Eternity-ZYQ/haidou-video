package com.yimeng.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.entity.HistoryAccountBean;
import com.yimeng.entity.Member;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.PwdEditText;
import com.google.gson.JsonObject;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.impl.FullScreenPopupView;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/4 2:37 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SetPayPwdDialog extends FullScreenPopupView {

    private PwdEditText mPwdEt;
    private Button mBtnSubmit;
    private TextView mTvDialogDetail;
    private Activity mActivity;
    // 登录密码
    private String loginPwd;
    // 支付密码
    private String payPwd;
    // 支付密码是否设置过
    private boolean payPwdIsSet = false;
    private Member mMember;
    private TextView mTvDialogTitle;
    private EditText mLoginEt;
    private int isFirstInput = 1;

    public SetPayPwdDialog(@NonNull Context context) {
        super(context);
        this.mActivity = (Activity) context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_set_pay_pwd;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        fbiView();

        mMember = CommonUtils.getMember();
        payPwdIsSet = mMember.isSetPayPwd();
        if (!payPwdIsSet) {// 未设置过密码
            mTvDialogTitle.setText("设置支付密码");
            mTvDialogDetail.setText("请输入支付密码");

            mLoginEt.setVisibility(GONE);
            mPwdEt.setVisibility(VISIBLE);

        } else {
            // 修改密码
            mTvDialogTitle.setText("修改支付密码");
            mTvDialogDetail.setText("请输入登录密码");

            mLoginEt.setVisibility(VISIBLE);
            mPwdEt.setVisibility(GONE);
            mBtnSubmit.setVisibility(VISIBLE);
            mBtnSubmit.setText("下一步");
        }

        mPwdEt.setOnPwdResultListener(new PwdEditText.onPwdResultListener() {
            @Override
            public void onResult(View view, String pwd) {
                if (!payPwdIsSet) {// 未设置过密码
                    if (isFirstInput == 1) {
                        isFirstInput = 2;
                        payPwd = pwd;
                        mTvDialogTitle.setText("确认支付密码");
                        mTvDialogDetail.setText("请输入支付密码");
                        mPwdEt.setText("");
                    } else if (isFirstInput == 2) {
                        if (!payPwd.equals(pwd)) {
                            ToastUtils.showToast("两次密码不一致");
                            return;
                        }
                        mBtnSubmit.setVisibility(VISIBLE);
                        mBtnSubmit.setText("确定");
                    }

                } else {
                    if (isFirstInput == 2) {
                        payPwd = pwd;
                        mTvDialogTitle.setText("确认支付密码");
                        mTvDialogDetail.setText("请输入支付密码");
                        isFirstInput = 3;
                        mPwdEt.setText("");
                    } else if (isFirstInput == 3) {
                        if (!payPwd.equals(pwd)) {
                            ToastUtils.showToast("两次密码不一致");
                            return;
                        }
                        mBtnSubmit.setVisibility(VISIBLE);
                    }

                }
            }
        });


        findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!payPwdIsSet) {// 首次设置密码
                    if (TextUtils.isEmpty(payPwd) || payPwd.length() < 6) {
                        ToastUtils.showToast("请输入支付密码");
                        return;
                    }

                    // 设置密码
                    setPayPwd();

                } else {
                    if (isFirstInput == 1) {
                        // 验证登录密码
                        String pwd = mLoginEt.getText().toString().trim();
                        List<HistoryAccountBean> loginAccount = LitePal.where("accountNo=?", mMember.getMobileNo()).find(HistoryAccountBean.class);
                        if(!loginAccount.isEmpty()) {
                            if (loginAccount.get(0).getPwd().equals(pwd)) {
                                checkLoginNext(pwd);
                            } else {
                                ToastUtils.showToast("登录密码输入不正确");
                            }
                        }else{
                            checkLoginNext(pwd);
                        }

                    } else if (isFirstInput == 2) {


                    } else {
                        if (TextUtils.isEmpty(payPwd) || payPwd.length() < 6) {
                            ToastUtils.showToast("请输入支付密码");
                            return;
                        }

                        // 修改密码
                        updatePayPwd();
                    }
                }
            }
        });
    }

    private void checkLoginNext(String pwd){
        isFirstInput = 2;
        loginPwd = pwd;

        mTvDialogDetail.setText("请输入支付密码");
        mBtnSubmit.setVisibility(GONE);
        mLoginEt.setVisibility(GONE);
        mPwdEt.setVisibility(VISIBLE);

        mBtnSubmit.setText("确定");
        // 获取焦点，显示键盘
        mPwdEt.setFocusable(true);
        mPwdEt.setFocusableInTouchMode(true);
        mPwdEt.requestFocus();
        KeyboardUtils.showKeyBoard(mPwdEt);
    }

    private void fbiView() {
        mLoginEt = findViewById(R.id.loginEt);
        mTvDialogTitle = findViewById(R.id.tv_dialog_title);
        mTvDialogDetail = findViewById(R.id.tv_dialog_detail);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mPwdEt = findViewById(R.id.pwdEt);
    }

    /**
     * 设置支付密码
     */
    private void updatePayPwd() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("password", loginPwd);
        params.put("payCode", payPwd);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_UPDATE_PAY_PWD, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", mActivity.getString(R.string.net_error)));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "设置成功！"));
                dismiss();
            }
        });
    }

    /**
     * 设置支付密码
     */
    private void setPayPwd() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("payCode", payPwd);
        new OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_SET_PAY_PWD, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", mActivity.getString(R.string.net_error)));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "密码设置成功，请重新支付！"));
                Member member = CommonUtils.getMember();
                member.setPayCodeStatus("yes");
                String resultStr = GsonUtils.getGson().toJson(member);
                SharedPreferencesUtils.put(Constants.USER_INFO, resultStr);
                dismiss();
            }
        });
    }
}
