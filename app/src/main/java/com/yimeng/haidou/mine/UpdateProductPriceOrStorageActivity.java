package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;

public class UpdateProductPriceOrStorageActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.update_price_et)
    EditText update_price_et;
    @Bind(R.id.update_storage_et)
    EditText update_storage_et;
    @Bind(R.id.update_storage_ll)
    LinearLayout update_storage_ll;

    private MyHandler mHandler;
    private String what;
    private String productNo;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_update_product_price_or_storage;
    }

    @Override
    protected void init() {
        mHandler = new MyHandler(UpdateProductPriceOrStorageActivity.this);


        what = getIntent().getStringExtra("what");
        if (what.equals("price")) {
            String price = UnitUtil.getMoney(getIntent().getStringExtra("price"));

            toolBar.setTitle("修改价格");
            update_price_et.setVisibility(View.VISIBLE);
            update_price_et.setText(price);
        }else {
            int storage = getIntent().getIntExtra("storage", 0);
            toolBar.setTitle("修改库存");
            update_storage_ll.setVisibility(View.VISIBLE);
            update_storage_et.setText(String.valueOf(storage));
        }
        productNo = getIntent().getStringExtra("productNo");


    }

    @Override
    protected void initListener() {

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                if (what.equals("price")) {
                    if (update_price_et.getText().toString().trim().isEmpty()) {
                        return;
                    }
                    String price = UnitUtil.commitMoney(update_price_et.getText().toString().trim());
                    HttpParameterUtil.getInstance().updateProductPrice(mHandler, productNo, price);
                }else {
                    if (update_storage_et.getText().toString().trim().isEmpty()) {
                        return;
                    }
                    HttpParameterUtil.getInstance().updateProductStorage(mHandler, productNo, update_storage_et.getText().toString().trim());
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private static class MyHandler extends Handler {

        private WeakReference<UpdateProductPriceOrStorageActivity> mImpl;

        public MyHandler(UpdateProductPriceOrStorageActivity activity) {
            this.mImpl = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().handleMsg(msg);
            }
        }
    }

    private void handleMsg(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_UPDATE_PRODUCT_STORAGE_SUCCESS:
                ToastUtils.showToast("修改成功");
                finish();
                break;
            case ConstantHandler.WHAT_UPDATE_PRODUCT_STORAGE_FAIL:
                ToastUtils.showToast("修改失败");
                break;
            case ConstantHandler.WHAT_UPDATE_PRODUCT_PRICE_SUCCESS:
                ToastUtils.showToast("修改成功");
                finish();
                break;
            case ConstantHandler.WHAT_UPDATE_PRODUCT_PRICE_FAIL:
                ToastUtils.showToast("修改失败");
                break;
        }
    }

}
