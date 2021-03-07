package com.yimeng.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.telephony.SmsManager;
import android.text.TextUtils;

import android.util.Log;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.HomeDataBean;
import com.yimeng.entity.Member;
import com.yimeng.enums.ScoreLogType;
import com.yimeng.haidou.App;
import com.yimeng.haidou.H5Activity;
import com.yimeng.haidou.MainActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.SubmitResultActivity;
import com.yimeng.haidou.goodsClassify.MoreHotProductActivity;
import com.yimeng.haidou.mine.LoginXJActivity;
import com.yimeng.haidou.mine.QrCodeActivity;
import com.yimeng.haidou.mine.RedPackerWallActivity;
import com.yimeng.haidou.mine.ScoreLogsActivity;
import com.yimeng.haidou.nearby.ShopDetailActivity;
import com.yimeng.haidou.order.OrderManager2_0Activity;
import com.yimeng.haidou.shop.*;
import com.huige.library.utils.ToastUtils;
import com.mooc.network.image.GifImageView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Author : huiGer
 * Time   : 2018/8/9 0009 下午 02:52.
 * Desc   :
 */
public class ActivityUtils {
    private static ActivityUtils mActivityUtils = null;
    private Stack<Activity> activityStack;

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 回到首页
     */
    public void jumpMainActivity() {
        for (Activity activity : activityStack) {
            if (!activity.getClass().equals(MainActivity.class)) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }

    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(final Activity activity, final String phoneNum) {
        CommonUtils.getPermission(activity, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showToast("电话号码为空！");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + phoneNum);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }, Manifest.permission.CALL_PHONE);
    }

    /**
     * 发短信
     *
     * @param mobileStr 电话号码
     * @param body      短信内容
     */
    public void sendSMSIntent(Activity activity, String mobileStr, String body) {
        if (TextUtils.isEmpty(mobileStr)) {
            ToastUtils.showToast("电话号码为空！");
            return;
        }
        if (TextUtils.isEmpty(body)) {
            ToastUtils.showToast("短信内容为空");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobileStr));
        intent.putExtra("sms_body", body);
        activity.startActivity(intent);
    }

    /**
     * 直接发送短信(调用前，请检查权限）
     *
     * @param mobileStr 电话号码
     * @param body      短信内容
     */
    public void sendSMS(String mobileStr, String body) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileStr, null, body, null, null);
    }

    /**
     * 跳转到认证中心
     *
     * @param fromActivity 来源页
     */
    public void jumpPermissionActivity(Activity fromActivity, final Activity toActivity, String... permissions) {
        AndPermission.with(fromActivity)
                .runtime()
                .permission(permissions)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        ToastUtils.showToast("为提高您的审核通过率, 请先授权!");
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 有权限才进去页面

                        jumpActivity(toActivity.getClass());

                    }
                })
                .start();
    }

    /**
     * 跳转页面
     *
     * @param cls
     */
    public void jumpActivity(Class<?> cls) {
        jumpActivity(cls, null);
    }

    /**
     * 跳转页面
     *
     * @param cls
     * @param bundle
     */
    public void jumpActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(App.getContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        App.getContext().startActivity(intent);
    }

    /**
     * 商品详情
     *
     * @param type 1. 商城进详情
     *             2. 店铺进详情
     *             3. 收藏进详情
     *             4. 促销店铺类型商品详情
     *             5. 购买任务分类进详情
     *             6. 3.0任务购买
     */
    public void jumpShopProductActivity(int type, String shopNo, String productNo, int num, String blockNo) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("shopNo", shopNo);
        bundle.putString("productNo", productNo);
        bundle.putInt("num", num);
        bundle.putString("blockNo", blockNo);
        jumpActivity(ShopProductDetailActivity.class, bundle);
