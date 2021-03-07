package com.yimeng.haidou.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.dialog.ActivateMemberDialog;
import com.yimeng.entity.Member;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.InputItemLayout;
import com.yimeng.widget.MyToolBar;
import com.yimeng.haidou.R;
import com.google.gson.JsonObject;
import com.huige.library.popupwind.PopupWindowUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.XPopup;

import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/22 0022 上午 10:55.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人资料
 * </pre>
 */
public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.input_nickname)
    InputItemLayout inputNickname;
    @Bind(R.id.input_sex)
    InputItemLayout inputSex;
    @Bind(R.id.input_mobile)
    InputItemLayout inputMobile;
    @Bind(R.id.info_name_or_state_tv)
    TextView infoNameOrStateTv;
    @Bind(R.id.truenameTV)
    TextView truenameTV;
    @Bind(R.id.statusTV)
    TextView statusTV;
    @Bind(R.id.rl_userName)
    RelativeLayout rlUserName;
    @Bind(R.id.input_email)
    InputItemLayout inputEmail;
    @Bind(R.id.tv_activate)
    TextView tvActivate;

    // 是否激活
    private boolean isActivate = false;

    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
                super.onRightClick();
                // 保存
                updateUserInfo();
            }
        });

        inputSex.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupWindowUtils(UserInfoActivity.this, R.layout.popupwind_sex_layout)
                        .setStyle(R.style.popupWindowAsBottom)
                        .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setOnClickListenerByViewId(R.id.tv_man, new PopupWindowUtils.onPopupWindClickListener() {
                            @Override
                            public void onClick(View view) {
                                inputSex.setEditText("男");
                            }
                        })
                        .setOnClickListenerByViewId(R.id.tv_women, new PopupWindowUtils.onPopupWindClickListener() {
                            @Override
                            public void onClick(View view) {
                                inputSex.setEditText("女");
                            }
                        })
                        .setOnClickListenerByViewId(R.id.tv_cancel, null)
                        .showAtLocation(UserInfoActivity.this, v, Gravity.BOTTOM, 0, 0);
            }
        });

        inputEmail.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().jumpActivity(UpdateUserEmailActivity.class);
            }
        });


    }

    /**
     * 修改资料
     */
    private void updateUserInfo() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        String nickname = inputNickname.getInputText();
        String sex = inputSex.getInputText();

        if (TextUtils.isEmpty(nickname)) {
            ToastUtils.showToast(R.string.input_nickname);
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            ToastUtils.showToast(R.string.select_gender);
            return;
        }
        params.put("nickname", nickname);
        params.put("sex", sex);

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_UPDATE_MEMBER_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改失败!"));
                    return;
                }

                Member member = GsonUtils.getGson().fromJson((String) SharedPreferencesUtils.get(Constants.USER_INFO, ""), Member.class);
                member.setNickname(inputNickname.getInputText());
                member.setSex(inputSex.getInputText());
                SharedPreferencesUtils.put(Constants.USER_INFO, GsonUtils.getGson().toJson(member));
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改成功!"));
                finish();
            }
        });


    }

    @Override
    protected void loadData() {

    }


    /**
     * 前往实名认证
     */
    @OnClick(R.id.truenameTV)
    public void toAuthName() {
        ActivityUtils.getInstance().jumpActivityForResult(this, IDCardInfoActivity.class, 0x1, null);
    }

    @OnClick(R.id.tv_activate)
    public void activate() {
        if (!isActivate) {
            HashMap<String, String> params = CommonUtils.createParams();
            params.put("token", CommonUtils.getToken());
            params.put("menuNo", "platform_account_activefee");

            mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_ACTIVATE, params, new CallbackCommon() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                    if (jsonObject.get("status").getAsInt() != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取信息失败!"));
                        return;
                    }

                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    String amt = GsonUtils.parseJson(data, "introduction", "");
                    new XPopup.Builder(UserInfoActivity.this)
                            .asCustom(new ActivateMemberDialog(amt, UserInfoActivity.this))
                            .show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String token = CommonUtils.getToken();
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", token);
        if (mOkHttpCommon == null) {
            mOkHttpCommon = new OkHttpCommon();
        }

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_MEMBER_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取信息失败!"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                inputNickname.setEditText(GsonUtils.parseJson(data, "nickname", ""));
                inputMobile.setEditText(GsonUtils.parseJson(data, "mobileNo", ""));
                inputSex.setEditText(GsonUtils.parseJson(data, "sex", ""));
                inputEmail.setEditText(GsonUtils.parseJson(data, "email", ""));

                // 是否激活
                isActivate = GsonUtils.parseJson(data, "isOldUsr", "0").equals("1");
                tvActivate.setText(isActivate ? "已激活" : "去激活");

                String memberName = GsonUtils.parseJson(data, "memberName", "");
                // 认证信息 0未实名,1已实名,2实名失败,3审核中
                String nameAuthFlag = GsonUtils.parseJson(data, "nameAuthFlag", "0");
                switch (nameAuthFlag) {
                    case "0":
                        truenameTV.setVisibility(View.VISIBLE);
                        statusTV.setVisibility(View.GONE);
                        break;
                    case "1":
                        truenameTV.setVisibility(View.GONE);
                        statusTV.setVisibility(View.VISIBLE);
                        statusTV.setText(memberName);
                        infoNameOrStateTv.setText(R.string.realname_success);
                        break;
                    case "2":
                        truenameTV.setVisibility(View.VISIBLE);
                        statusTV.setVisibility(View.GONE);
                        infoNameOrStateTv.setText(R.string.realname_fail);
                        break;
                    case "3":
                        truenameTV.setVisibility(View.INVISIBLE);
                        statusTV.setVisibility(View.GONE);
                        infoNameOrStateTv.setText(R.string.realname_under_review);
                        break;
                    default:

                }
                SharedPreferencesUtils.put(Constants.USER_AUTH_STATUS, nameAuthFlag);
            }
        });

    }

    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void result(){
        // 是否激活
        isActivate = true;
        tvActivate.setText("已激活");
    }

}
