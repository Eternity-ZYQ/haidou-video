package com.yimeng.haidou.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.dialog.PaySelDialog;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ActiveRankingBean;
import com.yimeng.entity.DownloadLogs;
import com.yimeng.entity.ExpTaskBean;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelShop1;
import com.yimeng.entity.TaskBean;
import com.yimeng.entity.TaskItemBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.QRCodeUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.haidou.R;
import com.yimeng.haidou.circle.ReleaseActivity;
import com.yimeng.haidou.goodsClassify.CompanySaleClassifyActivity;
import com.yimeng.haidou.mine.CommodityActivity;
import com.yimeng.haidou.mine.IDCardInfoActivity;
import com.yimeng.haidou.offline.SelPartnerTypeActivity;
import com.yimeng.haidou.task.adapter.TaskAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.huige.library.widget.LimitScrollView;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/23 0023 下午 05:42.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务
 * </pre>
 */
public class TabTaskFragment extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private OkHttpCommon mOkHttpCommon;
    private List<TaskBean> mParentList;
    private BaseQuickAdapter mAdapter;
    //    private ImageView mIvGrade;
    private TextView mTvEarnings;
    private TextView mTvRanking;
    private TextView mTvTaskName;
    private TextView mTvTaskDesc;

    /**
     * 当前任务页码编号
     */
    private int mPage = 1;
    private LimitScrollView mLimitScrollView;

    // 我的排行
    private ActiveRankingBean mineActiveRanking;
    private AlertDialog mShareShopDialog;
    private CircleImageView mCivShopLogo;
    private TextView mTvShopName;
    private TextView mTvShopAddress;
    private TextView mTvPredictMoneySum;
    private TextView mTvPredictMoneyToday;
    private TextView mTvMoneyToday;
    private TextView mTvOrderToday;
    private TextView mTvIncomeSum; // 总收益
    private TextView mTvIncome; // 预计收益
    private boolean isShareBitmap = false;
    /**
     * 分享朋友圈的那个任务
     */
    private String shareTaskNo;
    private boolean isHeadClose, taskIsClose1, taskIsClose2, taskIsClose3, taskIsClose4;

    /**
     * 任务头部关闭按钮
     */
    private View.OnClickListener closeTaskHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isHeadClose) {
                mIvTaskClose.setRotation(0);
                mTvTaskDesc.setVisibility(View.VISIBLE);
            } else {
                mIvTaskClose.setRotation(180);
                mTvTaskDesc.setVisibility(View.GONE);
            }
            isHeadClose = !isHeadClose;
        }
    };
    private ImageView mIvTaskClose;
    private Handler mHandler = new Handler();

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_task;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            smartRefresh.autoRefresh();
            if (mLimitScrollView != null) {
                mLimitScrollView.startScroll();
            }
        } else {
            if (mLimitScrollView != null) {
                mLimitScrollView.stopScroll();
            }
        }
    }

    @Override
    protected void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mParentList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mParentList.add(new TaskBean(i));
        }
        mAdapter = new BaseQuickAdapter<TaskBean, BaseViewHolder>(
                R.layout.task_item_head, mParentList
        ) {


            @Override
            protected void convert(BaseViewHolder helper, final TaskBean item) {
                if (TextUtils.isEmpty(item.getHeadName())) {
                    helper.setGone(R.id.layout_content, false);
                    return;
                }
                helper.setGone(R.id.layout_content, true);

                helper.setText(R.id.tv_title, item.getHeadName());

                if (!item.getHeadName().equals("每日任务") && item.getComplete() == 0) {
                    helper.setVisible(R.id.btn_start_task, true)
                            .addOnClickListener(R.id.btn_start_task);
                    double hasRecommend = item.getHasRecommend();
                    double lowRecommend = item.getLowRecommend();
                    double hasHasSales = item.getHasSales();
                    double lowLowSales = item.getLowSales();
                    if (hasRecommend >= lowRecommend && hasHasSales >= lowLowSales) {
                        // 任务完成
                        String taskName = item.getTaskGradeAlias().contains("推荐") ? "下一个" : item.getTaskGradeAlias();
                        helper.setGone(R.id.btn_start_task, true)
                                .setText(R.id.btn_start_task, "开启" + taskName + "任务");
                    } else {
//                        helper.setText(R.id.btn_start_task, "前往添加促销商品");
                        helper.setGone(R.id.btn_start_task, false);
                    }
                } else {
                    helper.setGone(R.id.btn_start_task, false);
                }

                RecyclerView recyclerViewTask = helper.getView(R.id.recyclerView_task);
                recyclerViewTask.setLayoutManager(new LinearLayoutManager(getContext()));
                final TaskAdapter taskAdapter = new TaskAdapter(item.getChildren());
                recyclerViewTask.setAdapter(taskAdapter);
                taskAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        TaskItemBean taskItemBean = item.getChildren().get(position);
                        setChildClick(view, taskItemBean);
                    }
                });
                taskAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        TaskItemBean taskItemBean = item.getChildren().get(position);

                        int itemType = taskItemBean.getItemType();
                        if (itemType == TaskAdapter.ITEM_EXP
                                && taskItemBean.getComplete() != 0) {
                            ActivityUtils.getInstance().jumpActivity(ExpTaskLogsActivity.class);
                        } else if (itemType == TaskAdapter.ITEM_DOWNLOAD) {
                            downloadAPK(taskItemBean);
                        }
                    }
                });

                if (helper.getAdapterPosition() == 1) {//推荐/购买任务
                    helper.getView(R.id.iv_item).setRotation(taskIsClose1 ? 90 : -90);
                    recyclerViewTask.setVisibility(taskIsClose1 ? View.GONE : View.VISIBLE);
                } else if (helper.getAdapterPosition() == 2) {//买二配三
                    helper.getView(R.id.iv_item).setRotation(taskIsClose2 ? 90 : -90);
                    recyclerViewTask.setVisibility(taskIsClose2 ? View.GONE : View.VISIBLE);
                }
