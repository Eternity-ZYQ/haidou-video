package com.yimeng.haidou.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.ModelWithdrawDeposit
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_income_logs.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/10 12:00 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 收益账户记录
 * </pre>
 */
class IncomeLogsActivity : BaseActivity() {

    private var mPage: Int = 1

    private var mList: MutableList<ModelWithdrawDeposit> = mutableListOf()
    private val mAdapter: BaseQuickAdapter<ModelWithdrawDeposit, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ModelWithdrawDeposit, BaseViewHolder>(
                R.layout.item_lv_withdraw_deposit_records, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: ModelWithdrawDeposit?) {

                item?.let {
                    var limit = if (it.accountType == "zhichu") "-" else "+"

                    helper?.apply {
                        setText(R.id.titleTV, it.accountSource)
                        setText(R.id.dateTV, DateUtil.getAssignDate(UnitUtil.getLong(it.createTime.trim()), 3))
                        setText(R.id.moneyTV, limit + UnitUtil.getMoney(it.amt))
                        setTextColor(R.id.moneyTV, if (it.accountType == "zhichu")
                            ContextCompat.getColor(mContext, R.color.c_333333) else ContextCompat.getColor(mContext, R.color.c_money))
                    }
                }

            }
        }
    }

    private val mOkHttpCommon: OkHttpCommon by lazy {
        OkHttpCommon()
    }

    override fun setLayoutResId(): Int = R.layout.activity_income_logs

    override fun init() {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@IncomeLogsActivity)
            adapter = mAdapter
            mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
        }
    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                loadData()
            }
        })
    }

    override fun loadData() {
        var params = CommonUtils.createParams()
        CommonUtils.addPageParams(params, mPage)
        params["amtType"] = "zhanneiFreeze"

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_PAY_RECORDS, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)

                if (jsonObject?.get("status")?.asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                    return
                }

                var array = jsonObject.get("data").asJsonObject.get("rows").asJsonArray.toString()

                val list = GsonUtils.getGson().fromJson<List<ModelWithdrawDeposit>>(array, object : TypeToken<List<ModelWithdrawDeposit>>() {}.type)

                if (list.size < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData()
                }
                if (mPage == 1 && mList.isNotEmpty()) {
                    mList.clear()
                }
                mPage++
                mList.addAll(list)
                mAdapter.notifyDataSetChanged()
            }
        })
    }
}
