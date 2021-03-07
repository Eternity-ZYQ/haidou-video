package com.yimeng.haidou.task

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yimeng.base.BaseFragment
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.haidou.task.adapter.ActiveRankingAdapter
import com.yimeng.dialog.SendOrderDialog
import com.yimeng.entity.ActiveRankingBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.adapter_active_ranking_item.*
import kotlinx.android.synthetic.main.fragment_active_ranking_top.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/26 11:45 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 排行榜
 * </pre>
 */
class ActiveRankingTopFragment : BaseFragment() {

    private var mList: MutableList<ActiveRankingBean> = mutableListOf()
    private val mAdapter: ActiveRankingAdapter by lazy {
        ActiveRankingAdapter(R.layout.adapter_active_ranking_item, mList, mType == "fs", CommonUtils.getMember())
    }

    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }
    private var mType = ""

    companion object {
        fun getInstance(type: String): ActiveRankingTopFragment {
            return ActiveRankingTopFragment().apply {
                var bundle = Bundle()
                bundle.putString("type", type)
                arguments = bundle
            }
        }
    }


    override fun setLayoutResId(): Int = R.layout.fragment_active_ranking_top

    override fun init() {

        arguments?.apply { mType = getString("type") }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }

        iv_send_order.visibility = View.GONE
        tv_member_shop.visibility = View.GONE
        tv_member_order.visibility = View.GONE
        tv_member_mobile.visibility = View.GONE

    }

    override fun initListener() {

        smartRefresh.setOnRefreshListener {
            loadData()
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            var rankingBean = mList[position]
            XPopup.Builder(activity)
                    .asCustom(context?.let { SendOrderDialog(it, mType, rankingBean.memberNo) })
                    .show()
        }
    }

    override fun loadData() {

        var params = CommonUtils.createParams()
        if (mType.isNotEmpty()) {
            params["type"] = mType
        }
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_ACTIVE_RANKING_LIST, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                smartRefresh.finishRefresh()
                ToastUtils.showToast("获取排行数据失败！")
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                if (jsonObject?.get("status")?.asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取排行数据失败！"))
                    return
                }

                var dataObj = jsonObject.get("data").asJsonObject
                var arrStr = dataObj.get("rows").asJsonArray.toString()

                var list = GsonUtils.getGson().fromJson<List<ActiveRankingBean>>(arrStr,
                        object : TypeToken<List<ActiveRankingBean>>() {}.type)
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
        })
    }
}