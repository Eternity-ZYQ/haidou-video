package com.yimeng.haidou.mine;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.Member;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.FileUtil;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.QRCodeUtil;
import com.yimeng.haidou.R;
import com.google.gson.JsonObject;
import com.huige.library.popupwind.PopupWindowUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.yanzhenjie.permission.AndPermission;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/22 0022 下午 05:59.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 二维码
 * </pre>
 */
public class QrCodeActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.qrcodeSDV)
    ImageView qrcodeSDV;
    @Bind(R.id.logoSDV)
    CircleImageView logoSDV;
    @Bind(R.id.tv_qr_title)
    TextView tv_qr_title;
    @Bind(R.id.tv_inviteCode)
    TextView tv_inviteCode;
    @Bind(R.id.layout_qrcode)
    RelativeLayout layoutQRCode;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    String[] perms = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    PopupWindowUtils popupWindowUtils;
    private Bitmap mImageQR;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_qrcode;
    }

    @Override
    protected void init() {


    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void loadData() {

        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        if (TextUtils.isEmpty(userInfo)) {
            ToastUtils.showToast(R.string.data_error);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
        if (bundle != null) {
            if (bundle.getString("mType", "").equals("shop")) {
                // 店铺
                tvTitle.setText("商家二维码");
//                String mImage = bundle.getString("mImage", "");
//                CommonUtils.showRadiusImage(logoSDV, mImage, DeviceUtils.dp2px(this, 6), true, true, true, true);
                String mQRUrl = bundle.getString("mQRUrl", "");
                QRCodeUtil.createChineseQRCode(this, mQRUrl, qrcodeSDV);
//                tv_qr_title.setText(bundle.getString("title"));
                tv_qr_title.setText("用户扫上面的二维码，可直接进店消费！");
                getShopDetail(bundle.getString("shopNo"));
            }
        } else {
            // 个人
            tvTitle.setText("我的二维码");
            String memberNo = member.getMemberNo();
            String inviteCode = member.getInviteCode();
            String url = ConstantsUrl.SHARE_URL_HEADER + "memberNo=" + memberNo + "&inviteCode=" + inviteCode + "&memberType=normal";
            QRCodeUtil.createChineseQRCode(this, url, qrcodeSDV);
//            CommonUtils.showRadiusImage(logoSDV, member.getHeadPath(), DeviceUtils.dp2px(this, 6), true, true, true, true);
            tv_qr_title.setText("邀请好友扫下面的二维码注册，\n您可成为Ta的推荐人，Ta消费您获得奖励。");
            tv_inviteCode.setText("邀请码：" + member.getInviteCode());

        }

        CommonUtils.showImage(logoSDV, member.getHeadPath());
        tvUserName.setText(member.getNickname());

    }

    /**
     * 获取店铺信息
     */
    private void getShopDetail(String shopNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("shopNo", shopNo);
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_SHOP_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    String shopName = data.get("shopName").getAsString();
                    tvUserName.setText(shopName);
                    String logoPath = data.get("logoPath").getAsString();
                    CommonUtils.showImage(logoSDV, logoPath);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                showPopup();
                break;
            default:

        }
    }

    private void showPopup() {
        if (popupWindowUtils == null) {
            popupWindowUtils = new PopupWindowUtils(this, R.layout.popup_qrcode_layout)
                    .setLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setStyle(R.style.popupWindowAsBottom)
                    .setOnClickListenerByViewId(R.id.btn_shape_qrcode, new PopupWindowUtils.onPopupWindClickListener() {
                        @Override
                        public void onClick(View view) {
                            CommonUtils.shareBitmap(QrCodeActivity.this, mImageQR = getViewBitmap(layoutQRCode));
                            popupWindowUtils.dismiss();
                        }
                    })
                    .setOnClickListenerByViewId(R.id.btn_save_qrcode, new PopupWindowUtils.onPopupWindClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 保存二维码
                            saveQRCode();
                            popupWindowUtils.dismiss();
                        }
                    })
                    .setOnClickListenerByViewId(R.id.btn_cancel, new PopupWindowUtils.onPopupWindClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindowUtils.dismiss();
                        }
                    })
            ;
        }
        popupWindowUtils.showAtLocation(this, tv_qr_title, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 将View转为Bitmap
     *
     * @param addViewContent View
     * @return Bitmap
     */
    public Bitmap getViewBitmap(View addViewContent) {

//        addViewContent.setDrawingCacheEnabled(true);
//        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
//        addViewContent.buildDrawingCache();
//        Bitmap cacheBitmap = addViewContent.getDrawingCache();
//        return Bitmap.createBitmap(cacheBitmap);

        addViewContent.setDrawingCacheEnabled(true);
        Bitmap bitmapResult = Bitmap.createBitmap(addViewContent.getDrawingCache());
        addViewContent.setDrawingCacheEnabled(false);
        return bitmapResult;
    }

    /**
     * 保存二维码
     */
    private void saveQRCode() {
        if (!requestCodeLocationPermissions()) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageQR = getViewBitmap(layoutQRCode);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FileUtil.saveBitmap(QrCodeActivity.this, mImageQR);
            }
        }, 1500);
        ToastUtils.showToast("保存成功!");
    }

    /**
     * 请求权限
     *
     * @return
     */
    private boolean requestCodeLocationPermissions() {
        if (!AndPermission.hasPermissions(this, perms)) {
            CommonUtils.getPermission(this, null, perms);
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeLocationPermissions();
    }

}