//                else if (helper.getAdapterPosition() == 3) {//新手任务
//                    helper.getView(R.id.iv_item).setRotation(taskIsClose3 ? 90 : -90);
//                    recyclerViewTask.setVisibility(taskIsClose3 ? View.GONE : View.VISIBLE);
//                }
                else if (helper.getAdapterPosition() == 3) {//每日任务
                    helper.getView(R.id.iv_item).setRotation(taskIsClose4 ? 90 : -90);
                    recyclerViewTask.setVisibility(taskIsClose4 ? View.GONE : View.VISIBLE);
                }


            }
        };
        setHeadLayout();
        recyclerView.setAdapter(mAdapter);

        mOkHttpCommon = new OkHttpCommon();
    }

    /**
     * 排行榜
     */
    @OnClick(R.id.tv_user_ranking)
    public void jumpRanking() {
        // 活跃度排行榜
        Bundle bundle = new Bundle();
        bundle.putSerializable("mineRanking", mineActiveRanking);
        ActivityUtils.getInstance().jumpActivity(ActiveRanking2Activity.class, bundle);
    }

    /**
     * 活跃度规则
     */
    @OnClick(R.id.tv_active_help)
    public void jumpActiveHelp() {
        ActivityUtils.getInstance().jumpH5Activity("活跃度规则", ConstantsUrl.URL_PROTOCOL + "活跃度规则");
    }

    /**
     * 任务点击
     *
     * @param taskItemBean
     */
    private void setChildClick(View view, TaskItemBean taskItemBean) {
        switch (taskItemBean.getItemType()) {
            case TaskAdapter.ITEM_DAILY:    // 每日任务
                if (taskItemBean.getAdType().equals("share")) {
                    CommonUtils.shareApp();
//                } else if (taskItemBean.getAdType().equals("download")) {
//                    if (TextUtils.isEmpty(taskItemBean.getAdUrl()) || !taskItemBean.getAdUrl().startsWith("http")) {
//                        ToastUtils.showToast("下载地址错误!");
//                        return;
//                    }
//                    downloadAPK(taskItemBean);

                } else if (taskItemBean.getAdType().equals("moments")) {
                    // 分享到商圈
                    Member member = CommonUtils.getMember();
                    if (CommonUtils.checkLogin() && member != null) {
                        if (member.getJob().equals("open")) {
                            getShopDetail(member.getTelePhone());
                        } else {
                            ToastUtils.showToast("您还未开店, 请先开店吧!");
                        }
                    }
                } else if (taskItemBean.getAdType().equals("cps")) {
                    // 分享到微信QQ
                    shareUM(taskItemBean);
                }
                break;
            case TaskAdapter.ITEM_NO_START: // 未开始
//                ActivityUtils.getInstance().jumpShopDetailActivity(taskItemBean.getShopNo(), true);

                Bundle bundle = new Bundle();
                bundle.putInt("taskType", 1);
                bundle.putString("taskItemNo", taskItemBean.getTaskItemNo());
                ActivityUtils.getInstance().jumpActivity(CompanySaleClassifyActivity.class, bundle);
                break;
            case TaskAdapter.ITEM_EXP:      // 体验任务
                if (taskItemBean.getComplete() == 0 || taskItemBean.getComplete() == 2) { // 去完成
                    bundle = new Bundle();
                    bundle.putInt("taskType", 1);
                    ActivityUtils.getInstance().jumpActivity(CompanySaleClassifyActivity.class, bundle);
                } else {
                    ActivityUtils.getInstance().jumpActivity(ExpTaskLogsActivity.class);
                }
                break;
            case TaskAdapter.ITEM_RECOMMEND:    // 推荐任务
                CommonUtils.shareApp();
                break;
            case TaskAdapter.ITEM_WAIT: // 好友助力
                CommonUtils.shareAppUM(getActivity(), null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "");
                break;
            default:

        }
    }


    /**
     * 友盟分享
     */
    private void shareUM(TaskItemBean taskItemBean) {
        if (taskItemBean == null) return;
        if (!CommonUtils.checkLogin()) {
            ToastUtils.showToast(R.string.please_login_do);
//            ActivityUtils.getInstance().jumpActivity(LoginActivity.class);
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        shareTaskNo = taskItemBean.getTaskNo();
        if (isShareBitmap) {
            Member member = CommonUtils.getMember();
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_qrcode, null);
            ImageView logoSDV = view.findViewById(R.id.logoSDV);
            ImageView qrcodeSDV = view.findViewById(R.id.qrcodeSDV);
            TextView tvUserName = view.findViewById(R.id.tv_user_name);
            TextView tv_detail = view.findViewById(R.id.tv_detail);
            tv_detail.setVisibility(View.VISIBLE);
            tv_detail.setText("（总收益：" + UnitUtil.getMoney(mineActiveRanking.getSaleTotalAmt()) + "）");

            CommonUtils.showImage(logoSDV, member.getHeadPath());
            tvUserName.setText(member.getNickname());
            String url = ConstantsUrl.SHARE_URL_HEADER;
            String memberNo = TextUtils.isEmpty(member.getMemberNo()) ? "" : "memberNo=" + member.getMemberNo();
            url += memberNo + "&memberType=normal";
            // 生成二维码
            qrcodeSDV.setImageBitmap(QRCodeUtil.createQRCodeBitmap(url, DeviceUtils.dp2px(getActivity(), 150), DeviceUtils.dp2px(getActivity(), 150)));
            // 生成图片
            final Bitmap shareBitmap = QRCodeUtil.getViewBitmap(getActivity(), view);
            // 分享
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.shareAppUM(getActivity(), shareBitmap, "", "", shareTaskNo);
                }
            }, 500);
        } else {
            CommonUtils.shareAppUM(getActivity(), null, taskItemBean.getTitle(), taskItemBean.getRemark(), shareTaskNo);
        }

    }


    /**
     * 微信分享
     */
    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    public void onShareWechatResult(boolean result) {
        if (TextUtils.isEmpty(shareTaskNo)) {
            return;
        }
        EventBus.getDefault().clear();
        NetComment.shareAddActivite(getActivity(), shareTaskNo);
        shareTaskNo = "";
    }

    /**
     * 下载
     */
    private void downloadAPK(TaskItemBean taskItemBean) {
        if (taskItemBean == null) return;
        if (!CommonUtils.checkLogin()) {
            ToastUtils.showToast(R.string.please_login_str);
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        Member member = CommonUtils.getMember();
        String taskNo = taskItemBean.getTaskNo();
        List<DownloadLogs> downloadLogsList = LitePal.where("taskNo = ?", taskNo).find(DownloadLogs.class);

        if (downloadLogsList != null && !downloadLogsList.isEmpty()) {
            for (DownloadLogs logs : downloadLogsList) {
                if (member.getMemberNo().equals(logs.getMemberNo())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(logs.getDownloadTime()));
                    Calendar calendarNow = Calendar.getInstance();
                    calendarNow.setTime(new Date(System.currentTimeMillis()));

                    if (calendar.get(Calendar.DAY_OF_MONTH) == calendarNow.get(Calendar.DAY_OF_MONTH)) {
                        // 今日已下载
                        ToastUtils.showToast("任务已完成，活跃度已增加，请明日再来");
                        return;
                    }
                }
            }
        }
        new DownloadLogs(member.getMemberNo(), taskNo, System.currentTimeMillis()).save();
        clickADS2Internet(taskNo);

        ActivityUtils.getInstance().jumpInternetExplorer(taskItemBean.getAdUrl());

    }

    /**
     * 点击广告上传点击事件
     */
    private void clickADS2Internet(String taskNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("taskNo", taskNo);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_CLICK_DOWNLOAD_ADS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

            }
        });

    }

    /**
     * 分享店铺收益到商圈弹窗
     *
     * @param modelShop
     */
    private void shareShop(ModelShop1 modelShop) {

        if (mShareShopDialog == null) {
            View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_shop, null);
            final View shareContent = dialogLayout.findViewById(R.id.frame_content);

            dialogLayout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mShareShopDialog != null)
                        mShareShopDialog.dismiss();
                }
            });

            dialogLayout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mShareShopDialog != null)
                        mShareShopDialog.dismiss();
                    Bitmap shareBitmap = QRCodeUtil.getViewBitmap(shareContent);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isShareTask", true);
                    EventBus.getDefault().postSticky(shareBitmap, EventTags.SHARE_BITMAP);
                    ActivityUtils.getInstance().jumpActivity(ReleaseActivity.class, bundle);
                }
            });

            mCivShopLogo = dialogLayout.findViewById(R.id.civ_shop_logo);
            mTvShopName = dialogLayout.findViewById(R.id.tv_shopName);
            mTvShopAddress = dialogLayout.findViewById(R.id.tv_shop_address);
            mTvPredictMoneySum = dialogLayout.findViewById(R.id.tv_predict_money_sum);
            mTvPredictMoneyToday = dialogLayout.findViewById(R.id.tv_predict_money_today);
            mTvMoneyToday = dialogLayout.findViewById(R.id.tv_money_today);
            mTvOrderToday = dialogLayout.findViewById(R.id.tv_order_today);

            mShareShopDialog = new AlertDialog.Builder(getActivity())
                    .setView(dialogLayout)
                    .create();
        }


        CommonUtils.showImage(mCivShopLogo, modelShop.getLogoPath());
        mTvShopName.setText(modelShop.getShopName());
        mTvShopAddress.setText(modelShop.getAddress());

        // 累计收益
        mTvPredictMoneySum.setText(UnitUtil.getMoney(modelShop.getTotalIncome()));
        // 总订单
        mTvPredictMoneyToday.setText("总订单\t" + modelShop.getTotalOrderCount());
        // 今日收入
        mTvMoneyToday.setText(UnitUtil.getMoney(modelShop.getTodayIncome()));
        // 今日订单
        mTvOrderToday.setText("今日订单\t" + modelShop.getTodayOrderCount());

        if (!mShareShopDialog.isShowing())
            mShareShopDialog.show();

    }

    /**
     * 获取店铺信息
     */
    private void getShopDetail(String shopNo) {
        SimpleDialog.showLoadingHintDialog(getActivity(), 1);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("shopNo", shopNo);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_SHOP_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                SimpleDialog.showLoadingHintDialog(getActivity(), 1);
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                SimpleDialog.cancelLoadingHintDialog();
                if (jsonObject.get("status").getAsInt() == 1) {
                    String data = jsonObject.get("data").getAsJsonObject().toString();
                    ModelShop1 modelShop = GsonUtils.getGson().fromJson(data, ModelShop1.class);

                    shareShop(modelShop);
                } else {
                    ToastUtils.showToast("店铺信息获取失败");
                }
            }
        });

    }

    /**
     * 设置头部数据
     */
    private void setHeadLayout() {
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.head_task_layout, null);
        mAdapter.setHeaderView(headView);
