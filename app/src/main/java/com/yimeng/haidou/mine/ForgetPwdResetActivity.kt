package com.yimeng.haidou.mine

import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.showToast
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_pwd_reset.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/11 5:38 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 忘记密码重新设置
 * </pre>
 */
class ForgetPwdResetActivity : BaseActivity() {
    /**
     * 1. 邮箱
     * 2。手机
     */
    var type : Int = 1
    var accountStr : String?=null

    override fun setLayoutResId(): Int = R.layout.activity_forget_pwd_reset

    override fun init() {
        var bundle = intent.extras
        type = bundle.getInt("type")
        accountStr  = bundle.getString("accountStr")

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
        btn.setOnClickListener { setNewPwd() }
    }

    /**
     * 设置新密码
     */
    private fun setNewPwd() {
        var pwd = input_new_pwd.inputText
        if (pwd.isEmpty() || pwd.length < 6 || pwd.length > 18){
            showToast("请输入新密码(6到18位字母或数字)")
            return
        }

        var pwd2 = input_new_pwd2.inputText
        if (pwd2.isEmpty() || pwd2.length < 6 || pwd2.length > 18){
            showToast("请确认新密码(6到18位字母或数字)")
            return
        }

        if(pwd != pwd2) {
            showToast("两次密码输入不一致")
            return
        }


        var params = CommonUtils.createParams()
        if(type  == 1) {
            params["email"] = accountStr
        }else{
            params["mobile"] = accountStr
        }
        params["password"] = pwd

        OkHttpCommon().postLoadData(this, ConstantsUrl.URL_FORGET_PWD_RESET,  params,
                object : CallbackCommon{
                    override fun onFailure(call: Call?, e: IOException?) {
                        showToast(getString(R.string.net_error))
                    }

                    override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                        if(jsonObject?.get("status")?.asInt != 1) {
                            showToast(GsonUtils.parseJson(jsonObject, "msg", "设置失败，请稍后重试！"))
                            return
                        }
                        showToast(GsonUtils.parseJson(jsonObject, "msg", "设置成功！"))
                        finish()
                    }
                })

        
    }

    override fun loadData() {
    }


}
