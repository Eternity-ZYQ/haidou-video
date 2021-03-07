package com.yimeng.haidou.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-25 16:15:37
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的代金券
 */
public class MyCouponActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private List<String> titles = Arrays.asList("未使用", "已使用", "已过期");
    /**
     * 购买选择代金券
     */
    private boolean mIsSelect = false;
    /**
     * 是否来源于购买任务
     */
    private boolean mIsTask = false;
    // 3.0任务模块编号
    private String mBlockNo ="";

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_coupon;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mIsSelect = bundle.getBoolean("isSelect", false);
            mIsTask = bundle.getBoolean("isTask", false);
            mBlockNo = bundle.getString("blockNo", "");
        }
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MyCouponFragment.getInstance(mIsSelect, mIsTask, mBlockNo, position);
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

}
