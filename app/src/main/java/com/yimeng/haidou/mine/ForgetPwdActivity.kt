package com.yimeng.haidou.mine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.yimeng.widget.MyToolBar
import com.yimeng.widget.NavCommonView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/8 7:04 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 忘记密码
 * </pre>
 */
class ForgetPwdActivity : BaseActivity() {


    lateinit var mEmailFragment: ForgetPwdByEmailFragment
    lateinit var mMobileFragment: ForgetPwdByMobileFragment


    override fun setLayoutResId(): Int = R.layout.activity_forget_pwd


    override fun init() {

        mEmailFragment = ForgetPwdByEmailFragment()
        mMobileFragment = ForgetPwdByMobileFragment()
        navCommonView.setNavType("邮箱", "手机", "", "", "")
        navCommonView.setCurrentNavCount(2)

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> mEmailFragment
                    else -> mMobileFragment
                }
            }

            override fun getCount(): Int = 2
        }


    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        btn.setOnClickListener { forgetPwd() }

        viewPager.addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        navCommonView.setCurrentPosition(position)
                    }
                })

        navCommonView.setOnClickListener(NavCommonView.OnClickListener { position ->
            viewPager.currentItem = position
        })
    }

    /**
     * 找回密码
     */
    private fun forgetPwd() {
        val index = viewPager.currentItem
        if (index == 0)
            forgetPwdByEmail()
        else
            forgetPwdByMobile()
    }

    /**
     * 手机验证码找回密码
     */
    private fun forgetPwdByMobile() {

        var mobileStr = mMobileFragment.getMobile()
        var codeStr = mMobileFragment.getMobileCode()

        if (mobileStr.isEmpty()) {
            showToast("请输入手机号码")
            return
        }

        if (codeStr.isEmpty()) {
            showToast("请输入验证码")
            return
        }

        next(2, mobileStr, codeStr)

    }

    /**
     * 邮箱找回密码
     */
    private fun forgetPwdByEmail() {
        var emailAddressStr = mEmailFragment.getEmailAddress()
        var codeStr = mEmailFragment.getEmailCode()

        if (emailAddressStr.isEmpty()) {
            showToast("请输入邮箱地址")
            return
        }

        if (!StringUtils.isEmail(emailAddressStr)) {
            showToast("请输入正确邮箱地址")
            return
        }

        if (codeStr.isEmpty()) {
            showToast("请输入验证码")
            return
        }

        next(1, emailAddressStr, codeStr)

    }

    /**
     * 验证验证码
     * @param type 1: 邮箱
     *             2：手机号
     */
    private fun next(type: Int, accountStr: String, code: String) {
        var params = CommonUtils.createParams()
        params["code"] = code
        if (type == 1) {
            params["email"] = accountStr
        } else {
            params["mobile"] = accountStr
        }

        OkHttpCommon().postLoadData(this, ConstantsUrl.URL_FORGET_PWD_CHECK_CODE, params,
                object : CallbackCommon {
                    override fun onFailure(call: Call?, e: IOException?) {
                        showToast(getString(R.string.net_error))
                    }

                    override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                        if (jsonObject?.get("status")?.asInt != 1) {
                            showToast(GsonUtils.parseJson(jsonObject, "msg", "验证码不正确！"))
                            return
                        }
                        var bundle = Bundle()
                        bundle.putInt("type", type)
                        bundle.putString("accountStr", accountStr)
                        ActivityUtils.getInstance().jumpActivity(ForgetPwdResetActivity::class.java, bundle)
                        finish()
                    }
                })

    }

    override fun loadData() {


    }

    override fun onDestroy() {
        super.onDestroy()
        mEmailFragment.countdownDisposable?.dispose()
        mMobileFragment.countdownDisposable?.dispose()

    }

}
