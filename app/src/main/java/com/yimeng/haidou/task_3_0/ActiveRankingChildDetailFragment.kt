package com.yimeng.haidou.task_3_0

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.yimeng.base.BaseFragment
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.haidou.task_3_0.adapter.ActiveRanking3_0Adapter
import com.yimeng.entity.ActiveRankingBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_active_ranking3_0.*
import kotlinx.android.synthetic.main.adapter_active_ranking_item.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/10 2:03 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class ActiveRankingChildDetailFragment : BaseFragment() {

    private var mList = mutableListOf<ActiveRankingBean>()
    private lateinit var mAdapter: ActiveRanking3_0Adapter
    private val mOkHttpCommon = OkHttpCommon()
    private var mMerchantTaskNo = ""

    companion object{
        fun getInstance(merchantTaskNo:String):ActiveRankingChildDetailFragment = ActiveRankingChildDetailFragment().apply {
            var bundle = Bundle()
            bundle.putString("merchantTaskNo",merchantTaskNo)
            arguments = bundle
        }
    }

    override fun setLayoutResId(): Int = R.layout.fragment_active_ranking_child_detail

    override fun init() {

        arguments?.let {
            mMerchantTaskNo = it.getString("merchantTaskNo")
        }

        mAdapter = ActiveRanking3_0Adapter(mList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }

    override fun initListener() {

        smartRefresh.setOnRefreshListener { getData() }

    }

    private fun getData() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_3_0_BLOCK_ACTIVE_RANKING, CommonUtils.createParams().apply {
            put("merchantTaskNo", mMerchantTaskNo)
        }, object :CallbackCommon{
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                smartRefresh.finishRefresh()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.let {
                    if(it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var dataObj = it.get("data").asJsonObject
                    var arrStr = dataObj.get("rows").asJsonArray.toString()
                    var list = GsonUtils.getGson().fromJson<List<ActiveRankingBean>>(arrStr, object :TypeToken<List<ActiveRankingBean>>(){}.type)

                    if (mList.isNotEmpty()) mList.clear()
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()

                    // 自己的排名
                    var otherDataStr = dataObj.get("otherData").asJsonObject.toString()
                    var mineActiveRanking = GsonUtils.getGson().fromJson(otherDataStr, ActiveRankingBean::class.java)
                    mineActiveRanking?.apply {
                        var split = info.split(",")
                        tv_active.text = split[0]
                        CommonUtils.showImage(civ_user_head, split[2])
                        tv_member_name.text = split[1]
                        tv_ranking?.apply {
                            when (UnitUtil.getInt(rowno)) {
                                0 -> {
                                    text = "-"
                                    setBackgroundResource(android.R.color.transparent)
                                }
                                1 -> {
                                    text = ""
                                    setBackgroundResource(R.mipmap.ico_ranking_1)
                                }
                                2 -> {
                                    text = ""
                                    setBackgroundResource(R.mipmap.ico_ranking_2)
                                }
                                3 -> {
                                    text = ""
                                    setBackgroundResource(R.mipmap.ico_ranking_3)
                                }
                                else -> {
                                    text = rowno
                                    setBackgroundResource(android.R.color.transparent)
                                }
                            }
                        }
                    }

                }
            }
        })
    }

    override fun loadData() {
        smartRefresh.autoRefresh()

    }
}