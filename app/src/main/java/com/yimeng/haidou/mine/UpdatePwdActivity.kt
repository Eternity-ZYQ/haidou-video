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
import kotlinx.android.synthetic.main.activity_update_pwd.*
import okhttp3.Call
import java.io.IOException


/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/8 5:24 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 修改密码
 * </pre>
 */
class UpdatePwdActivity : BaseActivity() {

    override fun setLayoutResId(): Int = R.layout.activity_update_pwd

    override fun init() {

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {

            override fun onRightClick() {
                // 修改密码
                updatePwd()
            }
        })

//        input_old_pwd.getmEditText().inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
//        input_new_pwd.getmEditText().inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
//        input_new_pwd2.getmEditText().inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD


    }

    /**
     * 修改密码
     */
    private fun updatePwd() {
        var oldpwd = input_old_pwd.inputText
        var newpwd = input_new_pwd.inputText
        var newpwd2 = input_new_pwd2.inputText

        if (oldpwd.isEmpty()) {
            showToast("请输入原密码")
            return
        }

        if (newpwd.isEmpty() || newpwd.length < 6 || newpwd.length > 18){
            showToast("请输入新密码(6到18位字母或数字)")
            return
        }

        if (newpwd2.isEmpty()|| newpwd2.length < 6 || newpwd2.length > 18){
            showToast("请确认新密码(6到18位字母或数字)")
            return
        }

        if (newpwd != newpwd2){
            showToast("两次输入的密码不一致")
            return
        }


        var params = CommonUtils.createParams()
        params["password"] = newpwd
        params["oldPassword"] = oldpwd

        OkHttpCommon().postLoadData(this, ConstantsUrl.URL_UPDATE_PASSWORD, params, object : CallbackCommon{
            override fun onFailure(call: Call?, e: IOException?) {
                showToast("修改失败，请稍后重试！")
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                if (jsonObject?.get("status")?.asInt != 1){
                    showToast(GsonUtils.parseJson(jsonObject, "msg", "修改失败，请稍后重试！"))
                    return
                }
                showToast(GsonUtils.parseJson(jsonObject, "msg", "修改成功！"))
                finish()
            }
        })

    }

    override fun loadData() {
    }

}
