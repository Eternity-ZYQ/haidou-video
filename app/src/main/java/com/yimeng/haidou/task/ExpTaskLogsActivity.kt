package com.yimeng.haidou.task

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.ExpTaskLogBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_exp_task_logs.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/25 5:05 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 体验任务流水
 * </pre>
 */
class ExpTaskLogsActivity : BaseActivity() {

    private val mAdapter: BaseQuickAdapter<ExpTaskLogBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ExpTaskLogBean, BaseViewHolder>(
                R.layout.adapter_cash_logs_item_layout, mList) {
            override fun convert(helper: BaseViewHolder?, item: ExpTaskLogBean?) {
                helper?.run {
                    var moneyStr = if(UnitUtil.getInt(item?.settlementAmt) == 0) "收益在路上" else "+"+UnitUtil.getMoney(item?.settlementAmt)
                    setText(R.id.titleTV, "任务收益")
                    setText(R.id.describeTV, DateUtil.getAssignDate(item?.createTime!!, 3))
                    setText(R.id.otherTV, moneyStr)
                }
            }
        }
    }

    private var mPage = 1

    private val mList: MutableList<ExpTaskLogBean> = mutableListOf()

    private val mOkHttpCommon: OkHttpCommon by lazy {
        OkHttpCommon()
    }

    override fun setLayoutResId(): Int = R.layout.activity_exp_task_logs

    override fun init() {

//       var mAdapter = object : BaseQuickAdapter<ExpTaskLogBean, BaseViewHolder>(
//               R.layout.adapter_cash_logs_item_layout, mList){
//            override fun convert(helper: BaseViewHolder?, item: ExpTaskLogBean?) {
//            }
//        }

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@ExpTaskLogsActivity)
            adapter = mAdapter
        }
        var emptyLayout = LayoutInflater.from(this).inflate(R.layout.layout_empty, null)
        emptyLayout.findViewById<TextView>(R.id.tv_empty).text = "推荐好友完成任务，可加速收益到账"
        mAdapter.emptyView = emptyLayout
    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                loadData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                loadData()
            }
        })
    }

    override fun loadData() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_EXP_TASK_LOGS, CommonUtils.createParams(),
                object : CallbackCommon {
                    override fun onFailure(call: Call?, e: IOException?) {
                        showSmartRefreshGetDataFail(smartRefresh, mPage)
                    }

                    override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                        showSmartRefreshGetDataFail(smartRefresh, mPage)

                        if(jsonObject?.get("status")?.asInt != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败"))
                            return
                        }

                        var jsonArr = jsonObject.get("data").asJsonObject.get("rows").asJsonArray.toString()
                        var list = GsonUtils.getGson().fromJson<List<ExpTaskLogBean>>(jsonArr, object : TypeToken<List<ExpTaskLogBean>>() {}.type)
                        if(list.size < Constants.MAX_LIMIT) {
                            smartRefresh.finishLoadMoreWithNoMoreData()
                        }
                        if(mPage == 1 && mList.isNotEmpty()) {
                            mList.clear()
                        }
                        mList.addAll(list)
                        mPage++

                        mAdapter.notifyDataSetChanged()
                    }
                })

    }

}
