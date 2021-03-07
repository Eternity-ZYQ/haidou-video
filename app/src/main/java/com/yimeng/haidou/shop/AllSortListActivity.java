package com.yimeng.haidou.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.goodsClassify.TabGoodsClassifyFragment;
import com.yimeng.widget.MyToolBar;

import butterknife.Bind;

/**
 * 所有分类
 */

public class AllSortListActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_all_sort;
    }

    @Override
    protected void init() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHideTitle", true);
        TabGoodsClassifyFragment fragment = TabGoodsClassifyFragment.getInstanceGoodsClassify(bundle);
        transaction.add(R.id.frame_content, fragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


}
