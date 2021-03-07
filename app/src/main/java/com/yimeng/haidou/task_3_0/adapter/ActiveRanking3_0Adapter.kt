package com.yimeng.haidou.task_3_0.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.ActiveRankingBean
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.SpannableStringUtils

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 11:04 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 3.0排行榜
 * </pre>
 */
class ActiveRanking3_0Adapter(list: List<ActiveRankingBean>) :
        BaseQuickAdapter<ActiveRankingBean, BaseViewHolder>(R.layout.adapter_active_ranking_item, list) {

    override fun convert(helper: BaseViewHolder?, item: ActiveRankingBean?) {

        item?.apply {
            helper?.apply {
                setText(R.id.tv_active, extracts)

                setText(R.id.tv_member_name, nickname)


                CommonUtils.showImage(getView(R.id.civ_user_head) as ImageView, headPath)

                var tvRanking = getView<TextView>(R.id.tv_ranking)
                when (adapterPosition) {
                    0 -> {
                        tvRanking.setBackgroundResource(R.mipmap.ico_ranking_1)
                        tvRanking.text = ""
                    }
                    1 -> {
                        tvRanking.setBackgroundResource(R.mipmap.ico_ranking_2)
                        tvRanking.text = ""
                    }
                    2 -> {
                        tvRanking.setBackgroundResource(R.mipmap.ico_ranking_3)
                        tvRanking.text = ""
                    }
                    else -> {
                        tvRanking.setBackgroundColor(Color.TRANSPARENT)
                        tvRanking.text = SpannableStringUtils.getBuilder((adapterPosition + 1).toString())
                                .setForegroundColor(ContextCompat.getColor(mContext, R.color.c_333333))
                                .create()
                    }
                }
            }
        }
    }
}