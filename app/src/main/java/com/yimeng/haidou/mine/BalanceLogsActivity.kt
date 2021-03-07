package com.yimeng.haidou.mine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.enums.BalanceLogType
import com.yimeng.widget.MyToolBar
import kotlinx.android.synthetic.main.activity_balance_logs.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/27 11:43 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 余额流水记录
 * </pre>
 */
class BalanceLogsActivity : BaseActivity() {

    private var isShop = false
    private val balanceFragment: BalanceLogsFragment by lazy {
        BalanceLogsFragment().apply {
            var bundle = Bundle()
            bundle.putBoolean("isShop", isShop)
            bundle.putString("type", BalanceLogType.balance)
            arguments = bundle
        }
    }

    private val withdrawFragment: BalanceLogsFragment by lazy {
        BalanceLogsFragment().apply {
            var bundle = Bundle()
            bundle.putBoolean("isShop", isShop)
            bundle.putString("type", BalanceLogType.withdraw)
            arguments = bundle
        }
    }

    private val mFragmentAdapter: FragmentPagerAdapter by lazy {
        object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return if (position == 0) balanceFragment
                else withdrawFragment
            }

            override fun getCount(): Int = 2
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_balance_logs

    override fun init() {

        var bundle = intent.extras
        isShop = bundle?.getBoolean("isShop", false)!!

        viewPager.adapter = mFragmentAdapter

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tabLayout.setCurrentPosition(position)
            }
        })

        tabLayout.setOnClickListener { position: Int ->
            viewPager.currentItem = position
        }

    }

    override fun loadData() {
    }

}