//        mIvGrade = headView.findViewById(R.id.iv_grade);
        mTvEarnings = headView.findViewById(R.id.tv_earnings);
        mTvTaskName = headView.findViewById(R.id.tv_task_name);
        mIvTaskClose = headView.findViewById(R.id.iv_task_close);
        headView.findViewById(R.id.layout_content).setOnClickListener(closeTaskHeadListener);

        mTvTaskDesc = headView.findViewById(R.id.tv_task_desc);
        // 总收益
        mTvIncomeSum = headView.findViewById(R.id.tv_income_sum);
        // 预计收益
        mTvIncome = headView.findViewById(R.id.tv_income);

        // 当前排行
        mTvRanking = headView.findViewById(R.id.tv_ranking);
        mTvRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mineActiveRanking != null) {
                    if (UnitUtil.getInt(mineActiveRanking.getRowno()) == 0) {// 活跃度为0
                        // 未开店
                        // 是否实名
                        if (!CommonUtils.checkAuth()) {
                            ToastUtils.showToast("您还未实名, 请先实名认证吧");
                            ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
                            return;
                        }
                        // 是否开店
                        if (CommonUtils.checkOpenShop()) {
                            jumpRanking();// 已开店，前往排行榜
                            return;
                        }

                        if ((Boolean) SharedPreferencesUtils.get(Constants.MINE_SHOP_APPLY_LOADING, false)) {
                            ToastUtils.showToast("店铺审核中");
                            return;
                        }
                        ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
                    } else {
                        // 活跃度不为0，前往排行榜
                        jumpRanking();
                    }
                }
            }
        });
        // 刷新排行
        mTvEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNowRanking();
            }
        });

        // 滚动
        mLimitScrollView = headView.findViewById(R.id.limitAds);
    }

    @Override
    protected void initListener() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
                showTipDialog();
                changeVipStatus();
            }

        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("msg", "TabTaskFragment -> onItemClick: " + position);
                TaskBean taskBean = mParentList.get(position);

                if (position == 0) {//推荐/购买任务
                    taskIsClose1 = !taskIsClose1;
                    if (taskBean.getComplete() == 0) {
                        taskBean.setExpanded(true);
                    } else {
                        getCurrentTaskDetail(taskBean.getTaskNo());
                    }
                } else if (position == 1) {//买二配三
                    taskIsClose2 = !taskIsClose2;
                    if (taskBean.getComplete() == 0) {
                        taskBean.setExpanded(true);
                    } else {
                        getCurrentTaskDetailNew(taskBean.getTaskNo());
                    }
                }
