package com.yimeng.haidou.mall

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseFragment
import com.yimeng.entity.HomeDataBean
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.haidou.R
import kotlinx.android.synthetic.main.fragment_mall.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-25 22:41.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商城item fragment
 * </pre>
 */
class MallFragment : BaseFragment() {

    companion object {
        fun getInstance(): MallFragment {
            return MallFragment()
        }
    }

    private val mList = mutableListOf<HomeDataBean.DataBean>()
    private val mAdapter by lazy {
        object : BaseQuickAdapter<HomeDataBean.DataBean, BaseViewHolder>(R.layout.adapter_mall_product_item, mList) {
            override fun convert(helper: BaseViewHolder, item: HomeDataBean.DataBean) {
                helper.getView<ImageView>(R.id.iv_product_logo).run {
                    CommonUtils.showRadiusImage(this, CommonUtils.parseImageUrl(item.imagesPath),
                            SizeUtils.dp2px(10f), true, true, false, false)
                }
                item.run {
                    helper.setText(R.id.tv_product_price, UnitUtil.getMoney(price))
                            .setText(R.id.tv_product_name, productName)
                            .setText(R.id.tv_product_pay, mContext.resources.getString(R.string.personPay, hasSaled))
                }

            }
        }
    }

    /**
     * 1. 综合
     * 2. 销量
     * 3. 最新
     */
    private var sortType = 1

    override fun setLayoutResId(): Int = R.layout.fragment_mall

    override fun init() {

        recyclerView.run {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }




    }

    override fun loadData() {
    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { _, _, position ->
            val dataBean = mList[position]
            ActivityUtils.getInstance().jumpShopProductActivity(1, dataBean.shopNo, dataBean.productNo, 0, "")
        }

        cb_zh.setOnClickListener {
            sortType = 1

            cb_zh.isChecked = true
            cb_xl.isChecked = false
            cb_zx.isChecked = false

            v_zh.visibility = View.VISIBLE
            v_xl.visibility = View.INVISIBLE
            v_zx.visibility = View.INVISIBLE
        }

        cb_xl.setOnClickListener {
            sortType = 2

            cb_zh.isChecked = false
            cb_xl.isChecked = true
            cb_zx.isChecked = false

            v_zh.visibility = View.INVISIBLE
            v_xl.visibility = View.VISIBLE
            v_zx.visibility = View.INVISIBLE
        }

        cb_zx.setOnClickListener {
            sortType = 3

            cb_zh.isChecked = false
            cb_xl.isChecked = false
            cb_zx.isChecked = true

            v_zh.visibility = View.INVISIBLE
            v_xl.visibility = View.INVISIBLE
            v_zx.visibility = View.VISIBLE
        }

    }

    /**
     * 刷新数据
     */
    fun refreshData(homeDataBean: HomeDataBean) {
        if (mList.isNotEmpty()) mList.clear()
        iv_banner?.let { CommonUtils.showRadiusImage(it, CommonUtils.parseImageUrl(homeDataBean.logo),
                SizeUtils.dp2px(10f), true, true, true, true) }

        mList?.addAll(homeDataBean.data)
        mAdapter?.notifyDataSetChanged()
    }
}