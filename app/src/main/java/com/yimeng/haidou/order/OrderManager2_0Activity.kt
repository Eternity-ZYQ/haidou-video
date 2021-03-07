package com.yimeng.haidou.order

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.yimeng.base.BaseActivity
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import kotlinx.android.synthetic.main.activity_order_manager2_0.*
import org.simple.eventbus.EventBus

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/21 5:40 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 订单管理
 * </pre>
 */
class OrderManager2_0Activity : BaseActivity() {


//    private val mallOrderFragment: MallOrderFragment by lazy {
//        MallOrderFragment()
//    }
//    private val shopOrderFragment: ShopOrderFragment by lazy {
//        ShopOrderFragment()
//    }
    private val fragmentPagerAdapter: FragmentPagerAdapter by lazy {
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> ShopOrderFragment()
                    else -> MallOrderFragment()
                }
            }

            override fun getCount(): Int = 2
        }
    }

    private var defaultPosition = 0

    override fun setLayoutResId(): Int = R.layout.activity_order_manager2_0


    override fun init() {

        viewPager.adapter = fragmentPagerAdapter

        var bundle = intent.extras
        var index = bundle.getInt("index", 0)
        if(index == 0)
            rb_shop_order.isChecked = true
        else
            rb_mall_order.isChecked = true
        viewPager.currentItem = index

        defaultPosition = bundle.getInt("position", 0)

//        (fragmentPagerAdapter.getItem(0) as ShopOrderFragment).checkViewPager(defaultPosition)
        EventBus.getDefault().postSticky(defaultPosition, if(index == 0)EventTags.CHECK_SHOP_INDEX else EventTags.CHECK_MALL_INDEX)

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }

        rb_shop_order.setOnClickListener { viewPager.currentItem = 0 }
        rb_mall_order.setOnClickListener { viewPager.currentItem = 1 }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> rb_shop_order.isChecked = true
                    1 -> rb_mall_order.isChecked = true
                }
            }
        })
    }

    override fun loadData() {
    }


}
