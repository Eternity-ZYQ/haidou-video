package com.yimeng.haidou.mine.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.entity.AgencyBean;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;

import java.util.List;

/**
 * Author : huiGer
 * Time   : 2018/9/17 0017 上午 11:35.
 * Desc   : 代理
 */
public class ProxyPersonAdapter extends BaseQuickAdapter<AgencyBean, BaseViewHolder> {

    public ProxyPersonAdapter(@Nullable List<AgencyBean> data) {
        super(R.layout.adapter_proxy_person_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AgencyBean item) {
        helper.addOnClickListener(R.id.tv_del).addOnClickListener(R.id.tv_update)
                .setText(R.id.tv_user_name, item.getMemberName())
                .setText(R.id.tv_user_mobile, item.getMobileNo())
                .setText(R.id.tv_user_extracts, "代理商提成: " + item.getExtracts()+"%")
        ;

        CommonUtils.showImage(helper.getView(R.id.iv_user_head), item.getHeadPath());
    }

}