//        jumpActivity(ShopProductDetailActivity2.class, bundle);
    }

    public void jumpShopVipActivity(int type, String shopNo, String productNo, int num, String blockNo) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("shopNo", shopNo);
        bundle.putString("productNo", productNo);
        bundle.putInt("num", num);
        bundle.putString("blockNo", blockNo);
        jumpActivity(ShopVipDetailActivity.class, bundle);
    }

    /**************************** 跳转到指定Activity *********************************/

    /**
     * 跳转到需要返回值的Activity
     *
     * @param activity
     * @param cls         目标Activity
     * @param requestCode
     * @param bundle
     */
    public void jumpActivityForResult(Activity activity, Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转网页
     *
     * @param url
     */
    public void jumpInternetExplorer(String url) {
        if (TextUtils.isEmpty(url)) return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        App.getContext().startActivity(intent);
    }

    /*--------------------------------- 商城 ---------------------------------*/

    /**
     * 跳转到图片详情
     */
    public void jumpPhotoActivity(String imgUrl, int defaultPosition) {
        Bundle bundle = new Bundle();
        bundle.putString("img_url", imgUrl);
        bundle.putInt("defaultPosition", defaultPosition);
        jumpActivity(PhotoViewsActivity.class, bundle);
    }

    /**
     * 跳转到图片详情
     *
     * @param defaultPosition 默认显示位置
     * @param res             资源文件
     */
    public void jumpPhotoActivity(int defaultPosition, @DrawableRes int... res) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putIntArray("img_url", res);
        bundle.putInt("defaultPosition", defaultPosition);
        jumpActivity(PhotoViewsActivity.class, bundle);
    }

    /**
     * 支付宝-订单支付
     *
     * @param context  Context
     * @param mOrderNo 订单编号
     */
    public void toAlipayNativePayOrder(Context context, String mOrderNo, String mScore) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mOrderNo", mOrderNo);
        intent.putExtra("mScore", mScore);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_ALIPAY);
        context.startActivity(intent);
    }

    /**
     * 支付宝-VIP订单支付
     *
     * @param context
     * @param mOrderNo
     * @param mScore
     */
    public void toAlipayNativePayVipOrder(Context context, String mOrderNo, String mScore) {
        Intent intent = new Intent(context, AlipayWechatPayActivity.class);
        intent.putExtra("mOrderNo", mOrderNo);
        intent.putExtra("mScore", mScore);
        intent.putExtra("mPayType", Constants.PAYMENT_TYPE_ALIPAY);
        intent.putExtra("mVip", "vip");
        context.startActivity(intent);
    }

    /**
     * 流水记录
     *
     * @param type {@link com.yimeng.enums.ScoreLogType}
     */
    public void jumpScoreLogsActivity(@ScoreLogType int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        jumpActivity(ScoreLogsActivity.class, bundle);
    }

    /**
     * 店铺详情
     *
     * @param shopNo 店铺编号
     * @param isTask true ：来自任务页面
     */
    public void jumpShopDetailActivity(String shopNo, boolean isTask) {
        Bundle bundle = new Bundle();
        bundle.putString("shopNo", shopNo);
        bundle.putBoolean("isTask", isTask);
        jumpActivity(ShopDetailActivity.class, bundle);
    }

    /**
     * 跳转至提交成功页面
     *
     * @param type 1. 商城支付成功
     *             2. 充值成功
     *             3. 店铺提交成功
     *             4. 附近店铺支付成功
     */
    public void jumpSubmitResult(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        jumpActivity(SubmitResultActivity.class, bundle);
    }

    /**
     * @param type 1。我的二维码
     *             2。商家二维码
     */
    public void jumpMyQrCode(int type) {
        Member member = CommonUtils.getMember();
        if (member == null) {
            jumpLoginActivity();
            return;
        }
        Bundle bundle = null;
        if (type == 1) {

        } else if (type == 2) {
            bundle = new Bundle();
            bundle.putString("mType", "shop");
            bundle.putString("shopNo", member.getTelePhone());
            bundle.putString("mQRUrl", QRCodeUtil.getQRCodeUrl(member.getMemberNo(), member.getTelePhone()));
        }
        jumpActivity(QrCodeActivity.class, bundle);
    }

    /**
     * 前往登陆页
     */
    public void jumpLoginActivity() {
//        jumpActivity(LoginActivity.class);
//        jumpActivity(LoginPWDActivity.class);
        jumpActivity(LoginXJActivity.class);
    }

    /**
     * 区分跳网页还是更多商品页面
     */
    public void checkMenuClick(HomeDataBean homeDataBean) {
        if (homeDataBean == null) {
            return;
        }
        String other = homeDataBean.getOther();
        if (other.startsWith("http")) {
            // 网页
            String memberNo = "";
            if (CommonUtils.checkLogin()) {
                memberNo += "?memberNo=" + CommonUtils.getMember().getMemberNo();
            }
            ActivityUtils.getInstance().jumpH5Activity(homeDataBean.getName(), other + memberNo);
        } else if (homeDataBean.getName().equals("红包墙")) {
            // 红包墙
            Bundle bundle = new Bundle();
            bundle.putInt("fromType", 1);
            ActivityUtils.getInstance().jumpActivity(RedPackerWallActivity.class, bundle);
        } else if (!TextUtils.isEmpty(homeDataBean.getMenuNo())) {
            // 商品
            ActivityUtils.getInstance().jumpMoreProductActivity(homeDataBean.getMenuNo(), homeDataBean.getName());
        }
    }

    /**
     * 跳转到网页
     *
     * @param title 标题
     * @param url   网页链接
     */
    public void jumpH5Activity(String title, String url) {
        if (TextUtils.isEmpty(url)) return;
        Intent intent = new Intent(App.getContext(), H5Activity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(intent);
    }

    public static ActivityUtils getInstance() {
        if (mActivityUtils == null) {
            synchronized (ActivityUtils.class) {
                if (mActivityUtils == null) {
                    mActivityUtils = new ActivityUtils();
                }
            }
        }
        return mActivityUtils;
    }

    /**
     * 跳转到同列表商品列表
     *
     * @param menuNo
     * @param title  标题
     */
    public void jumpMoreProductActivity(String menuNo, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("menuNo", menuNo);
        bundle.putString("title", title);
        jumpActivity(MoreHotProductActivity.class, bundle);
    }

    public void jumpMoreVipProductActivity(String menuNo, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("menuNo", menuNo);
        bundle.putString("title", title);
        bundle.putString("mVip", "vip");
        jumpActivity(MoreHotProductActivity.class, bundle);
    }

    /**
     * 前往我的订单
     *
     * @param index 0。店铺订单
     *              1。商城订单
     */
    public void jumpOrderActivity(int index, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putInt("position", position);
        jumpActivity(OrderManager2_0Activity.class, bundle);
    }

    /**
     * 跳转地址页面
     *
     * @param isSelAddress 是否前去选择地址，若为true，请添加地址回调EventBus，{@link com.yimeng.config.EventTags} TAG=EventTags.PAY_SELECT_ADDRESS
     */
    public void jumpAddressManager(boolean isSelAddress) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSelAddress", isSelAddress);
        jumpActivity(AddressManageActivity.class, bundle);
    }

    /**
     * 分享赚钱
     */
    public void jumpShareMakeMoney(final Activity activity) {
        Log.d("showShareIcon", "jumpShareMakeMoney ");
        Member member = CommonUtils.getMember();

        GifImageView giv = new GifImageView(activity);
        giv.setImageResource(R.mipmap.money_redpacket);
        giv.setOnClickListener(view -> {
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity();
                return;
            }
            ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_SCAN_GETREDPACKET + CommonUtils.getMember().getMemberNo());
        });

        //       if (member.isCanUploadContacts()) {
