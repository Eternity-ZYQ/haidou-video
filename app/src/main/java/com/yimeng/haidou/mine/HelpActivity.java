package com.yimeng.haidou.mine;

import android.widget.ImageView;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018-12-26 14:15:58
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 */
public class HelpActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.iv)
    ImageView iv;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_help;
    }

    @Override
    protected void init() {
        toolBar.setTitle("扫码指引");

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }
}
