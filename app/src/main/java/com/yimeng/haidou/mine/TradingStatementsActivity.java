package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.BusinessListAdapter;
import com.yimeng.entity.BusinessModel;
import com.yimeng.entity.ModelTrdeStatistic;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
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
 * 交易报表
 *
 * @author xp
 * @describe 交易报表
 * @date 2018/6/5.
 */

public class TradingStatementsActivity extends BaseActivity implements OnDateSetListener {


    @Bind(R.id.totalOrderTV)
    TextView totalOrderTV;
    @Bind(R.id.totalIncomeTV)
    TextView totalIncomeTV;
    @Bind(R.id.todayOrderTV)
    TextView todayOrderTV;
    @Bind(R.id.todayIncomeTV)
    TextView todayIncomeTV;
    @Bind(R.id.dateTV)
    TextView dateTV;
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.tv_order_today)
    TextView tvOrderToday;
    @Bind(R.id.tv_money_today)
    TextView tvMoneyToday;

    /**
     * 页码
     */
    private int mPage;
    /**
     * 日期选择
     */
    private TimePickerDialog mDialogYearMonthDay;

    private BusinessListAdapter mBusinessListAdapter;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_trading_statements;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 1);

        long years = 365 * 1000 * 60 * 60 * 24L;
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setMinMillseconds(System.currentTimeMillis() - 80 * years)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.theme_color))
                .setCallBack(this)
                .setTitleStringId("日期选择")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setCancelStringId(getString(R.string.cancel))
                .setSureStringId("确定")
                .build();

        LinkedList<BusinessModel> mList = new LinkedList<>();
        mBusinessListAdapter = new BusinessListAdapter(this, mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mBusinessListAdapter);

        mPage = 1;
        String text = DateUtil.getAssignDate(new Date().getTime(), 1);
        dateTV.setText(text);
        getData(text);

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onLeftClick() {
                super.onLeftClick();
                finish();
            }

            @Override
            public void onRightClick() {
                super.onRightClick();
                // 查看报表
                Intent intent = new Intent(TradingStatementsActivity.this, LookStatementsActivity.class);
                intent.putExtra("type", mType);
//                if(mType == 2) {
                intent.putExtra("shopNo", getIntent().getStringExtra("shopNo"));
//                }
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    private int mType;// type=2 : 城市服务商查看报表


    @OnClick({R.id.dateRL})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.dateRL:
                mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = DateUtil.getAssignDate(millseconds, 1);
        dateTV.setText(text);
        mPage = 1;
       getData(text);
    }

    private void getData(String text){
        String token;
//        if(mType == 2) {
            token = getIntent().getStringExtra("shopNo");
//        }else{
//            token = PreferenceUtil.getString(ConstantOther.KEY_APP_TOKEN, "");
//        }
        HttpParameterUtil.getInstance().requestTradeStatistic(mPage,token, text, mHandler);
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<TradingStatementsActivity> mImpl;

        public MyHandler(TradingStatementsActivity mImpl) {
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
            case ConstantHandler.WHAT_TRDE_STATISTIC_SUCCESS:
                mPage = 2;
                ModelTrdeStatistic trdeStatistic = (ModelTrdeStatistic) msg.obj;

                // 总订单数
                totalOrderTV.setText(trdeStatistic.getTotalOrderCount() + "\n" + "总订单数");
                // 总收入
                totalIncomeTV.setText(UnitUtil.getMoney(trdeStatistic.getTotalEarn()));
                // 当天订单数
                todayOrderTV.setText("当天订单数" + trdeStatistic.getMonthOrderCount());
                // 当天收入
                todayIncomeTV.setText("当天收入" + UnitUtil.getMoney(trdeStatistic.getMonthEarn()));

                if(isFirstLoad) {
                    // 今日订单数
                    tvOrderToday.setText(trdeStatistic.getMonthOrderCount() +"\n今日订单数");
                    // 今日收入
                    tvMoneyToday.setText(UnitUtil.getMoney(trdeStatistic.getMonthEarn()) +"\n今日收入(元)");
                    isFirstLoad = false;
                }


                mBusinessListAdapter.mList = trdeStatistic.getmBusinessList();
                mBusinessListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_TRDE_STATISTIC_FAIL:
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

    private boolean isFirstLoad = true;
}
