package com.yimeng.haidou.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.OrderPagerAdapter;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.NavMyFavoriteView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/24 0024 下午 06:47.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的收藏
 * </pre>
 */
public class MyFavoriteActivity extends BaseActivity implements NavMyFavoriteView.OnClickListener{


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.order_page)
    ViewPager orderViewPager;
    @Bind(R.id.title_order)
    NavMyFavoriteView orderView;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_favorite;
    }

    @Override
    protected void init() {
        List<Fragment> fragments = new ArrayList<>(4);
        fragments.add(new GoodsFragment());
//        fragments.add(new MerchantFragment());
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
        orderView.setOnClickListener(this);
    }



    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());


    }

    @Override
    public void onClick(int position) {
        orderViewPager.setCurrentItem(position);
    }

    @Override
    protected void loadData() {

    }


}
