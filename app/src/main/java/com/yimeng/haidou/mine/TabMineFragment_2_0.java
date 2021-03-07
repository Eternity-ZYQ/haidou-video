package com.yimeng.haidou.mine;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lrad.adManager.LanRenManager;
import com.lrad.adManager.LoadAdError;
import com.lrad.adSource.IRewardVideoProvider;
import com.lrad.adlistener.ILanRenRewardAdListener;
import com.yimeng.base.BaseTakePhotoFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.config.XJConfig;
import com.yimeng.dialog.H5DialogUtils;
import com.yimeng.dialog.OpenVipDialog;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.AccountInfo;
import com.yimeng.entity.HomeDataBean;
import com.yimeng.entity.Member;
import com.yimeng.entity.MemberClickAdBean;
import com.yimeng.entity.MineTaskBean;
import com.yimeng.entity.OpenVipPriceBean;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.ObservableScrollView;
import com.yimeng.haidou.R;
import com.yimeng.haidou.circle.MineReleaseActivity;
import com.yimeng.haidou.offline.SelPartnerTypeActivity;
import com.yimeng.haidou.shop.collection.PayMentActivity;
import com.yimeng.haidou.task_3_0.MyFruitActivity;
import com.yimeng.haidou.task_3_0.MySeedActivity;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.Action;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.devio.takephoto.model.TResult;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

/**
 * <pre>
 *  Author : hui
 *  Time   : 2018/10/11 0011 下午 05:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人中心   v2.0
 * </pre>
 */
public class TabMineFragment_2_0 extends BaseTakePhotoFragment {


    private static final String TAG = "TabMineFragment_2_0";

    /**
     * 扫码
     */
    private final int FLAG_SCAN_QRCODE = 0x111;
    /**
     * 实名认证
     */
    private final int FLAG_AUTH_CODE = 0x222;
    @Bind(R.id.civ_user_head)
    CircleImageView civUserHead;
    @Bind(R.id.tv_user_mobile)
    TextView tvUserMobile;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_user_grade)
    TextView tvUserGrade;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    @Bind(R.id.layout_toolbar)
    RelativeLayout layoutToolbar;
    @Bind(R.id.layout_head)
    ConstraintLayout layoutHead;
    @Bind(R.id.bannerLayout)
    BGABanner mBannerLayout;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @Bind(R.id.tv_head_title)
    TextView tv_head_title;
    @Bind(R.id.tv_active)
    TextView tv_active;// 活跃度
    @Bind(R.id.tv_income)
    TextView tv_income;//新任务
    @Bind(R.id.tv_coupon)
    TextView tv_coupon;// 代金券
    @Bind(R.id.tv_balance)
    TextView tv_balance;//用户余额
    @Bind(R.id.tv_shop_wallet)
    TextView tv_shop_wallet;//商家账户
    @Bind(R.id.tv_score)
    TextView tv_score; // 积分
    @Bind(R.id.iv_create_shop)
    ImageView iv_create_shop;
    @Bind(R.id.iv_join_proxy)
    ImageView iv_join_proxy;
    @Bind(R.id.iv_vip)
    ImageView iv_vip;
    @Bind(R.id.tv_task_video)
    TextView tv_task_video;
    @Bind(R.id.tv_task_ads)
    TextView tv_task_ads;
    @Bind(R.id.btn_task_video)
    Button btn_task_video;
    @Bind(R.id.btn_task_ads)
    Button btn_task_ads;
    @Bind(R.id.reward_ad_container)
    ViewGroup reward_ad_container;


    private OkHttpCommon mOkHttpCommon;
    // 浏览广告次数
    private int mLookAdCount = 0;
    /**
     * 刷新用户数据
     */
    private BroadcastReceiver refreshMemberInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMemberInfo();
        }
    };
    private Handler mHandler = new Handler();
//    private RewardVideoAD rewardVideoAD;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 读取缓存
        try {
            String jsonStr = SPUtils.getInstance().getString(XJConfig.SP_MEMBER_COMPLE_AD, "");
            if (!jsonStr.isEmpty()) {
                MemberClickAdBean memberClickAdBean = GsonUtils.getGson().fromJson(jsonStr, MemberClickAdBean.class);
                Member member = CommonUtils.getMember();
                if (member == null) return;
                if (member.getMemberNo().equals(memberClickAdBean.getMemberNo()) && TimeUtils.isToday(memberClickAdBean.getLastClickTime())) {
                    mLookAdCount = memberClickAdBean.getCount();
                }
            }
        } catch (Exception e) {

        }


        // 广告
