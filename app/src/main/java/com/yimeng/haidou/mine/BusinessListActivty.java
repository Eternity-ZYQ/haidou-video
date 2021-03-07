package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.BusinessListAdapter;
import com.yimeng.entity.BusinessModel;
import com.yimeng.entity.ModelTrdeStatistic;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;
import com.yimeng.widget.MyToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * @author lijb
 *         交易列表
 */

public class BusinessListActivty extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.orderCountTV)
    TextView orderCountTV;
    @Bind(R.id.incomTV)
    TextView incomTV;
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
    private BusinessListAdapter busAdapter;
    private String mShopNo;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_business_list;
    }

    @Override
    protected void init() {
        mShopNo = getIntent().getStringExtra("shopNo");

        LinkedList<BusinessModel> busList = new LinkedList<>();
        busAdapter = new BusinessListAdapter(this, mHandler, busList, ConstantHandler.WHAT_HANDLER_CLICK);
        listView.setAdapter(busAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestTradeStatistic(mPage, mShopNo, DateUtil.getAssignDate(new Date().getTime(), 1), mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestTradeStatistic(mPage, mShopNo, DateUtil.getAssignDate(new Date().getTime(), 1), mHandler);
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



    @Override
    protected void onResume() {
        super.onResume();
        scrollView.smoothScrollTo(0, 0);
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<BusinessListActivty> mImpl;

        public MyHandler(BusinessListActivty mImpl) {
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

    private void disposeData(Message msg) {
        if(mRefreshLayout == null)return;
        switch (msg.what) {
            case ConstantHandler.WHAT_TRDE_STATISTIC_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                ModelTrdeStatistic model = (ModelTrdeStatistic) msg.obj;
                orderCountTV.setText(model.getMonthOrderCount());
                incomTV.setText(UnitUtil.getMoney(model.getMonthEarn()));
                busAdapter.mList = model.getmBusinessList();
                busAdapter.notifyDataSetChanged();
                if(model.getmBusinessList().size() < Constants.MAX_LIMIT) mRefreshLayout.finishLoadMoreWithNoMoreData();
                break;
            case ConstantHandler.WHAT_TRDE_STATISTIC_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                model = (ModelTrdeStatistic) msg.obj;
                busAdapter.mList.addAll(model.getmBusinessList());
                busAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_TRDE_STATISTIC_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_TRDE_STATISTIC_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                BusinessModel item = (BusinessModel) msg.obj;
                Intent intent = new Intent(this, BusinessDetailActivity.class);
                intent.putExtra("mOrderNo", item.getOrderNo());
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
