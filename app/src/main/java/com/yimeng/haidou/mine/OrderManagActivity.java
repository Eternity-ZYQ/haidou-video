package com.yimeng.haidou.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.OrderPagerAdapter;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.NavCommonView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lijb
 *         商家订单
 */

public class OrderManagActivity extends BaseActivity implements NavCommonView.OnClickListener {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.order_page)
    ViewPager orderViewPager;
    @Bind(R.id.title_order)
    NavCommonView orderView;

    private OrderShopFragment OrderShopFragmentNew;
    private OrderShopFragment OrderShopFragmentReceiving;
    private OrderShopFragment OrderShopFragmentComplete;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_order;
    }

    @Override
    protected void init() {

        orderView.setNavType(getString(R.string.new_order), "待收货", getString(R.string.finished), "", "");
        orderView.setOnClickListener(this);
        orderView.setCurrentNavCount(3);
        orderView.setCurrentPosition(0);

        OrderShopFragmentNew = new OrderShopFragment("new");
        OrderShopFragmentReceiving = new OrderShopFragment("noReceiving");
        OrderShopFragmentComplete = new OrderShopFragment("complete");

        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(OrderShopFragmentNew);
        fragments.add(OrderShopFragmentReceiving);
        fragments.add(OrderShopFragmentComplete);
        OrderPagerAdapter adapter = new OrderPagerAdapter(getSupportFragmentManager(), fragments);
        orderViewPager.setAdapter(adapter);
        orderViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }



    @Override
    public void onClick(int position) {
        orderViewPager.setCurrentItem(position);

        if(position == 0){
            OrderShopFragmentNew.onAutoRefresh();
        } else if(position == 1) {
            OrderShopFragmentReceiving.onAutoRefresh();
        }else {
            OrderShopFragmentComplete.onAutoRefresh();
        }
    }
}