//        getAdsData();
        if (!CommonUtils.checkLogin()) {
            return;
        }
        getMemberInfo(false);

        getAccountInfo(false);

//        getTaskInfo();
    }

    private void getMemberInfo() {
        getMemberInfo(true);
    }

    private void getMemberInfo(boolean isShowLoading) {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_MEMBER_INFO, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取信息失败!"));
                    return;
                }

                String jsonStr = jsonObject.get("data").getAsJsonObject().toString();
                SharedPreferencesUtils.put(Constants.USER_INFO, jsonStr);
                setUserInfo();
                smartRefresh.finishRefresh();
            }
        }, isShowLoading);
    }

    private void getAccountInfo() {
        getAccountInfo(true);
    }

    /**
     * 获取任务数/代金券/余额/店铺余额
     */
    private void getAccountInfo(boolean isShowLoading) {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_ACCOUNT_INFO, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败!"));
                    return;
                }

                AccountInfo data = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().toString(), AccountInfo.class);

                Log.e(TAG, "onResponse: " + data);

                if (data != null) {
                    // 新任务
//                    tv_task.setText("新任务\n" + data.getNewTaskCount());
                    // 彩蛋
                    tv_income.setText(String.format("%.4f", data.getExtra() / XJConfig.MONEY_UNOT));
                    // 活跃值
                    tv_coupon.setText(String.format("%.4f", data.getBaodanbi() / XJConfig.MONEY_UNOT));
                    // 用户余额
                    tv_balance.setText(String.format("%.2f", data.getBalance() / XJConfig.MONEY_UNOT * 10000));
                    // 金蛋值
                    tv_shop_wallet.setText(String.format("%.4f", data.getYongjin() / XJConfig.MONEY_UNOT));
                    // 积分
                    tv_active.setText("积分 " + "\n" + data.getScore());
//                    tv_score.setText("积分: " + data.getScore());
                }
            }
        }, isShowLoading);
    }

    private void setUserInfo() {
        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfo)) {
            Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
            CommonUtils.showImage(civUserHead, member.getHeadPath(), R.mipmap.xj_logo);
            tvUserMobile.setText(member.getMobileNo());
            tvUserName.setVisibility(View.VISIBLE);
            tvUserName.setText(member.getNickname());
            tvUserGrade.setText(member.getMemberGradeAlias());
            // 礼品券
            tv_score.setText(member.getMemberGradeName());
//            if (member.getMemberType().equals("cityProvider") ||
//                    (member.getMemberType().equals("proxy") && member.getJob().equals("1"))) {
//                itemAgency.setVisibility(View.VISIBLE);
//            } else {
//                itemAgency.setVisibility(View.GONE);
//            }

//            // 活跃度
//            tv_active.setText(
//                    SpannableStringUtils.getBuilder("活跃度\n")
////                            .setProportion(0.6f)
//                            .append("" + member.getExtracts())
//                            .create()
//            );

            if (member.getIsOldUsr() == 0) {
                showActivateDialog();
            }


            // 店铺状态
            String shopStatus = member.getJob();
            if (shopStatus.equals("open")) {
                iv_create_shop.setImageResource(R.mipmap.ico_my_shop);
            } else if (shopStatus.equals("apply")) {
                // 审核中
            } else {
                iv_create_shop.setImageResource(R.mipmap.ico_create_shop);
            }

            // 是否为代理
//            if (member.isAgent()) {
//                iv_join_proxy.setImageResource(R.mipmap.ico_my_proxy);
//            } else {
//                iv_join_proxy.setImageResource(R.mipmap.ico_proxy);
//            }

            // 是否为会员
//            if(!TextUtils.isEmpty(member.getVipType()) && member.getVipEndTime() > System.currentTimeMillis()) {
//                iv_vip.setVisibility(View.VISIBLE);
//            }else{
//                iv_vip.setVisibility(View.GONE);
//            }

            try {
                if (TimeUtils.isToday(member.getAmtTime())) { // 已完成
                    btn_task_video.setEnabled(false);
                    btn_task_video.setText("已完成");
                } else {
                    btn_task_video.setEnabled(true);
                    btn_task_video.setText("去完成");
                }

                if (TimeUtils.isToday(member.getContractTime())) {
                    btn_task_ads.setEnabled(false);
                    btn_task_ads.setText("已完成");
                } else {
                    btn_task_ads.setEnabled(true);
                    btn_task_ads.setText("去完成(0/5)");

                    if (mLookAdCount == 0) {
                        btn_task_ads.setText("去完成(0/5)");
                    } else if (mLookAdCount >= 5) {
                        btn_task_ads.setEnabled(false);
                        btn_task_ads.setText("已完成");
                    } else {
                        btn_task_ads.setText("去完成(" + mLookAdCount + "/5)");
                    }
                }

            } catch (Exception e) {

            }
        } else {
            civUserHead.setImageResource(R.mipmap.xj_logo);
            tvUserMobile.setText("立即登录");
            tvUserName.setVisibility(View.GONE);
            iv_vip.setVisibility(View.GONE);
        }
    }

    /**
     * 激活用户按钮
     */
    private void showActivateDialog() {
//        if (TimeUtils.isToday(SPUtils.getInstance().getLong(XJConfig.LAST_SHOW_ACTIVATE, 0))) {
        if (XJConfig.INSTANCE.isActivate()) {
            // 今日该用户已弹窗
            return;
        }

        // 存入时间戳
        SPUtils.getInstance().put(XJConfig.LAST_SHOW_ACTIVATE, System.currentTimeMillis());
        XJConfig.INSTANCE.setActivate(true);

        SimpleDialog.showConfirmDialog(getActivity(), "提示",
                "您的账号还未激活，前往激活？", "取消", "确定",
                null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                    }
                }, false);
    }

    /**
     * 获取个人任务
     */
    private void getTaskInfo() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_MINE_TASK, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败!"));
                    return;
                }
                List<MineTaskBean> list = GsonUtils.getGson()
                        .fromJson(jsonObject.get("data").getAsJsonArray().toString(),
                                new TypeToken<List<MineTaskBean>>() {
                                }.getType());
                try {
                    tv_task_video.setText(list.get(0).getTaskName());
                    tv_task_ads.setText(list.get(1).getTaskName());
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 获取广告数据
     */
    private void getAdsData() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_BANNER_LIST, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() != 1) {
                    return;
                }

                List<HomeDataBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonArray().toString(),
                        new TypeToken<List<HomeDataBean>>() {
                        }.getType()
                );
                if (!list.isEmpty()) {
                    mBannerLayout.setVisibility(View.VISIBLE);
                    mBannerLayout.setData(list, null);
                    mBannerLayout.setAdapter(new BGABanner.Adapter<ImageView, HomeDataBean>() {
                        @Override
                        public void fillBannerItem(BGABanner bgaBanner,
                                                   ImageView view,
                                                   @NonNull HomeDataBean bannerBean,
                                                   int i) {
                            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            CommonUtils.showImage(view, bannerBean.getLogo());
                        }
                    });
                } else {
                    mBannerLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected int setLayoutResId() {
//        return R.layout.fragment_tab_mine_2_0_1;
//        return R.layout.fragment_tab_mine_2_0;
        return R.layout.fragment_tab_mine_xj;
    }

    @Override
    protected void init() {
        mOkHttpCommon = new OkHttpCommon();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REFRESH_MEMBER_INFO);
        getActivity().registerReceiver(refreshMemberInfoReceiver, intentFilter);

//        tv_score.setText("积分: 0");

    }

    @Override
    protected void initListener() {
        scrollView.setOnScrollChangeListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

//                if (scrollY >= layoutHead.getMeasuredHeight() - layoutToolbar.getMeasuredHeight() - DeviceUtils.dp2px(getActivity(), 40)) {
                if (scrollY >= civUserHead.getY() - civUserHead.getMeasuredHeight()) {
                    layoutToolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.xj_background));

                    if (CommonUtils.checkLogin()) {
                        tv_head_title.setText(CommonUtils.getMember().getNickname());
                    }
                } else {
                    layoutToolbar.setBackgroundColor(Color.TRANSPARENT);
                    tv_head_title.setText("");
                }
            }
        });

        // 广告点击
        mBannerLayout.setDelegate(new BGABanner.Delegate<ImageView, HomeDataBean>() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, ImageView view, @Nullable HomeDataBean o, int i) {
                ActivityUtils.getInstance().checkMenuClick(o);
            }
        });

        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onResume();
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.iv_qrCode, R.id.civ_user_head, R.id.tv_user_mobile, R.id.tv_user_name, R.id.tv_active,
            R.id.layout_balance, R.id.layout_shop, R.id.item_friend, R.id.tv_score,
            R.id.item_help, R.id.tv_look_all, R.id.item_collect,
            R.id.iv_setting, R.id.iv_scan, R.id.layout_coupon, R.id.item_feedback,
            R.id.item_release, R.id.iv_create_shop, R.id.iv_join_proxy, R.id.layout_income, R.id.iv_msg,
            R.id.item_seed, R.id.item_fruit, R.id.item_red_packer, R.id.item_reel, R.id.item_fans,
            R.id.btn_task_ads, R.id.btn_task_video, R.id.item_user_info, R.id.item_address,
            R.id.item_account, R.id.item_share
    })
    public void onViewClicked(View view) {
        if (!CommonUtils.checkLogin()
                && view.getId() != R.id.item_help
//                && view.getId() != R.id.item_custom_services
                && view.getId() != R.id.iv_setting) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        switch (view.getId()) {
            case R.id.tv_score: //  积分
//                showScoreDialog();
//                ActivityUtils.getInstance().jumpScoreLogsActivity(ScoreLogType.score);
                break;
            case R.id.iv_scan:
                // 扫码
                CommonUtils.getPermission(getActivity(), new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), FLAG_SCAN_QRCODE);
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.iv_qrCode:            // 我的二维码
                ActivityUtils.getInstance().jumpMyQrCode(1);
                break;
            case R.id.civ_user_head:        // 修改头像
//                showSelPopupWind(view, 1);
                break;
            case R.id.tv_active:
                // 活跃度
//                ActivityUtils.getInstance().jumpScoreLogsActivity(ScoreLogType.active);
                break;
            case R.id.tv_user_mobile:
            case R.id.tv_user_name:
                // 修改资料
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.layout_shop:       // 商家账户

//                if (!CommonUtils.checkAuth()) { // 先实名认证
//                    ToastUtils.showToast("实名开店可提现");
//                    ActivityUtils.getInstance().jumpActivityForResult(getActivity(), IDCardInfoActivity.class, FLAG_AUTH_CODE, null);
//                    return;
//                }
//                if (!CommonUtils.getMember().getJob().equals("open")) {
//                    ToastUtils.showToast("实名开店可提现");
//                    ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
//                    return;
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putString("walletType", "shop");
//                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);

                LogActivity.Companion.start(LogActivity.type_yongjin);
                break;
            case R.id.layout_balance:       // 用户余额
//                ActivityUtils.getInstance().jumpActivity(MyWalletActivity.class);

//                bundle = new Bundle();
//                bundle.putString("walletType", "mine");
//                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);

                LogActivity.Companion.start(LogActivity.type_balance);
                break;
            case R.id.tv_look_all:           // 我的订单
//                ActivityUtils.getInstance().jumpActivity(OrderActivity.class);
                ActivityUtils.getInstance().jumpOrderActivity(0, 0);
                break;
//            case R.id.item_address:         // 我的地址
//                ActivityUtils.getInstance().jumpActivity(AddressManageActivity.class);
//                break;
            case R.id.item_friend:          // 我的伙伴
                ActivityUtils.getInstance().jumpActivity(MyInviteActivity.class);
                break;
            case R.id.item_collect:         // 我的收藏
                ActivityUtils.getInstance().jumpActivity(MyFavoriteActivity.class);
//                MyGoodFavoriteActivity.Companion.start();
                break;
            case R.id.iv_msg:               // 消息中心
                ActivityUtils.getInstance().jumpActivity(MsgActivity.class);
                break;
            case R.id.item_help:            // 使用帮助
                ActivityUtils.getInstance().jumpH5Activity("玩转促销王", ConstantsUrl.APP_USE_HELP_URL);
                break;
//            case R.id.item_custom_services: // 联系我们
//                ActivityUtils.getInstance().jumpActivity(CustomerServiceActivity.class);
//                break;
            case R.id.item_feedback:        // 意见反馈
                ActivityUtils.getInstance().jumpActivity(FeedbackActivity.class);
                break;
            case R.id.iv_setting:          // 更多设置
                ActivityUtils.getInstance().jumpActivity(MoreSettingActivity.class);
                break;
            case R.id.iv_join_proxy:
//                applyProxy();
//                if (CommonUtils.getMember().isAgent()) { // 已是代理
//                    ActivityUtils.getInstance().jumpActivity(MyProxyActivity.class);
////                    ActivityUtils.getInstance().jumpH5Activity();
//                } else {// 成为代理
//                    ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_APPLY_PROXY + CommonUtils.getMember().getMemberNo());
//                }
                ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_APPLY_PROXY + CommonUtils.getMember().getMemberNo());
                break;
            case R.id.layout_coupon:            // 我的代金券