//                else if (position == 2) {//新手任务
//                    taskIsClose3 = !taskIsClose3;
//                }
                else if (position == 2) {//每日任务
                    taskIsClose4 = !taskIsClose4;
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.btn_start_task) {
                    TaskBean taskBean = mParentList.get(position);
                    double hasRecommend = taskBean.getHasRecommend();
                    double lowRecommend = taskBean.getLowRecommend();
                    double hasHasSales = taskBean.getHasSales();
                    double lowLowSales = taskBean.getLowSales();
                    // 推荐任务完成和销售任务完成
                    if (hasRecommend >= lowRecommend && hasHasSales >= lowLowSales) {
                        // 开启任务
                        if (taskBean.getTaskGradeAlias().contains("推荐")) {
                            ToastUtils.showToast("想赚更多，请前往团购任务");
                            return;
                        }
                        view.setEnabled(false);
                        if (taskBean.isTaskNew()) {
                            upgradeNew(view);
                        } else {
                            upgrade(view);
                        }
                    } else {
                        // 是否实名
                        if (!CommonUtils.checkAuth()) {
                            ToastUtils.showToast("您还未实名, 请先实名认证吧");
                            ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
                            return;
                        }
                        // 是否开店
                        if (!CommonUtils.getMember().getJob().equals("open")) {
                            if ((Boolean) SharedPreferencesUtils.get(Constants.MINE_SHOP_APPLY_LOADING, false)) {
                                ToastUtils.showToast("店铺审核中");
                                return;
                            }

                            ToastUtils.showToast("您还未开店, 请先开店吧!");
                            ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
                            return;
                        }
                        // 前往添加促销商品
//                        ActivityUtils.getInstance().jumpActivity(MineShopProductManagerActivity.class);
                        Intent commodityIntent = new Intent(getActivity(), CommodityActivity.class);
                        commodityIntent.putExtra("productType", "cuxiao");
                        startActivity(commodityIntent);
                    }
                }
            }
        });
    }

    /**
     * 会员状态
     */
    private void changeVipStatus() {
        Member member = CommonUtils.getMember();
        if (member != null) {
            if (member.isVIP()) {


            } else {


            }
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();

        smartRefresh.autoRefresh();
        if (mLimitScrollView != null) {
            mLimitScrollView.startScroll();
        }
//        showTipDialog();
    }

    /**
     * 显示活跃度弹窗规则
     */
    private void showTipDialog() {

        // 不再显示
        if ((boolean) SharedPreferencesUtils.get(Constants.TASK_DIALOG_STATUS, false)) return;

        View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_task_tip_layout, null);
        final CheckBox cb = dialogLayout.findViewById(R.id.checkbox);
        new android.app.AlertDialog.Builder(getActivity())
                .setView(dialogLayout)
                .setPositiveButton("如何增加活跃度", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cb.isChecked()) {
                            SharedPreferencesUtils.put(Constants.TASK_DIALOG_STATUS, true);
                        }
                        jumpActiveHelp();
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (cb.isChecked()) {
                            SharedPreferencesUtils.put(Constants.TASK_DIALOG_STATUS, true);
                        }
                    }
                })
                .show();

    }

    private void getData() {
        if (!CommonUtils.checkLogin()) return;
        getTask();
        getTaskNew();
        getNowRanking();
        getMatchOrderList();
    }

    private void getTask() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_MEMBER_TASK, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取任务数据失败!"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
