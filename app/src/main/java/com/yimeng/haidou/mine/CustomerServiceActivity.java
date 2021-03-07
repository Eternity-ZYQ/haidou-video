package com.yimeng.haidou.mine;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.CustomerServiceBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.yanzhenjie.permission.Action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/16 0016 上午 11:19.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 联系客服
 * </pre>
 */
public class CustomerServiceActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.tv_staff_service)
    TextView tvStaffService;
    @Bind(R.id.tv_qq_service)
    TextView tvQqService;
    @Bind(R.id.tv_wechat_service)
    TextView tvWechatService;
    @Bind(R.id.tv_vipcn_service)
    TextView tvVipcnService;

    private List<CustomerServiceBean> mList;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void init() {


    }

    @Override
    protected void initListener() {

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("menuType", "touch");
        new OkHttpCommon().postLoadData(this, ConstantsUrl.CONCAT_CUSTOM_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                    return;
                }
                mList = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().get("introduction").toString(), new TypeToken<List<CustomerServiceBean>>() {
                }.getType());
            }
        });
    }

    @OnClick({R.id.tv_staff_service, R.id.tv_qq_service, R.id.tv_wechat_service, R.id.tv_vipcn_service})
    public void onViewClicked(View view) {
        CustomerServiceBean serviceBean = null;
        switch (view.getId()) {
            case R.id.tv_staff_service:
                serviceBean = mList.get(0);
                break;
            case R.id.tv_qq_service:
                serviceBean = mList.get(1);
                break;
            case R.id.tv_wechat_service:
                serviceBean = mList.get(2);
                break;
            case R.id.tv_vipcn_service:
                serviceBean = mList.get(3);
                break;
        }

        if (serviceBean != null) {
            String introduction = serviceBean.getIntroduction();
            switch (serviceBean.getName()) {
                case "hotline": // 电话
                    diallPhone(introduction);
                    break;
                case "qq": // qq
                    jumpQQ(introduction);
                    break;
                case "wechatOfficial": // 微信公众号
                    jumpWeChat(introduction);
                    break;
                case "wechat": // 微信
                    jumpWeChat(introduction);
                    break;
                default:

            }
        }

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    private void diallPhone(final String phoneNum) {
        CommonUtils.getPermission(this, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showToast("电话号码为空！");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + phoneNum);
                intent.setData(uri);
                startActivity(intent);
            }
        },Manifest.permission.CALL_PHONE);

//        if (AndPermission.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
//            if (TextUtils.isEmpty(phoneNum)) {
//                ToastUtils.showToast("电话号码为空！");
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            Uri uri = Uri.parse("tel:" + phoneNum);
//            intent.setData(uri);
//            startActivity(intent);
//        } else {
//            ToastUtils.showToast("请授予我们相应权限!");
//        }
    }

    /**
     * 跳转到QQ
     *
     * @param qq
     */
    public void jumpQQ(String qq) {
        try {
            //可以跳转到添加好友，如果qq号是好友了，直接聊天
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;//uin是发送过去的qq号码
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("请检查是否安装QQ");
        }
    }

    /**
     * 跳转到微信
     *
     * @param copyStr
     */
    public void jumpWeChat(String copyStr) {
        copyString(copyStr);
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showToast("请检查是否安装微信");
        }
    }

    /**
     * 复制信息
     *
     * @param str
     */
    public void copyString(String str) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
        ToastUtils.showToast("复制成功");
    }


}
