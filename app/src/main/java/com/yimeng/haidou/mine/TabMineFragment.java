package com.yimeng.haidou.mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseTakePhotoFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.HomeDataBean;
import com.yimeng.entity.Member;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.widget.MineItemLayout;
import com.yimeng.widget.ObservableScrollView;
import com.yimeng.haidou.R;
import com.yimeng.haidou.circle.MineReleaseActivity;
import com.yimeng.haidou.offline.SelPartnerTypeActivity;
import com.yimeng.haidou.order.OrderActivity;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.yanzhenjie.permission.Action;

import org.devio.takephoto.model.TResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/11 0011 下午 05:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人中心
 * </pre>
 */
public class TabMineFragment extends BaseTakePhotoFragment {

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
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.item_shop)
    MineItemLayout itemShop;
    @Bind(R.id.iv_become_proxy)
    ImageView ivBecomeProxy;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    @Bind(R.id.layout_toolbar)
    RelativeLayout layoutToolbar;
    @Bind(R.id.layout_head)
    ConstraintLayout layoutHead;
    @Bind(R.id.item_proxy)
    MineItemLayout itemProxy;
    @Bind(R.id.ll_apply_shop)
    LinearLayout llApplyShop;
    @Bind(R.id.bannerLayout)
    BGABanner mBannerLayout;

    /**
     * 积分规则弹窗
     */
    private AlertDialog scoreDialog;
    private OkHttpCommon mOkHttpCommon;

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
        // 广告
        getAdsData();
        if (!CommonUtils.checkLogin()) {
            return;
        }
        getMemberInfo();

    }

    /**
     * 获取广告数据
     */
    private void getAdsData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("menuType", "centerBanner");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_BANNER_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
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

    private void getMemberInfo() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_MEMBER_INFO, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
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
            }
        });
    }

    private void setUserInfo() {
        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfo)) {
            Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);
            CommonUtils.showImage(civUserHead, member.getHeadPath(), R.mipmap.my_touxiang);
            tvUserMobile.setText(member.getMobileNo());
            tvUserName.setVisibility(View.VISIBLE);
            tvUserName.setText(member.getNickname());
            tvUserGrade.setText(member.getMemberGradeAlias());
