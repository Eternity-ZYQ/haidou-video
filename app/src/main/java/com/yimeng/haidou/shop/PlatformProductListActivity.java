package com.yimeng.haidou.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelProductByMenuNo;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-22 17:06:08
 *  Email  : zhihuiemail@163.com
 *  Desc   : 促销产品展示列表
 */
public class PlatformProductListActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * 商品分类编号
     */
    private String mProductCategoryNo;
    /**
     * 所选分类
     */
    private String mMenuNo;
    private LinkedList<ModelProductByMenuNo> mList;
    private BaseQuickAdapter<ModelProductByMenuNo, BaseViewHolder> mAdapter;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_platform_product_list;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mShopNo = bundle.getString("shopNo");
            mProductCategoryNo = bundle.getString("productCategoryNo");
            mMenuNo = bundle.getString("menuNo");
            toolBar.setTitle(bundle.getString("title"));
        }
        mList = new LinkedList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<ModelProductByMenuNo, BaseViewHolder>(
                R.layout.adapter_platform_product_list_item, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelProductByMenuNo item) {
                CommonUtils.showImage(helper.getView(R.id.iv_product_logo), item.getImagesPath());

                helper.setText(R.id.tv_product_name, item.getProductName())
                        .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                        .setText(R.id.tv_product_desc, item.getDescription())
                        .setChecked(R.id.cb_check, item.isChecked())
                        .addOnClickListener(R.id.layout_check)
                ;

            }
        };
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                // 添加
                addProductToSale();
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.layout_check) {
                    // 选中
                    ModelProductByMenuNo product = mList.get(position);
                    product.setChecked(!product.isChecked());
                    mAdapter.notifyItemChanged(position);
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 促销商品详情
                Bundle bundle = new Bundle();
                bundle.putString("productNo", mList.get(position).getProductNo());
                ActivityUtils.getInstance().jumpActivity(SaleProduct2ShopActivity.class, bundle);
            }
        });
    }

    /**
     * 添加商品
     */
    private void addProductToSale(){
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("shopNo", mShopNo);
        String nos = getSelProducts();
        if(TextUtils.isEmpty(nos)) {
            ToastUtils.showToast("您未选中商品!");
            return;
        }
        params.put("productNos", nos);
        params.put("productCategoryNo", mProductCategoryNo);

        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_ADD_PRODUCT_2_SALE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("添加失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast("添加失败");
                 return;
                }
                ToastUtils.showToast("添加成功!");
                finish();

                EventBus.getDefault().post(true, "addProductToSale");
            }
        });
    }

    private String getSelProducts(){
        StringBuilder sb = new StringBuilder();
        for (ModelProductByMenuNo product : mList) {
            if(product.isChecked()) {
                sb.append(product.getProductNo()).append(",");
            }
        }
        if(TextUtils.isEmpty(sb.toString())) {
            return "";
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestProductByMenuNo(mMenuNo, mHandler);
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        if(mList == null || mAdapter== null) return;
        switch (msg.what) {
            case ConstantHandler.WHAT_PRODUCT_BY_MENU_NO_SUCCESS:
                LinkedList<ModelProductByMenuNo> list = (LinkedList<ModelProductByMenuNo>) msg.obj;
                if(!mList.isEmpty()) {
                    mList.clear();
                }
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();

                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                switch (msg.arg1) {
                    case R.id.clickLL:
                        ModelProductByMenuNo item = (ModelProductByMenuNo) msg.obj;
                        ActivityUtils.getInstance().jumpShopProductActivity(1, item.getShopNo(), item.getProductNo(), 0,"");
                        break;
                }

            default:
                break;
        }


    }

    private static class MyHandler extends Handler {

        private WeakReference<PlatformProductListActivity> mImpl;

        public MyHandler(PlatformProductListActivity mImpl) {
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
