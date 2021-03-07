package com.yimeng.haidou.mine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.OrderPagerAdapter;
import com.yimeng.widget.CustomViewPager;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.NavCommonView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lib
 *         分类商品列表
 */

public class GoodsListActivity extends BaseActivity implements NavCommonView.OnClickListener {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.navCommonView)
    NavCommonView navCommonView;
    @Bind(R.id.order_page)
    CustomViewPager orderViewPager;

    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * 分类编号
     */
    private String mProductCategoryNo;

    private ClassifyGoodsFragment mClassifyGoodsFragment1;
    private ClassifyGoodsFragment mClassifyGoodsFragment2;
    private ClassifyGoodsFragment mClassifyGoodsFragment3;
    private ClassifyGoodsFragment mClassifyGoodsFragment4;

    /**
     * entity 　自有商品
     * cuxiao   促销商品
     */
    private String mProductType = "entity";

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_goods_list;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mShopNo = intent.getStringExtra("mShopNo");
        mProductType = intent.getStringExtra("productType");
        mProductCategoryNo = intent.getStringExtra("mClassifyNo");
        String title = intent.getStringExtra("title");
        toolBar.setTitle(title);

        List<Fragment> fragments = new ArrayList<>(4);
        mClassifyGoodsFragment1 = new ClassifyGoodsFragment(0, mShopNo, mProductType, mProductCategoryNo);
        mClassifyGoodsFragment2 = new ClassifyGoodsFragment(1, mShopNo, mProductType, mProductCategoryNo);
        mClassifyGoodsFragment3 = new ClassifyGoodsFragment(2, mShopNo, mProductType, mProductCategoryNo);
        mClassifyGoodsFragment4 = new ClassifyGoodsFragment(3, mShopNo, mProductType, mProductCategoryNo);
        fragments.add(mClassifyGoodsFragment1);
        fragments.add(mClassifyGoodsFragment2);
        fragments.add(mClassifyGoodsFragment3);
        fragments.add(mClassifyGoodsFragment4);
        OrderPagerAdapter adapter = new OrderPagerAdapter(getSupportFragmentManager(), fragments);
        orderViewPager.setNoScroll(true);
        orderViewPager.setAdapter(adapter);
        orderViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        navCommonView.setOnClickListener(this);
        navCommonView.setCurrentNavCount(4);
        navCommonView.setCurrentPosition(0);
        navCommonView.setNavType(getString(R.string.on_sale), getString(R.string.in_the_warehouse), getString(R.string.under_review), getString(R.string.rejected), "");
//        navCommonView.setNavType(getString(R.string.on_sale), getString(R.string.in_the_warehouse), "", "", "");

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                Intent intent = new Intent(GoodsListActivity.this, EditCommodityActivity.class);
                intent.putExtra("mShopNo", mShopNo);
                intent.putExtra("productType", mProductType);
                intent.putExtra("mProductCategoryNo", mProductCategoryNo);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {

    }



    @Override
    public void onClick(int position) {
        orderViewPager.setCurrentItem(position);

        if (mClassifyGoodsFragment1 != null &&
                mClassifyGoodsFragment2 != null
                && mClassifyGoodsFragment3 != null
                && mClassifyGoodsFragment4 != null
                ) {
            switch (position) {
                case 0:
                    mClassifyGoodsFragment1.onAutoRefresh();
                    break;
                case 1:
                    mClassifyGoodsFragment2.onAutoRefresh();
                    break;
                case 2:
                    mClassifyGoodsFragment3.onAutoRefresh();
                    break;
                case 3:
                    mClassifyGoodsFragment4.onAutoRefresh();
                    break;
                default:
                    break;
            }
        }
    }


}
