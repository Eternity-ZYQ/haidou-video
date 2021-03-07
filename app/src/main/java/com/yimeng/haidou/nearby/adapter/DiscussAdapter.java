package com.yimeng.haidou.nearby.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelGrade;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/22 0022 下午 02:23.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺评价
 * </pre>
 */
public class DiscussAdapter extends BaseQuickAdapter<ModelGrade, BaseViewHolder> {
    public DiscussAdapter(@Nullable List<ModelGrade> data) {
        super(R.layout.adapter_shop_discuss, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModelGrade item) {
        CommonUtils.showRadiusImage(helper.getView(R.id.civ_user_head),  item.getAvatarImg(),DeviceUtils.dp2px(mContext, 30),
                true, true, true, true);
        helper.setText(R.id.tv_user_name, item.getName())
                .setText(R.id.tv_time, DateUtil.getAssignDate(item.getCreateTime(), 3))
                .setText(R.id.tv_discuss, item.getDescribe())
                .addOnClickListener(R.id.iv_1)
                .addOnClickListener(R.id.iv_2)
                .addOnClickListener(R.id.iv_3)
        ;

        final RatingStarView ratingStarView = helper.getView(R.id.gradeRSV);
        ratingStarView.setRating(UnitUtil.getFloat(item.getGrade()));

        if (!TextUtils.isEmpty(item.getImage1())) {
            ImageView iv1 = helper.getView(R.id.iv_1);
            iv1.setVisibility(View.VISIBLE);
            CommonUtils.showRadiusImage(iv1,  item.getImage1(),DeviceUtils.dp2px(mContext, 10),
                    true, true, true, true);
        }
        if (!TextUtils.isEmpty(item.getImage2())) {
            ImageView iv2 = helper.getView(R.id.iv_2);
            iv2.setVisibility(View.VISIBLE);
            CommonUtils.showRadiusImage(iv2,  item.getImage2(),DeviceUtils.dp2px(mContext, 10),
                    true, true, true, true);
        }
        if (!TextUtils.isEmpty(item.getImage3())) {
            ImageView iv3 = helper.getView(R.id.iv_3);
            iv3.setVisibility(View.VISIBLE);
            CommonUtils.showRadiusImage(iv3,  item.getImage3(),DeviceUtils.dp2px(mContext, 10),
                    true, true, true, true);
        }

    }
}
