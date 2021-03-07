package com.yimeng.haidou.shop;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author xp
 *         订单评价结果
 */

public class OrderCommentResultActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_order_comment_result;
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


    @OnClick({R.id.lookOrderTV})
    public void myOnClick() {
        finish();
    }
}
