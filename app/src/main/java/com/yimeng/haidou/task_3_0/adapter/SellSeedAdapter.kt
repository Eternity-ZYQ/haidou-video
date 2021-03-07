package com.yimeng.haidou.task_3_0.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.SellSeedBean
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 3:24 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售种子
 * </pre>
 */
class SellSeedAdapter(list: MutableList<SellSeedBean>) : BaseQuickAdapter<SellSeedBean, BaseViewHolder>(
        R.layout.adapter_sell_seed, list
) {

    private var mimeMemberNo = ""

    init {
        mimeMemberNo = CommonUtils.getMember().memberNo
    }

    override fun convert(helper: BaseViewHolder?, item: SellSeedBean?) {
        helper?.let {
            item?.apply {
                var isMine = mimeMemberNo == tradeFrom

                it.setText(R.id.tv_user_name, if (isMine) "挂卖价格￥${formatStr(tradeFromAmt / 100.0 / tradeNum)}/颗" else fromNikename)
                        .setText(R.id.tv_seed_num, "x$tradeNum")
                        .setText(R.id.btn, if (isMine) "全部收回" else "抢购￥${formatStr(tradeFromAmt / 100.0 / tradeNum)}/颗")
                        .setText(R.id.tv_time, DateUtil.getAssignDate(createTime, 3))
                        .addOnClickListener(R.id.btn)
            }
        }
    }

    private fun formatStr(d: Double): String = String.format("%.2f", d)
}