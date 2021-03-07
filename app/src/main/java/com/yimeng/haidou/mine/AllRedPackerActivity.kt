package com.yimeng.haidou.mine

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.widget.MyToolBar
import kotlinx.android.synthetic.main.activity_all_red_packer.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/26 3:53 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包墙
 * </pre>
 */
class AllRedPackerActivity : BaseActivity() {

    private var mTitles = mutableListOf("未推广", "推广中", "已领完")
    private val mFragmentAdapter: FragmentPagerAdapter by lazy {
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = RedPackerListFragment.getInstance(position)
            override fun getCount(): Int = mTitles.size
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_all_red_packer

    override fun init() {

        viewPager.apply {
            adapter = mFragmentAdapter
            offscreenPageLimit = 3
        }

        tabLayout.navigator = CommonNavigator(this).apply {
            adapter = object : CommonNavigatorAdapter(){
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView = SimplePagerTitleView(context).apply {
                    text = mTitles[index]
                    selectedColor = ContextCompat.getColor(context!!, R.color.theme_color)
                    normalColor = ContextCompat.getColor(context, R.color.c_333333)

                    setOnClickListener { viewPager.currentItem = index }
                }

                override fun getCount(): Int = mTitles.size

                override fun getIndicator(context: Context?): IPagerIndicator = LinePagerIndicator(context).apply {
                    setColors(ContextCompat.getColor(context!!, R.color.theme_color))
                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                }
            }
            isAdjustMode = true
        }
        ViewPagerHelper.bind(tabLayout, viewPager)


        intent.extras?.let {
            viewPager.currentItem = it.getInt("index", 0)
        }

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
    }

    override fun loadData() {
    }

}
