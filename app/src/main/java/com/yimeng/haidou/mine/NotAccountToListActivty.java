package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.NotAccountToAdapter;
import com.yimeng.entity.ModelOrderSettle;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.widget.ExpandListView;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * @author xp
 *         未入账交易列表
 */

public class NotAccountToListActivty extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ExpandListView listView;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    /**
     * 页码
     */
    private int mPage;
    /**
     * 交易列表adapter
     */
    private NotAccountToAdapter mNotAccountToAdapter;



    @Override
    protected int setLayoutResId() {
        return R.layout.activity_not_accountto_list;
    }

    @Override
    protected void init() {
        LinkedList<ModelOrderSettle> busList = new LinkedList<>();
        mNotAccountToAdapter = new NotAccountToAdapter(this, mHandler, busList, ConstantHandler.WHAT_HANDLER_CLICK);
        listView.setAdapter(mNotAccountToAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestOrderSettle(mPage, "0", mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestOrderSettle(mPage, "0", mHandler);
            }
        });
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }



    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<NotAccountToListActivty> mImpl;

        public MyHandler(NotAccountToListActivty mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_ORDER_SETTLE_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;

                mNotAccountToAdapter.mList = (LinkedList<ModelOrderSettle>) msg.obj;
                mNotAccountToAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_ORDER_SETTLE_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                mNotAccountToAdapter.mList.addAll((LinkedList<ModelOrderSettle>) msg.obj);
                mNotAccountToAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_ORDER_SETTLE_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_ORDER_SETTLE_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                break;
            default:
                break;
        }
    }


}
