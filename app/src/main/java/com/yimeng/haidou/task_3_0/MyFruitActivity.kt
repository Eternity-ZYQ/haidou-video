package com.yimeng.haidou.task_3_0

import android.os.Bundle
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.net.NetComment
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.SpannableStringUtils
import com.yimeng.widget.MyToolBar
import kotlinx.android.synthetic.main.activity_my_fruit.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 11:43 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的活跃果
 * </pre>
 */
class MyFruitActivity : BaseActivity() {


    override fun setLayoutResId(): Int = R.layout.activity_my_fruit

    override fun init() {

        NetComment.getMemberInfo {
            tv_fruit_num.text = SpannableStringUtils.getBuilder("我的活跃果\n")
                    .append("${it.fruitCount}颗").setProportion(2.0f)
                    .create()
        }

    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                ActivityUtils.getInstance().jumpActivity(SeedLogsActivity::class.java, Bundle().apply {
                    putInt("type", 2)
                })
            }
        })

        // 挂卖
        btn_sell.setOnClickListener {
            ActivityUtils.getInstance().jumpActivity(BlockFruitActivity::class.java, Bundle().apply {
                putInt("type", 1)
            })
        }

        // 抢购
        btn_pay.setOnClickListener {
            ActivityUtils.getInstance().jumpActivity(BlockFruitActivity::class.java, Bundle().apply {
                putInt("type", 2)
            })
        }

        // 我的挂卖
        btn_my_sell.setOnClickListener { ActivityUtils.getInstance().jumpActivity(MySaleFruitActivity::class.java) }



    }

    override fun loadData() {


    }



}
