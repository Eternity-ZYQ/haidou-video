package com.yimeng.haidou.shop.collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.utils.EditTextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetAmountActivity extends BaseActivity {

    @Bind(R.id.edMoney)
    EditText edMoney;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_set_amount;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        //限制输入位数
        EditTextUtil.keepTwoDecimals(edMoney, 2,7);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.toolBar_icon, R.id.btDetermine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolBar_icon:
                finish();
                break;
            case R.id.btDetermine:
                //完成
                //金额
                String strMoney = edMoney.getText().toString();
                Intent intent = new Intent();
                if (strMoney.isEmpty()) {
                    intent.putExtra(Constants.INTENT_PARAM_MONEY, 0.0);
                    setResult(Activity.RESULT_CANCELED);
                } else {
                    intent.putExtra(Constants.INTENT_PARAM_MONEY, Double.parseDouble(strMoney));
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}