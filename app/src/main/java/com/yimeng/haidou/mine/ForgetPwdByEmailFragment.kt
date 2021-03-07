package com.yimeng.haidou.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.yimeng.base.BaseFragment
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.StringUtils
import com.yimeng.utils.showToast
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_forget_pwd_email.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/10 10:51 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 忘记密码/邮箱找回
 * </pre>
 */

class ForgetPwdByEmailFragment : BaseFragment() {

    var countdownDisposable: Disposable? = null

    override fun setLayoutResId(): Int = R.layout.fragment_forget_pwd_email

    override fun init() {

    }

    override fun initListener() {
    }

    override fun loadData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_getCode?.setOnClickListener { getForgetCode() }
    }

    /**
     * 获取验证码
     */
    private fun getForgetCode() {
        var params = CommonUtils.createParams()
        var emailAddressStr = getEmailAddress()
        if (emailAddressStr.isEmpty()) {
            showToast("请输入邮箱地址！")
            return
        }

        if(!StringUtils.isEmail(emailAddressStr)) {
            showToast("请输入正确邮箱地址")
            return
        }

        params["email"] = emailAddressStr
        OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_FORGET_PWD_EMAIL_GET_CODE, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                showToast("发送失败，请稍后重试！")
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                if (jsonObject!!.get("status").asInt != 1) {
                    showToast(GsonUtils.parseJson(jsonObject, "msg", "发送失败，请稍后重试！"))
                    return
                }

                btn_getCode.isEnabled = false
                startCountdown()
            }
        })
    }

    /**
     * 开启倒计时
     */
    private fun startCountdown() {
        CommonUtils.countdown(60)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onSubscribe(d: Disposable) {
                        countdownDisposable = d
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNext(t: Int) {
                        btn_getCode.text = t.toString() + "s"
                        et_email.isEnabled = false
                    }

                    override fun onError(e: Throwable) {
                        resetCode()
                    }

                    override fun onComplete() {
                        resetCode()
                    }
                })
    }

    /**
     * 恢复按钮状态
     */
    private fun resetCode() {
        btn_getCode.text = getString(R.string.get_code)
        btn_getCode.isEnabled = true
        et_email.isEnabled = true
    }

    fun getEmailAddress(): String = et_email.text.toString().trim()

    fun getEmailCode(): String = et_code.text.toString().trim()

    override fun onDestroy() {
        super.onDestroy()
        countdownDisposable?.apply {
            if(!isDisposed) {
                dispose()
            }
        }
    }
}