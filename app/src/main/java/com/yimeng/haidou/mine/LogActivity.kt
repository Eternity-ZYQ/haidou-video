package com.yimeng.haidou.mine

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.XJConfig
import com.yimeng.dialog.ChangeCaiJinDialog
import com.yimeng.entity.LogBean
import com.yimeng.retorfit.ResponseBase
import com.yimeng.retorfit.ResponseListData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.finish
import com.yimeng.utils.showToast
import com.yimeng.widget.MyToolBar
import com.yimeng.haidou.R
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_log.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 23:43.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 用户流水
 * </pre>
 */
class LogActivity : BaseActivity() {

    companion object {

        const val type_extra = "extra"// 彩蛋
        const val type_baodanbi = "baodanbi"// 活跃值
        const val type_yongjin = "yongjin"// 金蛋值
        const val type_balance = "balance"//用户余额

        fun start(type: String) {
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, LogActivity::class.java)
                    .putExtra("type", type)
            )
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_log

    private var mType = ""
    private var mPage = 1

    private val mList = mutableListOf<LogBean>()
    private val mAdapter by lazy {
        object : BaseQuickAdapter<LogBean, BaseViewHolder>(R.layout.adapter_log_item, mList) {
            override fun convert(helper: BaseViewHolder, item: LogBean?) {
                item?.run {
//                    helper.setText(R.id.tv_log_name, accountSourceCn)
//                            .setText(R.id.tv_acount, "${if (accountType == "shouru") "+" else "-"} ${amt / XJConfig.MONEY_UNOT}")
//                            .setText(R.id.tv_acount_limit, "操作后余额：${platBalance / XJConfig.MONEY_UNOT}")

                    helper.setText(R.id.tv_log_name, accountSourceCn)

                    if ("renminbi" == item.currencyType)//人民币流水
                        helper.setText(R.id.tv_acount_limit, "操作后余额：${platBalance / XJConfig.MONEY_UNIT}")
                                .setText(R.id.tv_acount, "${if (accountType == "shouru") "+" else "-"} ${amt / XJConfig.MONEY_UNIT}")
                    else
                        helper.setText(R.id.tv_acount_limit, "操作后余额：${platBalance / XJConfig.MONEY_UNOT}")
                                .setText(R.id.tv_acount, "${if (accountType == "shouru") "+" else "-"} ${amt / XJConfig.MONEY_UNOT}")

                    try {
                        helper.setText(R.id.tv_time, "${TimeUtils.millis2String(createTime)}")
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    override fun init() {

        mType = intent.getStringExtra("type")

        when (mType) {
            type_extra -> {
                toolBar.run {
                    title = "彩蛋记录"
                    setRightContent("置换彩蛋")
                }

            }
            type_baodanbi -> toolBar.title = "活跃值记录"
            type_yongjin -> toolBar.title = "金蛋值记录"
            type_balance -> toolBar.title = "余额记录"
        }

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@LogActivity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)
        mAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).setTextColor(Color.WHITE)

    }

    override fun loadData() {
        getLog()
    }


    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                if (mType == type_extra) {
                    // 置换彩蛋
                    showChangeDialog()
                }
            }
        })

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getLog()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                if (mList.isNotEmpty()) mList.clear()
                getLog()
            }

        })
    }

    private fun getLog() {
        val start = (mPage - 1) * XJConfig.LIMIT
        RetrofitHelper.toDo().getLogs(mType, start)
                .enqueue(object : Callback<ResponseListData<LogBean>> {
                    override fun onFailure(call: Call<ResponseListData<LogBean>>, t: Throwable) {
                        smartRefresh.finish(false, mPage, -1)
                    }

                    override fun onResponse(call: Call<ResponseListData<LogBean>>, response: Response<ResponseListData<LogBean>>) {

                        if (RetrofitHelper.isSuccess(response)) {
                            response.body()?.data?.rows?.run {
                                mList.addAll(this)
                                mAdapter.notifyDataSetChanged()

                                smartRefresh.finish(true, mPage, size)
                            } ?: smartRefresh.finish(false, mPage, -1)
                        } else {
                            smartRefresh.finish(false, mPage, -1)
                        }
                    }

                })
    }

    private fun showChangeDialog() {
        XPopup.Builder(this)
                .asCustom(ChangeCaiJinDialog(this) {
                    changeCaiJin(it.toInt() * 1000000L)
                })
                .show()
    }

    private fun changeCaiJin(amt: Long) {
        RetrofitHelper.toDo().changeCaiJin(amt = amt)
                .enqueue(object : Callback<ResponseBase> {
                    override fun onFailure(call: Call<ResponseBase>, t: Throwable) {
                        showToast(RetrofitHelper.getErrMsg(t))
                    }

                    override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                        if (RetrofitHelper.isSuccess(response)) {
                            showToast("兑换成功！")
                        }
                    }
                })
    }

}
