package com.yimeng.haidou.task_3_0.adapter

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.haidou.task_3_0.SectionTaskChildActivity
import com.yimeng.entity.SectionTaskBean
import com.yimeng.utils.ActivityUtils

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/8 4:31 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 专区区块
 * </pre>
 */
class SectionTaskItemAdapter(var list: MutableList<SectionTaskBean>) : BaseQuickAdapter<SectionTaskBean, BaseViewHolder>(
        R.layout.adapter_section_item, list
) {
    // true 展开
    private var mExpandList = mutableListOf<Boolean>()

    fun refreshData() {
        if (mExpandList.isEmpty())
            data.forEach { mExpandList.add(true) }

        notifyDataSetChanged()
    }

    override fun convert(helper: BaseViewHolder?, item: SectionTaskBean?) {
        item?.apply {
            helper?.let {


                it.setText(R.id.tv_task_name, merchantTaskName + if(memberTaskNo.isEmpty()) "(消耗${seedNum}颗发财种子)" else "")
                        .setText(R.id.btn_activate, "激活$merchantTaskName")
                        .setGone(R.id.btn_activate, memberTaskNo.isEmpty())
                        .setGone(R.id.ll_buy, false)
                        .setGone(R.id.ll_sell, false)
                        .addOnClickListener(R.id.btn_buy)
                        .addOnClickListener(R.id.btn_sell)
                        .addOnClickListener(R.id.btn_activate)

                //mBuySellSet  0 买  1 卖
                if (item.merchantTaskType == "merchants" && memberTaskNo.isNotEmpty()) {
                    when (buySellSet) {
                        "0" -> {
                            //买
                            it.setGone(R.id.ll_buy, true)

                            // 购买进度
                            it.getView<ProgressBar>(R.id.progressBar_buy).apply {
                                max = buyNum
                                progress = mBuyNum
                            }
                            it.setText(R.id.tv_buy_loading, "$mBuyNum/$buyNum")
                        }

                        "1" -> {
                            // 卖满
                            if(mSellNum >= sellNum) {
                                it.setGone(R.id.ll_sell, false)
                                        .setText(R.id.btn_activate, "开启$merchantTaskName")
                                        .setGone(R.id.btn_activate, true)
                            }else{
                                // 卖
                                it.setGone(R.id.ll_sell, true)
                                // 销售进度
                                it.getView<ProgressBar>(R.id.progressBar_sell).apply {
                                    max = sellNum
                                    progress = mSellNum
                                }
                                it.setText(R.id.tv_sell_loading, "$mSellNum/$sellNum")

                                if (merchantTaskCode != "rec" && distributeStatus == 1 && distributeNum > 0) {
                                    it.setText(R.id.btn_sell, "抢卖1单")
                                } else {
                                    it.setText(R.id.btn_sell, "好友助力")
                                }
                            }
                        }
                    }
                }



                // 展开……显示
                var expandStatus = mExpandList[it.adapterPosition]
                it.setGone(R.id.ll_task_child, expandStatus)

                if(item.merchantTaskType == "rec" && memberTaskNo.isNotEmpty()) {
                    it.getView<ImageView>(R.id.iv_task_close).rotation = 0f
                }else{
                    it.getView<ImageView>(R.id.iv_task_close).rotation = if (expandStatus) 90f else -90f
                }

                it.getView<LinearLayout>(R.id.ll_task_title).setOnClickListener { view ->
                    if (item.merchantTaskType == "rec" && memberTaskNo.isNotEmpty()) {
                        ActivityUtils.getInstance().jumpActivity(SectionTaskChildActivity::class.java,
                                Bundle().apply { putString("merchantTaskNo", item.merchantTaskNo) })
                    } else {
                        view.setTag(R.id.click_filter_key, true)
                        mExpandList[helper.adapterPosition] = !expandStatus
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }


}