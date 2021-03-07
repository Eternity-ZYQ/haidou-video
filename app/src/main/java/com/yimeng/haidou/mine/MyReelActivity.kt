package com.yimeng.haidou.mine

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ActivityUtils
import com.yimeng.base.BaseActivity
import com.yimeng.widget.MyToolBar
import com.yimeng.haidou.R
import kotlinx.android.synthetic.main.activity_my_reel.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 18:53.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的金蛋
 * </pre>
 */
class MyReelActivity : BaseActivity() {

    companion object {
        fun start() {
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, MyReelActivity::class.java))
        }
    }

    private val title = mutableListOf("我的金蛋", "兑换金蛋")


    override fun setLayoutResId(): Int = R.layout.activity_my_reel


    override fun init() {

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) MineReelFragment.getInstance()
                else ChangeReelFragment.getInstance()
            }

            override fun getCount(): Int = title.size

            override fun getPageTitle(position: Int): CharSequence?  = title[position]

        }
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun loadData() {
    }


    override fun initListener() {
        toolBar.setOnToolBarClickListener(object :MyToolBar.OnToolBarClick(){})

    }

}
