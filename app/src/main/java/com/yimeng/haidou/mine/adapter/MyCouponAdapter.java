package com.yimeng.haidou.mine.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.CouponBean;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.SpannableStringUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/25 0025 下午 04:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代金券
 * </pre>
 */
public class MyCouponAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {
    /**
     * 0. 未使用
     * 1. 已使用
     * 2. 已过期
     */
    private int mType;
    /**
     * 是否为购买时选择优惠券
     */
    private boolean mIsSelect;
    /**
     * 是否来源于购买任务
     *
     * 购买任务隐藏赠送得到的代金券
     */
    private boolean mIsTask;

    public MyCouponAdapter(@Nullable List<CouponBean> data, int type, boolean isSelect, boolean isTask) {
        super(R.layout.adapter_coupon_item, data);
        this.mType = type;
        this.mIsSelect = isSelect;
        this.mIsTask = isTask;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        helper
                .setText(R.id.tv_money, SpannableStringUtils.getBuilder("¥")
                        .append((item.getAmt() / 100) + "").setProportion(2.0f)
                        .create())
                .setText(R.id.tv_coupon_name, item.getCouponSrc().equals("saleaward") ? "销售奖励" : "他人赠送")
                .setText(R.id.tv_coupon_time, "有效期:\t\t" +
                        DateUtil.getAssignDate(item.getCreateTime(), 1) +
                        "-" +
                        DateUtil.getAssignDate(item.getExpiredTime(), 1))
                .setGone(R.id.btn_send, item.getTransfered() == 1)
                .addOnClickListener(R.id.btn_send)
                .addOnClickListener(R.id.btn_use);

        if(mIsTask && !item.getCouponSrc().equals("saleaward")) {// 任务消费选择代金券时，隐藏赠送得到的代金券
            helper.getView(R.id.ll_content).setVisibility(View.GONE);
        }
        if (mType == 0) {
            // 未使用
            helper.setImageResource(R.id.iv_coupon_status, R.mipmap.ico14_daijinquan_zuo)
                    .setTextColor(R.id.tv_money, mContext.getResources().getColor(R.color.theme_color))
                    .setTextColor(R.id.tv_coupon_name, mContext.getResources().getColor(R.color.c_666666))
                    .setTextColor(R.id.tv_coupon_desc, mContext.getResources().getColor(R.color.c_666666))
                    .setTextColor(R.id.tv_coupon_time, mContext.getResources().getColor(R.color.c_666666))
                    .setText(R.id.btn_use, mIsSelect ? "去使用" : "折现")
                    .setBackgroundRes(R.id.btn_use, R.drawable.shape_rect_theme);
        } else {
            helper.setImageResource(R.id.iv_coupon_status, R.mipmap.ico14_daijinquan_shixiao)
                    .setTextColor(R.id.tv_money, mContext.getResources().getColor(R.color.c_cccccc))
                    .setTextColor(R.id.tv_coupon_name, mContext.getResources().getColor(R.color.c_cccccc))
                    .setTextColor(R.id.tv_coupon_desc, mContext.getResources().getColor(R.color.c_cccccc))
                    .setTextColor(R.id.tv_coupon_time, mContext.getResources().getColor(R.color.c_cccccc))
                    .setTextColor(R.id.btn_use, mContext.getResources().getColor(R.color.c_cccccc))
                    .setBackgroundRes(R.id.btn_use, R.drawable.shape_rect_666)
                    .setGone(R.id.btn_send, false);
            helper.getView(R.id.btn_use).setClickable(false);

            if (mType == 1) { // 已使用
                helper.setText(R.id.btn_use, "已使用");
            } else { // 已过期
                helper.setText(R.id.btn_use, "已过期");
            }
        }
    }
}
