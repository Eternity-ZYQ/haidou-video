package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelOrderDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;

import java.lang.ref.WeakReference;

import butterknife.Bind;

/**
 * 交易详情
 *
 * @author xp
 * @describe 交易详情.
 * @date 2018/7/19.
 */

public class BusinessDetailActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.incomeMoneyTV)
    TextView incomeMoneyTV;
    @Bind(R.id.currentStatusTV)
    TextView currentStatusTV;
    @Bind(R.id.payerTV)
    TextView payerTV;
    @Bind(R.id.orderNoTV)
    TextView orderNoTV;
    @Bind(R.id.payTypeTV)
    TextView payTypeTV;
    @Bind(R.id.payMoneyTV)
    TextView payMoneyTV;
    @Bind(R.id.businessTimeTV)
    TextView businessTimeTV;
    @Bind(R.id.businessNameTV)
    TextView businessNameTV;

    /**
     * 订单编号
     */
    private String mOrderNo;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_business;
    }

    @Override
    protected void init() {
        mOrderNo = getIntent().getStringExtra("mOrderNo");
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestOrderDetail(mOrderNo, mHandler);
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<BusinessDetailActivity> mImpl;

        public MyHandler(BusinessDetailActivity mImpl) {
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
            case ConstantHandler.WHAT_ORDER_DETAIL_SUCCESS:
                ModelOrderDetail orderDetail = (ModelOrderDetail) msg.obj;
                incomeMoneyTV.setText(UnitUtil.getMoney(orderDetail.getDueAmt()));
                currentStatusTV.setText(orderDetail.getOrderStatusStr());
                payerTV.setText(orderDetail.getNickname());
                orderNoTV.setText(orderDetail.getOrderNo());
                payTypeTV.setText(orderDetail.getPayTypeStr());
                payMoneyTV.setText(UnitUtil.getMoney(orderDetail.getRealAmt()));
                businessTimeTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(orderDetail.getCreateTime()), 3));
                businessNameTV.setText(orderDetail.getOrderName());
                break;
            default:
                break;
        }
    }
}
