package com.yimeng.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.haidou.R
import com.yimeng.entity.FilterFruitBean
import com.lxj.xpopup.impl.PartShadowPopupView
import kotlinx.android.synthetic.main.dialog_fruit_filter.view.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/24 5:02 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 果子条件筛选
 * </pre>
 */
class FruitFilterDialog(context: Context, datas: MutableList<FilterFruitBean>, checkNo:String, filterCallBack: FilterCallBack) : PartShadowPopupView(context) {

    private var mList = datas
    private var mCheckedNo = checkNo
    private var mFilterCallBack = filterCallBack

    override fun getImplLayoutId(): Int = R.layout.dialog_fruit_filter

    override fun onCreate() {
        super.onCreate()

        var mAdapter = object : BaseQuickAdapter<FilterFruitBean, BaseViewHolder>(
                R.layout.adapter_filter_item, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: FilterFruitBean?) {
                helper?.let {
                    item?.apply {
                        it.setText(R.id.tv_filter, title)
                                .setGone(R.id.iv_filter, no == mCheckedNo)
                    }
                }
            }
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            mFilterCallBack.filter(mList[position].no)
            dismiss()
        }
    }

    interface FilterCallBack{
        fun filter(checkNo:String)
    }
}