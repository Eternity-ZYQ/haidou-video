package com.yimeng.dialog

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.google.gson.JsonObject
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.custom_keyboard.view.*
import kotlinx.android.synthetic.main.dialog_pay_custom_keyboard.view.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/6 11:17 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 密码输入框
 * </pre>
 */
class PayCustomKeyboardDialog(context: Context, checkPwdSuccess: CheckPwdSuccess) : BottomPopupView(context) {

    private var mActivity: Activity? = null
    private var mCheckPwdSuccess: CheckPwdSuccess? = null
    private var lastTime: Long = 0
    private val mHandle = Handler()

    init {
        this.mActivity = context as Activity
        this.mCheckPwdSuccess = checkPwdSuccess
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_pay_custom_keyboard

    override fun onCreate() {
        super.onCreate()

        setViewTag(tv_key_0, tv_key_1, tv_key_2, tv_key_3, tv_key_4, tv_key_5, tv_key_6, tv_key_7,
                tv_key_8, tv_key_9, tv_key_delete)
        initListener()

    }


    fun initListener() {

        iv_close.setOnClickListener {
            dismiss()
        }
        tv_key_0.setOnClickListener { pwdEt.addPassword("0") }
        tv_key_1.setOnClickListener { pwdEt.addPassword("1") }
        tv_key_2.setOnClickListener { pwdEt.addPassword("2") }
        tv_key_3.setOnClickListener { pwdEt.addPassword("3") }
        tv_key_4.setOnClickListener { pwdEt.addPassword("4") }
        tv_key_5.setOnClickListener { pwdEt.addPassword("5") }
        tv_key_6.setOnClickListener { pwdEt.addPassword("6") }
        tv_key_7.setOnClickListener { pwdEt.addPassword("7") }
        tv_key_8.setOnClickListener { pwdEt.addPassword("8") }
        tv_key_9.setOnClickListener { pwdEt.addPassword("9") }
        tv_key_delete.setOnClickListener { pwdEt.deletePassword() }

        pwdEt.setOnPwdResultListener { view, pwd ->
            layout_keyboard.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            loadingView.loadLoading()
            mHandle.postDelayed({
                checkPayPwd()
            }, 2000)

        }
    }

    private fun setViewTag(vararg views: View) {
        views.forEach {
            it.setTag(R.id.click_filter_key, true)
        }
    }


    /**
     * check 支付密码
     */
    private fun checkPayPwd() {
        var params = CommonUtils.createParams()
        params["payCode"] = pwdEt.text.toString()
        OkHttpCommon().postLoadData(mActivity, ConstantsUrl.URL_CHECK_PAY_PWD, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
//                ToastUtils.showToast(R.string.net_error)
                mHandle.postDelayed({
                    layout_keyboard.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                    pwdEt.setText("")
                }, 2000)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
//                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", context.getString(R.string.net_error)))
                        loadingView.loadFailure()
                        mHandle.postDelayed({
                            layout_keyboard.visibility = View.VISIBLE
                            loadingView.visibility = View.GONE
                            pwdEt.setText("")
                        }, 2000)
                        return
                    }

                    mCheckPwdSuccess?.apply {
                        loadingView.loadSuccess()
                        mHandle.postDelayed({
                            onResult(true)
                            dismiss()
                        }, 2000)

                    }
                }


            }
        })
    }

    interface CheckPwdSuccess {
        fun onResult(result: Boolean)
    }
}