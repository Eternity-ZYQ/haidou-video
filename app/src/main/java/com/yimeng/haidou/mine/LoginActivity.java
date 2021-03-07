package com.yimeng.haidou.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.MainActivity;
import com.yimeng.haidou.R;
import com.yimeng.entity.Member;
import com.yimeng.jpush.TagAliasOperatorHelper;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.EncryptUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.VerifyCodeUtil;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/13 0013 上午 11:22.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 登录页
 * </pre>
 */
public class LoginActivity extends BaseActivity {


    private static final int REQUEST_READ_PHONE_STATE = 0x2221;
    protected Disposable coundownDisposable;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.iv_remove)
    ImageView ivRemove;
    @Bind(R.id.imgAuthCodeET)
    EditText imgAuthCodeEt;
    @Bind(R.id.imgAuthCode)
    ImageView imgAuthCode;
    @Bind(R.id.tv_get_text_code)
    TextView tvGetTextCode;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.btn_login)
    Button btnLogin;


    /**
     * 手机唯一标识码
     */
    private String bindNo = null;
    /**
     * 图片验证码
     */
    private String imgCode;
    private ArrayList<Object> permissionNames;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {


        // logo圆角
//        Glide.with(this)
//                .load(R.mipmap.logo)
//                .apply(new RequestOptions().transform(new RoundedCornersTransformation(DeviceUtils.dp2px(this, 10), 0)))
//                .into(ivLogo);

        // 同意用户注册协议和隐私政策
//        String str = "用户注册协议和隐私政策";
//        SpannableString ss = new SpannableString(str);
//        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_color)), 0, "用户注册协议".length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        ss.setSpan(new MyClickableSpan("用户注册协议"), 0, "用户注册协议".length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_color)), str.length() - "隐私政策".length(), str.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        ss.setSpan(new MyClickableSpan("隐私政策"), str.length() - "隐私政策".length(), str.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
//        tvProtocol.setHighlightColor(Color.TRANSPARENT);
//        tvProtocol.setText(ss);

        tvAppName.setText(
                SpannableStringUtils.getBuilder(getString(R.string.app_name) + "APP\n\n")
                        .append("销量即是王道").setProportion(0.7f)
                        .create()
        );

    }

    @Override
    protected void initListener() {
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    tvGetTextCode.setEnabled(true);
                    btnLogin.setEnabled(true);
                } else {
                    tvGetTextCode.setEnabled(false);
                    btnLogin.setEnabled(false);
                }

                ivRemove.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivRemove.setVisibility(etMobile.getText().toString().length() == 0 ? View.GONE : View.VISIBLE);

    }

    @Override
    protected void loadData() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            getImgCode();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (coundownDisposable != null && !coundownDisposable.isDisposed()) {
            coundownDisposable.dispose();
        }
    }

    /**
     * 获取图片验证码
     */
    @SuppressLint("CheckResult")
    private void getImgCode() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        bindNo = telephonyManager.getDeviceId();
        if (bindNo == null || bindNo.isEmpty()) {
            //android.provider.Settings;
            bindNo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        SharedPreferencesUtils.put(Constants.MOBILE_ID, bindNo);

        HashMap<String, String> params = new HashMap<>();
        bindNo = EncryptUtils.encryptMD5ToString(bindNo);
        params.put("identify", bindNo);
        new OkHttpCommon().postLoadData(LoginActivity.this, ConstantsUrl.GET_IMG_CODE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                setImageCode(jsonObject.get("data").getAsString());
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImgCode();
            }
        }
    }

    /**
     * 设置图片验证码
     *
     * @param code
     */
    private void setImageCode(String code) {
        imgCode = code;
        Bitmap codeBitmap = VerifyCodeUtil.getInstance().createBitmap(imgCode);
        imgAuthCode.setImageBitmap(codeBitmap);
    }

    @OnClick({R.id.iv_remove, R.id.imgAuthCode, R.id.tv_get_text_code, R.id.tv_protocol, R.id.btn_login,
            R.id.tv_forget_pwd, R.id.tv_change_login, R.id.tv_register})
    public void onViewClicked(View view) {
        final String mobileStr = etMobile.getText().toString();
        switch (view.getId()) {
            case R.id.iv_remove:
                etMobile.setText("");
                break;
            case R.id.imgAuthCode:      // 获取图片验证码
                getImgCode();
                break;
            case R.id.tv_get_text_code: // 获取短信验证码
                if (TextUtils.isEmpty(mobileStr)) {
                    ToastUtils.showToast(R.string.please_input_mobile);
                    return;
                }

                if (!imgCode.equals(imgAuthCodeEt.getText().toString())) {
                    ToastUtils.showToast("图片验证码错误");
                    return;
                }
                getCode(mobileStr);
                break;
            case R.id.btn_login:        // 登录
                login();
                break;
            case R.id.tv_protocol:      // 注册协议
                ActivityUtils.getInstance().jumpH5Activity("用户注册协议", ConstantsUrl.URL_PROTOCOL + "用户注册协议");
                break;
            case R.id.tv_change_login:  // 切换为密码登录方式
                finish();
                break;
            case R.id.tv_forget_pwd:    // 忘记密码
                ActivityUtils.getInstance().jumpActivity(ForgetPwdActivity.class);
                break;
            case R.id.tv_register:      // 用户注册
                ActivityUtils.getInstance().jumpH5Activity("用户注册", ConstantsUrl.URL_APP_REGISTER);
                break;
            default:
                break;
        }
    }

    /**
     * 获取短信验证码
     */
    private void getCode(String mobileStr) {
        tvGetTextCode.setEnabled(false);
        //发送验证码
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobileStr);
        map.put("identify", bindNo);
        map.put("validateCode", imgAuthCodeEt.getText().toString().trim());

        OkHttpCommon okHttpCommon = new OkHttpCommon();
        okHttpCommon.postLoadData(this, ConstantsUrl.GET_CODE_URL, map, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                resetCode();
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                    String msg = jsonObject.get("msg").isJsonNull() ? "获取验证码失败！" : jsonObject.get("msg").getAsString();
                    ToastUtils.showToast(msg);
                    resetCode();
                    setImageCode(jsonObject.get("data").getAsString());
                } else {
                    countdownTime();
                }
            }
        });
    }

    /**
     * 登录
     */
    private void login() {

        //获取手机号
        String mobileNo = etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNo)) {
//            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            ToastUtils.showToast("请输入手机号码或邮箱地址");
            return;
        }

        //获取验证码
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 同意协议
//        if (!cbProtocol.isChecked()) {
//            ToastUtils.showToast("请同意用户注册协议和隐私政策!");
//            return;
//        }

        //注册来源
        String registerSrc = "ANDROID";

        //登录
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobileNo", mobileNo);
        map.put("code", code);
        map.put("bindNo", bindNo);
        map.put("registerSrc", registerSrc);

        OkHttpCommon okHttpCommon = new OkHttpCommon();
        okHttpCommon.postLoadData(this, ConstantsUrl.APP_LOGIN_URL, map, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                    String msg = jsonObject.get("msg").isJsonNull() ? "登录失败！" : jsonObject.get("msg").getAsString();
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                EventBus.getDefault().post(true, "loginStatus");
                ActivityUtils.getInstance().jumpActivity(MainActivity.class);
                finish();

            }
        });

    }

    /**
     * 恢复按钮状态
     */
    private void resetCode() {
        tvGetTextCode.setText(R.string.get_code);
        tvGetTextCode.setEnabled(true);
        etMobile.setEnabled(true);
    }

    /**
     * 倒计时
     */
    private void countdownTime() {

        CommonUtils.countdown(60)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        coundownDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        tvGetTextCode.setText(integer + "s");
                        etMobile.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        resetCode();
                    }

                    @Override
                    public void onComplete() {
                        resetCode();
                    }
                });
    }

    class MyClickableSpan extends ClickableSpan {

        private String mContent;

        public MyClickableSpan(String content) {
            this.mContent = content;
        }

        @Override
        public void onClick(View widget) {
            ActivityUtils.getInstance().jumpH5Activity(mContent, ConstantsUrl.URL_PROTOCOL + mContent);
        }
    }
}
