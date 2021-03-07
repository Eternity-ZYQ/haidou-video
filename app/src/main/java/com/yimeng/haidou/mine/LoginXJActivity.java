package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.HistoryAccountBean;
import com.yimeng.entity.Member;
import com.yimeng.jpush.TagAliasOperatorHelper;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.EncryptUtils;
import com.yimeng.haidou.MainActivity;
import com.yimeng.haidou.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.SharedPreferencesUtils;

import org.litepal.LitePal;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/13 0013 上午 11:22.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 密码登录页
 * </pre>
 */
public class LoginXJActivity extends BaseActivity {

    @Bind(R.id.btn_login)
    Button btn_login;

    @Bind(R.id.tv_forget_pwd)
    View tv_forget_pwd;

    @Bind(R.id.tv_change_login)
    View tv_change_login;

    @Bind(R.id.tv_register)
    View tv_register;

    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.iv_remove)
    ImageView ivRemove;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    @Bind(R.id.ll_protocol)
    LinearLayout llProtocol;
    @Bind(R.id.iv_history_account)
    ImageView ivHistoryAccount;
    @Bind(R.id.ll_mobile)
    LinearLayout ll_mobile;
    @Bind(R.id.history_layout)
    ViewStub historyLayout;
    @Bind(R.id.iv_remove_pwd)
    ImageView iv_remove_pwd;

    /**
     * 手机唯一标识码
     */
    private String bindNo = "";

    /**
     * 历史账号是否显示
     */
    private boolean historyIsShow = false;
    /**
     * 历史账号
     */
    private List<HistoryAccountBean> mHistoryAccountList = new ArrayList<>();

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_login_xj;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init() {


        getIMEI();

        // 最后一次登录的账号回填显示
        HistoryAccountBean lastAccount = LitePal.findLast(HistoryAccountBean.class);
        if (lastAccount != null) {
            etMobile.setText(lastAccount.getAccountNo());
            etCode.setText(lastAccount.getPwd());
        }
    }

    /**
     * 获取手机id
     */
    private void getIMEI() {
        CommonUtils.getIMEI(this).subscribe(new Consumer<String>() {
            @Override
            public void accept(String imei) throws Exception {
                bindNo = EncryptUtils.encryptMD5ToString(imei);
            }
        });
    }

    @Override
    protected void initListener() {
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivRemove.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iv_remove_pwd.setVisibility(charSequence.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivRemove.setVisibility(etMobile.getText().toString().length() == 0 ? View.GONE : View.VISIBLE);

        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMobile.setText("");
                etCode.setText("");

            }
        });

        iv_remove_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCode.setText("");
            }
        });

        tvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().jumpH5Activity("用户注册协议", ConstantsUrl.URL_PROTOCOL + "用户注册协议");
            }
        });

        tv_change_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().jumpActivity(LoginActivity.class);
            }
        });

        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().jumpActivity(ForgetPwdActivity.class);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.Companion.start();
            }
        });

    }

