package com.yimeng.haidou.task_3_0

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.haidou.goodsClassify.CompanySaleClassifyActivity
import com.yimeng.haidou.mine.IDCardInfoActivity
import com.yimeng.haidou.offline.SelPartnerTypeActivity
import com.yimeng.haidou.task_3_0.adapter.SectionTaskItemAdapter
import com.yimeng.entity.SectionTaskBean
import com.yimeng.entity.TaskBlockBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.SharedPreferencesUtils
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.LimitScrollView
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_section_task.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 7:00 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 专区任务
 * </pre>
 */
class SectionTaskActivity : BaseActivity() {

    private var mBlockBean: TaskBlockBean? = null
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SectionTaskBean>()
    private val mAdapter: SectionTaskItemAdapter by lazy { SectionTaskItemAdapter(mList) }
    // 介绍是否显示
    private var mDetailIsShow = true


    override fun setLayoutResId(): Int = R.layout.activity_section_task

    override fun init() {
        intent.extras?.let {
            mBlockBean = it.getSerializable("TaskBlockBean") as TaskBlockBean

        }


        mBlockBean?.apply {
            toolBar.title = blockName
            tv_task_desc.text = blockIntroduct

        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SectionTaskActivity)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }


    }


    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshListener { getData() }

        // 排行榜
        tv_active_top.setOnClickListener { jumpRanking() }

        // 活跃度规则
        tv_active_help.setOnClickListener {
            ActivityUtils.getInstance().jumpH5Activity("活跃度规则", ConstantsUrl.URL_PROTOCOL + "活跃度规则")
        }
        // 排名
        tv_ranking.setOnClickListener {
            var ranking = it.tag as Int
            if (ranking == 0) {
                // 未开店
                // 是否实名
                if (!CommonUtils.checkAuth()) {
                    ToastUtils.showToast("您还未实名, 请先实名认证吧")
                    ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity::class.java)
                    return@setOnClickListener
                }
                // 是否开店
                if (CommonUtils.checkOpenShop()) {
                    jumpRanking()// 已开店，前往排行榜
                    return@setOnClickListener
                }

                if (SharedPreferencesUtils.get(Constants.MINE_SHOP_APPLY_LOADING, false) as Boolean) {
                    ToastUtils.showToast("店铺审核中")
                    return@setOnClickListener
                }
                ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity::class.java)
            } else {
                // 排行榜
                jumpRanking()
            }

        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var sectionTaskBean = mList[position]
            when (view.id) {
                R.id.btn_activate -> {
                    // 激活任务……卖满，开启任务
                    if (sectionTaskBean.mSellNum >= sectionTaskBean.sellNum) {
                        // 卖满，开启任务
                        upgradeTask(sectionTaskBean.memberTaskNo)
                    } else {
                        // 激活任务
                        NetComment.getMemberInfo {
                            if (it.seedCount < sectionTaskBean.seedNum) {
                                // 种子不足
//                                SimpleDialog.showConfirmDialog(this, "提示", "开启该任务区需要${sectionTaskBean.seedNum}颗发财种子，\n可用发财种子不足！",
//                                        "去抢购", "知道了", {
//                                    ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
//                                }, null, false)

                                XPopup.Builder(this)
                                        .maxWidth((DeviceUtils.getWindowWidth(this) * 0.75).toInt())
                                        .autoDismiss(true)
                                        .asConfirm("提示", "开启该任务区需要${sectionTaskBean.seedNum}颗发财种子，\n可用发财种子不足！",
                                                "去抢购", "知道了", null, {
                                            ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
                                        }, false)
                                        .bindLayout(R.layout.xpopup_custom_layout)
                                        .show()

                            } else {
                                when (sectionTaskBean.merchantTaskType) {
                                    "rec" -> {
                                        // 推荐任务
                                        activateRecTask(sectionTaskBean.merchantTaskNo)
                                    }
                                    "merchants" -> {
                                        // 商家任务
                                        activateMerchantsTask(sectionTaskBean.merchantTaskNo)
                                    }
                                }

                            }
                        }

                    }
                }

                R.id.btn_buy -> {
                    // 买
                    ActivityUtils.getInstance().jumpActivity(CompanySaleClassifyActivity::class.java, Bundle().apply {
                        putInt("taskType", 2)
                        putString("taskItemNo", sectionTaskBean.memberTaskNo)
                        putString("blockNo", sectionTaskBean.blockNo)
                        putString("title", mBlockBean?.blockName)
                    })
                }

                R.id.btn_sell -> {
                    // 卖
                    if (sectionTaskBean.distributeStatus == 1 && sectionTaskBean.distributeNum > 0) {
                        // 去抢单
                        grabOrder(sectionTaskBean.merchantTaskNo, sectionTaskBean.memberTaskNo)
                    } else {
                        // 好友助力
                        CommonUtils.shareAppUM(this, null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "")
                    }
                }
            }
        }

        layout_task_help.setOnClickListener {
            it.setTag(R.id.click_filter_key, true)
            mDetailIsShow = !mDetailIsShow
            iv_task_close.rotation = if (mDetailIsShow) 0f else 180f
            tv_task_desc.visibility = if (mDetailIsShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 前往排行榜
     */
    private fun jumpRanking() {
        var isCanJump = false
        mList.forEach {
            if(it.memberTaskNo.isNotEmpty()
//                    && it.merchantTaskCode != "rec"
            ) {
                isCanJump = true
            }
        }
        if(!isCanJump){
            ToastUtils.showToast("激活任务可查看排行榜")
            return
        }
        mBlockBean?.let {
            ActivityUtils.getInstance().jumpActivity(ActiveRankingChildActivity::class.java, Bundle().apply {
                putString("blockNo", it.blockNo)
                putString("blockName", it.blockName)
            })
        }
    }

    override fun loadData() {

        getMatchOrderAds()
    }

    override fun onResume() {
        super.onResume()
        smartRefresh.autoRefresh()
        limitAds?.startScroll()
    }

    override fun onStop() {
        super.onStop()
        limitAds?.stopScroll()
    }

    private fun getData() {
        mBlockBean?.let {
            mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_TASK, CommonUtils.createParams().apply {
                put("blockNo", it.blockNo)
                put("start", "0")
                put("limit", "100")
            }, object : CallbackCommon {
                override fun onFailure(call: Call?, e: IOException?) {
                    ToastUtils.showToast(R.string.net_error)
                    smartRefresh.finishRefresh()
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                    smartRefresh.finishRefresh()
                    jsonObject?.let {
                        if (it.get("status").asInt != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                            return
                        }
                        var dataObj = it.get("data").asJsonObject
                        var jsonArray = dataObj.get("rows").asJsonArray
                        var list = GsonUtils.getGson().fromJson<List<SectionTaskBean>>(jsonArray.toString(), object : TypeToken<List<SectionTaskBean>>() {}.type)
                        if (mList.isNotEmpty()) mList.clear()
                        mList.addAll(list)
                        mAdapter.refreshData()

                        var otherDataObj = dataObj.get("otherData").asJsonObject
                        tv_earnings.text = "平台已匹配" + (otherDataObj.get("orderMatchingNum").asInt + 10000) + "单"
                        // 总收益
                        tv_income_sum.text = "总收益\n${UnitUtil.getMoney(GsonUtils.parseJson(otherDataObj, "saleTotalAmt", "0"))}"
                        // 预计收益
                        tv_income.text = "预计收益\n${UnitUtil.getMoney(GsonUtils.parseJson(otherDataObj, "shopsProfit", "0"))}"

                        // 当前排行
                        val ranking = UnitUtil.getInt(GsonUtils.parseJson(otherDataObj, "rowno", "0"))
                        tv_ranking.tag = ranking
                        if (ranking == 0) {
                            if (CommonUtils.checkOpenShop()) {
                                tv_ranking.text = "查看排名"
                            } else {
                                tv_ranking.text = "开店提升排名"
                            }
                        } else {
                            //  mTvRanking.setText("第" + ranking + "名");
                            tv_ranking.text = "查看排名"
                        }
                    }
                }
            })
        }
    }

    /**
     * 激活商家模块任务
     */
    private fun activateMerchantsTask(merchantTaskNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_ACTIVATE_TASK, CommonUtils.createParams().apply {
            put("merchantTaskNo", merchantTaskNo)
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
     * 激活推荐模块任务
     */
    private fun activateRecTask(merchantTaskNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_ACTIVATE_REC_TASK, CommonUtils.createParams().apply {
            put("merchantTaskNo", merchantTaskNo)
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
     * 升级任务
     */
    private fun upgradeTask(memberTaskNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_UPGRADE_TASK, CommonUtils.createParams().apply {
            put("memberTaskNo", memberTaskNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "开启失败，请稍后重试！"))
                        return
                    }

                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "开启成功"))
                    smartRefresh.autoRefresh()
                }
            }
        })
    }

    /**
     * 抢单
     * @param merchantTaskNo 商家任务编号
     * @param merchantTaskNo 用户任务编号
     */
    private fun grabOrder(merchantTaskNo: String, memberTaskNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_3_0_TASK_GRAB_ORDER, CommonUtils.createParams().apply {
            put("merchantTaskNo", merchantTaskNo)
            put("memberTaskNo", memberTaskNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast(R.string.net_error)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "抢单失败，请稍后重试！"))
                        return
                    }

                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "抢单成功！"))
                    smartRefresh.autoRefresh()
                }
            }
        })
    }

    /**
     * 匹配订单广告
     */
    private fun getMatchOrderAds() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_3_0_ORDER_MATCH, CommonUtils.createParams().apply {
            put("blockNo", mBlockBean?.blockNo)
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
                val tv = TextView(this@SectionTaskActivity)
                tv.setPadding(0, DeviceUtils.dp2px(this@SectionTaskActivity, 8f), 0, DeviceUtils.dp2px(this@SectionTaskActivity, 8f))
                tv.text = list[position]
                tv.gravity = Gravity.CENTER
                tv.setTextColor(ContextCompat.getColor(this@SectionTaskActivity, R.color.c_333333))
                tv.textSize = 14f
                return tv
            }

            override fun getCount(): Int = list.size
        })
        limitAds.startScroll()
    }
}
