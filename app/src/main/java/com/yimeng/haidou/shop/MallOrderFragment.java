package com.yimeng.haidou.shop;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseFragment;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.OrderPagerAdapter;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.NavMallOrderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/21 6:20 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MallOrderFragment extends BaseFragment implements NavMallOrderView.OnClickListener {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.order_page)
    ViewPager mViewPager;
    @Bind(R.id.nav_mall_order_view)
    NavMallOrderView orderView;

    private OrderMallFragment mOrderMallFragment1;
    private OrderMallFragment mOrderMallFragment2;
    private OrderMallFragment mOrderMallFragment3;
    private OrderMallFragment mOrderMallFragment4;
    private OrderMallFragment mOrderMallFragment5;

    @Override
    protected int setLayoutResId() {
        return R.layout.mall_order_fragment;
    }

    @Override
    protected void init() {

        List<Fragment> fragments = new ArrayList<>(5);
        mOrderMallFragment1 = new OrderMallFragment("pay");
        mOrderMallFragment2 = new OrderMallFragment("noReceiving");
        mOrderMallFragment3 = new OrderMallFragment("nocomment");
        mOrderMallFragment4 = new OrderMallFragment("complete");
//        mOrderMallFragment5 = new OrderMallFragment("refundComplete");
        mOrderMallFragment5 = new OrderMallFragment("refund");
        fragments.add(mOrderMallFragment1);
        fragments.add(mOrderMallFragment2);
        fragments.add(mOrderMallFragment3);
        fragments.add(mOrderMallFragment4);
        fragments.add(mOrderMallFragment5);
        OrderPagerAdapter adapter = new OrderPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                orderView.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        orderView.setOnClickListener(this);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    public void onClick(int position) {
        mViewPager.setCurrentItem(position);
        if (mOrderMallFragment1 != null &&
                mOrderMallFragment2 != null &&
                mOrderMallFragment3 != null &&
                mOrderMallFragment4 != null
                && mOrderMallFragment5 != null
                ) {
            switch (position) {
                case 0:
                    mOrderMallFragment1.onAutoRefresh();
                    break;
                case 1:
                    mOrderMallFragment2.onAutoRefresh();
                    break;
                case 2:
                    mOrderMallFragment3.onAutoRefresh();
                    break;
                case 3:
                    mOrderMallFragment4.onAutoRefresh();
                    break;
                case 4:
                    mOrderMallFragment5.onAutoRefresh();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void loadData() {

    }
}
