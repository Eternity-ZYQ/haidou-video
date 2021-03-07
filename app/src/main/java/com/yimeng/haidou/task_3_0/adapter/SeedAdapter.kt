package com.yimeng.haidou.task_3_0.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.SeedBean
import com.yimeng.utils.DateUtil

/**
 * <pre>
 * Author : huiGer
 * Time   : 2019/8/5 3:18 PM.
 * Email  : zhihuiemail@163.com
 * Desc   : 发财种子
 * </pre>
 */
class SeedAdapter (var list: MutableList<SeedBean>, var index:Int) : BaseQuickAdapter<SeedBean, BaseViewHolder>(
        R.layout.adapter_seed_item, list
){
    private var mIndex = 0
    init {
        mIndex = index
    }

    override fun convert(helper: BaseViewHolder?, item: SeedBean?) {

        helper?.let {
            item?.apply {
                it.setGone(R.id.btn_send, mIndex == 0)
                        .setGone(R.id.btn_sell, mIndex == 0)
                        .addOnClickListener(R.id.btn_send)
                        .addOnClickListener(R.id.btn_sell)
                        .setText(R.id.tv_seed_price, "售价：￥${transfered * 0.01}")
                        .setText(R.id.tv_seed_time, "到期时间：${DateUtil.getAssignDate(expiredTime, 3)}")

            }
        }


    }
}
