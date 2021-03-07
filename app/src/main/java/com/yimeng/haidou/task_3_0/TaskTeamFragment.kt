package com.yimeng.haidou.task_3_0

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.TaskBlockBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.LimitScrollView
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_task_team.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 11:17 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 团队任务已激活
 * </pre>
 */
class TaskTeamFragment : BaseFragment() {

    private var mPage = 1
    private var mList = mutableListOf<TaskBlockBean>()
    private val mAdapter: BaseQuickAdapter<TaskBlockBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<TaskBlockBean, BaseViewHolder>(
                R.layout.adapter_team_task_section, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: TaskBlockBean?) {
                item?.apply {
                    helper?.let {
                        CommonUtils.showRadiusImage(it.getView(R.id.iv), item.blockImage,
                                DeviceUtils.dp2px(mContext, 10f), true, true, true, true)

//                        CommonUtils.showImage(it.getView(R.id.iv), item.blockImage)
                        it.setGone(R.id.tv, isOpen != 1)
                                .setText(R.id.tv, blockTitle)
                    }
                }
            }
        }
    }

    private val mOkHttpCommon = OkHttpCommon()


    companion object {
        fun getInstance(): TaskTeamFragment = TaskTeamFragment()
    }

    override fun setLayoutResId(): Int = R.layout.fragment_task_team

    override fun init() {

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(activity, 2)
            isNestedScrollingEnabled = false
        }

        iv_task.setOnClickListener {
            ActivityUtils.getInstance().jumpActivity(EnrichmentTreeActivity::class.java)
        }
    }

    override fun initListener() {

        mAdapter.setOnItemClickListener { _, _, position ->
            var blockBean = mList[position]

            if (blockBean.isOpen == 1) {
                // 任务已开启
                ActivityUtils.getInstance().jumpActivity(SectionTaskActivity::class.java,
                        Bundle().apply { putSerializable("TaskBlockBean", blockBean) })
            } else {
                // 激活任务
                NetComment.getMemberInfo {
                    if (it.seedCount < blockBean.seedNum) {
                        // 种子不足
//                        SimpleDialog.showConfirmDialog(activity, "提示", "开启该任务区需要${blockBean.seedNum}颗发财种子，\n可用发财种子不足！",
//                                "去抢购", "知道了", {
//                            ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
//                        }, null, false)

                        XPopup.Builder(activity)
                                .maxWidth((DeviceUtils.getWindowWidth(activity) * 0.75).toInt())
                                .autoDismiss(true)
                                .asConfirm("提示", "开启该任务区需要${blockBean.seedNum}颗发财种子，\n可用发财种子不足！",
                                        "去抢购", "知道了", null, {
                                    ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
                                }, false)
                                .bindLayout(R.layout.xpopup_custom_layout)
                                .show()
                    } else {
                        // 激活
                        activateBlock(blockBean.blockNo)
                    }
                }
            }
        }

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getData()


            }
        })

        // 活跃度总榜
        tv_active_top.setOnClickListener { ActivityUtils.getInstance().jumpActivity(ActiveRanking3_0Activity::class.java) }
    }


    override fun loadData() {
        getData()
        getMatchOrderAds()

        // 种子数
        NetComment.getGrabSeedInfo()

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            limitAds?.stopScroll()
        } else {
            limitAds?.startScroll()
            smartRefresh?.autoRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        limitAds?.startScroll()
        smartRefresh?.autoRefresh()
    }

    private fun getData() {

        // http://cuxiao.fengdikj.com/upload/fileupload/cuxiao/taskimg/richTreeBg.png
//        CommonUtils.showImage(iv_task, ConstantsUrl.UPLOAD_HEAD_URL + "/fileupload/cuxiao/taskimg/richTreeBg.png")
        CommonUtils.showImage(iv_task, "http://cuxiao.fengdikj.com/upload/fileupload/cuxiao/taskimg/richTreeBg.png")

        // 种子数，活跃值。。
        NetComment.getMemberInfo {
            tv_active_count.text = "${it.extracts}"
            tv_seed_count.text = "${it.seedCount}"
            tv_fruit_count.text = "${it.fruitCount}"
        }

        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_TEAM_TASK_BLOCK, CommonUtils.createParams().apply {
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
                    var dataObj = it.get("data").asJsonObject
                    var jsonArray = dataObj.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<TaskBlockBean>>(jsonArray.toString(), object : TypeToken<List<TaskBlockBean>>() {}.type)
                    if (mPage == 1 && mList.isNotEmpty()) mList.clear()

                    if (list.size < Constants.MAX_LIMIT) smartRefresh.finishLoadMoreWithNoMoreData()
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()

                    var otherDataObj = dataObj.get("otherData").asJsonObject
                    // 系统已配x单
                    tv_order_match_num.text = "${otherDataObj.get("orderMatchingNum").asInt + 10000}"
                }
            }
        })
    }


    /**
     * 激活专区
     */
    private fun activateBlock(blockNo: String) {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_TEAM_ACTIVATE_BLOCK, CommonUtils.createParams().apply {
            put("mtBlockNo", blockNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "激活失败，请稍后重试！"))
                        return
                    }

                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "激活成功"))
                    smartRefresh.autoRefresh()
                }
            }
        })
    }

    /**
     * 匹配订单广告
     */
    private fun getMatchOrderAds() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_3_0_ORDER_MATCH, CommonUtils.createParams().apply {

        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (jsonObject.get("status").asInt == 1) {

                        var jsonArray = jsonObject.get("data").asJsonArray
                        var list = mutableListOf<String>()

                        for (i in 0 until jsonArray.size()) {
                            list.add(jsonArray[i].toString())
                        }
                        setMatchOrderAdsData(list)
                    }
                }
            }
        })
    }

    /**
     * 匹配订单广告设置数据
     */
    private fun setMatchOrderAdsData(list: MutableList<String>) {
        if (list.isEmpty()) return

        limitAds.setAdapter(object : LimitScrollView.LimitScrollViewAdapter {
            override fun getView(position: Int): View {
                val tv = TextView(activity)
                tv.setPadding(0, DeviceUtils.dp2px(activity, 8f), 0, DeviceUtils.dp2px(activity, 8f))
                tv.text = list[position]
                tv.gravity = Gravity.CENTER
                tv.setTextColor(Color.WHITE)
                tv.textSize = 14f
                return tv
            }

            override fun getCount(): Int = list.size
        })
        limitAds.startScroll()
    }

}