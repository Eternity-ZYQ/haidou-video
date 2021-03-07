package com.yimeng.haidou.shop;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.BannerItem;
import com.yimeng.entity.ModelGetBannerList;
import com.yimeng.entity.ModelHotSalesCategoryList;
import com.yimeng.haidou.R;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/17 0017 下午 05:48.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商城
 * </pre>
 */
public class TabShopFragment extends BaseFragment {
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.sib_mall_banner)
    BGABanner mBanner;  //banner
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private List<ModelHotSalesCategoryList> classifyList;
    private ShopListFragmentAdapter mFragmentPagerAdapter;
    private boolean mRefreshGategory = true;//刷新分类 true可以刷新
    private MyHandler mHandler = new MyHandler(this);
    private LinkedList<ModelGetBannerList> modelGetBannerLists;  //轮播图
    private LinkedList<BannerItem> bannerList = new LinkedList<>();
    private boolean bannerHasLoad = false;



    @OnClick({R.id.iv_more, R.id.layout_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                // 更多分类
                startActivity(new Intent(getActivity(), AllSortListActivity.class));
                break;
            case R.id.layout_search:
                // 搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:

        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            mRefreshGategory = true;
            loadData();
        }
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void init() {

        classifyList = new ArrayList<>();
        mFragmentPagerAdapter = new ShopListFragmentAdapter(getFragmentManager());
        viewPager.setAdapter(mFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                ShopProductListFragment currentFragment = mFragmentPagerAdapter.getCurrentFragment();
                if (currentFragment != null) {
                    currentFragment.loadmoreData(smartRefresh);
                } else {
                    smartRefresh.finishLoadMore();
                }

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ShopProductListFragment currentFragment = mFragmentPagerAdapter.getCurrentFragment();
                if (currentFragment != null) {
                    currentFragment.refreshData(smartRefresh);
                } else {
                    smartRefresh.finishRefresh();
                }
                loadData();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ShopProductListFragment currentFragment = mFragmentPagerAdapter.getCurrentFragment();
                if (currentFragment != null) {
                    smartRefresh.autoRefresh();
                    currentFragment.refreshData(smartRefresh);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestHotSalesCategoryList("hotSales", mHandler);
        HttpParameterUtil.getInstance().requestGetBannerList("banner", mHandler);
    }

    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_HOT_SALES_CATEGORY_LIST_SUCCESS:
                //列表
                if ((classifyList != null && classifyList.size() != 0) && !mRefreshGategory) {
//                if ((classifyList != null && classifyList.size() != 0)) {
                    return;
                }
                classifyList = (LinkedList<ModelHotSalesCategoryList>) msg.obj;

                if (classifyList.size() == 0) {
                    return;
                }
                mFragmentPagerAdapter.notifyDataSetChanged();
                smartRefresh.autoRefresh();
                mRefreshGategory = false;
                break;
            case ConstantHandler.WHAT_GET_BANNER_LIST_SUCCESS:
                //banner图
                if (null != modelGetBannerLists) {
                    return;
                }
                modelGetBannerLists = (LinkedList<ModelGetBannerList>) msg.obj;

                if (!bannerHasLoad) {
                    for (int i = 0; i < modelGetBannerLists.size(); i++) {
                        String logo = modelGetBannerLists.get(i).getLogo();
                        String name = modelGetBannerLists.get(i).getName();
                        String other = modelGetBannerLists.get(i).getOther();
                        bannerList.add(new BannerItem(logo, name, other));
                        bannerHasLoad = true;
                    }
                }
                mBanner.setData(bannerList, null);
                mBanner.setAdapter(new BGABanner.Adapter<ImageView, BannerItem>() {
                    @Override
                    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable BannerItem model, int position) {
                        CommonUtils.showImage(itemView, model.getImgUrl());
                    }
                });


                break;
            default:

        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<TabShopFragment> impl;

        public MyHandler(TabShopFragment impl) {
            this.impl = new WeakReference<>(impl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (impl.get() != null) {
                impl.get().disposeData(msg);
            }
        }
    }

    private class ShopListFragmentAdapter extends FragmentPagerAdapter {
        private ShopProductListFragment currentFragment;

        public ShopListFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ShopProductListFragment.getNewInstance(classifyList.get(position).getMenuNo());
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (ShopProductListFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return classifyList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            ModelHotSalesCategoryList category = classifyList.get(position);
            if (category == null) {
                return "";
            }
            return category.getName();
        }

        public ShopProductListFragment getCurrentFragment() {
            return currentFragment;
        }
    }
}
