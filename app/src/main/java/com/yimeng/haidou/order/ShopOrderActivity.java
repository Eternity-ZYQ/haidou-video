package com.yimeng.haidou.order;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-4-2 17:27:06
 *  Email  : zhihuiemail@163.com
 *  Desc   : 附近店铺订单
 */
public class ShopOrderActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_order2;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

}
