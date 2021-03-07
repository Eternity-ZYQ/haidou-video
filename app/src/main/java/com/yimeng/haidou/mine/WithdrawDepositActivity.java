package com.yimeng.haidou.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelMemberBankcard;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.BigDecimalUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提现
 *
 * @author xp
 * @describe 提现.
 * @date 2017/3/29.
 */

public class WithdrawDepositActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.moneyET)
    EditText moneyET;
    @Bind(R.id.bankInfoTV)
    TextView bankInfoTV;
    @Bind(R.id.withdraw_service_charge_tv)
    TextView withdraw_service_charge_tv;
    @Bind(R.id.withdraw_all_tv)
    TextView withdraw_all_tv;
    @Bind(R.id.withdraw_service_hint_tv)
    TextView withdraw_service_hint_tv;

    /**
     * 银行卡信息
     */
    private ModelMemberBankcard mModelMemberBankcard;
    /**
     * 提现类型 余额balance,店铺shop,分润yongjin,赚呗提现earnings
     */
    private String mWithdrawType;
    private String mAllMoney;
    private double transferCharge;// 费率 店铺0.8%,其他1%
    private String transferChargeText;
    private int minNumber = 10;
    private MyHandler mHandler = new MyHandler(this);


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_withdraw_deposit;
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        mWithdrawType = intent.getStringExtra("mWithdrawType");
        mAllMoney = intent.getStringExtra("allMoney");

        switch (mWithdrawType) {
            case "yongjin":
                toolBar.setTitle("收益提现");
                transferCharge = 0.01;
                transferChargeText = "1.0%";
                Member member = CommonUtils.getMember();
                // 城市服务商分润提现10000的倍数，其他100的倍数
                if (member != null && member.getMemberType().equals("cityProvider")) {
                    minNumber = 10000;
                }
                withdraw_service_hint_tv.setText("提现金额最少" + minNumber + "元，且是" + minNumber + "的倍数");
                break;
            case "shop":
                toolBar.setTitle("店铺提现");
                withdraw_service_hint_tv.setText("提现金额最少10元");
                withdraw_service_charge_tv.setText("实际到账扣除0.8%手续费");
                transferCharge = 0.008;
                transferChargeText = "0.8%";
                break;
            case "balance":
                toolBar.setTitle("钱包提现");
                withdraw_service_hint_tv.setText("提现金额最少10元，且是10的倍数");
                transferCharge = 0.01;
                transferChargeText = "1.0%";
                break;
            default:
                withdraw_service_hint_tv.setText("提现金额最少10元，且是10的倍数");
                transferCharge = 0.01;
                transferChargeText = "1.0%";
        }

        // 默认最小金额，如果有
        if (Double.valueOf(mAllMoney) >= (double) minNumber) {
            moneyET.setText(minNumber + ".00");
        } else {
            moneyET.setText("0");
        }

        moneyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String temp = s.toString();
                if (temp.length() == 1 && temp.equals(".")) {
                    s.delete(0, 1);
                }
                if (temp.length() == 2 && temp.substring(0, 1).equals("0") && (temp.substring(0, 1)).equals(temp.substring(1, 2))) {
                    s.delete(1, 2);
                }
                if (temp.length() == 2 && temp.substring(0, 1).equals("0") && !(temp.substring(1, 2)).equals(".")) {
                    s.delete(0, 1);
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }

            }
        });

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {

                Intent intent = new Intent(WithdrawDepositActivity.this, WithdrawDepositRecordsActivity.class);
                intent.putExtra("type", mWithdrawType);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            mModelMemberBankcard = (ModelMemberBankcard) data.getExtras().get("mModelMemberBankcard");
            assert mModelMemberBankcard != null;
            String endNo = mModelMemberBankcard.getBankcardNum();
            endNo = endNo.length() > 4 ? endNo.substring(endNo.length() - 4) : endNo;
            String bankName = mModelMemberBankcard.getBankName();
            bankName = bankName.split(",")[2];
            bankInfoTV.setText(bankName + "(" + endNo + ")");
        }
    }

    @OnClick({R.id.submitBTN, R.id.bankRL, R.id.addBankRL, R.id.withdraw_all_tv})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.withdraw_all_tv:
                moneyET.setText(mAllMoney);
                calcMoney();
                break;
            case R.id.submitBTN:
                if (mModelMemberBankcard == null) {
                    ToastUtils.showToast("请添加或选择银行卡");
                    return;
                }

                String applyAmt = moneyET.getText().toString();
                String bankcardNo = mModelMemberBankcard.getBankcardNo();
                if (TextUtils.isEmpty(applyAmt)) {
                    ToastUtils.showToast("请输入提现金额");
                    return;
                }

                if (UnitUtil.getDouble(applyAmt) < (double) minNumber) {
                    ToastUtils.showToast("提现金额不能小于" + minNumber + "元");
                    return;
                }
                if (null == mWithdrawType || TextUtils.isEmpty(mWithdrawType)) {
                    return;
                }

                SimpleDialog.showLoadingHintDialog(this, 4);
                applyAmt = (int) (UnitUtil.getDouble(applyAmt) * 100.0) + "";
                HttpParameterUtil.getInstance().requestAddWithdrawApply(applyAmt, bankcardNo, mWithdrawType, mHandler);
                break;
            case R.id.bankRL:
                startActivityForResult(new Intent(this, BankListActivity.class), 0x01);
                break;
            case R.id.addBankRL:
                // 判断是否实名认证
                if (!CommonUtils.checkAuth()) { // 未实名认证弹框提示

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("请完成实名认证");
                    builder.setCancelable(true);
                    builder.setPositiveButton("认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                    return;
                }
                startActivityForResult(new Intent(this, UpdateBankActivity.class), 0x02);
                break;
            default:
                break;
        }
    }

    private void calcMoney() {

        Editable s = moneyET.getText();

        if (!s.toString().isEmpty()) {
            // 账户总金额除以倍数的余数
//            int maxNum = Integer.parseInt(BigDecimalUtil.calInteger(mAllMoney, String.valueOf(minNumber)));
//            // 输入金额除以倍数的余数
//            int num = Integer.parseInt(BigDecimalUtil.calInteger(s.toString(), String.valueOf(minNumber)));
//            if (num == 0) {
//                if (maxNum > 0) {
//                    moneyET.setText(minNumber + "");
//                } else {
//                    moneyET.setText("0");
//                }
//            } else if (num > maxNum) {
//                moneyET.setText(String.valueOf(maxNum * minNumber) + "");
//            } else {
//                moneyET.setText(num * minNumber + "");
//            }

            moneyET.setText(mAllMoney);
        }

        if (!s.toString().isEmpty() && Double.valueOf(s.toString()) > 0.0) {

            Double money = BigDecimalUtil.mul(Double.valueOf(moneyET.getText().toString()), transferCharge, 2);
            withdraw_service_charge_tv.setText("额外扣除￥" + money + "手续费（费率" + transferChargeText + ")");
        }

        if (s.length() == 0) {
            if (mWithdrawType.equals("shop")) {
                withdraw_service_charge_tv.setText("实际到账扣除0.8%手续费");
            } else {
                withdraw_service_charge_tv.setText("实际到账扣除1.0%手续费");
            }
        }
    }

    /**
     * 处理数据
     */
    private void disposeData(final Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交提现申请成功!");
                finish();
                break;
            case ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_FAIL:
                ToastUtils.showToast((String) msg.obj);
                SimpleDialog.cancelLoadingHintDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private static class MyHandler extends Handler {

        private WeakReference<WithdrawDepositActivity> mActivity;

        public MyHandler(WithdrawDepositActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mActivity.get() != null) {
                mActivity.get().disposeData(msg);
            }
        }
    }
}
