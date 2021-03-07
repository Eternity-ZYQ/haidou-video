package com.yimeng.haidou;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.SdkConfig;
import com.lrad.adManager.LanRenManager;
import com.lrad.adManager.LrAdConfig;
import com.yimeng.config.Constants;
import com.yimeng.config.XJConfig;
import com.yimeng.entity.Member;
import com.yimeng.jpush.TagAliasOperatorHelper;
import com.yimeng.retorfit.RetrofitHelper;
import com.yimeng.utils.CommonUtils;
import com.huige.library.HGUtils;
import com.huige.library.utils.log.LogLevel;
import com.huige.library.utils.log.LogUtils;
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.managers.setting.GlobalSetting;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;


/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/11 0011 下午 04:38.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class App extends Application {

    private static Context mContext;

    {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        String packageName = mContext.getPackageName();
        Log.d("Application", "Application onCreate 1: " + mContext.getPackageName());

        HGUtils.init(this);

        LogUtils.init().setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        initCSJAD();
        initGDTAD();
        initKSAD();
        initLRAD();

        Log.d("Application", "Application onCreate 2 : " + mContext.getPackageName());

        //Get all activity classes in the AndroidManifest.xml

        // initPush();

        Log.d("Application", "Application onCreate 3: " + mContext.getPackageName());

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // 友盟
        //  initUmeng();

//        BaiDuWakeUpManager.getInstance(this);

        // db
        LitePal.initialize(this);

        RetrofitHelper.INSTANCE.init(this);


        Utils.init(this);

        Log.d("Application", "Application onCreate 4: " + mContext.getPackageName());

        // 融合广告
//        FusionAdSDK.init(this, XJConfig.RONGHE_APPID);

        Log.d("Application", "Application onCreate 5: " + mContext.getPackageName());

    }

    /**
     * 推送
     */
    private void initPush() {
//        XGPushConfig.enableDebug(this, true);
//        // 绑定信鸽
//        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
//        if (TextUtils.isEmpty(userInfo)) {
//            return;
//        }
//        Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
//        if (member != null && !TextUtils.isEmpty(member.getMobileNo())) {
//            XGPushManager.bindAccount(this, member.getMobileNo());
//        } else {
//            XGPushManager.registerPush(this);
//        }

        JPushInterface.init(this);

        Member member = CommonUtils.getMember();
        if (member != null) {
            TagAliasOperatorHelper.getInstance().bindJPush(member.getMemberNo());
        }
    }

    /**
     * 友盟
     */
    private void initUmeng() {
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, "5d1f26924ca3571778000a8b", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin(Constants.WX_APPID, Constants.WX_SECRET);
        PlatformConfig.setQQZone("1108967600", "xUa9C5D4U7UFAdrl");
//        PlatformConfig.setSinaWeibo("2241160556", "7c63f932ef6938453c78c37b4421167f", "");
    }

    /**
     * 广点通
     */
    private void initGDTAD() {
        // 通过调用此方法初始化 SDK。如果需要在多个进程拉取广告，每个进程都需要初始化 SDK。
        GDTADManager.getInstance().initWith(this, "1111459889");

        GlobalSetting.setChannel(1);
        GlobalSetting.setEnableMediationTool(true);
    }

    private void initCSJAD() {
        TTAdSdk.init(this, new TTAdConfig.Builder()
                .appId("5145811")
                .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build());
    }

    private void initKSAD() {
        KsAdSDK.init(this, new SdkConfig.Builder()
                .appId("539000026") // 测试aapId，请联系快手平台申请正式AppId，必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(BuildConfig.DEBUG) // 是否开启sdk 调试日志 可选
                .build());
    }


    private void initLRAD() {
        LrAdConfig config = new LrAdConfig.Builder()
                .setAppKey("10000024")
                .setAppSecret("b5057dab98e43f7d4db32de337ed22d5")
                .enableAdnet()//开启广告点通
                .enableKuaiShou()//开启快手
                .enablePangle()//开启传山甲
//                .enableSigmob("sigmob的id", "sigmob的appKey")//sigmob
                .enablePX()//开启px，特别地，px不需要传入id或者appkey
                .setDebug(true)//开启日志 tag: LanRen_Log
                .build();
        LanRenManager.initConfig(config, this);
    }
}
