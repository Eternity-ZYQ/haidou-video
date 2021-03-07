package com.yimeng.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yimeng.interfaces.HandleBackInterface;
import com.yimeng.utils.HandleBackUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Author : huiGer
 * Time   : 2018/6/13 0013 上午 09:28.
 * Desc   : BaseFragment
 */
public abstract class BaseFragment extends Fragment implements HandleBackInterface {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(setLayoutResId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        init();
        initListener();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onBackPressed() {
        return HandleBackUtil.handleBackPress(this);
    }


    protected abstract int setLayoutResId();
    protected abstract void init();
    protected abstract void initListener();
    protected abstract void loadData();

    /**
     * 加载数据失败
     */
    protected void showSmartRefreshGetDataFail(SmartRefreshLayout smartRefreshLayout, int page){
        if(smartRefreshLayout == null) return;
        if(page == 1) {
            smartRefreshLayout.finishRefresh();
        }else{
            smartRefreshLayout.finishLoadMore();
        }
    }

}
