package com.yimeng.haidou.mine

import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseFragment
import com.yimeng.config.XJConfig
import com.yimeng.entity.ReelBean
import com.yimeng.retorfit.ResponseBase
import com.yimeng.retorfit.ResponseListData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.finish
import com.yimeng.utils.showToast
import com.yimeng.haidou.R
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_change_reel.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 18:58.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 兑换金蛋
 * </pre>
 */
class ChangeReelFragment : BaseFragment() {

    companion object {
        fun getInstance(): Fragment {
            return ChangeReelFragment()
        }
    }

    private var mPage = 1

    private val mList = mutableListOf<ReelBean>()
    private val mAdapter = object : BaseQuickAdapter<ReelBean, BaseViewHolder>(
            R.layout.adapter_change_reel_item, mList) {
        override fun convert(helper: BaseViewHolder, item: ReelBean?) {
            item?.run {
                helper.setText(R.id.tv_reel_name, feederName)
                        .setText(R.id.tv_reel_task, remark)
                        .setText(R.id.tv_reel_consume, "日产：${nissan / XJConfig.MONEY_UNOT}")
                        .setText(R.id.tv_reel_yield, "总产量：${totalOutput / XJConfig.MONEY_UNOT}")
                        .setText(R.id.tv_reel_validity, "有效期：${if (period > 1000) "永久" else "${period}天"}")
                        .addOnClickListener(R.id.btn_change)
            }
        }
    }

    override fun init() {

        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
        mAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).setTextColor(Color.parseColor("#999999"))

    }

    override fun loadData() {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            smartRefresh.autoRefresh()
        }
    }

    override fun initListener() {

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getReels()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                if (mList.isNotEmpty()) mList.clear()
                mPage = 1
                getReels()
            }

        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.btn_change) {
                // 兑换金蛋
                changeReel(mList[position].feederNo)
            }
        }

    }

    override fun setLayoutResId(): Int = R.layout.fragment_change_reel

    /**
     * 获取金蛋列表
     */
    private fun getReels() {
        val start = (mPage - 1) * XJConfig.LIMIT
        RetrofitHelper.toDo().getReels(start).enqueue(object : Callback<ResponseListData<ReelBean>> {
            override fun onFailure(call: Call<ResponseListData<ReelBean>>, t: Throwable) {
                smartRefresh.finish(false, mPage, -1)
            }

            override fun onResponse(call: Call<ResponseListData<ReelBean>>, response: Response<ResponseListData<ReelBean>>) {
                if (RetrofitHelper.isSuccess(response)) {
                    response.body()?.data?.rows?.run {
                        mList.addAll(this.filter {
                            it.price > 0
                        })
                        mList.sortBy { it.createTime }
                        mAdapter.notifyDataSetChanged()
                        smartRefresh.finish(true, mPage, size)
                    } ?: smartRefresh.finish(false, mPage, -1)
                } else {
                    smartRefresh.finish(false, mPage, -1)
                }
            }
        })
    }

    /**
     * 兑换金蛋
     */
    private fun changeReel(reelNo: String) {
        RetrofitHelper.toDo().changeReels(reelNo).enqueue(object : Callback<ResponseBase> {
            override fun onFailure(call: Call<ResponseBase>, t: Throwable) {
                showToast(RetrofitHelper.getErrMsg(t))
            }

            override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                if (RetrofitHelper.isSuccess(response)) {
                    showToast("兑换成功！")
                }
            }
        })
    }
}