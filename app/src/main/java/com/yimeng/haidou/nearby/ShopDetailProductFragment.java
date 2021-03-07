package com.yimeng.haidou.nearby;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.nearby.adapter.ShopProductAdapter;
import com.yimeng.haidou.shop.OrderSettlementActivity;
import com.yimeng.haidou.shopcard.adapter.ShopCartProductAdapter;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.entity.ShopDetailClassifyBean;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.ShopProductUtils;
import com.yimeng.utils.UnitUtil;
import com.huige.library.popupwind.PopupWindowUtils;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/18 0018 下午 04:43.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商店详情商品页面
 * </pre>
 */
public class ShopDetailProductFragment extends BaseFragment {


    @Bind(R.id.recyclerView_classify)
    RecyclerView recyclerViewClassify;
    @Bind(R.id.recyclerView_goods)
    RecyclerView recyclerViewGoods;
    @Bind(R.id.not_in_service_state_tv)
    TextView notInServiceStateTv;
    @Bind(R.id.iv_shopcart)
    ImageView ivShopcart;
    @Bind(R.id.tv_product_count)
    TextView tvProductCount;
    @Bind(R.id.frame_shopcart)
    FrameLayout frameShopcart;
    @Bind(R.id.tv_sum_price)
    TextView tvSumPrice;
    @Bind(R.id.btn_settle_accounts)
    Button btnSettleAccounts;
    @Bind(R.id.layout_shopcart)
    FrameLayout layoutShopCart;
    @Bind(R.id.ll_to_pay)
    LinearLayout ll_to_pay;

    private String mShopNo;
    private ShopProductAdapter mShopProductAdapter;
    private List<ShopDetailClassifyBean> mClassifyBeanList;
    private BaseQuickAdapter<ShopDetailClassifyBean, BaseViewHolder> mClassifyAdapter;
    /**
     * 最后选中的下标
     */
    private int lastCheckedPosition = 0;
    private MyHandler mHandler = new MyHandler(this);
    private LinkedList<ModelProductDetail> mList;
    private ShopCartProductAdapter mShopCartProductAdapter;

    private PopupWindowUtils mPopupWindowUtils;
    /**
     * 是否为促销商品
     */
    private boolean mIsSale = false;
    /**
     * 是否可以购买
     */
    private boolean mIsCanPay = true;

    /**
     * 店铺类型
     */
    private String mShopType;

    /**
     * @param shopNo 店铺号
     * @param isSale 是否为促销
     * @return
     */
    public static ShopDetailProductFragment getNewInstance(String shopNo, boolean isSale) {
        ShopDetailProductFragment fragment = new ShopDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shopNo", shopNo);
        bundle.putBoolean("isSale", isSale);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_detail_child_product;
    }

    @Override
    protected void init() {
        initRecyclerView();
        Bundle bundle = getArguments();
        mShopNo = bundle.getString("shopNo");
        mIsSale = bundle.getBoolean("isSale", false);
        initPopupWind();

        if (mIsSale) {
            //  促销商品
            layoutShopCart.setVisibility(View.GONE);
            mShopProductAdapter.setShowCountLayoutState(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tvProductCount.setText(ShopProductUtils.getInstance().getProductCount() + "");
        tvSumPrice.setText("¥" + ShopProductUtils.getInstance().getPriceSum());
        refreshProduct();
    }

    @Override
    protected void initListener() {
        //商品列表中
        mShopProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ModelProductDetail productBean = mShopProductAdapter.getItem(position);
                if (productBean == null) {
                    return;
                }
                int storage = UnitUtil.getInt(productBean.getStorage());
                /**
                 * 商品数量
                 */
                int productCount = productBean.getSelectCount();
                // 修改状态
                boolean updateProductResult = false;

                switch (view.getId()) {
                    case R.id.iv_product_add: // 增加
                        if (storage <= productCount) {
                            ToastUtils.showToast("库存不足");
                            return;
                        } else {
                            productCount++;
                        }

                        break;
                    case R.id.iv_product_minus: // 减少
                        if (productCount <= 0) {
                            productCount = 0;
                            break;
                        }
                        productCount--;
                        break;
                    default:
                }


                if (productCount > 0) {
                    updateProductResult = ShopProductUtils.getInstance().addProduct(productBean);
                } else {
                    updateProductResult = ShopProductUtils.getInstance().getShopProductSelectList().remove(productBean);
                }

                // 修改成功刷新页面
                if(updateProductResult) {
                    productBean.setSelectCount(productCount);
                    mShopProductAdapter.notifyItemChanged(position);
                    refreshGoods();
                }

            }
        });

