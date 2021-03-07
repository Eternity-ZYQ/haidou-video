package com.yimeng.haidou.shop;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;

/**
 * 商城订单
 */

public class MallOrderActivity extends BaseActivity {


    private MallOrderFragment mFragment;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_mall_order;
    }

    @Override
    protected void init() {

        mFragment = new MallOrderFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_content, mFragment);
        transaction.commit();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            // 评价完成
            mFragment.onClick(3);
        }
    }
}
