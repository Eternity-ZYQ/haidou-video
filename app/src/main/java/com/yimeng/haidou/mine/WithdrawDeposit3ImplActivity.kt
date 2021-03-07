package com.yimeng.haidou.mine

import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.alipay.sdk.app.AuthTask
import com.yimeng.alipay.AuthResult
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantHandler
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.SystemMsgBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.net.lxmm_net.HttpParameterUtil
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.SpannableStringUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import com.huige.library.utils.log.LogUtils
import com.tencent.mm.sdk.modelmsg.SendAuth
import com.tencent.mm.sdk.openapi.IWXAPI
import com.tencent.mm.sdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_withdraw_deposit3_impl.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/25 4:44 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 提现页面
 * </pre>
 */
class WithdrawDeposit3ImplActivity : BaseActivity() {

    /**
     * 微信是否授权
     */
    private var wechatIsAuth = false
    /**
     * 支付宝是否授权
     */
    private var alipayIsAuth = false
    /**
     * 是否提现到微信
     */
    private var mWithdrawType = -1
    /**
     * mine： 我的钱包
     * shop：店铺钱包
     */
    private var walletType = "mine"
    /**
     * 可提现金额
     */
    private var moneyAll: Int = 0
    /**
     * 店铺总金额
     */
    private var shopMoneyAll: Int = 0


