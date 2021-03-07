package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelTransactionRecords;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.TransactionRecordsAdapter;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * 交易记录
 *
 * @author xp
 * @describe 交易记录.(余额交易记录, 麻豆交易记录, 交易记录)
 * @date 2018/6/5.
 */

public class TransactionRecordsActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    /**
     * 页码
     */
    private int mPage;
    /**
     * recharge（充值），zhannei("用户余额"),shop("店铺余额"),  zhanneiFreeze("收益")
     */
    private String mAmtType;

    private TransactionRecordsAdapter mRecordsAdapter;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_transaction_records;
    }

    @Override
    protected void init() {
        mAmtType = "zhannei";

        LinkedList<ModelTransactionRecords> mList = new LinkedList<>();
        mRecordsAdapter = new TransactionRecordsAdapter(this, mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mRecordsAdapter);
        mRefreshLayout.autoRefresh();

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpParameterUtil.getInstance().requesttTrasactionRecords(mAmtType, mPage, mHandler);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requesttTrasactionRecords(mAmtType, mPage, mHandler);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_TRANSACTION_RECORDS_SUCCESS:
                mPage = 2;
                mRefreshLayout.finishRefresh();
                mRecordsAdapter.mList = (LinkedList<ModelTransactionRecords>) msg.obj;
                mRecordsAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_TRANSACTION_RECORDS_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_TRANSACTION_RECORDS_MORE_SUCCESS:
                mPage++;
                mRefreshLayout.finishLoadMore();
                mRecordsAdapter.mList.addAll((LinkedList<ModelTransactionRecords>) msg.obj);
                mRecordsAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_TRANSACTION_RECORDS_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<TransactionRecordsActivity> mImpl;

        public MyHandler(TransactionRecordsActivity mImpl) {
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
}
