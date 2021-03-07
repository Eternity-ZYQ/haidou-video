package com.yimeng.haidou.task_3_0

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.huige.library.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.fragment_tab_task_3_0.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import org.simple.eventbus.Subscriber

/**
 * <pre>
 * Author : huiGer
 * Time   : 2019/7/30 6:57 PM.
 * Email  : zhihuiemail@163.com
 * Desc   : 3.0任务系统
 * </pre>
 */
class TabTaskFragment_3_0 : BaseFragment() {

    private var mTitle = mutableListOf( "团队任务", "个人任务")
    private val mineFragment = TaskMineFragment.getInstance()
    // 已激活团队任务
    private val mTeamFragment: TaskTeamFragment by lazy { TaskTeamFragment.getInstance() }
    // 未激活团队任务
    private val mTeamUnActivateFragment: TaskTeamUnActivateFragment by lazy { TaskTeamUnActivateFragment.getInstance() }
    // 是否激活
    private var isActivate = false
    private val mFragmentAdapter: FragmentStatePagerAdapter by lazy {
        object : FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return if (position == 0) getTeamFragment()
                else mineFragment
            }

            override fun getCount(): Int = 2
            override fun getPageTitle(position: Int): CharSequence = mTitle[position]
            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        }
    }

    private fun getTeamFragment(): Fragment {
        return if (isActivate)
            mTeamFragment
        else
            mTeamUnActivateFragment
    }


    override fun setLayoutResId(): Int = R.layout.fragment_tab_task_3_0

    override fun init() {

        isActivate = SharedPreferencesUtils.get(Constants.TASK_NEW_IS_ACTIVATE, false) as Boolean
        viewPager.adapter = mFragmentAdapter
        tabLayout.navigator = CommonNavigator(activity).apply {
            adapter = object : CommonNavigatorAdapter(){
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return SimplePagerTitleView(context).apply {
                        text = mTitle[index]
                        normalColor = Color.WHITE
                        selectedColor = Color.WHITE

                        setOnClickListener { viewPager.currentItem = index }
                    }
                }

                override fun getCount(): Int = mTitle.size

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_WRAP_CONTENT
                        setColors(Color.WHITE)
                    }
                }

            }
            isAdjustMode = true
        }
        ViewPagerHelper.bind(tabLayout, viewPager)

    }

    override fun initListener() {

    }

    override fun loadData() {

    }


    /**
     * 激活回调
     */
    @Subscriber(tag = EventTags.ACTIVATE_TEAM_TASK)
    fun activateResult(result: Boolean) {
        isActivate = result
        mFragmentAdapter.notifyDataSetChanged()
    }
}
