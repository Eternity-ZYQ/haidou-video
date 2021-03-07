package com.yimeng.haidou.circle.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.Member;
import com.yimeng.entity.ParentChildCircleDetail;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/22 0022 下午 06:45.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商圈列表
 * </pre>
 */
public class FriendCircleAdapter extends BaseQuickAdapter<ParentChildCircleDetail, BaseViewHolder> {
    public FriendCircleAdapter(@Nullable List<ParentChildCircleDetail> data) {
        super(R.layout.adapter_friend_circle, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ParentChildCircleDetail item) {


        CommonUtils.showImage(helper.getView(R.id.civ_user_head), item.getHeadPath());

        helper.setText(R.id.tv_user_name, item.getNickname())
                .setText(R.id.tv_time, DateUtil.getAssignDate(item.getCreateTime(), 3))
                .setText(R.id.tv_discuss, item.getContent())
                .setText(R.id.tv_discuss_num, item.getCommentNum() + "")
                .setChecked(R.id.cb_praise_num, item.isMemberFabulous())
                .setText(R.id.cb_praise_num, item.getGiveNum() + "")
                .addOnClickListener(R.id.cb_praise_num)
                .addOnClickListener(R.id.ll_article)
                .addOnClickListener(R.id.tv_share)
                .addOnClickListener(R.id.tv_del)
                .addOnClickListener(R.id.layout_images)
        ;
        TextView tvContent = helper.getView(R.id.tv_discuss);
        tvContent.setMaxLines(10);

        // 自己发的可以删除
        helper.setGone(R.id.tv_del, false);
        Member member = CommonUtils.getMember();
        if(member != null) {
            if(member.getMemberNo().equals(item.getMemberNo())) {
                helper.setGone(R.id.tv_del, true);
            }
        }



        if(item.getIsReprinted() == 1) {
            // 转载内容
            helper.setGone(R.id.layout_images, false);
            helper.setVisible(R.id.ll_article, true);
            CommonUtils.showImage(helper.getView(R.id.iv_article_pic),item.getImages());
            helper.setText(R.id.tv_article_title, item.getReprintedTitle());


        }else{
            // 非转载内容
            helper.setGone(R.id.ll_article, false);
            String images = item.getImages();
            helper.setVisible(R.id.iv_1, false);
            helper.setVisible(R.id.iv_2, false);
            helper.setVisible(R.id.iv_3, false);
            if (!TextUtils.isEmpty(images)) {
                helper.setGone(R.id.layout_images, true);
                String[] split = images.split(",");
                if (split.length >= 1) {
//                CommonUtils.showRadiusImage((ImageView) helper.getView(R.id.iv_1), split[0]);
                    helper.setVisible(R.id.iv_1, true);
                    CommonUtils.showImage(helper.getView(R.id.iv_1), split[0]);
                    if(split.length >= 2) {
//                    CommonUtils.showRadiusImage((ImageView) helper.getView(R.id.iv_1), split[1]);
                        helper.setVisible(R.id.iv_2, true);
                        CommonUtils.showImage(helper.getView(R.id.iv_2), split[1]);
                        if(split.length >= 3) {
                            helper.setVisible(R.id.iv_3, true);
//                        CommonUtils.showRadiusImage((ImageView) helper.getView(R.id.iv_1), split[2]);
                            CommonUtils.showImage(helper.getView(R.id.iv_3), split[2]);
                        }
                    }
                }
            }else{
                helper.setGone(R.id.layout_images, false);
            }
        }
        



    }
}
