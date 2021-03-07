package com.yimeng.haidou.order;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/19 0019 下午 08:14.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 退货流程
 * </pre>
 */
public class ReturnFlowChartActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_return_flow_chart;
    }

    @Override
    protected void init() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }
}
