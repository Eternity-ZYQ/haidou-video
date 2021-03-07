package com.yimeng.haidou.shop.collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.BusinessListActivty;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.FileUtil;
import com.yimeng.utils.QRCodeUtil;
import com.yimeng.utils.StringUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.huige.library.widget.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 二维码收款activity
 *
 * @author yangjie
 */
public class QrCodeCollectionActivity extends AppCompatActivity {


    @Bind(R.id.toolBar_icon)
    ImageView toolBarIcon;
    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.tvSetAmount)
    TextView tvSetAmount;
    @Bind(R.id.ivQrCode)
    ImageView ivQrCode;
    @Bind(R.id.ivHead)
    CircleImageView ivHead;
    private String shopNo = "";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_collection);
        ButterKnife.bind(this);
        shopNo = getIntent().getStringExtra(Constants.INTENT_PARAM_SHOPNO);
        initView();
    }

    private void initView() {
        setQrCode(0);

        String userHead = (String) SharedPreferencesUtils.get(Constants.USER_HEAD_PATH, "");
        if (!userHead.isEmpty()) {
            CommonUtils.showImage(ivHead, userHead, R.mipmap.my_touxiang);
        }

    }

    @OnClick({R.id.toolBar_icon, R.id.linCollectionRecord, R.id.tvSetAmount, R.id.tvSaveQrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolBar_icon:
                finish();
                break;
            case R.id.linCollectionRecord:
                //历史记录
                Intent intent = new Intent(this, BusinessListActivty.class);
                intent.putExtra("shopNo", shopNo);
                startActivity(intent);
                break;
            case R.id.tvSetAmount:
                //设置金额|清除金额
                if (tvSetAmount.getText().toString().equals(getString(R.string.str_clean_amount))) {
                    setQrCode(0);
                    tvMoney.setVisibility(View.GONE);
                    tvSetAmount.setText(R.string.str_set_amount);
                } else {
                    intent = new Intent(this, SetAmountActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_SETAMOUNT);
                }
                break;
            case R.id.tvSaveQrcode:

                View mView = getWindow().getDecorView();
                mView.buildDrawingCache();
                Bitmap bitmap = mView.getDrawingCache();

                FileUtil.saveBitmap(this, bitmap);

                ToastUtils.showToast("保存成功");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SETAMOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                double money = data.getDoubleExtra(Constants.INTENT_PARAM_MONEY, 0);

                setQrCode(money);
            }
        }
    }

    public void setQrCode(double amount) {
        String strAmount = StringUtils.removeZeros(amount);
        //二维码地址
        String payUrl = "http://cuxiao.fengdikj.com/cuxiao/sm/payment/toAuthUnFreeze.do?shopNo=" + shopNo;

        if (amount != 0) {
            payUrl += "&&payAmt=" + strAmount;

            tvSetAmount.setText(R.string.str_clean_amount);
            tvMoney.setVisibility(View.VISIBLE);
        }

        tvMoney.setText(strAmount);


        LogUtils.v("payUrl:" + payUrl);
        //生成二维码
        bitmap = QRCodeUtil.createQRCodeBitmap(payUrl, getResources().getDimensionPixelOffset(R.dimen.dp_200), getResources().getDimensionPixelOffset(R.dimen.dp_200));

        ivQrCode.setImageBitmap(bitmap);
    }
}