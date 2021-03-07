package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.LookStatementsAdapter;
import com.yimeng.entity.ModelOrderReport;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.DateUtil;
import com.yimeng.widget.MyToolBar;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 查看报表
 *
 * @author xp
 * @describe 查看报表
 * @date 2018/6/5.
 */

public class LookStatementsActivity extends BaseActivity implements OnDateSetListener {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.dateTV)
    TextView dateTV;
    @Bind(R.id.listview)
    ListView mListView;

    /**
     * 日期选择
     */
    private TimePickerDialog mDialogYearMonthDay;

    private LookStatementsAdapter mLookStatementsAdapter;

    private int mType;
    @Override
    protected int setLayoutResId() {
        return R.layout.activity_look_statements;
    }

    @Override
    protected void init() {
        mType = getIntent().getIntExtra("type", 1);

        long years = 365 * 1000 * 60 * 60 * 24L;
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setMinMillseconds(System.currentTimeMillis() - 80 * years)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.theme_color))
                .setCallBack(this)
                .setTitleStringId("日期选择")
                .setYearText("")
                .setMonthText("")
                .setCancelStringId(getString(R.string.cancel))
                .setSureStringId("确定")
                .build();

        LinkedList<ModelOrderReport> mList = new LinkedList<>();
        mLookStatementsAdapter = new LookStatementsAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mLookStatementsAdapter);

        String text = DateUtil.getAssignDate(new Date().getTime(), 4);
        dateTV.setText(text);
        getData(text);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }



    @OnClick({R.id.dateRL,})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.dateRL:
                mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = DateUtil.getAssignDate(millseconds, 4);
        dateTV.setText(text);
        getData(text);
    }

    private void getData(String text){
        String token;
//        if(mType == 2) {
            token = getIntent().getStringExtra("shopNo");
//        }else{
//            token = PreferenceUtil.getString(ConstantOther.KEY_APP_TOKEN, "");
//        }
        HttpParameterUtil.getInstance().requestQueryOrderReport(token, text, mHandler);
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<LookStatementsActivity> mImpl;

        public MyHandler(LookStatementsActivity mImpl) {
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

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_ORDER_REPORT_SUCCESS:
                mLookStatementsAdapter.mList = (LinkedList<ModelOrderReport>) msg.obj;
                mLookStatementsAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_ORDER_REPORT_FAIL:
                break;
            default:
                break;
        }
    }
}