        // 购物车中
        mShopCartProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ModelProductDetail productBean = mShopCartProductAdapter.getItem(position);
                if (productBean == null) {
                    return;
                }
                int storage = UnitUtil.getInt(productBean.getStorage());
                /**
                 * 商品数量
                 */
                int productCount = productBean.getSelectCount();


                // 弹窗产品加减
                if (view.getId() == R.id.iv_product_add) {
                    // 增加
                    if (storage <= productCount) {
                        ToastUtils.showToast("库存不足");
                        return;
                    } else {
                        productCount++;
                    }
                    ShopProductUtils.getInstance().addProduct(productBean);
                } else if (view.getId() == R.id.iv_product_minus) {
                    // 减少
                    if (productCount == 1) {
                        ShopProductUtils.getInstance().removeProduct(productBean);
                    } else {
                        productCount--;
                        ShopProductUtils.getInstance().addProduct(productBean);
                    }
                }

                // 修改成功
                productBean.setSelectCount(productCount);
                mShopCartProductAdapter.notifyDataSetChanged();
                tvProductCount.setText(ShopProductUtils.getInstance().getProductCount() + "");
                tvSumPrice.setText("¥" + ShopProductUtils.getInstance().getPriceSum());
                refreshProduct();

            }
        });

        mClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopDetailClassifyBean bean = (ShopDetailClassifyBean) adapter.getData().get(position);
                if (bean != null) {
                    HttpParameterUtil.getInstance().getShopDetailProductByClassifyNo(mShopNo, bean.getProductCategoryNo(), mIsSale, mHandler);
                    mClassifyBeanList.get(lastCheckedPosition).setChecked(false);
                    lastCheckedPosition = position;
                    mClassifyBeanList.get(position).setChecked(true);
                    mClassifyAdapter.notifyDataSetChanged();
                }

            }
        });

        mShopProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModelProductDetail productDetail = mList.get(position);
                if (productDetail != null) {
                    if (mIsSale) {//促销商品
                        // 促销店铺类型商品
                        if (mList != null && !mList.isEmpty()) {
//                            productDetail.setSelectCount(1);
//                            ShopProductUtils.getInstance().addProduct(productDetail);
                            ActivityUtils.getInstance().jumpShopProductActivity(4, productDetail.getShopNo(), productDetail.getProductNo(), 1,"");
                        }
                    } else {
                        ActivityUtils.getInstance().jumpShopProductActivity(2, productDetail.getShopNo(), productDetail.getProductNo(), productDetail.getSelectCount(),"");
                    }


                }
            }
        });

    }

    /**
     * 添加商品
     */
    public void refreshGoods() {
        if (mShopCartProductAdapter == null) return;
        double priceSum = ShopProductUtils.getInstance().getPriceSum();
        tvSumPrice.setText("¥" + priceSum);
        mShopCartProductAdapter.notifyDataSetChanged();
        tvProductCount.setText(ShopProductUtils.getInstance().getProductCount() + "");
    }

    /**
     * 该店铺店铺类型
     * @param shopType
     */
    public void setShopType(String shopType){
        this.mShopType = shopType;
        mShopProductAdapter.setShopType(shopType);
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestShopClassify(mShopNo, mIsSale ? "cuxiao":"entity", mHandler);
    }

    private void initRecyclerView() {

        recyclerViewClassify.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassifyBeanList = new ArrayList<>();
        mClassifyAdapter = new BaseQuickAdapter<ShopDetailClassifyBean, BaseViewHolder>(
                R.layout.adapter_textview, mClassifyBeanList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ShopDetailClassifyBean item) {
                helper.getView(R.id.tv_classify).setBackgroundColor(item.isChecked() ?
                        mContext.getResources().getColor(R.color.white) : Color.parseColor("#E5E5E5"));
                helper.setText(R.id.tv_classify, item.getProductCategoryName());
                helper.setTextColor(R.id.tv_classify, item.isChecked() ?
                        mContext.getResources().getColor(R.color.theme_color) : mContext.getResources().getColor(R.color.c_333333));
            }
        };
        recyclerViewClassify.setAdapter(mClassifyAdapter);

        mList = new LinkedList<>();
        recyclerViewGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        mShopProductAdapter = new ShopProductAdapter(mList);
        recyclerViewGoods.setAdapter(mShopProductAdapter);
        recyclerViewGoods.getItemAnimator().setChangeDuration(0);

    }

    private void initPopupWind() {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popupwid_shopcart_layout, null);
        RecyclerView recyclerViewProduct = rootView.findViewById(R.id.recyclerView_product);
        // 购物车
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        mShopCartProductAdapter = new ShopCartProductAdapter(ShopProductUtils.getInstance().getShopProductSelectList());
        recyclerViewProduct.setAdapter(mShopCartProductAdapter);
        recyclerViewProduct.setNestedScrollingEnabled(false);

        mPopupWindowUtils = new PopupWindowUtils(getContext(), rootView)
                .setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                .setOnClickListenerByViewId(R.id.tv_clean_shopCart, new PopupWindowUtils.onPopupWindClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShopProductUtils.getInstance().getShopProductSelectList().clear();
                        mShopCartProductAdapter.notifyDataSetChanged();
                        tvProductCount.setText(ShopProductUtils.getInstance().getProductCount() + "");
                        tvSumPrice.setText("¥" + ShopProductUtils.getInstance().getPriceSum());
                        refreshProduct();
                    }
                })
        ;
    }

    /**
     * 刷新数据
     */
    public void refreshProduct() {
        if (mList == null || mShopProductAdapter == null) {
            return;
        }
        List<ModelProductDetail> shopProductSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
        for (int i = 0; i < mList.size(); i++) {
            ModelProductDetail modelProductDetail = mList.get(i);
            if (shopProductSelectList.size() != 0) {
                for (ModelProductDetail productSel : shopProductSelectList) {
                    if (modelProductDetail.getProductNo().equals(productSel.getProductNo())) {
                        modelProductDetail.setSelectCount(productSel.getSelectCount());
                        break;
                    }
                }
            } else {
                modelProductDetail.setSelectCount(0);
            }

        }
        mShopProductAdapter.notifyDataSetChanged();


    }


    @OnClick({R.id.iv_shopcart, R.id.btn_settle_accounts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_shopcart:
                mPopupWindowUtils.showAtLocation(getActivity(), view, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_settle_accounts: // 结算
                goPay();
                break;
        }
    }

    /**
     * 结算
     */
    private void goPay() {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        String shopNo = CommonUtils.getMember().getTelePhone();
        if (shopNo.equals(mShopNo)) {
            ToastUtils.showToast("不能购买自己店铺的商品!");
            return;
        }

        if (ShopProductUtils.getInstance().isEmpty()) {
            ToastUtils.showToast("请选择商品!");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putString("shopType", mShopType);
        ActivityUtils.getInstance().jumpActivity(OrderSettlementActivity.class, bundle);
    }

    /**
     * 店铺是否在范围内
     *
     * @param flag true 在范围内
     *             false 反之
     */
    public void shopIsScope(boolean flag) {
        if (!flag) {
            // 隐藏提交按钮、价格，显示不在与服务区文字
            layoutShopCart.setVisibility(View.VISIBLE);
            ll_to_pay.setVisibility(View.GONE);
            notInServiceStateTv.setVisibility(View.VISIBLE);
            mShopProductAdapter.setShowCountLayoutState(false);
            canGoPay(false);
        }
    }

    /**
     * 是否可购买
     *
     * @param isCanPay true 可购买
     *                 false 反之
     */
    public void canGoPay(boolean isCanPay) {
        btnSettleAccounts.setClickable(isCanPay);
        mIsCanPay = isCanPay;
    }

    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_SHOP_PRODUCT_CLASSIFY_SUCCESS:
                // 商品分类统计
                List<ShopDetailClassifyBean> list = (List<ShopDetailClassifyBean>) msg.obj;
                if (mClassifyAdapter != null && mClassifyBeanList != null && !list.isEmpty()) {
                    if (!mClassifyBeanList.isEmpty()) {
                        mClassifyBeanList.clear();
                    }
                    mClassifyBeanList.addAll(list);
                    ShopDetailClassifyBean classifyBean = mClassifyBeanList.get(0);
                    lastCheckedPosition = 0;
                    classifyBean.setChecked(true);
                    mClassifyAdapter.notifyDataSetChanged();
                    HttpParameterUtil.getInstance().getShopDetailProductByClassifyNo(mShopNo,
                            classifyBean.getProductCategoryNo(), mIsSale, mHandler);
                }
                break;
            case ConstantHandler.WHAT_SHOP_PRODUCT_BY_CLASSIFY_SUCCESS:
                List<ModelProductDetail> productList = (List<ModelProductDetail>) msg.obj;
                if (productList != null && mShopProductAdapter != null) {
                    if (!mList.isEmpty()) {
                        mList.clear();
                    }
                    mList.addAll(productList);
                    refreshProduct();
                }
                break;
            default:
                ToastUtils.showToast((String) msg.obj);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<ShopDetailProductFragment> mImpl;

        public MyHandler(ShopDetailProductFragment mImpl) {
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
