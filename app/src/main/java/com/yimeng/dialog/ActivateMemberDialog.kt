package com.yimeng.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.ActivityUtils
import com.yimeng.alipay.PayResult
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.haidou.R
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_activate_member.view.*
import okhttp3.Call
import org.simple.eventbus.EventBus
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-13 00:50.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class ActivateMemberDialog(amt:String, context: Context) :CenterPopupView(context){

    private val mAmt = amt

    @SuppressLint("HandlerLeak")
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val payResult = PayResult(msg.obj as Map<String?, String?>)

                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo = payResult.result // 同步返回需要验证的信息
                val resultStatus = payResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(true, EventTags.WECHAT_PAY_RESULT)
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(false, EventTags.WECHAT_PAY_RESULT)
                }
                dismiss()
            }
        }
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_activate_member

    override fun onCreate() {

        et_amt.run{
            setText(mAmt)
            isFocusable = false
            isFocusableInTouchMode = false
            isEnabled = false
        }

        btn.setOnClickListener {
            val params =
            OkHttpCommon().postLoadData(ActivityUtils.getTopActivity(), ConstantsUrl.URL_ACTIVATE_TOW, CommonUtils.createParams(), object : CallbackCommon {
                override fun onFailure(call: Call, e: IOException) {
                    ToastUtils.showToast("激活失败，请稍后重试！")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, jsonObject: JsonObject) {
                    if (jsonObject["status"].asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "激活失败，请稍后重试！"))
                        return
                    }
                    val data = jsonObject["data"].asJsonObject
                    val rewardNo =GsonUtils.parseJson(data, "rewardNo", "")
                    alipay(rewardNo)
                }
            })
        }
    }

    private fun alipay(rewardNo:String){
        val params = CommonUtils.createParams()
        params["rewardNo"] = rewardNo
        OkHttpCommon().postLoadData(ActivityUtils.getTopActivity(), ConstantsUrl.URL_ACTIVATE_PAY, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast("激活失败，请稍后重试！")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject["status"].asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "激活失败，请稍后重试！"))
                    return
                }
                val data = jsonObject["data"].asString
                val payRunnable = Runnable {
                    val alipay = PayTask(ActivityUtils.getTopActivity())
                    val result = alipay.payV2(data, true)
                    val msg = Message()
                    msg.what = 1
                    msg.obj = result
                    mHandler.sendMessage(msg)
                }
                val payThread = Thread(payRunnable)
                payThread.start()
            }
        })
    }

}