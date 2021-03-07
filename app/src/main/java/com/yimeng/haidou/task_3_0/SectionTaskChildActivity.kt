package com.yimeng.haidou.task_3_0

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.haidou.goodsClassify.CompanySaleClassifyActivity
import com.yimeng.haidou.task_3_0.adapter.SectionTaskChildItemAdapter
import com.yimeng.entity.SectionTaskBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_section_task_child.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/9 3:18 PM.
 *  Email  : zhihuieml@163.com
 *  Desc   : 团队任务 >> 区块 >> 模块 >> 子任务
 * </pre>
 */
class SectionTaskChildActivity : BaseActivity() {

    // 商家任务编号
    private var mMerchantTaskNo = ""
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SectionTaskBean>()
    private val mAdapter: SectionTaskChildItemAdapter by lazy { SectionTaskChildItemAdapter(mList) }

    override fun setLayoutResId(): Int = R.layout.activity_section_task_child

    override fun init() {
        intent.extras?.let {
            mMerchantTaskNo = it.getString("merchantTaskNo")
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SectionTaskChildActivity)
            adapter = mAdapter
        }


    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshListener { getData() }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var sectionTaskBean = mList[position]
            when (view.id) {
                R.id.btn_activate -> {
                    if(sectionTaskBean.mSellNum != sectionTaskBean.sellNum && position !=0) {
                        // 激活任务
                        var bean = mList[position - 1]
                        if(bean.mStatus == "progress") {
                            ToastUtils.showToast("请先激活上一个任务")
                            return@setOnItemChildClickListener
                        }
                    }

                    if(sectionTaskBean.mSellNum >= sectionTaskBean.sellNum) {
                        // 卖满，开启任务
                        upgradeTask(sectionTaskBean.memberTaskNo)
                    }else {
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
                                // 激活任务
                                activateTask(sectionTaskBean.memberTaskNo)
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
                    })
                }

                R.id.btn_sell -> {
                    // 卖
//                    if (sectionTaskBean.distributeStatus == 1 && sectionTaskBean.distributeNum > 0) {
//                        // 去抢单
//                        grabOrder(sectionTaskBean.merchantTaskNo, sectionTaskBean.memberTaskNo)
//
//                    } else {
//                        // 好友助力
//                        CommonUtils.shareAppUM(this, null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "")
//                    }

                    // 好友助力
                    CommonUtils.shareAppUM(this, null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "")
                }
            }
        }
    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }

    private fun getData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_QUERY_REC_TASK, CommonUtils.createParams().apply {
            put("merchantTaskNo", mMerchantTaskNo)
            put("start", "0")
            put("limit", "100")
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                smartRefresh.finishRefresh()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var jsonArray = it.get("data").asJsonObject.get("rows").asJsonArray
                    if(jsonArray.size() == 0) return
                    var list = GsonUtils.getGson().fromJson<List<SectionTaskBean>>(jsonArray.toString(), object : TypeToken<List<SectionTaskBean>>() {}.type)
                    if (mList.isNotEmpty()) mList.clear()
                    mList.addAll(list)
                    mAdapter.refreshData()
                }

            }
        })
    }

    /**
     * 激活推荐任务
     */
    private fun activateTask(memberTaskNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_ACTIVATE_REC_CHILD_TASK, CommonUtils.createParams().apply {
            put("memberTaskNo", memberTaskNo)
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
    private fun upgradeTask(memberTaskNo :String){
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
            put("merchantTaskNo",merchantTaskNo)
            put("memberTaskNo",memberTaskNo)
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
}
