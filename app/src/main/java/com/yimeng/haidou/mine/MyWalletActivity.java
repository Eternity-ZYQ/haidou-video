package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelWallet;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.SharedPreferencesUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * xp
 * 我的钱包
 */

public class MyWalletActivity extends BaseActivity {

    @Bind(R.id.moneyTV)
    TextView moneyTV;
    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    /**
     * 我的钱包信息
     */
    private ModelWallet mModelWallet;

    private MyHandler mHandler = new MyHandler(this);
    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void init() {

        moneyTV.setText("0");
        if (mModelWallet != null) {
            moneyTV.setText(UnitUtil.getMoney(mModelWallet.getBalance(), false));
        }

        mOkHttpCommon = new OkHttpCommon();


    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
//                // 余额记录
//                ActivityUtils.getInstance().jumpActivity(TransactionRecordsActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestMyWallet(mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.moneyLL, R.id.item_red_packer, R.id.item_bankCard, R.id.item_settle, R.id.item_earnings,
            R.id.item_bill, R.id.item_recharge, R.id.item_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.moneyLL:          // 余额
                ActivityUtils.getInstance().jumpActivity(TransactionRecordsActivity.class);
                break;
            case R.id.item_red_packer:  // 红包
                break;
            case R.id.item_bankCard:    // 我的银行卡
                ActivityUtils.getInstance().jumpActivity(BankListActivity.class);
                break;
            case R.id.item_settle:      // 我的结算卡

                break;
            case R.id.item_earnings:    // 我的收益
                goMyEarnings();
                break;
            case R.id.item_bill:        // 我的账单

                break;
            case R.id.item_recharge:    // 充值
                ActivityUtils.getInstance().jumpActivity(PayActivity.class);
                break;
            case R.id.item_withdraw:    // 提现
                if (mModelWallet == null) {
                    HttpParameterUtil.getInstance().requestMyWallet(mHandler);
                    return;
                }

                Intent intent = new Intent(this, WithdrawDeposit2Activity.class);
                intent.putExtra("mWithdrawType", "balance");
                double cash = UnitUtil.getDouble(mModelWallet.getScore());
                double cashWithdraw = UnitUtil.getDouble(mModelWallet.getCycleLoginTimes());
                intent.putExtra("allMoney",  Math.min(cash, cashWithdraw)* 0.01 + "");
                startActivity(intent);
                break;
        }
    }

    /**
     * 我的收益
     */
    private void goMyEarnings() {
        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        Member member = GsonUtils.getGson().fromJson(userInfo, Member.class);

        Intent data = new Intent(this, CityServiceProviderNewActivity.class);
        data.putExtra("type", member.getMemberType());
        startActivity(data);
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_MY_WALLET_SUCCESS:
                mModelWallet = (ModelWallet) msg.obj;
                if (mModelWallet != null) {
                    moneyTV.setText(UnitUtil.getMoney(mModelWallet.getBalance(), false));
                }
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<MyWalletActivity> mImpl;

        public MyHandler(MyWalletActivity mImpl) {
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
