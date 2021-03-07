package com.yimeng.haidou.mine;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;

/**
 * @author lib
 *         编辑商品分类
 */

public class EditClassifyActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.classifyNameET)
    EditText classifyNameET;

    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * add添加分类,edit编辑分类
     */
    private String mType;
    /**
     * 分类编号
     */
    private String mProductCategoryNo;
    /**
     * 分类名称
     */
    private String mProductCategoryName;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_edit_classify;
    }

    @Override
    protected void init() {
        mShopNo = getIntent().getStringExtra("shopNo");
        mType = getIntent().getStringExtra("type");
        mProductCategoryNo = getIntent().getStringExtra("productCategoryNo");
        mProductCategoryName = getIntent().getStringExtra("productCategoryName");
        toolBar.setTitle(TextUtils.equals(mType, "add") ? "添加分类" : "编辑分类");

        if(mProductCategoryName != null){
            classifyNameET.setText(mProductCategoryName);
            classifyNameET.setSelection(mProductCategoryName.length());
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                super.onRightClick();
                // 保存
                mProductCategoryName = classifyNameET.getText().toString();
                if (TextUtils.isEmpty(mProductCategoryName)) {
                    ToastUtils.showToast("请输入分类名称");
                    return;
                }
                SimpleDialog.showLoadingHintDialog(EditClassifyActivity.this, 4);
                if (TextUtils.equals(mType, "add")) {
                    HttpParameterUtil.getInstance().requestShopCommodityClassifyAdd(mShopNo, mProductCategoryName, mHandler);
                } else {
                    HttpParameterUtil.getInstance().requestShopCommodityClassifyEdit(mShopNo, mProductCategoryNo, mProductCategoryName, mHandler);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }



    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<EditClassifyActivity> mImpl;

        public MyHandler(EditClassifyActivity mImpl) {
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
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_ADD_SUCCESS:
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交成功");
                finish();
                break;
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_ADD_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }
}
