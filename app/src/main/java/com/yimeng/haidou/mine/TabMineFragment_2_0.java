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
 *  Time   : 2018/10/11 0011 ?????? 05:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : ????????????   v2.0
 * </pre>
 */
public class TabMineFragment_2_0 extends BaseTakePhotoFragment {


    private static final String TAG = "TabMineFragment_2_0";

    /**
     * ??????
     */
    private final int FLAG_SCAN_QRCODE = 0x111;
    /**
     * ????????????
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
    TextView tv_active;// ?????????
    @Bind(R.id.tv_income)
    TextView tv_income;//?????????
    @Bind(R.id.tv_coupon)
    TextView tv_coupon;// ?????????
    @Bind(R.id.tv_balance)
    TextView tv_balance;//????????????
    @Bind(R.id.tv_shop_wallet)
    TextView tv_shop_wallet;//????????????
    @Bind(R.id.tv_score)
    TextView tv_score; // ??????
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
    // ??????????????????
    private int mLookAdCount = 0;
    /**
     * ??????????????????
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
        // ????????????
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


        // ??????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "??????????????????!"));
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
     * ???????????????/?????????/??????/????????????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "??????????????????!"));
                    return;
                }

                AccountInfo data = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().toString(), AccountInfo.class);

                Log.e(TAG, "onResponse: " + data);

                if (data != null) {
                    // ?????????
//                    tv_task.setText("?????????\n" + data.getNewTaskCount());
                    // ??????
                    tv_income.setText(String.format("%.4f", data.getExtra() / XJConfig.MONEY_UNOT));
                    // ?????????
                    tv_coupon.setText(String.format("%.4f", data.getBaodanbi() / XJConfig.MONEY_UNOT));
                    // ????????????
                    tv_balance.setText(String.format("%.2f", data.getBalance() / XJConfig.MONEY_UNOT * 10000));
                    // ?????????
                    tv_shop_wallet.setText(String.format("%.4f", data.getYongjin() / XJConfig.MONEY_UNOT));
                    // ??????
                    tv_active.setText("?????? " + "\n" + data.getScore());
//                    tv_score.setText("??????: " + data.getScore());
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
            // ?????????
            tv_score.setText(member.getMemberGradeName());
//            if (member.getMemberType().equals("cityProvider") ||
//                    (member.getMemberType().equals("proxy") && member.getJob().equals("1"))) {
//                itemAgency.setVisibility(View.VISIBLE);
//            } else {
//                itemAgency.setVisibility(View.GONE);
//            }

//            // ?????????
//            tv_active.setText(
//                    SpannableStringUtils.getBuilder("?????????\n")
////                            .setProportion(0.6f)
//                            .append("" + member.getExtracts())
//                            .create()
//            );

            if (member.getIsOldUsr() == 0) {
                showActivateDialog();
            }


            // ????????????
            String shopStatus = member.getJob();
            if (shopStatus.equals("open")) {
                iv_create_shop.setImageResource(R.mipmap.ico_my_shop);
            } else if (shopStatus.equals("apply")) {
                // ?????????
            } else {
                iv_create_shop.setImageResource(R.mipmap.ico_create_shop);
            }

            // ???????????????
//            if (member.isAgent()) {
//                iv_join_proxy.setImageResource(R.mipmap.ico_my_proxy);
//            } else {
//                iv_join_proxy.setImageResource(R.mipmap.ico_proxy);
//            }

            // ???????????????
//            if(!TextUtils.isEmpty(member.getVipType()) && member.getVipEndTime() > System.currentTimeMillis()) {
//                iv_vip.setVisibility(View.VISIBLE);
//            }else{
//                iv_vip.setVisibility(View.GONE);
//            }

            try {
                if (TimeUtils.isToday(member.getAmtTime())) { // ?????????
                    btn_task_video.setEnabled(false);
                    btn_task_video.setText("?????????");
                } else {
                    btn_task_video.setEnabled(true);
                    btn_task_video.setText("?????????");
                }

                if (TimeUtils.isToday(member.getContractTime())) {
                    btn_task_ads.setEnabled(false);
                    btn_task_ads.setText("?????????");
                } else {
                    btn_task_ads.setEnabled(true);
                    btn_task_ads.setText("?????????(0/5)");

                    if (mLookAdCount == 0) {
                        btn_task_ads.setText("?????????(0/5)");
                    } else if (mLookAdCount >= 5) {
                        btn_task_ads.setEnabled(false);
                        btn_task_ads.setText("?????????");
                    } else {
                        btn_task_ads.setText("?????????(" + mLookAdCount + "/5)");
                    }
                }

            } catch (Exception e) {

            }
        } else {
            civUserHead.setImageResource(R.mipmap.xj_logo);
            tvUserMobile.setText("????????????");
            tvUserName.setVisibility(View.GONE);
            iv_vip.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????
     */
    private void showActivateDialog() {
//        if (TimeUtils.isToday(SPUtils.getInstance().getLong(XJConfig.LAST_SHOW_ACTIVATE, 0))) {
        if (XJConfig.INSTANCE.isActivate()) {
            // ????????????????????????
            return;
        }

        // ???????????????
        SPUtils.getInstance().put(XJConfig.LAST_SHOW_ACTIVATE, System.currentTimeMillis());
        XJConfig.INSTANCE.setActivate(true);

        SimpleDialog.showConfirmDialog(getActivity(), "??????",
                "??????????????????????????????????????????", "??????", "??????",
                null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                    }
                }, false);
    }

    /**
     * ??????????????????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "??????????????????!"));
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
     * ??????????????????
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

//        tv_score.setText("??????: 0");

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

        // ????????????
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
            case R.id.tv_score: //  ??????
//                showScoreDialog();
//                ActivityUtils.getInstance().jumpScoreLogsActivity(ScoreLogType.score);
                break;
            case R.id.iv_scan:
                // ??????
                CommonUtils.getPermission(getActivity(), new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), FLAG_SCAN_QRCODE);
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.iv_qrCode:            // ???????????????
                ActivityUtils.getInstance().jumpMyQrCode(1);
                break;
            case R.id.civ_user_head:        // ????????????
//                showSelPopupWind(view, 1);
                break;
            case R.id.tv_active:
                // ?????????
//                ActivityUtils.getInstance().jumpScoreLogsActivity(ScoreLogType.active);
                break;
            case R.id.tv_user_mobile:
            case R.id.tv_user_name:
                // ????????????
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.layout_shop:       // ????????????

//                if (!CommonUtils.checkAuth()) { // ???????????????
//                    ToastUtils.showToast("?????????????????????");
//                    ActivityUtils.getInstance().jumpActivityForResult(getActivity(), IDCardInfoActivity.class, FLAG_AUTH_CODE, null);
//                    return;
//                }
//                if (!CommonUtils.getMember().getJob().equals("open")) {
//                    ToastUtils.showToast("?????????????????????");
//                    ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
//                    return;
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putString("walletType", "shop");
//                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);

                LogActivity.Companion.start(LogActivity.type_yongjin);
                break;
            case R.id.layout_balance:       // ????????????
//                ActivityUtils.getInstance().jumpActivity(MyWalletActivity.class);

//                bundle = new Bundle();
//                bundle.putString("walletType", "mine");
//                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);

                LogActivity.Companion.start(LogActivity.type_balance);
                break;
            case R.id.tv_look_all:           // ????????????
//                ActivityUtils.getInstance().jumpActivity(OrderActivity.class);
                ActivityUtils.getInstance().jumpOrderActivity(0, 0);
                break;
//            case R.id.item_address:         // ????????????
//                ActivityUtils.getInstance().jumpActivity(AddressManageActivity.class);
//                break;
            case R.id.item_friend:          // ????????????
                ActivityUtils.getInstance().jumpActivity(MyInviteActivity.class);
                break;
            case R.id.item_collect:         // ????????????
                ActivityUtils.getInstance().jumpActivity(MyFavoriteActivity.class);
//                MyGoodFavoriteActivity.Companion.start();
                break;
            case R.id.iv_msg:               // ????????????
                ActivityUtils.getInstance().jumpActivity(MsgActivity.class);
                break;
            case R.id.item_help:            // ????????????
                ActivityUtils.getInstance().jumpH5Activity("???????????????", ConstantsUrl.APP_USE_HELP_URL);
                break;
//            case R.id.item_custom_services: // ????????????
//                ActivityUtils.getInstance().jumpActivity(CustomerServiceActivity.class);
//                break;
            case R.id.item_feedback:        // ????????????
                ActivityUtils.getInstance().jumpActivity(FeedbackActivity.class);
                break;
            case R.id.iv_setting:          // ????????????
                ActivityUtils.getInstance().jumpActivity(MoreSettingActivity.class);
                break;
            case R.id.iv_join_proxy:
//                applyProxy();
//                if (CommonUtils.getMember().isAgent()) { // ????????????
//                    ActivityUtils.getInstance().jumpActivity(MyProxyActivity.class);
////                    ActivityUtils.getInstance().jumpH5Activity();
//                } else {// ????????????
//                    ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_APPLY_PROXY + CommonUtils.getMember().getMemberNo());
//                }
                ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_APPLY_PROXY + CommonUtils.getMember().getMemberNo());
                break;
            case R.id.layout_coupon:            // ???????????????
//                ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class);

//                LogActivity.Companion.start(LogActivity.type_baodanbi);

                MyActiveActivity.Companion.start();
                break;
            case R.id.item_release:         // ????????????
                ActivityUtils.getInstance().jumpActivity(MineReleaseActivity.class);
                break;

            case R.id.iv_create_shop:
                if (CommonUtils.getMember().getJob().equals("open")) {// ????????????
                    ActivityUtils.getInstance().jumpActivity(MineShopDetailActivity.class);
                } else {// ????????????
                    shopJoin();
                }
                break;
            case R.id.layout_income:        // ????????????
//                ActivityUtils.getInstance().jumpActivity(IncomeAccountActivity.class);

                // ??????
                LogActivity.Companion.start(LogActivity.type_extra);
                break;
            case R.id.item_seed:            // ????????????
                ActivityUtils.getInstance().jumpActivity(MySeedActivity.class);
                break;
            case R.id.item_fruit:            // ?????????
                ActivityUtils.getInstance().jumpActivity(MyFruitActivity.class);
                break;
            case R.id.item_red_packer:      // ????????????
                ActivityUtils.getInstance().jumpActivity(RedPackerActivity.class);
                break;
            case R.id.item_reel: // ????????????
                MyReelActivity.Companion.start();
                break;
            case R.id.item_fans:    // ????????????
                MyFansActivity.Companion.start();
                break;
            case R.id.btn_task_ads:     // ????????????
                showAd();
                // showRongheAd();
                break;
            case R.id.btn_task_video:     // ????????????
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.video));
                break;
            case R.id.item_user_info:   // ????????????
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.item_address:     // ????????????
                ActivityUtils.getInstance().jumpAddressManager(false);
                break;
            case R.id.item_account:     // ????????????
                ActivityUtils.getInstance().jumpActivity(AccountSecurityActivity.class);
                break;
            case R.id.item_share:       // ??????
                CommonUtils.shareApp();
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    private void shopJoin() {
        if ((Boolean) SharedPreferencesUtils.get(Constants.MINE_SHOP_APPLY_LOADING, false)) {
            ToastUtils.showToast("???????????????");
            return;
        }
        Member member = CommonUtils.getMember();
        if (member == null) {
            return;
        }
        if (!CommonUtils.checkAuth()) { // ???????????????
            ToastUtils.showToast("???????????????, ?????????????????????");
            ActivityUtils.getInstance().jumpActivityForResult(getActivity(), IDCardInfoActivity.class, FLAG_AUTH_CODE, null);
            return;
        }
        ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
    }

    /**
     * ????????????
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
                    btn_task_ads.setText("?????????");
                    btn_task_ads.setEnabled(false);
                    lookAdComplete();
                } else {
                    btn_task_ads.setText("?????????(" + mLookAdCount + "/5)");
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
////                    ToastUtils.showToast("?????????" + count + "???????????????");
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
//             * ?????????????????????????????????????????????????????????
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
//             * ???????????????????????????????????????????????????????????????
//             */
//            @Override
//            public void onVideoCached() {
//                Log.d("huiger", "onVideoCached: ");
//            }
//
//            /**
//             * ????????????????????????
//             */
//            @Override
//            public void onVideoComplete() {
//                mLookAdCount++;
//                if (mLookAdCount >= 5) {
//                    btn_task_ads.setText("?????????");
//                    btn_task_ads.setEnabled(false);
//                    lookAdComplete();
//                } else {
//                    btn_task_ads.setText("?????????(" + mLookAdCount + "/5)");
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
//                Log.d("huiger", "?????????????????????");
//            }
//        }, true);
//        rewardVideoAD.loadAD();
    }

