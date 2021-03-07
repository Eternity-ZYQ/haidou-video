package com.yimeng.haidou.task_3_0.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.SellFruitBean
import com.yimeng.utils.CommonUtils

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 3:24 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售活跃果
 * </pre>
 */
class SellFruitAdapter(list: MutableList<SellFruitBean>, fruitType: Int) : BaseQuickAdapter<SellFruitBean, BaseViewHolder>(
        R.layout.adapter_sell_fruit, list
) {

    /**
     * 1. 我的活跃果
     * 2. 在售
     * 3. 我的挂卖
     */
    private var mFruitType = fruitType

    private var mimeMemberNo = ""

    init {
        mimeMemberNo = CommonUtils.getMember().memberNo
    }

    override fun convert(helper: BaseViewHolder?, item: SellFruitBean?) {
        helper?.let {
            item?.apply {
                var isMine = mimeMemberNo == tradeFrom

                it.setText(R.id.tv_fruit_grade, if (mFruitType == 1) merchantTaskName else recTitle)
                        .setText(R.id.tv_fruit_count, if (mFruitType == 1) "$fruitCount" else "$tradeNum")
                        .setGone(R.id.tv_fruit_do, false)
                        .setGone(R.id.btn, true)
                        .setText(R.id.btn, if (mFruitType == 1) "挂卖" else {
                            if (isMine) "收回"
                            else "抢购￥${formatStr(tradeFromAmt.toInt()/100.0)}"
                        })
                        .addOnClickListener(R.id.btn)
            }
        }
    }

    private fun formatStr(d: Double): String = String.format("%.2f", d)
}