package com.yimeng.haidou.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author lijb
 */

public class OrderPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> pages;

    public OrderPagerAdapter(FragmentManager fm, List<Fragment> pages) {
        super(fm);
        this.pages = pages;
    }


    @Override
    public int getCount() {
        return pages == null ? 0 : pages.size();
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }


    /*@Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        return pages.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        pages.remove(position);
    }*/
}
