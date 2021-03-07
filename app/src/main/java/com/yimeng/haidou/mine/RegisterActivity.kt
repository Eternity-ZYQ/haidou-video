package com.yimeng.haidou.mine

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.DeviceUtils
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.XJConfig
import com.yimeng.entity.HistoryAccountBean
import com.yimeng.entity.Member
import com.yimeng.jpush.TagAliasOperatorHelper
import com.yimeng.retorfit.ResponseData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.showToast
import com.yimeng.widget.MyToolBar
import com.yimeng.haidou.MainActivity
import com.yimeng.haidou.R
import com.google.gson.JsonObject
import com.huige.library.utils.SharedPreferencesUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import org.litepal.LitePal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-09 23:03.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 用户注册
 * </pre>
 */
class RegisterActivity : BaseActivity() {

    private var coundownDisposable: Disposable? = null
    private val mBindNo by lazy { DeviceUtils.getUniqueDeviceId() }

    companion object {
        fun start() {
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, RegisterActivity::class.java))
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_register

    override fun init() {


    }

    override fun loadData() {
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
        })

        tv_get_code.setOnClickListener { getCode() }
        btn_register.setOnClickListener { register() }
        tv_login.setOnClickListener { ActivityUtils.startActivity(LoginXJActivity::class.java) }

        tv_protocol.setOnClickListener { com.yimeng.utils.ActivityUtils.getInstance().jumpH5Activity("用户注册协议", ConstantsUrl.URL_PROTOCOL + "用户注册协议") }
    }

    /**
     * 获取验证码
     */
    private fun getCode() {
        val mobile = et_mobile.text.trim().toString()
        if (mobile.isBlank()) {
            showToast("请输入手机号")
            return
        }

        RetrofitHelper.toDo().getCode(mobile)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        showToast(RetrofitHelper.getErrMsg(t))
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        response.body()?.run {
                            if (get("retCode").asString == XJConfig.NET_SUCCESS) {
                                showToast("获取成功")
                                countdownTime()
                            } else {
                                showToast(get("retData").asString)
                            }
                        } ?: showToast("获取失败！")
                    }
                })

    }

    /**
     * 倒计时
     */
    private fun countdownTime() {
        CommonUtils.countdown(60)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onSubscribe(d: Disposable) {
                        coundownDisposable = d
                    }

                    override fun onNext(integer: Int) {
                        tv_get_code.text = "${integer}s"
                        et_mobile.isEnabled = false
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
        tv_get_code.setText(R.string.get_code)
        tv_get_code.isEnabled = true
        et_mobile.isEnabled = true
    }

    private fun register() {
        val mobile = et_mobile.text.trim().toString()
        if (mobile.isBlank()) {
            showToast("请输入手机号")
            return
        }

        val code = et_code.text.trim().toString()
        if (code.isBlank()) {
            showToast("请输入验证码")
            return
        }

        val pwd = et_pwd.text.trim().toString()
        if (pwd.isBlank()) {
            showToast("请输入密码")
            return
        }

        val pwd2 = et_pwd2.text.trim().toString()
        if (pwd2.isBlank()) {
            showToast("请输入确认密码")
            return
        }

        if (pwd != pwd2) {
            showToast("两次密码输入不一致")
            return
        }

        val invoteCode = et_invite_code.text.trim().toString()
        if (invoteCode.isBlank()) {
            showToast("请输入邀请码")
            return
        }

        val params = mutableMapOf<String, String>().apply {
            put("mobileNo", mobile)
            put("smsCode", code)
            put("password", pwd)
            put("inviteCode", invoteCode)
        }

        RetrofitHelper.toDo().register(params)
                .enqueue(object : Callback<ResponseData<Member>> {
                    override fun onFailure(call: Call<ResponseData<Member>>, t: Throwable) {
                        showToast(RetrofitHelper.getErrMsg(t))
                    }

                    override fun onResponse(call: Call<ResponseData<Member>>, response: Response<ResponseData<Member>>) {
                        response.body()?.run {
                            if (status == 1) {
                                showToast("注册成功")
                                if (this.data == null) {
                                    ActivityUtils.startActivity(LoginXJActivity::class.java)
                                    finish()
                                    return
                                }

                                val member = this.data
                                //存储用户信息
                                SharedPreferencesUtils.put(Constants.USER_INFO, GsonUtils.getGson().toJson(this.data))
                                SharedPreferencesUtils.put(Constants.IS_LOGIN, true)
                                CommonUtils.saveMember(member)

                                // 绑定推送
                                TagAliasOperatorHelper.getInstance().bindJPush(member.memberNo)


                                // 保存到常用账号
                                //new HistoryAccountBean(mobileNo, code).save();
                                val historyAccountBeans = LitePal.where("accountNo=?",mobile).find(HistoryAccountBean::class.java)
                                if (historyAccountBeans.isEmpty()) {
                                    HistoryAccountBean(mobile, pwd, member.headPath).save()
                                } else {
                                    val historyAccountBean = historyAccountBeans[0]
                                    historyAccountBean.pwd = code
                                    historyAccountBean.save()
                                }

                                ActivityUtils.startActivity(MainActivity::class.java)
                                finish()

                            } else {
                                showToast(msg)
                            }
                        } ?: showToast("注册失败")
                    }
                })

    }

}
