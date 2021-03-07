package com.yimeng.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.entity.ModelProductParams;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.CheckParameterAdapter;
import com.yimeng.widget.ExpandGridView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 产品参数
 */

public class DialogCheckParameterActivity extends AppCompatActivity {

    @Bind(R.id.gv_parameter)
    ExpandGridView mGridView;
    @Bind(R.id.ll_dialog_after_sales_service)
    LinearLayout mLL;
    @Bind(R.id.productParamsTV)
    TextView productParamsTV;

    @OnClick(R.id.rl_submit)
    void onclick() {
        finish();
    }

    @OnClick(R.id.rl_finish)
    void closeDialog() {
//        finish();
    }

    private CheckParameterAdapter mAdapter;
    private LinkedList<ModelProductParams> mList;
    ModelProductParams modelProductParams;

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
    }

    protected void initView() {

        setContentView(R.layout.dialog_check_parameter);
        ButterKnife.bind(this);
//        StatusBarCompat.compat(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    protected void initData() {
        mList = new LinkedList<>();

        String productParams = getIntent().getStringExtra("productParams");
        productParams = null == productParams ? "" : productParams;
        productParamsTV.setText(productParams);

//        Intent intent = getIntent();
//        ModelSwitchData modelSwitchData = (ModelSwitchData) intent.getSerializableExtra("modelSwitchData");
//        if (modelSwitchData != null) {
//            int size = modelSwitchData.getmProductParamsList().size();
//            for (int i = 0; i < size; i++) {
//                String key = modelSwitchData.getmProductParamsList().get(i).getKey();
//                String value = modelSwitchData.getmProductParamsList().get(i).getValue();
//                String sort = modelSwitchData.getmProductParamsList().get(i).getSort();
//                modelProductParams = new ModelProductParams(key, value, sort);
//                mList.add(modelProductParams);
//            }
//        }
//
//        mAdapter = new CheckParameterAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
//        mGridView.setAdapter(mAdapter);

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<DialogCheckParameterActivity> mImpl;

        public MyHandler(DialogCheckParameterActivity mImpl) {
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
