package com.yimeng.haidou.task_3_0

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.haidou.task_3_0.adapter.SellFruitAdapter
import com.yimeng.dialog.FruitFilterDialog
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.FilterFruitBean
import com.yimeng.entity.SectionTaskBean
import com.yimeng.entity.SellFruitBean
import com.yimeng.entity.TaskBlockBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_my_sale_fruit.*
import kotlinx.android.synthetic.main.activity_my_sale_fruit.smartRefresh
import kotlinx.android.synthetic.main.fragment_sale_fruit.recyclerView
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/24 4:38 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的挂卖-活跃果
 * </pre>
 */
class MySaleFruitActivity : BaseActivity() {


    private var mPage = 1
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<SellFruitBean>()
    private lateinit var mAdapter: SellFruitAdapter
    private var mBlockList = mutableListOf<TaskBlockBean>()
    private var mRecTaskList = mutableListOf<SectionTaskBean>()
    private var mBlockNo: String = ""
    private var mMerchantTaskNo: String = ""

    override fun setLayoutResId(): Int = R.layout.activity_my_sale_fruit

    override fun init() {

        mAdapter = SellFruitAdapter(mList, 3)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MySaleFruitActivity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getFruitData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                if(mList.isNotEmpty()) {
                    mList.clear()
                    mAdapter.notifyDataSetChanged()
                }
                getFruitData()
            }
        })

        mAdapter.setOnItemChildClickListener { _, _, position ->
            SimpleDialog.showConfirmDialog(this, "提示", "确定收回？", null, {
                recycleSeed(mList[position].fruitTradeNo)
            })
        }

        // 专区筛选
        ll_filter_block.setOnClickListener {
            if (mBlockList.isEmpty()) {
                getBlockData()
            } else {
                showFilterDialog(it, iv_block, 1)
            }
        }

        // 推荐任务筛选
        ll_filter_task.setOnClickListener {
            if(mBlockNo.isEmpty()){
                ToastUtils.showToast("请先选择专区")
                return@setOnClickListener
            }
            if (mRecTaskList.isEmpty()) {
                getRecTaskData()
            } else {
                showFilterDialog(it, iv_task,2)
            }
        }

    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }

    /**
     * 显示筛选
     */
    private fun showFilterDialog(view: View, filterIv:View, filterType:Int) {
        var filterList = mutableListOf<FilterFruitBean>()
        var filterNo = if (filterType == 1) {
            mBlockList.forEach {
                filterList.add(FilterFruitBean(it.blockName, it.blockNo))
            }
            mBlockNo
        } else {
            mRecTaskList.forEach {
                filterList.add(FilterFruitBean(it.merchantTaskName, it.merchantTaskNo))
            }
            mMerchantTaskNo
        }

        XPopup.Builder(this)
                .atView(view)
                .autoDismiss(true)
                .setPopupCallback(object :SimpleCallback(){
                    override fun onShow() {
                        super.onShow()
                        filterIv.rotation = 270f
                    }
                    override fun onDismiss() {
                        super.onDismiss()
                        filterIv.rotation = 90f
                    }
                })
                .asCustom(FruitFilterDialog(this, filterList, filterNo, object : FruitFilterDialog.FilterCallBack {
                    override fun filter(checkNo: String) {
                        if (filterType == 1) {
                            mBlockNo = checkNo
                            mMerchantTaskNo = ""
                        } else {
                            mMerchantTaskNo = checkNo
                        }
                        smartRefresh.autoRefresh()
                    }
                }))
                .show()
    }

    /**
     * 获取在售活跃果
     */
    private fun getFruitData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_FRUIT_SHOP, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
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
     * 获取专区数据
     */
    private fun getBlockData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_TASK_BLOCK, CommonUtils.createParams().apply {
            put("start", "0")
            put("limit", Int.MAX_VALUE.toString())
            put("isOpen", "1")
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
                    var dataObj = it.get("data").asJsonObject
                    var jsonArray = dataObj.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<TaskBlockBean>>(jsonArray.toString(), object : TypeToken<List<TaskBlockBean>>() {}.type)
                    mBlockList.addAll(list)
                }
                showFilterDialog(ll_filter_block, iv_block, 1)
            }
        })
    }

    /**
     * 推荐任务
     */
    private fun getRecTaskData(){
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_TASK, CommonUtils.createParams().apply {
            put("blockNo", mBlockNo)
            put("start", "0")
            put("limit", "100")
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var dataObj = it.get("data").asJsonObject
                    var jsonArray = dataObj.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<MutableList<SectionTaskBean>>(jsonArray.toString(), object : TypeToken<List<SectionTaskBean>>() {}.type)
                    list.forEach { taskBean ->
                        if (taskBean.memberTaskNo.isNotEmpty() && taskBean.merchantTaskCode == "rec") {
                            mRecTaskList.add(taskBean)
                        }
                    }
                }
                showFilterDialog(ll_filter_task, iv_task, 2)
            }
        })
    }
}
