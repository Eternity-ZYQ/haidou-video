package com.yimeng.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.PayTask
import com.yimeng.alipay.PayResult
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.XJConfig
import com.yimeng.entity.AccountInfo
import com.yimeng.enums.PayType
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.haidou.R
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.tencent.mm.sdk.modelpay.PayReq
import com.tencent.mm.sdk.openapi.IWXAPI
import com.tencent.mm.sdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.dialog_pay_seed.view.*
import okhttp3.Call
import org.simple.eventbus.EventBus
import java.io.IOException

// TODO 支付

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 5:30 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 余额、微信、支付宝支付
 *  @param title 标题
 *  @param priceSum 总价，单位分
 *  @param rewardNo 支付id
 *  @param callbackEventTag 支付回调
 *  @param
 * </pre>
 */

class CommonAllPayDialog @JvmOverloads constructor(context: Context, title: String, priceSum: String,
                                                   rewardNo: String, callbackEventTag: String, vararg payTypes: PayType) : CenterPopupView(context) {

    // 标题
    private var mTitle: String = title

    // 分
    private var mPriceSum: String = priceSum
    private var mRewardNo: String = rewardNo

    // 支付回调
    private var mCallbackEventTag = callbackEventTag

    // 支持的支付方式
    private var mPayTypes: Array<out PayType> = if (payTypes.isEmpty()) arrayOf(PayType.ALIPAY, PayType.WECHAT) else payTypes

    // 微信支付
    private var msgApi: IWXAPI = WXAPIFactory.createWXAPI(context, Constants.WX_APPID)
    private val mOkHttpCommon = OkHttpCommon()
    private val MSG_ERROR = "支付失败，请稍后重试！"

    @SuppressLint("HandlerLeak")
    internal var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val payResult = PayResult(msg.obj as Map<String, String>)

                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo = payResult.result// 同步返回需要验证的信息
                val resultStatus = payResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(true, mCallbackEventTag)
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(false, mCallbackEventTag)
                }
            }
        }
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_pay_seed


    override fun onCreate() {
        super.onCreate()
        tv_title.text = mTitle
        tv_seed_price_sum.text = String.format("￥%.2f", UnitUtil.getDouble(mPriceSum) / 100.0)

        mPayTypes.forEach {
            when (it) {
                PayType.YU_E -> {
                    ll_yue.visibility = View.VISIBLE
                    getAccountInfo()
                }
                PayType.WECHAT -> {
                    ll_wechat.visibility = View.VISIBLE
                }
                PayType.ALIPAY -> {
                    ll_alipay.visibility = View.VISIBLE
                }
            }
        }

        setDefaultPay()

        ll_yue.setTag(R.id.click_filter_key, true)
        ll_yue.setOnClickListener {
            cb_yue.isChecked = true
            cb_wechat.isChecked = false
            cb_alipay.isChecked = false
        }

        ll_wechat.setTag(R.id.click_filter_key, true)
        ll_wechat.setOnClickListener {
            cb_yue.isChecked = false
            cb_wechat.isChecked = true
            cb_alipay.isChecked = false
        }

        ll_alipay.setTag(R.id.click_filter_key, true)
        ll_alipay.setOnClickListener {
            cb_yue.isChecked = false
            cb_wechat.isChecked = false
            cb_alipay.isChecked = true
        }

        btn_cancel.setOnClickListener { dismiss() }
        btn_submit.setOnClickListener {
            when {
                cb_yue.isChecked -> {
                    toYue()
                }
                cb_wechat.isChecked -> {
                    ToastUtils.showToast("正在调起支付，请稍后...")
                    toWeChat()
                }
                else -> {
                    ToastUtils.showToast("正在调起支付，请稍后...")
                    toAlipay()
                }
            }

            dismiss()
        }
    }

    /**
     * 我的钱包余额
     */
    private fun getAccountInfo() {
        mOkHttpCommon.postLoadData(ActivityUtils.getInstance().currentActivity(), ConstantsUrl.URL_ACCOUNT_INFO, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "网络错误"))
                        return
                    }
                    val data = GsonUtils.getGson().fromJson(jsonObject.get("data").asJsonObject.toString(), AccountInfo::class.java)
                    tv_balance.text = "账户余额：${UnitUtil.formatMoney("${data.balance / XJConfig.MONEY_UNOT}", true)}"

                }
            }
        })
    }

    /**
     * 默认支付方式
     */
    private fun setDefaultPay() {
        when (mPayTypes[0]) {
            PayType.YU_E -> {
                cb_yue.isChecked = true
                cb_wechat.isChecked = false
                cb_alipay.isChecked = false
            }
            PayType.WECHAT -> {
                cb_yue.isChecked = false
                cb_wechat.isChecked = true
                cb_alipay.isChecked = false
            }
            PayType.ALIPAY -> {
                cb_yue.isChecked = false
                cb_wechat.isChecked = false
                cb_alipay.isChecked = true
            }
        }
    }

    /**
     * 余额支付
     */
    private fun toYue() {
        val member = CommonUtils.getMember() ?: return
        if (!member.isSetPayPwd) {
            // 设置支付密码
            XPopup.Builder(context)
                    .hasShadowBg(false)
                    .autoOpenSoftInput(true)
                    .asCustom(SetPayPwdDialog(context))
                    .show()
        } else {
            XPopup.Builder(context)
                    .autoOpenSoftInput(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(PayCustomKeyboardDialog(context, object : PayCustomKeyboardDialog.CheckPwdSuccess {
                        override fun onResult(result: Boolean) {
                            ToastUtils.showToast("正在调起支付，请稍后...")
                            yue()
                        }
                    }))
                    .show()
        }
    }

    /**
     * 执行余额支付
     */
    private fun yue() {
        val params = CommonUtils.createParams()
        params["rewardNo"] = mRewardNo
        OkHttpCommon().postLoadData(ActivityUtils.getInstance().currentActivity(), ConstantsUrl.GET_REWARD_YUE_PARAMS, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast(MSG_ERROR)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", MSG_ERROR))
                    return
                }
                EventBus.getDefault().post(true, mCallbackEventTag)
            }
        })
    }

    /**
     * 支付宝支付
     */
    private fun toAlipay() {
        val params = CommonUtils.createParams()
        params["rewardNo"] = mRewardNo
        mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.GET_REWARD_ALIPAY_PARAMS, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast(MSG_ERROR)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", MSG_ERROR))
                    return
                }

                val data = jsonObject.get("data").asString
                val payRunnable = Runnable {
                    val alipay = PayTask(context as Activity)
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

    /**
     * 微信支付
     */
    private fun toWeChat() {
        val params = CommonUtils.createParams()
        params["rewardNo"] = mRewardNo
        mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.GET_REWARD_WECHAT_PARAMS, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast(MSG_ERROR)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", MSG_ERROR))
                    return
                }

                val data = jsonObject.get("data").asJsonObject
                val appid = data.get("appid").asString // appid
                val partnerid = data.get("partnerid").asString // 商户号
                val prepayid = data.get("prepayid").asString // 预支付交易会话ID
                val package1 = "Sign=WXPay" // 扩展字段
                val noncestr = data.get("noncestr").asString // 随机字符串
                val timestamp = data.get("timestamp").asString // 时间戳
                val sign = data.get("sign").asString // 签名

                val request = PayReq()
                request.appId = appid
                request.partnerId = partnerid
                request.prepayId = prepayid
                request.packageValue = package1
                request.nonceStr = noncestr
                request.timeStamp = timestamp
                request.sign = sign
                request.extData = mCallbackEventTag
                msgApi.sendReq(request)
            }
        })
    }

}