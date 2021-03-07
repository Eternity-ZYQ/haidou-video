package com.yimeng.haidou.task_3_0

import android.os.Bundle
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.dialog.SeedSellDialog
import com.yimeng.dialog.SimpleDialog
import com.yimeng.net.NetComment
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.SpannableStringUtils
import com.yimeng.widget.MyToolBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import kotlinx.android.synthetic.main.activity_my_seed.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 11:43 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的发财种子
 * </pre>
 */
class MySeedActivity : BaseActivity() {


    // 我的种子数量
    private var mMineSeedCount = 0

    override fun setLayoutResId(): Int = R.layout.activity_my_seed

    override fun init() {


    }

    private fun getSeed() {
        NetComment.getMemberInfo {
            mMineSeedCount = it.seedCount
            tv_seed_num.text = SpannableStringUtils.getBuilder("发财种子\n")
                    .append("${it.seedCount}颗").setProportion(2.0f)
                    .create()
        }
    }

    override fun onResume() {
        super.onResume()
        getSeed()
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                ActivityUtils.getInstance().jumpActivity(SeedLogsActivity::class.java, Bundle().apply {
                    putInt("type", 1)
                })
            }
        })

        btn_pay.setOnClickListener { ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java) }

        btn_sell.setOnClickListener {
            if (mMineSeedCount <= 0) {
                //种子数量不足
                SimpleDialog.showConfirmDialog(this, "提示", "您还没去发财种子，立即前往抢购？", null, {
                    ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
                })
            } else {
                // 挂卖
                XPopup.Builder(this)
                        .setPopupCallback(object : SimpleCallback() {
                            override fun onDismiss() {
                                super.onDismiss()
                                getSeed()
                            }
                        })
                        .asCustom(SeedSellDialog(this, mMineSeedCount, 1))
                        .show()
            }
        }

        // 我的挂卖
        btn_my_sell.setOnClickListener {
            ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java, Bundle().apply {
                putString("memberNo", CommonUtils.getMember().memberNo)
            })
        }

    }

    override fun loadData() {

    }


}
