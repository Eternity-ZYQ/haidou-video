package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.entity.ModelPayRecords;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.haidou.home.adapter.PayRecordsAdapter;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * xp
 * 充值记录
 */

public class PayRecordsActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage;
    private PayRecordsAdapter mRecordsAdapter;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_pay_records;
    }

    @Override
    protected void init() {

        LinkedList<ModelPayRecords> mList = new LinkedList<>();
        mRecordsAdapter = new PayRecordsAdapter(this, mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mRecordsAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpParameterUtil.getInstance().requesttPayRecords(mPage, mHandler);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requesttPayRecords(mPage, mHandler);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<PayRecordsActivity> mImpl;

        public MyHandler(PayRecordsActivity mImpl) {
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
            case ConstantHandler.WHAT_PAY_RECORDS_SUCCESS:
                mRefreshLayout.finishRefresh();
                ModelPayRecords payRecords = (ModelPayRecords) msg.obj;
//                moneyTV.setText(UnitUtil.getMoney(payRecords.getTotalRechargeMoney()));
//                mmUnitTV.setText(payRecords.getTotalRechargeCount());
                mRecordsAdapter.mList = payRecords.getmRecordList();
                mRecordsAdapter.notifyDataSetChanged();
                mPage = 2;
                if(mRecordsAdapter.mList.size() < Constants.MAX_LIMIT) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                break;
            case ConstantHandler.WHAT_PAY_RECORDS_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_PAY_RECORDS_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                payRecords = (ModelPayRecords) msg.obj;
                mRecordsAdapter.mList.addAll(payRecords.getmRecordList());
                mRecordsAdapter.notifyDataSetChanged();
                mPage = payRecords.getmRecordList().size() > 0 ? ++mPage : mPage;
                break;
            case ConstantHandler.WHAT_PAY_RECORDS_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            default:
                break;
        }
    }

}
