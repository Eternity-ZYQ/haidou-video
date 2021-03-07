package com.yimeng.haidou.mine;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.BuildConfig;
import com.yimeng.haidou.R;
import com.yimeng.entity.Member;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.StringUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.widget.ItemLayout;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/16 0016 下午 02:30.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 更多设置
 * </pre>
 */
public class MoreSettingActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.item_version)
    ItemLayout itemVersion;
    @Bind(R.id.tv_about)
    TextView tvAbout;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_more_setting;
    }

    @Override
    protected void init() {


        if (!(boolean) SharedPreferencesUtils.get(Constants.IS_LOGIN, false)) {
            btnLogin.setText("立即登录");
        }

        // 版本号
        if (BuildConfig.DEBUG) {
            itemVersion.setRightText("V" + BuildConfig.VERSION_NAME + "(" + BuildConfig.BUILD_TIME + ")");
        } else {
            itemVersion.setRightText("V" + BuildConfig.VERSION_NAME);
        }

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.tv_about, R.id.tv_check_update, R.id.btn_login, R.id.tv_update_pwd,
            R.id.tv_user_info, R.id.tv_my_address, R.id.tv_after_sales_service, R.id.tv_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_about:
                ActivityUtils.getInstance().jumpH5Activity("关于我们", BuildConfig.Host);
                break;
            case R.id.tv_check_update:// 检查更新
                NetComment.checkVersion(this);
                break;
            case R.id.btn_login:
                if ((boolean) SharedPreferencesUtils.get(Constants.IS_LOGIN, false)) {
                    //退出
                    exitSystem();
                    CommonUtils.cleanMember();
                    EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.home));
                    ActivityUtils.getInstance().jumpMainActivity();
                }
                ActivityUtils.getInstance().jumpLoginActivity();
                break;
            case R.id.tv_update_pwd:    // 账户安全
                ActivityUtils.getInstance().jumpActivity(AccountSecurityActivity.class);
                break;
            case R.id.tv_user_info:     // 个人资料
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.tv_my_address:    // 收货地址
                ActivityUtils.getInstance().jumpAddressManager(false);
                break;
            case R.id.tv_after_sales_service:   // 联系我们
                ActivityUtils.getInstance().jumpActivity(CustomerServiceActivity.class);
                break;
            case R.id.tv_feedback:      // 问题反馈
                ActivityUtils.getInstance().jumpActivity(FeedbackActivity.class);
                break;
            default:

        }

    }

    /**
     * 退出系统
     */
    private void exitSystem() {
        Map<String, String> map = new HashMap<>();
        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
        if (member == null || StringUtils.isEmpty(member.getMobileNo())) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        map.put("mobileNo", member.getMobileNo());
        OkHttpCommon okHttpCommon = new OkHttpCommon();
        okHttpCommon.postLoadData(this, ConstantsUrl.APP_LOGIN_OUT_URL, map, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

            }
        });
    }


}
