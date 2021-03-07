package com.yimeng.haidou.mine

import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.StringUtils
import com.yimeng.utils.showToast
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_update_user_email.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/11 6:35 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 修改用户邮箱地址
 * </pre>
 */
class UpdateUserEmailActivity : BaseActivity() {
    override fun setLayoutResId(): Int = R.layout.activity_update_user_email

    override fun init() {




    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
        btn.setOnClickListener { resetEmail() }

    }

    /**
     * 重设邮箱地址
     */
    private fun resetEmail() {
        var pwd = input_new_pwd.inputText
        if(pwd.isEmpty()) {
            showToast("请输入您的密码")
            return
        }

        var email = input_email.inputText
        if(email.isEmpty()) {
            showToast("请输入新的邮箱地址")
            return
        }

        if(!StringUtils.isEmail(email)) {
            showToast("邮箱地址不合法！")
            return
        }

        var params = CommonUtils.createParams()
        params["password"] = pwd
        params["email"] = email

        OkHttpCommon().postLoadData(this,ConstantsUrl.URL_UPDATE_EMAIL, params,
                object : CallbackCommon{
                    override fun onFailure(call: Call?, e: IOException?) {
                        showToast(getString(R.string.net_error))
                    }

                    override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                        if(jsonObject?.get("status")?.asInt != 1) {
                            showToast("修改失败，请稍后重试！")
                            return
                        }

                        showToast("修改成功！")
                        finish()
                    }
                })

    }

    override fun loadData() {
    }



}
