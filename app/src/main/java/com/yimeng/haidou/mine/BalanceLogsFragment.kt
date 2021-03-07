package com.yimeng.haidou.mine

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.ModelWithdrawDeposit
import com.yimeng.enums.BalanceLogType
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_balance_logs.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/27 11:53 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class BalanceLogsFragment : BaseFragment() {

    private var mType: String = ""
    private var isShop: Boolean = false
    private var mPage: Int = 1

    private var mList: MutableList<ModelWithdrawDeposit> = mutableListOf()
    private val mAdapter: BaseQuickAdapter<ModelWithdrawDeposit, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ModelWithdrawDeposit, BaseViewHolder>(
                R.layout.item_lv_withdraw_deposit_records, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: ModelWithdrawDeposit?) {
                if (mType == BalanceLogType.withdraw)
                    item?.let {

                        val title = "提现" + UnitUtil.getMoney(it.applyAmt)

                        helper?.apply {
                            setText(R.id.titleTV, title)
                            setText(R.id.dateTV, DateUtil.getAssignDate(UnitUtil.getLong(it.createTime), 3))
                            setText(R.id.moneyTV, "-" + UnitUtil.getMoney(it.applyAmt))
                            setVisible(R.id.statusTV, !TextUtils.isEmpty(it.withdrawState))
                            setText(R.id.statusTV, it.withdrawState)
                            setTextColor(R.id.moneyTV, Color.parseColor("#FE3C3C"))
                            setGone(R.id.reject_iv, it.isReject == 1)
                            addOnClickListener(R.id.reject_iv)
                        }
                    }
                else
                    item?.let {
                        var limit = if (it.accountType == "zhichu") "-" else "+"

                        helper?.apply {
                            setText(R.id.titleTV, it.accountSource)
                            setText(R.id.dateTV, DateUtil.getAssignDate(UnitUtil.getLong(it.createTime.trim()), 3))
                            setText(R.id.moneyTV, limit + UnitUtil.getMoney(it.amt))
                            setTextColor(R.id.moneyTV, if (it.accountType == "zhichu")
                                ContextCompat.getColor(mContext, R.color.c_333333) else ContextCompat.getColor(mContext, R.color.c_money))

                            if (isShop && UnitUtil.getInt(item.originalPrice) > 0) {
                                setVisible(R.id.iv_tip, true)
                                changeView(getView(R.id.iv_tip), getView(R.id.ll_content), item)

                                setText(R.id.tv_product_original_price, UnitUtil.getMoney(item.originalPrice))
                                setText(R.id.tv_product_vip_price, UnitUtil.getMoney(item.vipPrice))
                                setText(R.id.tv_platform_amt, UnitUtil.getMoney(item.platformAmt))

                                getView<LinearLayout>(R.id.ll_title).setOnClickListener {
                                    item.isExpand = !item.isExpand
                                    changeView(getView(R.id.iv_tip), getView(R.id.ll_content), item)
                                    notifyItemChanged(helper.adapterPosition)
                                }
                            }else{
                                setVisible(R.id.iv_tip, false)
                            }

                        }
                    }
            }

            fun changeView(ivTip: ImageView, llConent: LinearLayout, item: ModelWithdrawDeposit) {
                if (item.isExpand) {
                    // 当前展开
                    ivTip.rotation = -90f
                    llConent.visibility = View.VISIBLE
                } else {
                    // 当前折叠
                    ivTip.rotation = 90f
                    llConent.visibility = View.GONE
                }
            }

        }
    }

    private val mOkHttpCommon: OkHttpCommon by lazy {
        OkHttpCommon()
    }

    override fun setLayoutResId(): Int = R.layout.fragment_balance_logs


    override fun init() {

        var bundle = arguments
        bundle?.let {
            mType = bundle.getString("type")
            isShop = bundle.getBoolean("isShop", false)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
            mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
        }

    }

    override fun initListener() {


        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                loadData()
            }
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.reject_iv) {
                var modelWithdrawDeposit = mList[position]
                val reason = modelWithdrawDeposit.remark
                if (reason != null && reason.isNotEmpty()) {
                    SimpleDialog.showSimpleRemarkWithTitleDialog(activity, "驳回原因", reason)
                }
            }
        }

    }

    override fun loadData() {
        var params = CommonUtils.createParams()
        CommonUtils.addPageParams(params, mPage)
        var url = ""
        if (mType == BalanceLogType.withdraw) {
            // 提现
            url = ConstantsUrl.URL_WITHDRAW_APPLY_V_2_1_0

            if (isShop)
                params["withdrawApplyCategory"] = "baodanbi"
            else
                params["withdrawApplyCategory"] = "balance"
        } else if (mType == BalanceLogType.balance) {
            // 余额
            url = ConstantsUrl.URL_PAY_RECORDS

            if (isShop)
                params["amtType"] = "shop"
            else
                params["amtType"] = "zhannei"
        }


        mOkHttpCommon.postLoadData(activity, url, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)
                ToastUtils.showToast("查询失败")
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)

                if (jsonObject?.get("status")?.asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败"))
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