//                if (mPage == 1 && mParentList != null) {
//                    mParentList.clear();
//                }
                setDailyTask(data.get("daily").getAsJsonObject());
                setCurrentTask(data.get("current").getAsJsonObject());
                mAdapter.notifyDataSetChanged();


                // 获取体验任务
//                getExpTask();
            }
        });
    }

    private void getTaskNew() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_MEMBER_TASK_NEW, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取任务数据失败!"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();

                TaskBean bean = GsonUtils.getGson().fromJson(data.get("current").getAsJsonObject().toString(), TaskBean.class);
                // 进行中
                if (bean.getComplete() == 0) {
                    TaskItemBean taskItemBean = null;
                    List<TaskItemBean> children = bean.getChildren();
                    if (bean.getLowRecommend() > 0) {
                        taskItemBean = new TaskItemBean();
                        taskItemBean.setItemType(TaskAdapter.ITEM_RECOMMEND);
                        taskItemBean.setTitle("推荐任务");
                        taskItemBean.setLowRecommend(bean.getLowRecommend());
                        taskItemBean.setHasRecommend(bean.getHasRecommend());
                        taskItemBean.setTaskGradeAlias(bean.getTaskGradeAlias());
                        children.add(taskItemBean);
                    }

                    if (bean.getLowSales() > 0) {
                        // 销售任务
                        taskItemBean = new TaskItemBean();
                        taskItemBean.setItemType(TaskAdapter.ITEM_WAIT);
                        taskItemBean.setTitle("销售订单任务");
                        taskItemBean.setLowRecommend(bean.getLowSales());
                        taskItemBean.setHasRecommend(bean.getHasSales());
                        taskItemBean.setTaskGradeAlias(bean.getTaskGradeAlias());
                        children.add(taskItemBean);
                    }
                } else {
                    if (bean.getComplete() == 1) {
                        getCurrentTaskDetailNew(bean.getTaskNo());
                    }
                }
                bean.setHeadName(bean.getTaskGradeAlias() + "任务");
                bean.setTaskNew(true);
                mParentList.set(1, bean);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLimitScrollView != null) {
            mLimitScrollView.stopScroll();
        }
    }

    /**
     * 系统匹配订单数
     */
    private void getMatchOrderList() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_SYSTEM_MATCH_ORDER_LIST, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() == 1) {

                    final JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        list.add(jsonArray.get(i).getAsString());
                    }
                    setLimitData(list);
                }
            }
        });
    }

    private void setLimitData(final List<String> list) {
        if (list.isEmpty()) return;
        mLimitScrollView.setAdapter(new LimitScrollView.LimitScrollViewAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public View getView(int position) {
                TextView tv = new TextView(getActivity());
                tv.setPadding(0, DeviceUtils.dp2px(getActivity(), 8), 0, DeviceUtils.dp2px(getActivity(), 8));
                tv.setText(list.get(position));
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getActivity().getResources().getColor(R.color.c_333333));
                tv.setTextSize(12);
                return tv;
            }
        });
        mLimitScrollView.startScroll();
    }


    /**
     * 获取当前排行
     */
    private void getNowRanking() {

        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_NOW_ACTIVE_RANKING, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取当前活跃度记录失败"));
                    return;
                }

                String data = jsonObject.get("data").getAsJsonObject().toString();
                mineActiveRanking = GsonUtils.getGson().fromJson(data, ActiveRankingBean.class);

                // 当前排行
                int ranking = UnitUtil.getInt(mineActiveRanking.getRowno());
                if (ranking == 0) {
                    if (CommonUtils.checkOpenShop()) {
//                        mTvRanking.setText("未上榜");
                        mTvRanking.setText("查看排名");
                    } else {
                        mTvRanking.setText("开店提升排名");
                    }
                } else {
//                    mTvRanking.setText("第" + ranking + "名");
                    mTvRanking.setText("查看排名");
                }

                // 接单数
