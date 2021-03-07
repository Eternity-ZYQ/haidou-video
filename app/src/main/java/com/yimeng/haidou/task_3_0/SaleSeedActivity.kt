package com.yimeng.haidou.task_3_0

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.haidou.task_3_0.adapter.SellSeedAdapter
import com.yimeng.dialog.CommonAllPayDialog
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.SellSeedBean
import com.yimeng.enums.PayType
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_sale_seed.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/8 3:42 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售种子
 * </pre>
 */
class SaleSeedActivity : BaseActivity() {

    private var mPage = 1
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SellSeedBean>()
    private lateinit var mAdapter: SellSeedAdapter
    private var mMemberNo = ""
    // 种子市场价
    private var mSeedPrice = 0.0

    override fun setLayoutResId(): Int = R.layout.activity_sale_seed

    override fun init() {

        intent.extras?.let {
            mMemberNo = it.getString("memberNo")
        }

        mAdapter = SellSeedAdapter(mList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SaleSeedActivity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)


        if (mMemberNo.isNotEmpty()) {
            toolBar.setRightTextVisible(View.GONE)
        }
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {

            override fun onRightClick() {
                ActivityUtils.getInstance().jumpActivity(SeedChartActivity::class.java)
            }
        })

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getData()
                getSeedPrice()
            }
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var seedBean = mList[position]
            var isMIne = CommonUtils.getMember().memberNo == seedBean.tradeFrom
            if (isMIne) {
                // 收回
                SimpleDialog.showConfirmDialog(this, "提示", "确定收回？", null, {
                    recycleSeed(seedBean.seedTradeNo)
                })
            } else {
                //抢购
                paySeed(seedBean.seedTradeNo, seedBean.tradeFromAmt.toString())
            }
        }
    }

    /**
     * 抢购
     */
    private fun paySeed(tradeNo: String, sumPrice: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_PAY_SEED, CommonUtils.createParams().apply {
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
                    XPopup.Builder(this@SaleSeedActivity)
                            .dismissOnTouchOutside(false)
                            .asCustom(CommonAllPayDialog(this@SaleSeedActivity, "抢购发财种子", sumPrice, rewardNo,EventTags.PAY_SEED_RESULT, PayType.WECHAT, PayType.ALIPAY))
                            .show()
                }
            }
        })
    }

    /**
     * 收回种子
     */
    private fun recycleSeed(tradeNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_RECYCLE_SEED, CommonUtils.createParams().apply {
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
     * 获取种子市场价
     */
    private fun getSeedPrice() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_SEED_PRICE, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    // 分
                    var price = UnitUtil.getInt(it.get("data").asString)
                    mSeedPrice = price / 100.0
                    tv_seed_price.text = "当前市价:￥$mSeedPrice/颗"

                }
            }
        })
    }

    /**
     * 获取在售种子
     */
    private fun getData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SEED_SHOP, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
            if (mMemberNo.isNotEmpty())
                put("tradeFrom", mMemberNo)
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
                    var list = GsonUtils.getGson().fromJson<List<SellSeedBean>>(jsonArray.toString(), object : TypeToken<List<SellSeedBean>>() {}.type)
                    if (list.size < Constants.MAX_LIMIT) smartRefresh.finishLoadMoreWithNoMoreData()
                    if (mPage == 1 && mList.isNotEmpty()) mList.clear()
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    @Subscriber(tag = EventTags.PAY_SEED_RESULT)
    fun onPayResult(flag: Boolean) {
        if (flag) {
            ToastUtils.showToast("抢购成功")
            smartRefresh.autoRefresh()
        } else {
            ToastUtils.showToast("抢购失败")
        }
    }
}
