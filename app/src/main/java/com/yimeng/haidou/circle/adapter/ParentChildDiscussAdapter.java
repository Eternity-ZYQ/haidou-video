package com.yimeng.haidou.circle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ParentChildDiscussBean;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/24 0024 上午 11:18.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 评论列表适配器
 * </pre>
 */
public class ParentChildDiscussAdapter extends BaseQuickAdapter<ParentChildDiscussBean, BaseViewHolder> {


    public ParentChildDiscussAdapter(@Nullable List<ParentChildDiscussBean> data) {
        super(R.layout.adapter_parent_child_discuss, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ParentChildDiscussBean item) {
        helper.setText(R.id.tv_user_name, item.getNickname())
                .setText(R.id.tv_time, DateUtil.getAssignDate(item.getCreateTime(), 3))
                .setText(R.id.tv_discuss, item.getContent());

        CommonUtils.showImage(helper.getView(R.id.civ_user_head), item.getHeadPath());
    }
}
