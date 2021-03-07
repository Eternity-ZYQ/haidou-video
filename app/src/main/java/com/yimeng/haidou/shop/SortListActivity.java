package com.yimeng.haidou.shop;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.SortListAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelProductByMenuNo;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.widget.ExpandGridView;
import com.yimeng.widget.MyToolBar;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by user on 2018/6/13.
 * 分类商品列表
 */

public class SortListActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.gv_sort_list)
    ExpandGridView mGridView;
    @Bind(R.id.iv_empty)
    ImageView iv_empty;

    private SortListAdapter mAdapter;
    private LinkedList<ModelProductByMenuNo> mList;
    /**
     * 0. 普通购买
     * 1. 任务购买
     * 2. 3.0任务购买
     */
    private int mTaskType = 0;
    // 任务编号
    private String mTaskItemNo;
    // 3.0任务区块编号
    private String mBlockNo;
    private String mShopNo = "";
    // 是否为自促商品
    private boolean mIsShopSale = false;
    private String mMenuNo;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_sort_list;
    }

    @Override
    protected void init() {
        mList = new LinkedList<>();

        Intent intent = getIntent();
        mMenuNo = intent.getStringExtra("extra");
        String title = intent.getStringExtra("title");
        toolBar.setTitle(title);
        mTaskType = intent.getIntExtra("taskType", 0);
        mTaskItemNo = intent.getStringExtra("taskItemNo");
        mBlockNo = intent.getStringExtra("blockNo");
        mIsShopSale = intent.getBooleanExtra("isShopSale", false);

        mAdapter = new SortListAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        SimpleDialog.showLoadingHintDialog(this, 1);
        if (mIsShopSale) {
            HttpParameterUtil.getInstance().getShopDetailProductByClassifyNo(mShopNo,
                    mMenuNo, true, mHandler);
        } else {
            HttpParameterUtil.getInstance().requestProductByMenuNo(mMenuNo, mHandler);
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<SortListActivity> mImpl;

        public MyHandler(SortListActivity mImpl) {
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
        SimpleDialog.cancelLoadingHintDialog();
        switch (msg.what) {
            case ConstantHandler.WHAT_PRODUCT_BY_MENU_NO_SUCCESS:
                mList = (LinkedList<ModelProductByMenuNo>) msg.obj;
                iv_empty.setVisibility(mList.isEmpty() ? View.VISIBLE : View.GONE);
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                switch (msg.arg1) {
                    case R.id.clickLL:
                        ModelProductByMenuNo item = (ModelProductByMenuNo) msg.obj;
                        String shopNo = (mTaskType == 1 || mTaskType == 2) ? mTaskItemNo : item.getShopNo();
                        int type;
                        // TODO new: 2019/5/30 因新任务没有自促商品，故mIsShopSale永为   false
                        if (mIsShopSale) {
                            type = 4;
                        } else {
                            if (mTaskType == 1) {
                                type = 5;
                            } else if (mTaskType == 2) {
                                type = 6;
                            } else {
                                type = 1;
                            }
                        }
                        ActivityUtils.getInstance().jumpShopProductActivity(type, shopNo, item.getProductNo(), 0, mBlockNo);
                        break;
                }
                break;
            case ConstantHandler.WHAT_SHOP_PRODUCT_BY_CLASSIFY_SUCCESS:
                List<ModelProductDetail> productList = (List<ModelProductDetail>) msg.obj;
                for (ModelProductDetail productDetail : productList) {
                    ModelProductByMenuNo modelProductByMenuNo = new ModelProductByMenuNo();
                    modelProductByMenuNo.setImagesPath(productDetail.getImagesPath());
                    modelProductByMenuNo.setProductName(productDetail.getProductName());
                    modelProductByMenuNo.setPrice(productDetail.getPrice());
                    modelProductByMenuNo.setHasSaled(productDetail.getHasSaled());
                    modelProductByMenuNo.setShopNo(productDetail.getShopNo());
                    modelProductByMenuNo.setProductNo(productDetail.getProductNo());

                    mList.add(modelProductByMenuNo);
                }
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
