package com.yimeng.haidou.task

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.SendOrderBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_task_send_order.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 10:45 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务排行榜合伙人派单
 * </pre>
 */
class TaskSendOrderActivity : BaseActivity() {

    private var checkedSendOrderBean: SendOrderBean? = null
    private var checkedIndedx = 0
    private var mList = mutableListOf<SendOrderBean>()
    private val mAdapter: BaseQuickAdapter<SendOrderBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<SendOrderBean, BaseViewHolder>(R.layout.adapter_send_order_item, mList) {
            override fun convert(helper: BaseViewHolder?, item: SendOrderBean?) {
                item?.apply {
                    helper?.apply {
                        CommonUtils.showImage(getView(R.id.iv_product), productImg)
                        setText(R.id.tv_product_name, orderName)
                        setText(R.id.tv_member_mobile, "手机号：$mobileNo")
                        setText(R.id.tv_member_name, "买家：$nickname")
                        setText(R.id.tv_product_price, UnitUtil.getMoney(dueAmt))
                        setBackgroundColor(R.id.layout_content, if (isChecked) Color.parseColor("#fdf7f0") else ContextCompat.getColor(mContext, R.color.white))
                        setChecked(R.id.checkbox, isChecked)
                        if(!mIsSel) {
                            setVisible(R.id.checkbox, false)
                        }
                    }
                }
            }
        }
    }

    private var mIsSel = true
    private var mType: String = ""
    private var distributeMemberNo: String = ""
    private val mOkHttpCommon: OkHttpCommon by lazy {
        OkHttpCommon()
    }

    override fun setLayoutResId(): Int = R.layout.activity_task_send_order

    override fun init() {

        intent.extras?.apply {
            mType = getString("type", "")
            distributeMemberNo = getString("distributeMemberNo", "")
            mIsSel = getBoolean("isSel", true)
        }

        if(!mIsSel) {
            btn_submit.visibility = View.GONE
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TaskSendOrderActivity)
            adapter = mAdapter
        }
    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        mAdapter.setOnItemClickListener { adapter, view, position ->
            if(!mIsSel)return@setOnItemClickListener

            var sendOrderBean = mList[position]
            if (checkedSendOrderBean != null) {
                mList[checkedIndedx].isChecked = false
            }
            sendOrderBean.isChecked = true
            checkedSendOrderBean = sendOrderBean
            checkedIndedx = position
            mAdapter.notifyDataSetChanged()

            btn_submit.isEnabled = true
        }
        smartRefresh.setOnRefreshListener {
            checkedSendOrderBean = null
            btn_submit.isEnabled =false
            loadData()
        }

        btn_submit.setOnClickListener {
            checkedSendOrderBean?.apply {
                SimpleDialog.showConfirmDialog(this@TaskSendOrderActivity, null,"确定要派单吗？", null, {
                    sendOrder()
                })
            }
        }
    }

    /**
     * 合伙人派单
     */
    private fun sendOrder() {
        checkedSendOrderBean?.apply {
            var params = CommonUtils.createParams()
            params["distributeMemberNo"] = distributeMemberNo
            params["orderNo"] = orderNo
            params["taskType"] = "${if (mType == "fs") 1 else 0}"
            mOkHttpCommon.postLoadData(this@TaskSendOrderActivity, ConstantsUrl.URL_AGENT_SEND_ORDER, params, object : CallbackCommon {
                override fun onFailure(call: Call?, e: IOException?) {
                    ToastUtils.showToast(R.string.net_error)
                }

                override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                    jsonObject?.apply {
                        if(jsonObject.get("status").asInt != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                            return
                        }
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "派单成功"))
                        finish()
                    }
                }
            })
        }


    }

    override fun loadData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_AGENT_QUERY_SEND_ORDER, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                        return
                    }
                    mList.clear()
                    var dataArr = jsonObject.get("data").asJsonArray
                    if (!dataArr.isJsonNull) {
                        var list = GsonUtils.getGson().fromJson<List<SendOrderBean>>(dataArr.toString(), object : TypeToken<List<SendOrderBean>>() {}.type)
                        mList.addAll(list)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

}