//                mTvEarnings.setText("已接任务" + UnitUtil.getInt(mineActiveRanking.getTaskOrderNum()) + "单");
                mTvEarnings.setText("平台已匹配" + (UnitUtil.getInt(mineActiveRanking.getOrderMatchingNum()) + 10000) + "单");

                // 总收益
                mTvIncomeSum.setText("总收益\n" + UnitUtil.getMoney(mineActiveRanking.getSaleTotalAmt()));
                if (UnitUtil.getInt(mineActiveRanking.getSaleTotalAmt()) / 100 > 2000) {
                    isShareBitmap = true;
                }
                // 预计收益
                mTvIncome.setText("预计收益\n" + UnitUtil.getMoney(mineActiveRanking.getShopsProfit()));

            }
        });
    }

    /**
     * 获取体验任务
     */
    private void getExpTask() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_EXP_TASK_PROGRESS, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1 && !jsonObject.get("data").isJsonNull()) {

                    ExpTaskBean expTaskBean = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().toString(), ExpTaskBean.class);

                    TaskBean bean = mParentList.get(2);
                    bean.setComplete(-1);
                    List<TaskItemBean> children = bean.getChildren();

                    TaskItemBean taskItemBean = null;
                    if (children.isEmpty()) {
                        taskItemBean = new TaskItemBean();
                    } else {
                        taskItemBean = children.get(0);
                    }

                    taskItemBean.setTitle(expTaskBean.getTitle());
                    taskItemBean.setComplete(expTaskBean.getComplete());
                    taskItemBean.setItemType(TaskAdapter.ITEM_EXP);
                    if (children.isEmpty()) {
                        children.add(taskItemBean);
                    }
                    bean.setHeadName("新手任务");
                    bean.setChildren(children);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 开启任务
     */
    private void upgrade(final View view) {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_UPGRADE, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
                view.setEnabled(true);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                view.setEnabled(true);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "开启任务失败!"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "开启任务成功!"));
                smartRefresh.autoRefresh();
            }
        });
    }

    /**
     * 获取当前任务详情
     */
    private void getCurrentTaskDetail(String currentTaskNo) {
        if (TextUtils.isEmpty(currentTaskNo)) return;

        HashMap<String, String> params = CommonUtils.createParams();
        params.put("taskNo", currentTaskNo);
        CommonUtils.addPageParams(params, mPage);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_DETAIL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取任务数据失败!"));
                }

                List<TaskItemBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<TaskItemBean>>() {
                        }.getType()
                );
                setCurrentTaskDetail(list);
            }
        });
    }

    /**
     * 设置当前任务详情
     */
    private void setCurrentTaskDetail(List<TaskItemBean> list) {
        TaskBean taskBean = mParentList.get(0);
        List<TaskItemBean> childrenList = taskBean.getChildren();
        if (mPage == 1) {
            childrenList.clear();
        }

        for (TaskItemBean taskItemBean : list) {
            // 处理每个任务
            if (taskItemBean.getStatus().equals("complete")) {
                // 已完成
                taskItemBean.setItemType(TaskAdapter.ITEM_COMPLETE);
            } else {
                taskItemBean.setItemType(TaskAdapter.ITEM_NO_START);
            }
        }
        childrenList.addAll(list);
        taskBean.setExpanded(true);
        if (list.size() == Constants.MAX_LIMIT) {
            mPage++;
            smartRefresh.setEnableLoadMore(true);
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 开启富豪任务
     */
    private void upgradeNew(final View view) {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_UPGRADE_NEW, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
                view.setEnabled(true);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                view.setEnabled(true);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "开启任务失败!"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "开启任务成功!"));
                smartRefresh.autoRefresh();
            }
        });
    }

    /**
     * 获取当前富豪任务详情
     */
    private void getCurrentTaskDetailNew(String currentTaskNo) {
        if (TextUtils.isEmpty(currentTaskNo)) return;

        HashMap<String, String> params = CommonUtils.createParams();
        params.put("taskNo", currentTaskNo);
        CommonUtils.addPageParams(params, mPage);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_TASK_DETAIL_NEW, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取任务数据失败!"));
                }

                List<TaskItemBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<TaskItemBean>>() {
                        }.getType()
                );
                setCurrentTaskDetailNew(list);
            }
        });
    }

    /**
     * 设置当前富豪任务详情
     */
    private void setCurrentTaskDetailNew(List<TaskItemBean> list) {
        TaskBean taskBean = mParentList.get(1);
        List<TaskItemBean> childrenList = taskBean.getChildren();
        if (mPage == 1) {
            childrenList.clear();
        }

        for (TaskItemBean taskItemBean : list) {
            // 处理每个任务
            if (taskItemBean.getStatus().equals("complete")) {
                // 已完成
                taskItemBean.setItemType(TaskAdapter.ITEM_COMPLETE);
            } else {
                taskItemBean.setItemType(TaskAdapter.ITEM_NO_START);
            }
        }
        childrenList.addAll(list);
        taskBean.setExpanded(true);
        if (list.size() == Constants.MAX_LIMIT) {
            mPage++;
            smartRefresh.setEnableLoadMore(true);
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 设置每日任务
     */
    private void setDailyTask(JsonObject dailyTask) {
        TaskBean bean = GsonUtils.getGson().fromJson(dailyTask.toString(), TaskBean.class);

        List<TaskItemBean> children = bean.getChildren();
        for (TaskItemBean taskItemBean : children) {
            if (taskItemBean.getAdType().equals("download")) {
                taskItemBean.setItemType(TaskAdapter.ITEM_DOWNLOAD);
            } else {
                taskItemBean.setItemType(TaskAdapter.ITEM_DAILY);
            }
        }
        bean.setHeadName("每日任务");
        bean.setChildren(children);
//        mParentList.add(bean);
        mParentList.set(2, bean);
    }

    /**
     * 设置当前任务标题
     */
    private void setCurrentTask(JsonObject currentTask) {
        TaskBean bean = GsonUtils.getGson().fromJson(currentTask.toString(), TaskBean.class);
//        // 等级
//        switch (bean.getTaskGrade()) {
//            case 0:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji1);
//                break;
//            case 1:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji2);
//                break;
//            case 2:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji3);
//                break;
//            case 3:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji4);
//                break;
//            case 4:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji5);
//                break;
//            case 5:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji6);
//                break;
//            case 6:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji7);
//                break;
//            case 7:
//                mIvGrade.setImageResource(R.mipmap.ico1_dengji8);
//                break;
//            default:
//        }

        // 当前收益
//        mTvEarnings.setText("当前收益:\t\t" + UnitUtil.getMoney(bean.getCurrentIncome()));
        // 任务
        mTvTaskName.setText(bean.getTitle());
        mTvTaskDesc.setText(bean.getDescription());

        // 进行中
        if (bean.getComplete() == 0) {
            TaskItemBean taskItemBean = null;
            List<TaskItemBean> children = bean.getChildren();
            if (bean.getLowRecommend() > 0) {
                taskItemBean = new TaskItemBean();
                taskItemBean.setItemType(TaskAdapter.ITEM_RECOMMEND);
                taskItemBean.setTitle("推荐任务");
                taskItemBean.setLowRecommend(bean.getLowRecommend());
                taskItemBean.setHasRecommend(bean.getHasRecommend());
                taskItemBean.setTaskGradeAlias(bean.getTaskGradeAlias());
                children.add(taskItemBean);
            }

            if (bean.getLowSales() > 0) {
                // 销售任务
                taskItemBean = new TaskItemBean();
                taskItemBean.setItemType(TaskAdapter.ITEM_WAIT);
                taskItemBean.setTitle("销售订单任务");
                taskItemBean.setLowRecommend(bean.getLowSales());
                taskItemBean.setHasRecommend(bean.getHasSales());
                taskItemBean.setTaskGradeAlias(bean.getTaskGradeAlias());
                children.add(taskItemBean);
            }


        } else {
            if (bean.getComplete() == 1) {
                getCurrentTaskDetail(bean.getTaskNo());
            }
        }

        bean.setHeadName(bean.getTaskGradeAlias() + "任务");
//        mParentList.add(bean);
        mParentList.set(0, bean);
    }

    /**
     * 购买活跃度
     */
    private void getShortcut() {
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_PAY_ACTIVE, CommonUtils.createParams(), new CallbackCommon() {
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

                final String rewardNo = jsonObject.get("data").getAsJsonObject().get("rewardNo").getAsString();

                new XPopup.Builder(getContext())
                        .asCustom(new PaySelDialog(getActivity(), EventTags.WECHAT_PAY_RESULT, rewardNo))
                        .show();
            }
        });
    }
}
