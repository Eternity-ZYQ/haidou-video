package com.yimeng.haidou.mine.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ProxyMemberBean;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/17 11:11 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代理列表数据适配器
 * </pre>
 */
public class MineProxyAdapter extends BaseQuickAdapter<ProxyMemberBean, BaseViewHolder> {
    public MineProxyAdapter(@Nullable List<ProxyMemberBean> data) {
        super(R.layout.adapter_proxy_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProxyMemberBean item) {

        CommonUtils.showImage(helper.getView(R.id.iv_user_head), item.getHeadPath());

        helper.setText(R.id.tv_user_name, item.getNickname())
                .setText(R.id.tv_user_mobile, item.getMobileNo())
                .setText(R.id.tv_time, item.getContractTime() ==  0 ? "未结算" :DateUtil.getAssignDate(item.getContractTime(), 3))
                .setText(R.id.tv_user_grade, item.getGradeName())
                .setText(R.id.tv_shop_name, item.getJob().equals("open") ? item.getShopName() : "未开店")
                .setGone(R.id.iv_settlement, item.getJoinedRemark().equals("settlement"));

    }
}