    private val wxApi: IWXAPI by lazy { WXAPIFactory.createWXAPI(this, Constants.WX_APPID, true) }
    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }
    private val mHandler = MyHandler(this)

    override fun setLayoutResId(): Int = R.layout.activity_withdraw_deposit3_impl

    override fun init() {

        intent.extras?.apply {
            walletType = getString("walletType", "mine")
            moneyAll = getInt("moneyAll")
            shopMoneyAll = getInt("shopMoneyAll")
        }

        if (walletType == "shop") {
            toolBar.title = "商户余额提现"

            tv_wallet_tip.movementMethod = LinkMovementMethod.getInstance()
            tv_wallet_tip.text = SpannableStringUtils.getBuilder("其中")
                    .append(UnitUtil.getMoney(moneyAll.toString())).setForegroundColor(ContextCompat.getColor(this@WithdrawDeposit3ImplActivity, R.color.c_money))
                    .append("可转出至用户余额或提现\t\t")
                    .append("查看原因").setForegroundColor(resources.getColor(R.color.c_link_color)).setClickSpan(object : ClickableSpan() {
                        override fun onClick(view: View) {
                            SimpleDialog.showSimpleRemarkWithTitleDialog(this@WithdrawDeposit3ImplActivity, "可转出或可提现金额", "买家收货后卖家收益结算到账，即可提现或转账到自己的用户余额，用于消费抵扣")
                        }
                    })
                    .create()
        } else {
            toolBar.title = "用户余额提现"
            tv_wallet_tip.text = SpannableStringUtils.getBuilder("可提现余额为")
                    .append(UnitUtil.getMoney(moneyAll.toString())).setForegroundColor(ContextCompat.getColor(this@WithdrawDeposit3ImplActivity, R.color.c_money))
                    .create()
        }

        val member = CommonUtils.getMember()
        if (!TextUtils.isEmpty(member!!.nameMM)) {
            tv_wechat_info.text = "到微信余额(" + member.nameMM + ")"
            tv_bind_wechat.text = "换绑"
            wechatIsAuth = true
        } else {
            tv_bind_wechat.text = "去绑定"
        }
        if (!TextUtils.isEmpty(member.nameMMphone)) {
            tv_alipay_info.text = "到支付宝余额(" + member.nameMMphone + ")"
            tv_bind_alipay.text = "换绑"
            alipayIsAuth = true
        } else {
            tv_bind_alipay.text = "去绑定"
        }


    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        ll_wechat_type.setOnClickListener(mOnClickListener)
        ll_alipay_type.setOnClickListener(mOnClickListener)
        tv_bind_wechat.setOnClickListener(mOnClickListener)
        tv_bind_alipay.setOnClickListener(mOnClickListener)
        submitBTN.setOnClickListener(mOnClickListener)
        tv_withdraw_all.setOnClickListener(mOnClickListener)
    }

    override fun loadData() {
        getWithdrawTips()
    }

    /**
     * 获取提现提示语
     */
    private fun getWithdrawTips() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_WITHDRAW_TIPS, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {

                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                        return
                    }

                    var msgBean = GsonUtils.getGson().fromJson(jsonObject.get("data").asJsonObject.toString(), SystemMsgBean::class.java)

                    SimpleDialog.showConfirmDialog(this@WithdrawDeposit3ImplActivity, msgBean.introduction,
                            msgBean.parentNo, null, "知道了",null, null, true)
                }

            }
        })
    }

    private val mOnClickListener = View.OnClickListener {

        when (it.id) {
            R.id.ll_wechat_type -> { // 提现到微信
                if (wechatIsAuth) {
                    mWithdrawType = 1
                    changeViewStatus()
                } else {
                    authWechat()
                }
            }
            R.id.ll_alipay_type -> { // 提现到支付宝
                if (alipayIsAuth) {
                    mWithdrawType = 2
                    changeViewStatus()
                } else {
                    authAlipay()
                }
            }
            R.id.tv_bind_wechat -> {// 授权微信
                authWechat()
            }
            R.id.tv_bind_alipay -> {// 授权支付宝
                authAlipay()
            }
            R.id.submitBTN -> { // 提现
                if (mWithdrawType == -1) {
                    ToastUtils.showToast("请选择提现方式")
                    return@OnClickListener
                }
                var applyAmt = moneyET.text.toString()
                if (TextUtils.isEmpty(applyAmt)) {
                    ToastUtils.showToast("请输入提现金额")
                    return@OnClickListener
                }

                if (UnitUtil.getDouble(applyAmt) <= 0) {
                    ToastUtils.showToast("提现金额不能小于0元")
                    return@OnClickListener
                }
                applyAmt = (UnitUtil.getDouble(applyAmt) * 100.0).toInt().toString() + ""
                SimpleDialog.showLoadingHintDialog(this, 1)
                HttpParameterUtil.getInstance().requestAddWithdrawApply(walletType, applyAmt, mWithdrawType == 1, mHandler)
            }

            R.id.tv_withdraw_all -> {// 全部提现
                moneyET.setText((moneyAll / 100.0).toString())
            }
        }
    }

    private fun changeViewStatus() {
        cb_wechat.isChecked = mWithdrawType == 1
        cb_alipay.isChecked = mWithdrawType == 2
    }

    /**
     * 获取微信用户信息
     */
    private fun authWechat() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = packageName + Math.random() * 100
        wxApi.sendReq(req)
    }

    /**
     * 获取支付宝用户信息
     */
    private fun authAlipay() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_ALIPAY_AUTH, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast("绑定失败!")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "绑定失败!"))
                    return
                }
                toAlipayAuth(jsonObject.get("data").asString)
            }
        })


    }

    /**
     * 调起支付宝
     *
     * @param signInfo
     */
    private fun toAlipayAuth(signInfo: String) {
        if (TextUtils.isEmpty(signInfo)) {
            ToastUtils.showToast("绑定失败!")
            return
        }
        val runnable = Runnable {
            val authTask = AuthTask(this@WithdrawDeposit3ImplActivity)
            val resultMap = authTask.authV2(signInfo, true)

            val msg = Message.obtain()
            msg.what = 111111
            msg.obj = resultMap
            mHandler.sendMessage(msg)
        }
        val authThread = Thread(runnable)
        authThread.start()
    }

    @Subscriber(tag = EventTags.WECHAT_AUTH_RESULT)
    fun wecharAuthResult(code: String) {
        if (!TextUtils.isEmpty(code)) {
            val url = "https://api.weixin.qq.com/sns/oauth2/access_token"
            val params = HashMap<String, String>()
            params["appid"] = Constants.WX_APPID
            params["secret"] = Constants.WX_SECRET
            params["grant_type"] = "authorization_code"
            params["code"] = code
            mOkHttpCommon.getLoadData(this, url, params, object : CallbackCommon {
                override fun onFailure(call: Call, e: IOException) {
                    ToastUtils.showToast("绑定失败!")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, jsonObject: JsonObject) {
                    LogUtils.json(jsonObject.toString())

                    if (jsonObject.has("errcode")) {
                        // 授权失败
                        ToastUtils.showToast("绑定失败(code:" + jsonObject.get("errcode").asString + ")")
                    } else {
                        // 授权成功
                        getWechatMemberInfo(jsonObject.get("access_token").asString,
                                jsonObject.get("openid").asString)
                    }
                }
            })
        }
    }

    /**
     * 获取微信用户信息
     */
    private fun getWechatMemberInfo(access_token: String, openId: String) {
        val params = HashMap<String, String>()
        params["access_token"] = access_token
        params["openid"] = openId
        mOkHttpCommon.getLoadData(this, "https://api.weixin.qq.com/sns/userinfo", params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast("绑定失败!")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                LogUtils.json(jsonObject.toString())
                if (jsonObject.has("errcode")) {
                    // 授权失败
                    ToastUtils.showToast("绑定失败")
                } else {
                    ToastUtils.showToast("绑定成功")
                    val nickname = jsonObject.get("nickname").asString
                    tv_wechat_info.text = "到微信余额($nickname)"
                    tv_bind_wechat.text = "换绑"
                    mWithdrawType = 1
                    changeViewStatus()
                    wechatIsAuth = true
                    saveAuthInfo(true, nickname, jsonObject.get("openid").asString)
                }
            }
        })
    }

    /**
     * 保存用户信息
     *
     * @param isWechat 是否为微信信息
     * @param nickname 昵称
     * @param openid   openid
     */
    fun saveAuthInfo(isWechat: Boolean, nickname: String, openid: String) {
        val params = CommonUtils.createParams()
        if (isWechat) {
            params["nameMM"] = nickname
            params["openid"] = openid
        } else {
            params["nameMMphone"] = nickname
            params["unionid"] = openid
        }
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SAVE_WECHAR_ALIPAY_AUTH_INFO, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {

            }
        })
    }

    /**
     * 获取支付宝用户信息
     *
     * @param authCode
     */
    private fun getAlipayMemberInfo(authCode: String) {
        val params = CommonUtils.createParams()
        params["authCode"] = authCode
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_ALIPAY_MEMBER_INFO, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast("绑定失败")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "绑定失败"))
                    return
                }
                ToastUtils.showToast("绑定成功")
                val data = jsonObject.get("data").asJsonObject
                val nickname = if (data.get("nickname").isJsonNull) data.get("userId").asString else data.get("nickname").asString
                tv_alipay_info.text = "到支付宝余额($nickname)"
                tv_bind_alipay.text = "换绑"
                mWithdrawType = 2
                changeViewStatus()
                alipayIsAuth = true
                saveAuthInfo(false, nickname, data.get("userId").asString)
            }
        })
    }

    private class MyHandler(mActivity: WithdrawDeposit3ImplActivity) : Handler() {

        private val mActivity: WeakReference<WithdrawDeposit3ImplActivity> = WeakReference(mActivity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (mActivity.get() != null) {
                mActivity.get()!!.disposeData(msg)
            }
        }
    }

    private fun disposeData(msg: Message) {
        SimpleDialog.cancelLoadingHintDialog()
        when (msg.what) {

            ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS -> {
                ToastUtils.showToast("提交提现申请成功!")
                finish()
            }

            ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_FAIL -> {
                ToastUtils.showToast(msg.obj as String)
            }

            111111 -> {
                val authResult = AuthResult(msg.obj as Map<String, String>, true)
                val resultStatus = authResult.resultStatus
                LogUtils.d(authResult.toString())
                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.resultCode, "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    getAlipayMemberInfo(authResult.authCode)
                } else {
                    // 其他状态值则为授权失败
                    ToastUtils.showToast("绑定失败")
                }
            }

        }
    }
}
