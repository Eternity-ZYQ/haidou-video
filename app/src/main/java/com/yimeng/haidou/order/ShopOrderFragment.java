package com.yimeng.haidou.order;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseFragment;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.OrderPagerAdapter;
import com.yimeng.widget.CustomViewPager;
import com.yimeng.widget.NavCommonView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/21 0021 下午 06:44.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺订单
 * </pre>
 */
public class ShopOrderFragment extends BaseFragment{

    @Bind(R.id.navCommonView)
    NavCommonView navCommonView;
    @Bind(R.id.order_page)
    CustomViewPager orderPage;

    private List<Fragment> mFragments;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragmentshop_order;
    }

    @Override
    protected void init() {
        EventBus.getDefault().registerSticky(this);
        navCommonView.setCurrentNavCount(4);
        navCommonView.setCurrentPosition(0);
        navCommonView.setNavType("待发货", "待收货", "待评价", "已完成", "");

        mFragments = new ArrayList<>();
        mFragments.add(OrderStoreFragment.getInstance(0));
        mFragments.add(OrderStoreFragment.getInstance(1));
        mFragments.add(OrderStoreFragment.getInstance(2));
        mFragments.add(OrderStoreFragment.getInstance(3));
        OrderPagerAdapter adapter = new OrderPagerAdapter(getChildFragmentManager(), mFragments);
        orderPage.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        navCommonView.setOnClickListener(new NavCommonView.OnClickListener() {
            @Override
            public void onClick(int position) {
                orderPage.setCurrentItem(position);
                mFragments.get(position).onResume();
            }
        });

        orderPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navCommonView.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void loadData() {

    }


    @Subscriber(tag = EventTags.CHECK_SHOP_INDEX)
    public void checkIndex(int position){
        orderPage.setCurrentItem(position);
    }
}
