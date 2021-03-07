package com.yimeng.haidou.task_3_0

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import com.yimeng.base.BaseActivity
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
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_block_task_fruit2.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/23 2:47 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 区块下的任务列表
 * </pre>
 */
class BlockTaskFruitActivity2 : BaseActivity() {

    /**
     * 1. 我的活跃果
     * 2. 抢购活跃果
     */
    private var mType = 1
    private var mPage = 1
    private var mBlockNo: String = ""
    private var mBlockName: String = ""
    // 活跃果市场价
    private var mFruitPrice = 0.0

    private var mList = mutableListOf<SellFruitBean>()
    private lateinit var mAdapter: SellFruitAdapter
    private val mOkHttpCommon = OkHttpCommon()


    override fun setLayoutResId(): Int = R.layout.activity_block_task_fruit2

    override fun init() {
        intent.extras?.let {
            mType = it.getInt("type", 1)
            mBlockNo = it.getString("blockNo", "")
            mBlockName = it.getString("blockName", "")
        }

        toolBar.title = "${mBlockName}活跃果"

        mAdapter = SellFruitAdapter(mList, mType)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@BlockTaskFruitActivity2)
            adapter = mAdapter
        }

    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshLoadMoreListener(object: OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getData()
            }
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var fruitBean = mList[position]
            var isMIne = CommonUtils.getMember().memberNo == fruitBean.tradeFrom

            when (mType) {
                1 -> { // 我的活跃果
                    XPopup.Builder(this)
                            .setPopupCallback(object : SimpleCallback() {
                                override fun onDismiss() {
                                    super.onDismiss()
                                    smartRefresh.autoRefresh()
                                }
                            })
                            .asCustom(SeedSellDialog(this, fruitBean.fruitCount, 2, fruitBean))
                            .show()
                }
                2 -> { // 抢购活跃果
                    if (isMIne) {
                        // 收回
                        SimpleDialog.showConfirmDialog(this, "提示", "确定收回？", null, {
                            recycleSeed(fruitBean.fruitTradeNo)
                        })
                    } else {
                        //抢购
                        payFruit(fruitBean.fruitTradeNo, fruitBean.tradeFromAmt)
                    }
                }
            }
        }
    }

    override fun loadData() {

        smartRefresh.autoRefresh()
        getFruitPrice()

    }

    private fun getData() {

        if (mType == 1) {
            smartRefresh.setEnableLoadMore(false)
            mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_TASK, CommonUtils.createParams().apply {
                put("blockNo", mBlockNo)
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


                        if (mList.isNotEmpty()) mList.clear()
                        var list = GsonUtils.getGson().fromJson<MutableList<SellFruitBean>>(jsonArray.toString(), object : TypeToken<List<SellFruitBean>>() {}.type)
                        setData(list)
                    }
                }
            })
        } else {
            mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_FRUIT_SHOP, CommonUtils.createParams().apply {
                CommonUtils.addPageParams(this, mPage)
                put("blockNo", mBlockNo)
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


    }

    private fun setData(tankingList: MutableList<SellFruitBean>) {
        tankingList.forEach {
            if (it.merchantTaskCode == "rec") {
                mList.add(it)
            }
        }

        mAdapter.notifyDataSetChanged()

    }

    /**
     * 获取活跃果市场价
     */
    private fun getFruitPrice() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_FRUIT_PRICE, CommonUtils.createParams().apply {
            put("blockNo", mBlockNo)
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

                    // 分
                    var price = UnitUtil.getInt(it.get("data").asString)
                    mFruitPrice = price / 100.0
                    tv_seed_price.text = "当前市价:￥$mFruitPrice/颗"
                }
            }
        })
    }

    /**
     * 收回活跃果
     */
    private fun recycleSeed(tradeNo: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_RECYCLE_FRUIT, CommonUtils.createParams().apply {
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

    /**
     * 抢购
     */
    private fun payFruit(tradeNo: String, sumPrice: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_PAY_FRUIT, CommonUtils.createParams().apply {
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
                    XPopup.Builder(this@BlockTaskFruitActivity2)
                            .dismissOnTouchOutside(false)
                            .asCustom(CommonAllPayDialog(this@BlockTaskFruitActivity2, "抢购活跃果", sumPrice, rewardNo, EventTags.PAY_FRUIT_RESULT, PayType.WECHAT, PayType.ALIPAY))
                            .show()
                }
            }
        })
    }
}