//                ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class);

//                LogActivity.Companion.start(LogActivity.type_baodanbi);

                MyActiveActivity.Companion.start();
                break;
            case R.id.item_release:         // 我的商圈
                ActivityUtils.getInstance().jumpActivity(MineReleaseActivity.class);
                break;

            case R.id.iv_create_shop:
                if (CommonUtils.getMember().getJob().equals("open")) {// 我的店铺
                    ActivityUtils.getInstance().jumpActivity(MineShopDetailActivity.class);
                } else {// 申请开店
                    shopJoin();
                }
                break;
            case R.id.layout_income:        // 收益账户
//                ActivityUtils.getInstance().jumpActivity(IncomeAccountActivity.class);

                // 彩蛋
                LogActivity.Companion.start(LogActivity.type_extra);
                break;
            case R.id.item_seed:            // 发财种子
                ActivityUtils.getInstance().jumpActivity(MySeedActivity.class);
                break;
            case R.id.item_fruit:            // 活跃果
                ActivityUtils.getInstance().jumpActivity(MyFruitActivity.class);
                break;
            case R.id.item_red_packer:      // 推广红包
                ActivityUtils.getInstance().jumpActivity(RedPackerActivity.class);
                break;
            case R.id.item_reel: // 我的金蛋
                MyReelActivity.Companion.start();
                break;
            case R.id.item_fans:    // 我的粉丝
                MyFansActivity.Companion.start();
                break;
            case R.id.btn_task_ads:     // 广告任务
                showAd();
                // showRongheAd();
                break;
            case R.id.btn_task_video:     // 视频任务
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.video));
                break;
            case R.id.item_user_info:   // 个人资料
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.item_address:     // 收货地址
                ActivityUtils.getInstance().jumpAddressManager(false);
                break;
            case R.id.item_account:     // 账户安全
                ActivityUtils.getInstance().jumpActivity(AccountSecurityActivity.class);
                break;
            case R.id.item_share:       // 分享
                CommonUtils.shareApp();
                break;
            default:
                break;
        }
    }

    /**
     * 申请开店
     */
    private void shopJoin() {
        if ((Boolean) SharedPreferencesUtils.get(Constants.MINE_SHOP_APPLY_LOADING, false)) {
            ToastUtils.showToast("店铺审核中");
            return;
        }
        Member member = CommonUtils.getMember();
        if (member == null) {
            return;
        }
        if (!CommonUtils.checkAuth()) { // 先实名认证
            ToastUtils.showToast("您还未实名, 请先实名认证吧");
            ActivityUtils.getInstance().jumpActivityForResult(getActivity(), IDCardInfoActivity.class, FLAG_AUTH_CODE, null);
            return;
        }
        ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
    }

    /**
     * 显示广告
     */
    private void showAd() {
        Log.d("huiger-msg", "TabMineFragment_2_0 -> showAd: " + "");
        LanRenManager.getInstance().loadReward(getActivity(), "20000062", new ILanRenRewardAdListener() {

            @Override
            public void onAdError(LoadAdError loadAdError) {
                ToastUtils.showToast(loadAdError.getMessage());
            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdClose() {
                reward_ad_container.setVisibility(View.GONE);
            }

            @Override
            public void onAdExpose() {

            }

            @Override
            public void onAdLoad(IRewardVideoProvider iRewardVideoProvider) {
                reward_ad_container.setVisibility(View.VISIBLE);
                iRewardVideoProvider.show(getActivity(), reward_ad_container);
            }

            @Override
            public void onAdLoadList(List<IRewardVideoProvider> list) {

            }

            @Override
            public void onReward() {

            }

            @Override
            public void onVideoComplete() {
                mLookAdCount++;
                if (mLookAdCount >= 5) {
                    btn_task_ads.setText("已完成");
                    btn_task_ads.setEnabled(false);
                    lookAdComplete();
                } else {
                    btn_task_ads.setText("去完成(" + mLookAdCount + "/5)");
                }

                Member member = CommonUtils.getMember();
                if (member != null) {
                    MemberClickAdBean memberClickAdBean = new MemberClickAdBean();
                    memberClickAdBean.setCount(mLookAdCount);
                    memberClickAdBean.setLastClickTime(System.currentTimeMillis());
                    memberClickAdBean.setMemberNo(member.getMemberNo());
                    SPUtils.getInstance().put(XJConfig.SP_MEMBER_COMPLE_AD, GsonUtils.getGson().toJson(memberClickAdBean));
                }
            }
        });

//        rewardVideoAD = new RewardVideoAD(getActivity(), XJConfig.GDTAD_VIDEO_AD_ID, new RewardVideoADListener() {
//            @Override
//            public void onADClick() {
//                Log.d("huiger", "onADClick: ");
//            }
//
//            @Override
//            public void onADClose() {
//                Log.d("huiger", "onADClose: ");
////                if (mLookAdCount >= 5) {
////                    lookAdComplete();
////                } else {
////                    int count = 5 - mLookAdCount;
////                    ToastUtils.showToast("您还有" + count + "个广告任务");
////                    showAd();
////                }
//            }
//
//            @Override
//            public void onADExpose() {
//                Log.d("huiger", "onADExpose: ");
//            }
//
//            /**
//             * 广告加载成功，可在此回调后进行广告展示
//             **/
//            @Override
//            public void onADLoad() {
//                Log.d("huiger", "onADLoad: ");
//
//                String msg = "load ad success ! expireTime = " + new Date(System.currentTimeMillis() +
//                        rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
//                Log.d("huiger", msg);
//
//                if (rewardVideoAD.getRewardAdType() == RewardVideoAD.REWARD_TYPE_VIDEO) {
//                    Log.d("huiger", "eCPMLevel = " + rewardVideoAD.getECPMLevel() + " ,video duration = " + rewardVideoAD.getVideoDuration());
//                } else if (rewardVideoAD.getRewardAdType() == RewardVideoAD.REWARD_TYPE_PAGE) {
//                    Log.d("huiger", "eCPMLevel = " + rewardVideoAD.getECPMLevel());
//                }
//
//
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rewardVideoAD.showAD(getActivity());
//                    }
//                }, 500);
//            }
//
//            @Override
//            public void onADShow() {
//                Log.d("huiger", "onADShow: ");
//            }
//
//            @Override
//            public void onError(AdError adError) {
//                Log.d("huiger", "onError: " + adError.getErrorMsg());
//                ToastUtils.showToast(adError.getErrorMsg());
//            }
//
//            @Override
//            public void onReward() {
//                Log.d("huiger", "onReward: ");
//            }
//
//            /**
//             * 视频素材缓存成功，可在此回调后进行广告展示
//             */
//            @Override
//            public void onVideoCached() {
//                Log.d("huiger", "onVideoCached: ");
//            }
//
//            /**
//             * 激励视频播放完毕
//             */
//            @Override
//            public void onVideoComplete() {
//                mLookAdCount++;
//                if (mLookAdCount >= 5) {
//                    btn_task_ads.setText("已完成");
//                    btn_task_ads.setEnabled(false);
//                    lookAdComplete();
//                } else {
//                    btn_task_ads.setText("去完成(" + mLookAdCount + "/5)");
//                }
//
//                Member member = CommonUtils.getMember();
//                if (member != null) {
//                    MemberClickAdBean memberClickAdBean = new MemberClickAdBean();
//                    memberClickAdBean.setCount(mLookAdCount);
//                    memberClickAdBean.setLastClickTime(System.currentTimeMillis());
//                    memberClickAdBean.setMemberNo(member.getMemberNo());
//                    SPUtils.getInstance().put(XJConfig.SP_MEMBER_COMPLE_AD, GsonUtils.getGson().toJson(memberClickAdBean));
//                }
//
//
//                Log.d("huiger", "存入缓存成功！");
//            }
//        }, true);
//        rewardVideoAD.loadAD();
    }

//    private void showRongheAd() {
//        AdCode adCode = new AdCode.Builder()
//                .setCodeId(XJConfig.RONGHE_VIDEO_AD_ID)
////                .setUserId(userId)  //用户ID  如需服务器回调则必传此参数
//                .setOrientation(AdCode.Orientation.VERTICAL)
//                .build();
//        FusionAdSDK.loadRewardVideoAd(getActivity(), adCode, new RewardVideoAdListener() {
//            /**
//             * 加载失败回调
//             * @param type
//             * @param code
//             * @param msg
//             */
//            @Override
//            public void onError(int type, int code, String msg) {
//                ToastUtils.showToast(msg);
//            }
//
//            /**
//             * 广告加载完成的回调，接入方可以在这个回调中进行渲染
//             * @param rewardVideoAd 激励视频广告接口
//             */
//            @Override
//            public void onRewardVideoAdLoad(RewardVideoAd rewardVideoAd) {
//                rewardVideoAd.show(getActivity());
//            }
//
//            /**
//             * 广告视频本地加载完成的回调，接入方可以在这个回调后直接播放本地视频
//             */
//            @Override
//            public void onRewardVideoCached() {
//
//            }
//
//            /**
//             * 广告曝光回调
//             */
//            @Override
//            public void onAdShow() {
//
//            }
//
//            /**
//             * 广告点击回调
//             */
//            @Override
//            public void onAdClicked() {
//
//            }
//
//            /**
//             * 广告关闭回调
//             */
//            @Override
//            public void onAdClosed() {
//
//            }
//
//            /**
//             * 视频播放完毕回调
//             */
//            @Override
//            public void onVideoComplete() {
//
//            }
//
//            /**
//             * 视频播放出错回调
//             */
//            @Override
//            public void onVideoError() {
//
//            }
//
//            /**
//             * 视频广告播完验证奖励有效性回调
//             * var1为服务器主动验证机制时候的验证ID  不需要服务器或者服务器通知机制时候不需要关注此参数
//             */
//            @Override
//            public void onReward(String var1) {
//
//            }
//
//            /**
//             * 视频广告计数统计回调 注：2.7.8版本次接口不做回调
//             * 注：2.7.8版本次接口不做回调
//             */
//            @Override
//            public void onVideoVerify() {
//
//            }
//        });
//    }

    /**
     * 激励视频完成
     */
    private void lookAdComplete() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("commonTaskNo", "20200326200754883meiri");

        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_COMPLETE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败!"));
                } else {
                    ToastUtils.showToast("任务完成！");
                }
            }
        });
    }

    /**
     * 我的订单
     */
    @OnClick({R.id.tv_order_pay, R.id.tv_order_noReceiving, R.id.tv_order_nocomment, R.id.tv_order_complete})
    public void orderClick(View v) {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        int position = 0;
        switch (v.getId()) {
            case R.id.tv_order_pay: // 未发货
                position = 0;
                break;
            case R.id.tv_order_noReceiving:// 待收货
                position = 1;
                break;
            case R.id.tv_order_nocomment:// 待评价
                position = 2;
                break;
            case R.id.tv_order_complete:// 已完成
                position = 3;
                break;
            default:
        }

        ActivityUtils.getInstance().jumpOrderActivity(0, position);
    }

    /**
     * 开通会员
     */
    @OnClick({R.id.iv_vip_open, R.id.iv_vip_year})
    public void onOpenVipClick(View v) {

        if (v.getId() == R.id.iv_vip_open) {// 开通会员
            openVip();
        } else {
            // vip年

        }


    }

    /**
     * 开通会员
     */
    private void openVip() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_VIP_PRICE, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)));
                    return;
                }

                String dataStr = jsonObject.get("data").getAsJsonObject().toString();
                OpenVipPriceBean openVipPriceBean = GsonUtils.getGson().fromJson(dataStr, OpenVipPriceBean.class);
                new XPopup.Builder(getActivity())
                        .asCustom(new OpenVipDialog(getActivity(), openVipPriceBean))
                        .show();
            }
        });
    }

    /**
     * 会员开通回调
     *
     * @param isOpen
     */
    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void openVipResult(boolean isOpen) {
        Log.d("huiger-msg", "TabMineFragment_2_0 -> openVipResult: " + "");
        if (isOpen) {
            ToastUtils.showToast("激活成功");
            getMemberInfo();
        } else {
            ToastUtils.showToast("激活失败");
        }
    }

