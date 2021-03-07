package com.yimeng.dialog

import android.content.Context
import com.yimeng.haidou.R
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.UnitUtil
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_get_red_packer_success.view.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/9/3 7:37 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包领取成功
 *  @param money 分
 * </pre>
 */
class GetRedPackerSuccessDialog(context: Context, money: String) : CenterPopupView(context) {
    private var mMoney = money
    override fun getImplLayoutId(): Int = R.layout.dialog_get_red_packer_success

    override fun onCreate() {
        super.onCreate()

        tv_money.text = "抢到${UnitUtil.getMoney(mMoney, false)}元"

        iv_close.setOnClickListener { dismiss() }
        btn.setOnClickListener {
            dismiss()
            ActivityUtils.getInstance().jumpMainActivity()
        }
    }
}