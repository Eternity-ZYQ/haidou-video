package com.yimeng.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelSimple;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.AfterSalesServiceAdapter;
import com.yimeng.widget.ExpandGridView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 售后服务
 */

public class DialogAfterSalesServiceActivity extends AppCompatActivity {

    @Bind(R.id.gv_afer_sales_service)
    ExpandGridView mGridView;
    @Bind(R.id.ll_dialog_after_sales_service)
    LinearLayout mLL;

    @OnClick(R.id.rl_submit)
    void onclick() {
        finish();
    }

    @OnClick(R.id.rl_finish)
    void closeDialog() {
        finish();
    }

    private AfterSalesServiceAdapter mAdapter;
    private LinkedList<ModelSimple> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    protected void initView() {
        setContentView(R.layout.dialog_after_sales_service);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    protected void initData() {
        mList = new LinkedList<>();
        Intent intent = getIntent();
        String customService = intent.getStringExtra("customService");
        if (customService != null && !TextUtils.isEmpty(customService)) {
            String[] item = customService.split("Ë");
            int p = item.length;
            for (int i = 0; i < p; i++) {
                mList.add(new ModelSimple(item[i]));
            }
            mAdapter = new AfterSalesServiceAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
            mGridView.setAdapter(mAdapter);
        }

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<DialogAfterSalesServiceActivity> mImpl;

        public MyHandler(DialogAfterSalesServiceActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                // mImpl.get().disposeData(msg);
            }
        }
    }

}
