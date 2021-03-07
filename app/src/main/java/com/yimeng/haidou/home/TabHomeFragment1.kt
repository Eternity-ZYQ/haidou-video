package com.yimeng.haidou.home

import com.yimeng.base.BaseFragment
import com.yimeng.utils.showToast
import com.yimeng.haidou.R
import kotlinx.android.synthetic.main.fragment_tab_home_1.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-15 00:20.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class TabHomeFragment1 :BaseFragment(){
    override fun init() {
    }

    override fun loadData() {
    }

    override fun initListener() {
        iv.setOnClickListener { showToast("暂未开放") }
    }

    override fun setLayoutResId(): Int = R.layout.fragment_tab_home_1
}