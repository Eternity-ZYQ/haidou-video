package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelBonus;
import com.yimeng.haidou.R;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的收益
 */

public class CityServiceProviderNewActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.moneyTV)
    TextView moneyTV;
    @Bind(R.id.unSettlementTv)
    TextView unSettlementTv;
    @Bind(R.id.canWithdrawMoneyTV)
    TextView canWithdrawMoneyTV;

    /**
     * 我的收益统计
     */
    private ModelBonus mModelBonus;
    private String canWithdrawMoney = "0.00";
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_city_service_provider_new;
    }

    @Override
    protected void init() {


    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestBonus(mHandler);
    }

    @OnClick({R.id.clickTwoRL, R.id.clickOneRL})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.clickOneRL:
//                // 我的区域现金交易分润
                Intent intent = new Intent(this, WithdrawDepositActivity.class);
                intent.putExtra("mWithdrawType", "yongjin");
                intent.putExtra("allMoney", canWithdrawMoney);
                startActivity(intent);
                break;
            case R.id.clickTwoRL:
//                // 推荐区域现金交易分分润
                intent = new Intent(this, FenRunRecordsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_BONUS_SUCCESS:
                mModelBonus = (ModelBonus) msg.obj;
                showData();
                break;
            default:
                break;
        }
    }

    private void showData() {

        if (null != mModelBonus) {
            moneyTV.setText(UnitUtil.getMoney(mModelBonus.getBonusAccountAmt()));
            unSettlementTv.setText(UnitUtil.getMoney(mModelBonus.getUnSettlement()));
            Double canWi = Double.parseDouble(mModelBonus.getBonusAccountAmt()) - Double.parseDouble(mModelBonus.getUnSettlement());
            if (canWi > 0) {
                canWithdrawMoneyTV.setText(UnitUtil.getMoney(canWi.toString()));
                canWithdrawMoney = UnitUtil.getMoney(canWi.toString(), false);
            } else {
                canWithdrawMoneyTV.setText("¥0.00");
                canWithdrawMoney = "0.00";
            }
        }

    }

    private static class MyHandler extends Handler {

        private WeakReference<CityServiceProviderNewActivity> mImpl;

        public MyHandler(CityServiceProviderNewActivity mImpl) {
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
}
