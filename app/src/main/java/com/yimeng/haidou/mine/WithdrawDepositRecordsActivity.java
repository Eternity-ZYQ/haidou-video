package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelWithdrawDeposit;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.WithdrawDepositRecordsAdapter;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * xp
 * 提现记录
 */

public class WithdrawDepositRecordsActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage;
    private WithdrawDepositRecordsAdapter mRecordsAdapter;
    private String mWithdrawType;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_withdraw_deposit_records;
    }

    @Override
    protected void init() {
        mWithdrawType = getIntent().getStringExtra("type");

        LinkedList<ModelWithdrawDeposit> mList = new LinkedList<>();
        mRecordsAdapter = new WithdrawDepositRecordsAdapter(this, mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mRecordsAdapter);

        mRefreshLayout.autoRefresh();

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpParameterUtil.getInstance().requestWithdrawApply(mPage, mHandler, mWithdrawType);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestWithdrawApply(mPage, mHandler, mWithdrawType);
            }
        });
    }

    @Override
    protected void loadData() {

    }


    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<WithdrawDepositRecordsActivity> mImpl;

        public MyHandler(WithdrawDepositRecordsActivity mImpl) {
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
            case ConstantHandler.WHAT_WITHDRAW_APPLY_SUCCESS:
                mRefreshLayout.finishRefresh();
                mRecordsAdapter.mList = (LinkedList<ModelWithdrawDeposit>) msg.obj;
                mRecordsAdapter.notifyDataSetChanged();
                mPage = 2;
                break;
            case ConstantHandler.WHAT_WITHDRAW_APPLY_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_WITHDRAW_APPLY_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mRecordsAdapter.mList.addAll((Collection<? extends ModelWithdrawDeposit>) msg.obj);
                mRecordsAdapter.notifyDataSetChanged();
                mPage++;
                break;
            case ConstantHandler.WHAT_WITHDRAW_APPLY_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case 0x01:
                String reason = (String) msg.obj;
                if (reason != null && !reason.isEmpty()) {
                    SimpleDialog.showSimpleRemarkWithTitleDialog(WithdrawDepositRecordsActivity.this, "驳回原因", reason);
                }
                break;
            default:
                break;
        }
    }

}
