package com.yimeng.haidou.mine

import android.annotation.SuppressLint
import android.graphics.Color
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.dialog.SetPayPwdDialog
import com.yimeng.net.NetComment
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.widget.MyToolBar
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_account_security.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/18 10:26 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 账户安全
 * </pre>
 */
class AccountSecurityActivity : BaseActivity() {
    override fun setLayoutResId(): Int = R.layout.activity_account_security

    override fun init() {

        var member = CommonUtils.getMember()
        item_update_mobile.setRightText(member?.mobileNo)
        item_update_mobile.rightTextView.setTextColor(Color.WHITE)

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        item_update_mobile.setOnItemClickListener {

        }

        tv_update_pwd.setOnClickListener {
            ActivityUtils.getInstance().jumpActivity(UpdatePwdActivity::class.java)
        }

        tv_update_pay_pwd.apply {
            var str = if (CommonUtils.getMember().isSetPayPwd) "修改" else "设置"
            text = "${str}支付密码"
            setOnClickListener {
                XPopup.Builder(this@AccountSecurityActivity)
                        .hasShadowBg(false)
                        .autoOpenSoftInput(true)
                        .asCustom(SetPayPwdDialog(this@AccountSecurityActivity))
                        .show()
            }
        }
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()

        NetComment.getMemberInfo {
            var str = if (it.isSetPayPwd) "修改" else "设置"
            tv_update_pay_pwd.text = "${str}支付密码"
        }
    }


}
