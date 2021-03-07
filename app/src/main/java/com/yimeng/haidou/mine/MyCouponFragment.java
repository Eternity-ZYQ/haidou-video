package com.yimeng.haidou.mine;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.MyCouponAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.CouponBean;
import com.yimeng.entity.Member;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/25 0025 下午 04:19.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的代金券(未使用/已使用/已过期)
 *              0. 未使用
 *              1. 已使用
 *              2. 已过期
 * </pre>
 */
public class MyCouponFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    /**
     * 0. 未使用
     * 1. 已使用
     * 2. 已过期
     */
    private int mType = 0;
    private int mPage = 1;
    private OkHttpCommon mOkHttpCommon;
    private MyCouponAdapter mAdapter;
    private List<CouponBean> mList;
    /**
     * 购买选择代金券
     */
    private boolean mIsSelect;
    /**
     * 是否来源于购买任务
     */
    private boolean mIsTask;
    // 3.0 任务区块编号
    private String mBlockNo;
    /**
     * 转余额汇率
     */
    private int rate;

    public static MyCouponFragment getInstance(boolean isSelect, boolean isTask, String blockNo, int type) {
        MyCouponFragment fragment = new MyCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mType", type);
        bundle.putBoolean("isSelect", isSelect);
        bundle.putBoolean("isTask", isTask);
        bundle.putString("blockNo", blockNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_coupon_my;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt("mType", 0);
            mIsSelect = bundle.getBoolean("isSelect", false);
            mIsTask = bundle.getBoolean("isTask", false);
            mBlockNo = bundle.getString("blockNo", "");
        }

        mOkHttpCommon = new OkHttpCommon();

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyCouponAdapter(mList, mType, mIsSelect, mIsTask);
        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mType == 0 && mIsSelect) {
                    // 未使用 && 购买时选择代金券
                    getActivity().finish();
                    EventBus.getDefault().post(mList.get(position), "selectedCoupon");
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final CouponBean couponBean = mList.get(position);
                if (view.getId() == R.id.btn_send) {
                    // 赠送
                    showInputDialog(couponBean.getCouponNo());
                } else if (view.getId() == R.id.btn_use) {
                    // 去使用
                    if (mIsSelect) {
                        // 购买时选择代金券
                        EventBus.getDefault().post(couponBean, "selectedCoupon");
                        getActivity().finish();
                    } else {
                        // 代金券转余额
                        long money = couponBean.getAmt() * rate / 10 / 100;

                        SimpleDialog.showConfirmDialog(getActivity(), "折现到用户余额", "可折现金额" + money + "元", null, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                coupon2Money(couponBean.getCouponNo());
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 代金券转余额
     */
    private void coupon2Money(String couponNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("couponNo", couponNo);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_COUPON_2_MONEY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "失败"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "成功"));
                smartRefresh.autoRefresh();
            }
        });

    }

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("used", mType + "");
        if(mType==0 && !TextUtils.isEmpty(mBlockNo)) {
            params.put("blockNo",mBlockNo);
        }
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_MY_COUPON_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast("查询代金券失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询代金券失败"));
                    return;
                }
                JsonObject dataJsonObj = jsonObject.get("data").getAsJsonObject();

                // 转余额汇率
                rate = dataJsonObj.get("otherData").getAsInt();

                LogUtils.json(jsonObject.toString());
                List<CouponBean> list = GsonUtils.getGson().fromJson(
                        dataJsonObj.get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<CouponBean>>() {
                        }.getType()
                );
                if (list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.setEnableLoadMoreWhenContentNotFull(false);
                }
                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mList.addAll(list);
                mPage++;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 显示输入对话框
     */
    private void showInputDialog(final String couponNo) {
        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogLayout).create();
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (DeviceUtils.getWindowWidth(getActivity()) * 0.8);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        TextView tvTitle = dialogLayout.findViewById(R.id.tv_dialog_title);
        tvTitle.setText("转赠");
        final EditText etInput = dialogLayout.findViewById(R.id.et_input);
        etInput.setHint("请输入手机号");
        etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        dialogLayout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogLayout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询用户信息
                String mobileNo = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(mobileNo)) {
                    ToastUtils.showToast("请输入用户手机号");
                    return;
                }
                if (CommonUtils.getMember().getMobileNo().equals(mobileNo)) {
                    ToastUtils.showToast("不能赠送给自己哦");
                    return;
                }
                getUserInfo(couponNo, mobileNo);
                dialog.dismiss();
            }
        });
    }

    /**
     * 查询获赠者用户信息
     *
     * @param mobileNo
     */
    private void getUserInfo(final String couponNo, String mobileNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("mobile", mobileNo);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_COUPON_GET_USER_INFO, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("查询失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败!"));
                    return;
                }

                Member member = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().toString(), Member.class);
                showMemberInfoDialog(couponNo, member);
            }
        });
    }

    /**
     * 显示用户信息对话框
     *
     * @param member
     */
    private void showMemberInfoDialog(final String couponNo, final Member member) {
        if (member == null) return;

        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_user_info_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogLayout).create();
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (DeviceUtils.getWindowWidth(getActivity()) * 0.85);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        TextView tvUserNo = dialogLayout.findViewById(R.id.tv_user_no);
        tvUserNo.setText("编号:\t" + member.getMemberNo());
        TextView tvUserName = dialogLayout.findViewById(R.id.tv_user_name);
        tvUserName.setText("姓名:\t" + member.getNickname());
        TextView tvUserMobile = dialogLayout.findViewById(R.id.tv_user_mobile);
        tvUserMobile.setText("手机号:\t" + member.getMobileNo());

        dialogLayout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogLayout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 转增
                giveCoupon(couponNo, member.getMemberNo());
                dialog.dismiss();
            }
        });

    }

    /**
     * 赠送代金券
     */
    private void giveCoupon(String couponNo, String memberNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("memberNo", memberNo);
        params.put("couponNo", couponNo);
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GIVE_COUPON_TO_MEMBER, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("赠送失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "赠送失败!"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "赠送成功!"));
                smartRefresh.autoRefresh();
            }
        });
    }

    @Override
    protected void loadData() {
        getData();
    }

}