//    @OnClick({R.id.iv_remove, R.id.iv_remove_pwd, R.id.tv_protocol, R.id.btn_login, R.id.tv_forget_pwd, R.id.tv_change_login,
//            R.id.tv_register})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_remove:
//
//                break;
//            case R.id.iv_remove_pwd:
//
//                break;
//            case R.id.btn_login:        // 登录
//
//                break;
//            case R.id.tv_protocol:      // 注册协议
//
//                break;
//            case R.id.tv_forget_pwd:    // 忘记密码
//
//                break;
//            case R.id.tv_change_login:  // 使用验证码登录
//
//                break;
//            case R.id.tv_register:      // 用户注册
////                ActivityUtils.getInstance().jumpH5Activity("用户注册", ConstantsUrl.URL_APP_REGISTER);
//
//                break;
//        }
//    }

    @Override
    protected void loadData() {

    }


    /**
     * 显示常用号码
     */
    @OnClick(R.id.iv_history_account)
    public void changeHistoryAccount(View view) {
        ivHistoryAccount.setRotation(270);
        if (historyIsShow) {
            hideHistoryAccount();
        } else {
            showHistoryAccount();
        }

    }

    /**
     * 关闭历史账号
     */
    private void hideHistoryAccount() {
        historyIsShow = false;
        historyLayout.setVisibility(View.GONE);
        ivHistoryAccount.setRotation(90);
    }


    /**
     * 显示历史账号
     */
    private void showHistoryAccount() {
        historyIsShow = true;
        mHistoryAccountList = LitePal.findAll(HistoryAccountBean.class);
        if (mHistoryAccountList.isEmpty()) {
            return;
        }
        historyLayout.setVisibility(View.VISIBLE);
//        historyLayout.inflate();
        // 刷新高度
        refreshAccountHeight();

        RecyclerView rvHistory = findViewById(R.id.recyclerView_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter<HistoryAccountBean, BaseViewHolder> adapter = new BaseQuickAdapter<HistoryAccountBean, BaseViewHolder>(
                R.layout.adapter_history_account_layout, mHistoryAccountList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, HistoryAccountBean item) {
                if (item != null) {
                    helper.setText(R.id.tv_account, item.getAccountNo())
                            .addOnClickListener(R.id.iv_remove);
                    CommonUtils.showImage(helper.getView(R.id.civ_user_head), item.getHeadpath());
                }
            }

        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 删除账号
                HistoryAccountBean historyAccountBean = mHistoryAccountList.get(position);
                int deleteResult = historyAccountBean.delete();
                if (deleteResult != 0) {
                    adapter.remove(position);
                }
                // 刷新高度
                refreshAccountHeight();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 回填账号
                if (mHistoryAccountList != null && !mHistoryAccountList.isEmpty()) {
                    HistoryAccountBean historyAccountBean = mHistoryAccountList.get(position);

                    etMobile.setText(historyAccountBean.getAccountNo());
                    etCode.setText(historyAccountBean.getPwd());
                    hideHistoryAccount();
                }
            }
        });
        rvHistory.setAdapter(adapter);
    }

    /**
     * 刷新账号窗高度
     */
    private void refreshAccountHeight() {

        int historyHeight = 0;
        if (mHistoryAccountList.size() == 1) {
            historyHeight = DeviceUtils.dp2px(this, 60);
        } else if (mHistoryAccountList.size() == 2) {
            historyHeight = DeviceUtils.dp2px(this, 120);
        } else if (mHistoryAccountList.size() >= 3) {
            historyHeight = DeviceUtils.dp2px(this, 180);
        }
        historyLayout.getLayoutParams().height = historyHeight;
    }

    /**
     * 登录
     */
    private void login() {

        //获取手机号
        final String mobileNo = etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNo)) {
            Toast.makeText(LoginXJActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取密码
        final String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(LoginXJActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(bindNo)) {
            getIMEI();
            return;
        }

        //注册来源
        String registerSrc = "ANDROID";

        //登录
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobileNo", mobileNo);
        map.put("password", code);
        map.put("bindNo", bindNo);
        map.put("registerSrc", registerSrc);

        OkHttpCommon okHttpCommon = new OkHttpCommon();
        okHttpCommon.postLoadData(LoginXJActivity.this, ConstantsUrl.APP_LOGIN_PWD_URL, map, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                    String msg = jsonObject.get("msg").isJsonNull() ? "登录失败！" : jsonObject.get("msg").getAsString();
                    Toast.makeText(LoginXJActivity.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                //存储用户信息
                JsonObject memberJson = jsonObject.getAsJsonObject("data");
                Member member = new Gson().fromJson(memberJson.toString(), Member.class);
                SharedPreferencesUtils.put(Constants.USER_INFO, memberJson.toString());
                SharedPreferencesUtils.put(Constants.IS_LOGIN, true);
                CommonUtils.saveMember(member);

                // 上传通讯录
//                NetComment.sendPhoneContactsHttp(LoginActivity.this);

                // 绑定推送
                TagAliasOperatorHelper.getInstance().bindJPush(member.getMemberNo());

                // 保存到常用账号
//                new HistoryAccountBean(mobileNo, code).save();
                List<HistoryAccountBean> historyAccountBeans = LitePal.where("accountNo=?", mobileNo).find(HistoryAccountBean.class);
                if (historyAccountBeans.isEmpty()) {
                    new HistoryAccountBean(mobileNo, code, member.getHeadPath()).save();
                } else {
                    HistoryAccountBean historyAccountBean = historyAccountBeans.get(0);
                    historyAccountBean.setPwd(code);
                    historyAccountBean.save();
                }


                ActivityUtils.getInstance().jumpActivity(MainActivity.class);
                finish();
            }
        });

    }

    /**
     * @param status true 密码登录成功
     */
    @Subscriber(tag = "loginStatus")
    public void mobileLoginResule(boolean status) {
        if (status) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (historyIsShow) {
            hideHistoryAccount();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int size = mHistoryAccountList.size();

        if (size != 0) {
            int mx = (int) ev.getRawX();
            int my = (int) ev.getRawY();
            int x = (int) ll_mobile.getX();
            int y = (int) ll_mobile.getY();
            int w = ll_mobile.getMeasuredWidth();
            int h = ll_mobile.getMeasuredHeight();

            Log.d("msg", "LoginPWDActivity -> dispatchTouchEvent: mx  " + mx + "---" + my);
            Log.d("msg", "LoginPWDActivity -> dispatchTouchEvent: x  " + x + "---" + y);
            Log.d("msg", "LoginPWDActivity -> dispatchTouchEvent: w  " + w + "---" + h);

            int accountHeight;

            if (size >= 3) {
                accountHeight = DeviceUtils.dp2px(this, 180);
            } else if (size == 2) {
                accountHeight = DeviceUtils.dp2px(this, 120);
            } else {
                accountHeight = DeviceUtils.dp2px(this, 60);
            }

            if (mx > x && mx < x + w && my > y && my < y + h + accountHeight) {
                Log.d("msg", "LoginPWDActivity -> dispatchTouchEvent: " + "在范围内");
            } else {
                if (historyIsShow) {
                    hideHistoryAccount();
                    return true;
                }
            }
        }


        return super.dispatchTouchEvent(ev);
    }
}
