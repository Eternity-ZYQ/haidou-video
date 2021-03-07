package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelAccountDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * xp
 * 店铺帐户
 */

public class ShopAccountActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.moneyTV)
    TextView moneyTV;
    @Bind(R.id.notRecordedTV)
    TextView notRecordedTV;
    @Bind({R.id.withdraw_shop_tv})
    TextView withdrawTv;

    /**
     * 帐户详情
     */
    private ModelAccountDetail mModelAccountDetail;
    private boolean canWithdraw = false;// 能否提现


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_account;
    }

    @Override
    protected void init() {
        moneyTV.setText("0");
        notRecordedTV.setText("0");

        // 0-9点才可以提现
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if (hour >= 0 && hour < 9) {
//            canWithdraw = true;
//        } else {
//            withdrawTv.setTextColor(Color.GRAY);
//        }

        canWithdraw = true;
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

        HttpParameterUtil.getInstance().requestQueryShopAccountDetail(mHandler);
    }


    @OnClick({R.id.clickRL, R.id.moneyLL, R.id.notRecordedLL})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.clickRL:
                if (!canWithdraw) return;
                Intent intent = new Intent(this, WithdrawDeposit3Activity.class);
                intent.putExtra("mWithdrawType", "shop");
                intent.putExtra("allMoney", moneyTV.getText().toString());
                startActivity(intent);
                break;
            case R.id.moneyLL:
                intent = new Intent(this, TransactionRecordsActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.notRecordedLL:
                startActivity(new Intent(this, NotAccountToListActivty.class));
                break;
            default:
                break;
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<ShopAccountActivity> mImpl;

        public MyHandler(ShopAccountActivity mImpl) {
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
            case ConstantHandler.WHAT_ACCOUNT_DETAIL_SUCCESS:
                mModelAccountDetail = (ModelAccountDetail) msg.obj;
                moneyTV.setText(UnitUtil.getMoney(mModelAccountDetail.getYongjin(), false));
                notRecordedTV.setText(UnitUtil.getMoney(mModelAccountDetail.getNoSettle(), false));
                break;
            case ConstantHandler.WHAT_ACCOUNT_DETAIL_FAIL:
                break;
            default:
                break;
        }
    }

}
