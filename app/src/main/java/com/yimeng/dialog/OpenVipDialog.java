package com.yimeng.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.entity.Member;
import com.yimeng.entity.OpenVipPriceBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/27 5:20 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 开通会员
 * </pre>
 */
public class OpenVipDialog extends BottomPopupView {

    private Activity mActivity;
    private Button mBtnSubmit;
    private OpenVipPriceBean mOpenVipPriceBean;
    private RecyclerView mRecyclerView, mRecyclerViewPrice;
    // 选中的那个vip套餐
    private OpenVipPriceBean.VipTypeBean mCheckVipType;
    private OkHttpCommon mOkHttpCommon;



    // 特权介绍
    private BaseQuickAdapter<OpenVipPriceBean.VipRewardArticleBean, BaseViewHolder> mAdapter = new BaseQuickAdapter<OpenVipPriceBean.VipRewardArticleBean, BaseViewHolder>(
            R.layout.adapter_open_vip
    ) {
        @Override
        protected void convert(BaseViewHolder helper, OpenVipPriceBean.VipRewardArticleBean item) {
            helper.setText(R.id.tv_vip_detail, item.getName());
            CommonUtils.showImage(helper.getView(R.id.iv_vip), item.getLogo());
        }
    };
    // 价格
    private BaseQuickAdapter<OpenVipPriceBean.VipTypeBean, BaseViewHolder> mVipPriceAdapter = new BaseQuickAdapter<OpenVipPriceBean.VipTypeBean, BaseViewHolder>(
            R.layout.adapter_open_vip_price
    ) {
        @Override
        protected void convert(BaseViewHolder helper, OpenVipPriceBean.VipTypeBean item) {
            CheckBox checkbox = helper.getView(R.id.checkbox);
            TextView tvVipPrice = helper.getView(R.id.tv_vip_price);
            TextView tvVipDetail = helper.getView(R.id.tv_vip_detail);
            if (item.isChecked()) {
                checkbox.setChecked(true);
                tvVipPrice.setEnabled(true);
                tvVipDetail.setTextColor(ContextCompat.getColor(getContext(), R.color.c_666666));
            } else {
                checkbox.setChecked(false);
                tvVipPrice.setEnabled(false);
                tvVipDetail.setTextColor(Color.WHITE);
            }
            tvVipPrice.setText(item.getIntroduction());
            tvVipDetail.setText(item.getParentNo());
        }
    };
    private TextView mTvVipType;

    public OpenVipDialog(@NonNull Context context, OpenVipPriceBean openVipPriceBean) {
        super(context);
        mActivity = (Activity) context;
        this.mOpenVipPriceBean = openVipPriceBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_open_vip;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        Member member = CommonUtils.getMember();
        mTvVipType = findViewById(R.id.tv_vip_type);
        mBtnSubmit = findViewById(R.id.btn_submit);
        if (member != null) {
            if (member.isVIP()) {
                mTvVipType.setText("VIP到期时间：" + DateUtil.getAssignDate(member.getVipEndTime(), 3));
                mBtnSubmit.setText("续费会员");
            } else {
                mTvVipType.setText("成为会员省时更省钱");
                mBtnSubmit.setText("开通会员");
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter);
        if (mOpenVipPriceBean != null)
            mAdapter.setNewData(mOpenVipPriceBean.getVipRewardArticle());

        mRecyclerViewPrice = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview, null);
        mRecyclerViewPrice.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerViewPrice.setAdapter(mVipPriceAdapter);
        if (mOpenVipPriceBean != null)
            mVipPriceAdapter.setNewData(mOpenVipPriceBean.getVipType());
        mVipPriceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OpenVipPriceBean.VipTypeBean vipTypeBean = mOpenVipPriceBean.getVipType().get(position);
                if (mCheckVipType == vipTypeBean) {
                    return;
                }
                if (mCheckVipType != null) {
                    mCheckVipType.setChecked(false);
                }
                vipTypeBean.setChecked(true);
                mCheckVipType = vipTypeBean;
                mVipPriceAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setFooterView(mRecyclerViewPrice);


        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckVipType == null) {
                    ToastUtils.showToast("您还未选择套餐");
                    return;
                }
                makeVipFee(mCheckVipType.getMenuNo());
                dismiss();
            }
        });

        findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mOkHttpCommon = new OkHttpCommon();
    }


    public void makeVipFee(String vipNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("vipType", vipNo);
        mOkHttpCommon.postLoadData(mActivity, ConstantsUrl.URL_VIP_OPEN, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getContext().getString(R.string.net_error)));
                    return;
                }

                final String rewardNo = jsonObject.get("data").getAsJsonObject().get("rewardNo").getAsString();

                new XPopup.Builder(getContext())
                        .asCustom(new PaySelDialog(getContext(),EventTags.WECHAT_PAY_RESULT, rewardNo))
                        .show();
            }
        });
    }


}