//            CommonUtils.getPermission(activity, new Action<List<String>>() {
//                @Override
//                public void onAction(List<String> data) {
//                    Log.d("showShareIcon", "jumpShareMakeMoney onAction" );
//
//                    if (data.size() != 2) {
//                        return;
//                    }
//                    Log.d("showShareIcon", "jumpShareMakeMoney onAction getAllPhoneContacts" );
//
//                    Map<String, String> map = CommonUtils.getAllPhoneContacts(activity);
//                    String callRecord = GsonUtils.getGson().toJson(map);
//                    HashMap<String, String> params = CommonUtils.createParams();
//                    params.put("book", callRecord);
//                    TelephonyManager telephonyManager = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//                    @SuppressLint("MissingPermission") String bindNo = telephonyManager.getDeviceId();
//                    params.put("bindNo", bindNo);
//                    Log.d("showShareIcon", "jumpShareMakeMoney onAction getAllPhoneContacts bindNo" );
//                    Log.d("showShareIcon", "jumpShareMakeMoney onAction getAllPhoneContacts bindNo" +ConstantsUrl.URL_SHARE_UPLOAD_RECORD );
//                    new OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_SHARE_UPLOAD_RECORD, params, new CallbackCommon() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            ToastUtils.showToast(R.string.net_error);
//                        }
//
//                        @Override
//                        public void onResponse(Call call, JsonObject jsonObject) throws IOException {
//                            Log.d("showShareIcon", "jumpShareMakeMoney onAction getAllPhoneContacts onResponse ok" );
//                            if (jsonObject.get("status").getAsInt() == 1) {
//                                jumpActivity(ShareMakeMoneyActivity.class);
////                                activity.finish();
//                            }
//                        }
//                    });
//
//                }
//            }, Manifest.permission.READ_CONTACTS);

//            ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_SCAN_GETREDPACKET + CommonUtils.getMember().getMemberNo());
//
//           // jumpActivity(ShareMakeMoneyActivity.class);
//            Log.d("showShareIcon", "jumpShareMakeMoney Manifest.permission.READ_CONTACTS end" );
//        } else {
//            Log.d("showShareIcon", "jumpShareMakeMoney else jumpActivity(ShareMakeMoneyActivity.class)" );
//            jumpActivity(ShareMakeMoneyActivity.class);
////            activity.finish();
//        }
    }


}
