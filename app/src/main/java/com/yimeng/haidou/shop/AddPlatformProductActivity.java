package com.yimeng.haidou.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.goodsClassify.TabGoodsClassifyFragment;

import org.simple.eventbus.Subscriber;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-22 14:55:43
 *  Email  : zhihuiemail@163.com
 *  Desc   : 添加平台商品
 */
public class AddPlatformProductActivity extends BaseActivity {


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_add_platfrom_product;
    }

    @Override
    protected void init() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getExtras();
        bundle.putBoolean("isSale", true);
        TabGoodsClassifyFragment fragment = TabGoodsClassifyFragment.getInstanceGoodsClassify(bundle);
        transaction.add(R.id.layout_content, fragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    @Subscriber(tag = "addProductToSale")
    public void onResult(boolean flag){
        if(flag) {
            finish();
        }
    }
}
