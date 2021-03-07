package com.yimeng.haidou.task_3_0

import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.SeedLogBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_seed_logs.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 4:09 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 种子记录
 * </pre>
 */
class SeedLogsActivity : BaseActivity() {


    /**
     * 1. 种子记录
     * 2. 活跃果记录
     */
    private var mType = 1

    private var mPage = 1
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SeedLogBean>()
    private val mAdapter: BaseQuickAdapter<SeedLogBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<SeedLogBean, BaseViewHolder>(
                R.layout.adapter_seed_logs, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: SeedLogBean?) {
                helper?.let {
                    item?.apply {
                        it.setText(R.id.tv_log_name, logName)
                                .setText(R.id.tv_log_time, DateUtil.getAssignDate(item.createTime, 3))
                                .setText(R.id.tv_count, "${if (logInOut == "out") "-" else "+"}$tradeNum")
                    }
                }
            }
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_seed_logs

    override fun init() {
        intent.extras?.let {
            mType = it.getInt("type",1)
        }

        toolBar.title = if(mType == 1) "发财种子记录" else "活跃果记录"

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SeedLogsActivity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getData()
            }
        })
    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }

    private fun getData() {
        var url = if(mType == 1) ConstantsUrl.URL_SEED_LOGS else ConstantsUrl.URL_FRUIT_LOGS

        mOkHttpCommon.postLoadData(this, url, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                showSmartRefreshGetDataFail(smartRefresh, mPage)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)
                jsonObject?.let {
                    if(it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var arr = it.get("data").asJsonObject.get("rows").asJsonArray
                    if(arr.size() == 0) return
                    var jsonArrStr = arr.toString()
                    var list = GsonUtils.getGson().fromJson<List<SeedLogBean>>(jsonArrStr, object :TypeToken<List<SeedLogBean>>(){}.type)
                    if(list.size < Constants.MAX_LIMIT) smartRefresh.finishLoadMoreWithNoMoreData()
                    if(mPage == 1 && mList.isNotEmpty()) mList.clear()
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyLoadMoreToLoading()
                }
            }
        })
    }
}
