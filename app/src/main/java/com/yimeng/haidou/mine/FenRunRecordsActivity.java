package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelPayRecords;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.CommonBaseAdapter;
import com.yimeng.haidou.mine.adapter.ViewHolder;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.DateUtil;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * 分润记录
 * 我的收益列表
 *
 * Asher 复制修改‘充值记录’ 2018-10-8
 *
 */

public class FenRunRecordsActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.fen_yun_empty_view_tv)
    TextView empty_view;

    private int mPage;
    private CommonBaseAdapter<ModelPayRecords> mRecordsAdapter;
    private LinkedList<ModelPayRecords> mList;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_fen_run_records;
    }

    @Override
    protected void init() {
        mList = new LinkedList<>();
//        mRecordsAdapter = new PayRecordsAdapter(this, mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
        mRecordsAdapter = new CommonBaseAdapter<ModelPayRecords>(this, R.layout.item_fen_run_records, mList) {
            @Override
            public void convert(ViewHolder holder, ModelPayRecords bean, int position) {

                TextView source = holder.getView(R.id.fen_run_source_tv);
                source.setText(bean.getTitle());
                TextView time = holder.getView(R.id.fen_run_time_tv);
                time.setText(DateUtil.getAssignDate(Long.parseLong(bean.getDate()), 3));
                TextView remark = holder.getView(R.id.fen_run_remark_tv);
                remark.setText(bean.getRemark());
                TextView money = holder.getView(R.id.fen_run_money_tv);
                money.setText(bean.getMoney());
            }
        };
        mListView.setAdapter(mRecordsAdapter);

        mRefreshLayout.autoRefresh();

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpParameterUtil.getInstance().requestFenRunRecords(mPage, mHandler);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestFenRunRecords(mPage, mHandler);
            }
        });

    }

    @Override
    protected void loadData() {

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<FenRunRecordsActivity> mImpl;

        public MyHandler(FenRunRecordsActivity mImpl) {
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
                mList.clear();
                mList.addAll(payRecords.getmRecordList());
//                mList = payRecords.getmRecordList();
                mRecordsAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    mRefreshLayout.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }else {
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    empty_view.setVisibility(View.GONE);
                }
                mPage = 2;
                break;
            case ConstantHandler.WHAT_PAY_RECORDS_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_PAY_RECORDS_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                payRecords = (ModelPayRecords) msg.obj;
                mList.addAll(payRecords.getmRecordList());
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
