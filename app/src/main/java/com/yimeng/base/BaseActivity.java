package com.yimeng.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.yimeng.baiduAi.BaiDuWakeUpManager;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.HandleBackUtil;
import com.yimeng.utils.WindowUiUtil;
import com.yimeng.widget.UtilsStyle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Author : huiGer
 * Time : 2018/6/13 0013 上午 09:15.
 * Desc : BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityUtils.getInstance().addActivity(this);


//        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
//        StatusBarUtil.StatusBarLightMode(this, true);

        //设置透明状态栏和导航栏
//        StatusBarUtil.setTranslucent(this);

        //设置透明状态栏
        WindowUiUtil.extendStatusBar(this);


        setContentView(setLayoutResId());

        UtilsStyle.statusBarLightMode(this);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        init();
        initListener();
        loadData();

        initBaiDuAi();
    }

    private void initBaiDuAi() {
        BaiDuWakeUpManager.getInstance(this);
    }

    protected abstract int setLayoutResId();

    protected abstract void init();

    protected abstract void initListener();

    protected abstract void loadData();

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == ConstantHandler.BAIDU_REQUEST_CODE && resultCode == RESULT_OK) {
//            ArrayList<String> results = data.getStringArrayListExtra("results");
//
//            if (results != null && results.size() > 0) {
//                String message = results.get(0);
//
//                LogUtils.d("识别结果：" + message);
//            }
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        ActivityUtils.getInstance().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (!HandleBackUtil.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            CommonUtils.hideKeyboard(ev, view);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 加载数据失败
     */
    protected void showSmartRefreshGetDataFail(SmartRefreshLayout smartRefreshLayout, int page) {
        if (smartRefreshLayout == null) return;
        if (page == 1) {
            smartRefreshLayout.finishRefresh();
        } else {
            smartRefreshLayout.finishLoadMore();
        }
    }

}
