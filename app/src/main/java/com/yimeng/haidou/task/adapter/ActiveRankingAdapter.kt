package com.yimeng.haidou.task.adapter

import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.ActiveRankingBean
import com.yimeng.entity.Member
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.SpannableStringUtils

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 11:04 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class ActiveRankingAdapter(@LayoutRes layoutId: Int, list: List<ActiveRankingBean>, isFS: Boolean, @NonNull member: Member) :
        BaseQuickAdapter<ActiveRankingBean, BaseViewHolder>(layoutId, list) {

    private var isFS = false
    private var member:Member ?= null

    init {
        this.isFS = isFS
        this.member = member

    }

    override fun convert(helper: BaseViewHolder?, item: ActiveRankingBean?) {

        item?.apply {
            helper?.apply {
                setText(R.id.tv_active, extracts)

                if(member!!.isAgent) { // 合伙人
                    setText(R.id.tv_member_shop, "店铺名：$nickname")
                    setText(R.id.tv_member_order, "缺单数：${if (isFS) taskCount else expireTime}")
                    setText(R.id.tv_member_name, "昵称：$nkName")
                    setText(R.id.tv_member_mobile, "手机号：$mobileNo")
                    addOnClickListener(R.id.iv_send_order)
                    if(member!!.memberNo == memberNo) {
                        setGone(R.id.iv_send_order, false)
                    }
                }else{
                    setGone(R.id.tv_member_shop, false)
                    setGone(R.id.tv_member_order, false)
                    setGone(R.id.tv_member_mobile, false)
                    setGone(R.id.iv_send_order, false)
                    setText(R.id.tv_member_name, nickname)
                }


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