//    /**
//     * 积分弹窗
//     */
//    private void showScoreDialog() {
//        new H5DialogUtils(getActivity())
//                .setTitle("积分规则")
//                .setUrl(ConstantsUrl.URL_PROTOCOL + "积分规则").show();
//    }

//    /**
//     * 代理申请
//     */
//    private void applyProxy() {
//        if (!CommonUtils.checkLogin()) {
//            ActivityUtils.getInstance().jumpLoginActivity();
//            return;
//        }
//        if (CommonUtils.getMember().getMemberGrade() < 1) {
//            ToastUtils.showToast("请先开店，并添加促销商品再参与代理活动");
//            return;
//        }
//
//        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_APPLY_PROXY, CommonUtils.createParams(), new CallbackCommon() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                ToastUtils.showToast(R.string.text_network_connected_error);
//            }
//
//            @Override
//            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
//                if (jsonObject.get("status").getAsInt() != 1) {
//                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "申请失败，请稍后再试！"));
//                    return;
//                }
//                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "代理申请提交成功，客服即将联系您，请耐心等候！"));
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_SCAN_QRCODE && resultCode == Activity.RESULT_OK && data != null) {
            String result = data.getStringExtra(Constant.CODED_CONTENT);
            if (result.contains("http")) {
                if (result.contains("payment") && result.contains("toAuthUnFreeze")) {
                    //http://cuxiao.fengdikj.com/cuxiao/sm/payment/toAuthUnFreeze.do?shopNo=201906191924573104666&&payAmt=2121
                    //扫码支付
                    String shopNo = "";
                    String payAmt = "";


                    if (result.contains("&&payAmt")) {
                        payAmt = result.substring(result.indexOf("payAmt=") + 7);
                        shopNo = result.substring(result.indexOf("shopNo=") + 7, result.indexOf("&&payAmt"));
                    } else {
                        shopNo = result.substring(result.indexOf("shopNo=") + 7);
                    }

                    Intent intent = new Intent(getActivity(), PayMentActivity.class);
                    intent.putExtra(Constants.INTENT_PARAM_SHOPNO, shopNo);
                    if (!payAmt.isEmpty()) {
                        intent.putExtra(Constants.INTENT_PARAM_MONEY, payAmt);
                    }
                    startActivity(intent);


                } else {
                    Map<String, String> paramsMap = CommonUtils.urlToMap(result);
                    if (paramsMap.containsKey("shopNo")) {
                        ActivityUtils.getInstance().jumpShopDetailActivity(paramsMap.get("shopNo"), false);
                    } else {
                        ActivityUtils.getInstance().jumpInternetExplorer(result);
                    }
                }
            } else {
                ToastUtils.showToast("扫描到:\t\t" + result);
            }

        } else if (requestCode == FLAG_AUTH_CODE && resultCode == Activity.RESULT_OK) {
            getMemberInfo();
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String pathUrl = getTakeSuccessPath(tResult);

        NetComment.uploadPic(getActivity(), pathUrl, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                updateUserHead(url);
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast(msg);
            }
        });
    }

    /**
     * 修改头像地址
     *
     * @param userHeadUrl 头像地址
     */
    private void updateUserHead(final String userHeadUrl) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("headPath", userHeadUrl);


        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_UPDATE_AVATAR, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改成功!"));
                    CommonUtils.showImage(civUserHead, userHeadUrl);
                    // 替换缓存
                    String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
                    Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
                    member.setHeadPath(userHeadUrl);
                    String resultStr = GsonUtils.getGson().toJson(member);
                    SharedPreferencesUtils.put(Constants.USER_INFO, resultStr);
                } else {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改失败!"));
                }
            }
        });
    }


}
