package com.yimeng.haidou.task_3_0

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.TaskBlockBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_active_ranking3_0.*
import kotlinx.android.synthetic.main.fragment_task_team.smartRefresh
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/10 10:19 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 活跃度排行总榜
 * </pre>
 */
class ActiveRanking3_0Activity : BaseActivity() {

    private var mPage = 1
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<TaskBlockBean>()
    private val mAdapter: BaseQuickAdapter<TaskBlockBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<TaskBlockBean, BaseViewHolder>(
                R.layout.adapter_active_ranking_3_0, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: TaskBlockBean?) {
                item?.apply {
                    helper?.let {
                        it.setText(R.id.tv_active_ranking_name, "$blockName 排行榜")
                    }
                }
            }
        }
    }


    override fun setLayoutResId(): Int = R.layout.activity_active_ranking3_0

    override fun init() {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ActiveRanking3_0Activity)
            adapter = mAdapter
        }


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

        mAdapter.setOnItemClickListener { _, _, position ->
            var taskBlockBean = mList[position]

            if(taskBlockBean.isOpen != 1){
                ToastUtils.showToast("激活该专区任务可查看排行榜")
                return@setOnItemClickListener
            }

            // 专区排行榜
            ActivityUtils.getInstance().jumpActivity(ActiveRankingChildActivity::class.java, Bundle().apply {
                putString("blockNo", taskBlockBean.blockNo)
                putString("blockName", taskBlockBean.blockName)
            })

        }

    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }

    private fun getData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_TASK_BLOCK, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                showSmartRefreshGetDataFail(smartRefresh, mPage)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    var jsonArray = it.get("data").asJsonObject.get("rows").asJsonArray
                    if(jsonArray.size() == 0) return
                    var list = GsonUtils.getGson().fromJson<List<TaskBlockBean>>(jsonArray.toString(), object : TypeToken<List<TaskBlockBean>>() {}.type)
                    if (mPage == 1 && mList.isNotEmpty()) mList.clear()

                    if (list.size < Constants.MAX_LIMIT) smartRefresh.finishLoadMoreWithNoMoreData()
                    mPage++
//                    list.forEach { bean ->
//                        if(bean.isOpen == 1) {
//                            mList.add(bean)
//                        }
//                    }
                    mList.addAll(list)
                    mAdapter.notifyLoadMoreToLoading()
                }
            }
        })
    }
}