//    private void showRongheAd() {
//        AdCode adCode = new AdCode.Builder()
//                .setCodeId(XJConfig.RONGHE_VIDEO_AD_ID)
////                .setUserId(userId)  //??????ID  ???????????????????????????????????????
//                .setOrientation(AdCode.Orientation.VERTICAL)
//                .build();
//        FusionAdSDK.loadRewardVideoAd(getActivity(), adCode, new RewardVideoAdListener() {
//            /**
//             * ??????????????????
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
//             * ???????????????????????????????????????????????????????????????????????????
//             * @param rewardVideoAd ????????????????????????
//             */
//            @Override
//            public void onRewardVideoAdLoad(RewardVideoAd rewardVideoAd) {
//                rewardVideoAd.show(getActivity());
//            }
//
//            /**
//             * ???????????????????????????????????????????????????????????????????????????????????????????????????
//             */
//            @Override
//            public void onRewardVideoCached() {
//
//            }
//
//            /**
//             * ??????????????????
//             */
//            @Override
//            public void onAdShow() {
//
//            }
//
//            /**
//             * ??????????????????
//             */
//            @Override
//            public void onAdClicked() {
//
//            }
//
//            /**
//             * ??????????????????
//             */
//            @Override
//            public void onAdClosed() {
//
//            }
//
//            /**
//             * ????????????????????????
//             */
//            @Override
//            public void onVideoComplete() {
//
//            }
//
//            /**
//             * ????????????????????????
//             */
//            @Override
//            public void onVideoError() {
//
//            }
//
//            /**
//             * ?????????????????????????????????????????????
//             * var1?????????????????????????????????????????????ID  ???????????????????????????????????????????????????????????????????????????
//             */
//            @Override
//            public void onReward(String var1) {
//
//            }
//
//            /**
//             * ?????????????????????????????? ??????2.7.8???????????????????????????
//             * ??????2.7.8???????????????????????????
//             */
//            @Override
//            public void onVideoVerify() {
//
//            }
//        });
//    }

    /**
     * ??????????????????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "??????????????????!"));
                } else {
                    ToastUtils.showToast("???????????????");
                }
            }
        });
    }

    /**
     * ????????????
     */
    @OnClick({R.id.tv_order_pay, R.id.tv_order_noReceiving, R.id.tv_order_nocomment, R.id.tv_order_complete})
    public void orderClick(View v) {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        int position = 0;
        switch (v.getId()) {
            case R.id.tv_order_pay: // ?????????
                position = 0;
                break;
            case R.id.tv_order_noReceiving:// ?????????
                position = 1;
                break;
            case R.id.tv_order_nocomment:// ?????????
                position = 2;
                break;
            case R.id.tv_order_complete:// ?????????
                position = 3;
                break;
            default:
        }

        ActivityUtils.getInstance().jumpOrderActivity(0, position);
    }

    /**
     * ????????????
     */
    @OnClick({R.id.iv_vip_open, R.id.iv_vip_year})
    public void onOpenVipClick(View v) {

        if (v.getId() == R.id.iv_vip_open) {// ????????????
            openVip();
        } else {
            // vip???

        }


    }

    /**
     * ????????????
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
     * ??????????????????
     *
     * @param isOpen
     */
    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void openVipResult(boolean isOpen) {
        Log.d("huiger-msg", "TabMineFragment_2_0 -> openVipResult: " + "");
        if (isOpen) {
            ToastUtils.showToast("????????????");
            getMemberInfo();
        } else {
            ToastUtils.showToast("????????????");
        }
    }

