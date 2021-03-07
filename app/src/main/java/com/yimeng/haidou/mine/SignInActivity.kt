package com.yimeng.haidou.mine

import android.graphics.Color
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.widget.SignTaskLayout
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/24 5:22 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 签到
 * </pre>
 */
class SignInActivity : BaseActivity() {


    private var mList = mutableListOf("", "", "", "", "", "")
    private val mAdapter: BaseQuickAdapter<String, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_sign_task_item, mList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.apply {

                    var signLayout = getView<SignTaskLayout>(R.id.signLayout)
                    signLayout.setDoTaskClickListener {

                    }
                }
            }
        }
    }

    private var mCompleteList = mutableListOf("", "", "", "")
    private val mCompleteAdapter: BaseQuickAdapter<String, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_sign_task_item, mCompleteList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.apply {

                    var signLayout = getView<SignTaskLayout>(R.id.signLayout)
                    signLayout.setDoTaskClickListener {

                    }
                }
            }
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_sign_in

    override fun init() {

        StatusBarUtil.transparencyBar(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SignInActivity)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }

        var footLayout = LayoutInflater.from(this).inflate(R.layout.sign_task_foot_layout, null)
        footLayout.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@SignInActivity)
            adapter = mCompleteAdapter
            isNestedScrollingEnabled = false
        }
        mAdapter.setFooterView(footLayout)

    }

    override fun initListener() {

        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY >= DeviceUtils.dp2px(this, 25f)) {
                layout_head.setBackgroundColor(Color.parseColor("#202020"))
                tv_title.visibility = View.VISIBLE
            } else {
                layout_head.setBackgroundColor(Color.TRANSPARENT)
                tv_title.visibility = View.GONE
            }
        }


    }

    override fun loadData() {
    }

}
