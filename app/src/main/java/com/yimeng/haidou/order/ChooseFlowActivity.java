package com.yimeng.haidou.order;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.FlowListModel;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.CommonBaseAdapter;
import com.yimeng.haidou.mine.adapter.ViewHolder;
import com.yimeng.widget.MyToolBar;

import java.lang.ref.WeakReference;

import butterknife.Bind;

/**
 * 选择物流
 * Author:Asher
 */
public class ChooseFlowActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.activity_choose_flow_list_lv)
    ListView activity_choose_flow_list_lv;

    private MyHandler mMyHandler;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_choose_flow;
    }

    @Override
    protected void init() {
        mMyHandler = new MyHandler(ChooseFlowActivity.this);

        HttpParameterUtil.getInstance().queryFlowList(mMyHandler);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    private static class MyHandler extends Handler {

        private WeakReference<ChooseFlowActivity> mImpl;

        public MyHandler(ChooseFlowActivity chooseFlowActivity) {

            this.mImpl = new WeakReference<>(chooseFlowActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().handleMsg(msg);
            }
        }
    }

    private void handleMsg(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_FLOW_LIST_SUCCESS:
                FlowListModel flowListModel = (FlowListModel) msg.obj;
                initAdapter(flowListModel);
                break;
        }
    }

    private void initAdapter(final FlowListModel flowListModel) {

        CommonBaseAdapter adapter = new CommonBaseAdapter<FlowListModel.DataBean>(this, R.layout.item_flow_lv, flowListModel.getData()) {

            @Override
            public void convert(ViewHolder holder, FlowListModel.DataBean bean, int position) {

                TextView flowCompName = holder.getView(R.id.item_flow_company_name_tv);
                flowCompName.setText(bean.getLogisticsCompanyName());
            }
        };
        activity_choose_flow_list_lv.setAdapter(adapter);

        activity_choose_flow_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = flowListModel.getData().get(position).getLogisticsCompanyName();
                String pinyin = flowListModel.getData().get(position).getLogisticsCompanyPinyin();
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("pinyin", pinyin);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