//            if (member.getMemberType().equals("cityProvider") ||
//                    (member.getMemberType().equals("proxy") && member.getJob().equals("1"))) {
//                itemAgency.setVisibility(View.VISIBLE);
//            } else {
//                itemAgency.setVisibility(View.GONE);
//            }

            if (member.getIsOldUsr() == 0) {
                showActivateDialog();
            }

            // 店铺状态
            String shopStatus = member.getJob();
            if (shopStatus.equals("open")) {
                itemShop.setVisibility(View.VISIBLE);
                llApplyShop.setVisibility(View.GONE);

                // 是否为代理
                if (member.isAgent()) {
                    ivBecomeProxy.setVisibility(View.GONE);
                    itemProxy.setVisibility(View.VISIBLE);
                } else {
                    ivBecomeProxy.setVisibility(View.VISIBLE);
                    itemProxy.setVisibility(View.GONE);
                }
            } else if (shopStatus.equals("apply")) {
                // 审核中
            } else {
                itemShop.setVisibility(View.GONE);
                llApplyShop.setVisibility(View.VISIBLE);
            }


        } else {
            civUserHead.setImageResource(R.mipmap.logo);
            tvUserMobile.setText("立即登录");
            tvUserName.setVisibility(View.GONE);
        }
    }

    /**
     * 激活用户按钮
     */
    private void showActivateDialog() {
        SimpleDialog.showConfirmDialog(getActivity(), "提示",
                "您的账号还未激活，前往激活？", "取消", "确定",
                null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                    }
                }, false);
    }

    @OnClick({R.id.iv_qrCode, R.id.civ_user_head, R.id.tv_user_mobile, R.id.tv_user_name, R.id.iv_right,
            R.id.item_wallet, R.id.item_friend, R.id.item_msg, R.id.tv_score,
            R.id.item_help, R.id.item_custom_services, R.id.item_feedback, R.id.item_partner,
            R.id.item_address, R.id.item_order, R.id.item_collect, R.id.item_proxy, R.id.view_click,
            R.id.item_setting, R.id.item_shop, R.id.iv_scan, R.id.item_coupon, R.id.iv_become_proxy,
            R.id.item_release, R.id.iv_create_shop, R.id.iv_join_proxy
    })
    public void onViewClicked(View view) {
        if (!CommonUtils.checkLogin()
                && view.getId() != R.id.item_help
                && view.getId() != R.id.item_custom_services
                && view.getId() != R.id.item_setting) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        switch (view.getId()) {
            case R.id.tv_score: //  积分
                showScoreDialog();
                break;
            case R.id.iv_scan:              // 扫码
                CommonUtils.getPermission(getActivity(), new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        startActivityForResult(new Intent(getActivity(), ScanForResultActivity.class), FLAG_SCAN_QRCODE);
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.iv_qrCode:            // 我的二维码
                ActivityUtils.getInstance().jumpMyQrCode(1);
                break;
            case R.id.civ_user_head:        // 修改头像
                showSelPopupWind(view, 1);
                break;
            case R.id.tv_user_mobile:
            case R.id.tv_user_name:
            case R.id.view_click:
            case R.id.iv_right:             // 修改资料
                ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                break;
            case R.id.item_wallet:          // 我的钱包
//                ActivityUtils.getInstance().jumpActivity(MyWalletActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("walletType", "mine");
                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);
                break;
            case R.id.item_order:           // 我的订单
                ActivityUtils.getInstance().jumpActivity(OrderActivity.class);
                break;
            case R.id.item_address:         // 我的地址
                ActivityUtils.getInstance().jumpAddressManager(false);
                break;
            case R.id.item_friend:          // 我的伙伴
                ActivityUtils.getInstance().jumpActivity(MyInviteActivity.class);
                break;
            case R.id.item_proxy:          // 我的代理
//                ActivityUtils.getInstance().jumpActivity(MyAgencyActivity.class);
                ActivityUtils.getInstance().jumpActivity(MyProxyActivity.class);
                break;
            case R.id.item_collect:         // 我的收藏
                ActivityUtils.getInstance().jumpActivity(MyFavoriteActivity.class);
                break;
            case R.id.item_msg:             // 消息中心
                ActivityUtils.getInstance().jumpActivity(MsgActivity.class);
                break;
            case R.id.item_help:            // 使用帮助
                ActivityUtils.getInstance().jumpH5Activity("玩转促销王", ConstantsUrl.APP_USE_HELP_URL);
                break;
            case R.id.item_custom_services: // 联系我们
                ActivityUtils.getInstance().jumpActivity(CustomerServiceActivity.class);
                break;
            case R.id.item_feedback:        // 意见反馈
                ActivityUtils.getInstance().jumpActivity(FeedbackActivity.class);
                break;
            case R.id.item_setting:          // 更多设置
                ActivityUtils.getInstance().jumpActivity(MoreSettingActivity.class);
                break;
            case R.id.item_partner:         // 合伙人
                ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
                break;
            case R.id.item_shop:            // 我的店铺
                ActivityUtils.getInstance().jumpActivity(MineShopDetailActivity.class);
                break;
            case R.id.iv_become_proxy:      // 成为代理
//                applyProxy();
                ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_APPLY_PROXY + CommonUtils.getMember().getMemberNo());
                break;
            case R.id.item_coupon:          // 我的代金券
                ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class);
                break;
            case R.id.item_release:         // 我的商圈
                ActivityUtils.getInstance().jumpActivity(MineReleaseActivity.class);
                break;
            case R.id.iv_create_shop:       // 申请开店
                shopJoin();
                break;
            case R.id.iv_join_proxy:
                ToastUtils.showToast("请先开店，并添加促销商品再参与代理活动");
                break;
        }
    }

    /**
     * 积分弹窗
     */
    private void showScoreDialog() {
        if (scoreDialog == null) {
            View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_score_layout, null);

            scoreDialog = new AlertDialog.Builder(getContext())
                    .setView(dialogLayout)
                    .create();

            dialogLayout.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scoreDialog.dismiss();
                }
            });

            TextView tvContent = dialogLayout.findViewById(R.id.tv_dialog_content);
            tvContent.setText(
                    SpannableStringUtils.getBuilder("1.赚取积分\n\n").setBold()
                            .append("买家购买商品，积分增加金额的等价数值。\n\n买家每完成一条任务，平台会赠送一定积分奖励。\n\n\n")
                            .append("2.使用积分\n\n").setBold()
                            .append("随着平台业务的发展，后期积分会用于抽奖，积分商城商品兑换，敬请期待...")
                            .create()
            );
        }

        scoreDialog.show();

    }

    /**
     * 申请开店
     */
    private void shopJoin() {
        Member member = CommonUtils.getMember();
        if (member == null) {
            return;
        }
        if (!CommonUtils.checkAuth()) { // 先实名认证
            ActivityUtils.getInstance().jumpActivityForResult(getActivity(), IDCardInfoActivity.class, FLAG_AUTH_CODE, null);
            return;
        }
        ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
    }

    /**
     * 代理申请
     */
    private void applyProxy() {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        if (CommonUtils.getMember().getMemberGrade() < 1) {
            ToastUtils.showToast("请先开店，并添加促销商品再参与代理活动");
            return;
        }

        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_APPLY_PROXY, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("申请失败，请稍后再试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "申请失败，请稍后再试！"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "代理申请提交成功，客服即将联系您，请耐心等候！"));
            }
        });
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    protected void init() {
        mOkHttpCommon = new OkHttpCommon();
    }

    @Override
    protected void initListener() {
        scrollView.setOnScrollChangeListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= layoutHead.getMeasuredHeight() - layoutToolbar.getMeasuredHeight()) {
                    layoutToolbar.setBackgroundColor(Color.parseColor("#d8a961"));
                } else {
                    layoutToolbar.setBackgroundColor(Color.TRANSPARENT);
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
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_SCAN_QRCODE && resultCode == Activity.RESULT_OK && data != null) {
            String result = data.getStringExtra("result");
            if (result.contains("http")) {
                Map<String, String> paramsMap = CommonUtils.urlToMap(result);
                if (paramsMap.containsKey("shopNo")) {
                    ActivityUtils.getInstance().jumpShopDetailActivity(paramsMap.get("shopNo"), false);
                } else {
                    ActivityUtils.getInstance().jumpInternetExplorer(result);
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
                ToastUtils.showToast("修改失败!");
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