//    /**
//     * ????????????
//     */
//    private void showScoreDialog() {
//        new H5DialogUtils(getActivity())
//                .setTitle("????????????")
//                .setUrl(ConstantsUrl.URL_PROTOCOL + "????????????").show();
//    }

//    /**
//     * ????????????
//     */
//    private void applyProxy() {
//        if (!CommonUtils.checkLogin()) {
//            ActivityUtils.getInstance().jumpLoginActivity();
//            return;
//        }
//        if (CommonUtils.getMember().getMemberGrade() < 1) {
//            ToastUtils.showToast("?????????????????????????????????????????????????????????");
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
//                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "?????????????????????????????????"));
//                    return;
//                }
//                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "?????????????????????????????????????????????????????????????????????"));
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
                    //????????????
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
                ToastUtils.showToast("?????????:\t\t" + result);
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
     * ??????????????????
     *
     * @param userHeadUrl ????????????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "????????????!"));
                    CommonUtils.showImage(civUserHead, userHeadUrl);
                    // ????????????
                    String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
                    Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
                    member.setHeadPath(userHeadUrl);
                    String resultStr = GsonUtils.getGson().toJson(member);
                    SharedPreferencesUtils.put(Constants.USER_INFO, resultStr);
                } else {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "????????????!"));
                }
            }
        });
    }


}
