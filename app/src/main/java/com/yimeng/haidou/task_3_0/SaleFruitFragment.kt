package com.yimeng.haidou.task_3_0

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.haidou.task_3_0.adapter.SellFruitAdapter
import com.yimeng.dialog.CommonAllPayDialog
import com.yimeng.dialog.SeedSellDialog
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.SellFruitBean
import com.yimeng.enums.PayType
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_sale_fruit.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/8 3:42 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售活跃果/我的h活跃果
 * </pre>
 */
class SaleFruitFragment : BaseFragment() {
    /**
     * 1. 我的活跃果
     * 2. 抢购活跃果
     */
    private var mType = 1
    private var mPage = 1
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SellFruitBean>()
    private lateinit var mAdapter: SellFruitAdapter
    private var mBlockNo = ""
    private var mMerchantTaskNo = ""

    companion object {
        fun getInstance(blockNo: String, merchantTaskNo: String, type: Int): SaleFruitFragment {
            return SaleFruitFragment().apply {
                arguments = Bundle().apply {
                    putString("blockNo", blockNo)
                    putString("merchantTaskNo", merchantTaskNo)
                    putInt("type", type)
                }
            }
        }
    }

    override fun setLayoutResId(): Int = R.layout.fragment_sale_fruit


    override fun init() {

        arguments?.let {
            mType = it.getInt("type", 1)
            mBlockNo = it.getString("blockNo")
            mMerchantTaskNo = it.getString("merchantTaskNo")
        }

        mAdapter = SellFruitAdapter(mList, mType)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)


    }

    override fun initListener() {

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                if (mType == 1) { // 我的活跃果
                    getMineFruitData()
                } else {
                    getFruitData()
                }
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                if (mType == 1) { // 我的活跃果
                    getMineFruitData()
                } else {
                    getFruitData()
                }
            }
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var fruitBean = mList[position]
            var isMIne = CommonUtils.getMember().memberNo == fruitBean.tradeFrom

            when (mType) {
                1 -> { // 我的活跃果
                    XPopup.Builder(activity)
                            .setPopupCallback(object : SimpleCallback() {
                                override fun onDismiss() {
                                    super.onDismiss()
                                    smartRefresh.autoRefresh()
                                }
                            })
                            .asCustom(SeedSellDialog(activity!!, fruitBean.fruitCount, 2, fruitBean))
                            .show()
                }
                2 -> { // 抢购活跃果
                    if (isMIne) {
                        // 收回
                        SimpleDialog.showConfirmDialog(activity, "提示", "确定收回？", null, {
                            recycleSeed(fruitBean.fruitTradeNo)
                        })
                    } else {
                        //抢购
                        payFruit(fruitBean.fruitTradeNo, fruitBean.tradeFromAmt)
                    }
                }
                else -> { // 我的活跃果挂卖
                        SimpleDialog.showConfirmDialog(activity, "提示", "确定收回？", null, {
                            recycleSeed(fruitBean.fruitTradeNo)
                        })
                }
            }
        }
    }

    /**
     * 我的活跃果数据
     */
    private fun getMineFruitData() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_TEAM_BLOCK_QUERY_REC_TASK, CommonUtils.createParams().apply {
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
                smartRefresh.finishLoadMoreWithNoMoreData()
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var jsonArray = it.get("data").asJsonObject.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<SellFruitBean>>(jsonArray.toString(), object : TypeToken<List<SellFruitBean>>() {}.type)
                    if (mList.isNotEmpty()) mList.clear()
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }

            }
        })

    }

    /**
     * 抢购
     */
    private fun payFruit(tradeNo: String, sumPrice: String) {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_PAY_FRUIT, CommonUtils.createParams().apply {
            put("tradeNo", tradeNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    var rewardNo = it.get("data").asJsonObject.get("rewardNo").asString
                    XPopup.Builder(activity)
                            .dismissOnTouchOutside(false)
                            .asCustom(CommonAllPayDialog(context!!, "抢购活跃果", sumPrice, rewardNo, EventTags.PAY_FRUIT_RESULT, PayType.WECHAT, PayType.ALIPAY))
                            .show()
                }
            }
        })
    }

    /**
     * 收回活跃果
     */
    private fun recycleSeed(tradeNo: String) {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_RECYCLE_FRUIT, CommonUtils.createParams().apply {
            put("tradeNo", tradeNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "收回成功！"))
                    smartRefresh.autoRefresh()
                }
            }
        })
    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }


    /**
     * 获取在售活跃果
     */
    private fun getFruitData() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_FRUIT_SHOP, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
            if (mType == 3)
                put("tradeFrom", CommonUtils.getMember().memberNo)
            put("blockNo", mBlockNo)
            put("merchantTaskNo", mMerchantTaskNo)
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
                    var list = GsonUtils.getGson().fromJson<List<SellFruitBean>>(jsonArray.toString(), object : TypeToken<List<SellFruitBean>>() {}.type)
                    if (list.size < Constants.MAX_LIMIT) smartRefresh.finishLoadMoreWithNoMoreData()
                    if (mPage == 1 && mList.isNotEmpty()) mList.clear()
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    @Subscriber(tag = EventTags.PAY_FRUIT_RESULT)
    fun onPayResult(flag: Boolean) {
        if (flag) {
            ToastUtils.showToast("抢购成功")
            smartRefresh.autoRefresh()
        } else {
            ToastUtils.showToast("抢购失败")
        }
    }
}
