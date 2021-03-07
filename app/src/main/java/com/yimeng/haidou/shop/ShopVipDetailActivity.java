package com.yimeng.haidou.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.DialogAfterSalesServiceActivity;
import com.yimeng.dialog.DialogSelectParameterActivity;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.*;
import com.yimeng.net.NetComment;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.*;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.yimeng.haidou.R;
import com.huige.library.interfaces.OnViewResultListener;
import com.huige.library.popupwind.PopupWindowUtils;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShopVipDetailActivity extends BaseActivity {

    public String productSetNo;//规格参数编号
    public String selParams;//规格参数描述
    public String num;//购买数量
    public String color = "";//颜色
    public String size = "";//尺寸
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.sib_mall_banner)
    BGABanner sibMallBanner;
    @Bind(R.id.tv_product_name)
    TextView tvProductName;
    @Bind(R.id.tv_product_desc)
    TextView tvProductDesc;
    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;
    @Bind(R.id.tv_selected)
    TextView tvSelected;
    @Bind(R.id.sdv_goto)
    ImageView sdvGoto;
    @Bind(R.id.tv_my_wallet_balance)
    TextView selectedParamTV;
    @Bind(R.id.rl_select_specifications)
    RelativeLayout rlSelectSpecifications;
    @Bind(R.id.tv_after_sale)
    TextView tvAfterSale;
    @Bind(R.id.tv_product_parameter)
    TextView tvProductParameter;
    @Bind(R.id.tv_tab_detail)
    RadioButton tvTabDetail;
    @Bind(R.id.tv_tab_evaluate)
    RadioButton tvTabEvaluate;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_collect)
    TextView tvCollect;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.layout_count)
    LinearLayout layoutCount;
    @Bind(R.id.iv_product_minus)
    ImageView ivProductMinus;
    @Bind(R.id.tv_product_count)
    TextView tvProductCount;
    @Bind(R.id.iv_product_add)
    ImageView ivProductAdd;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.tv_product_feeMode_type)
    TextView tv_product_feeMode_type;
    @Bind(R.id.tv_sale_type)
    TextView tv_sale_type;


    ModelSwitchData modelSwitchData;
    private ModelProductDetail mProductDetail;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * 是否收藏
     */
    private boolean IsCollect;
    private boolean bannerHasLoad = false;
    private Drawable isCollect, unCollect;
    private BaseQuickAdapter<BannerItem, BaseViewHolder> mBannerAdapter;
    private List<BannerItem> mBannerItemList;
    private BaseQuickAdapter<ModelGoodsDetailGrade, BaseViewHolder> mEvaluateAdapter;
    private List<ModelGoodsDetailGrade> mEvaluateList;
    private int mPage = 1;
    /**
     * 1. 商城商品详情
     * 2. 店铺进详情
     * 3. 收藏进详情
     * 4. 促销商品
     * 5. 购买任务分类进详情
     * 6. 3.0任务购买
     */
    private int mType;
    // 3.0任务区块编号
    private String mBlockNo;
    private int mNum;
    private String mShopNo, mTaskItemNo, mProductNo;
    private int mProductCount;
    /**
     * 店铺详情
     */
    private ModelShopDetail mModelShopDetail;
    /**
     * 是否可以购买
     */
    private boolean mIsCanPay = true;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_product_detail;
    }

    @Override
    protected void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            ToastUtils.showToast(R.string.data_error);
            finish();
            return;
        }
        isCollect = getResources().getDrawable(R.mipmap.ico_collect_hl);
        isCollect.setBounds(0, 0, isCollect.getIntrinsicWidth(), isCollect.getIntrinsicHeight());
        unCollect = getResources().getDrawable(R.mipmap.ico_collect);
        unCollect.setBounds(0, 0, unCollect.getIntrinsicWidth(), unCollect.getIntrinsicHeight());

        mType = bundle.getInt("type", 1);
        mNum = bundle.getInt("num", 0);
        mShopNo = mTaskItemNo = bundle.getString("shopNo");
        mProductNo = bundle.getString("productNo");
        mBlockNo = bundle.getString("blockNo", "");

        initAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mBannerAdapter);

        if (mType == 1) {

        } else if (mType == 2 || mType == 4) {// 店铺进详情
            rlSelectSpecifications.setVisibility(View.GONE);
            tvAfterSale.setVisibility(View.GONE);
            tvProductParameter.setVisibility(View.GONE);

            tvTabDetail.setVisibility(View.GONE);
            tvTabEvaluate.performClick();
            if (mType == 4) {
                // 促销商品

            } else {
                layoutCount.setVisibility(View.VISIBLE);
                if (mNum > 0) {
                    ivProductAdd.setVisibility(View.VISIBLE);
                    ivProductMinus.setVisibility(View.VISIBLE);
                    tvProductCount.setVisibility(View.VISIBLE);
                    tvProductCount.setText(mNum + "");
                    tvProductCount.setBackgroundColor(Color.TRANSPARENT);
                    tvProductCount.setTextColor(getResources().getColor(R.color.c_333333));
                } else {
                    tvProductCount.setVisibility(View.VISIBLE);
                    tvProductCount.setText("加入购物车");
                    tvProductCount.setBackgroundResource(R.drawable.shape_theme_radius_20);
                    tvProductCount.setTextColor(Color.WHITE);
                }
            }


        } else if (mType == 3) {
            // 收藏进详情
            btnPay.setText("进店购买");

            rlSelectSpecifications.setVisibility(View.GONE);
            tvAfterSale.setVisibility(View.GONE);
            tvProductParameter.setVisibility(View.GONE);

            layoutCount.setVisibility(View.GONE);

            tvTabDetail.setVisibility(View.GONE);
            tvTabEvaluate.performClick();

        } else if (mType == 5) {

        } else {

        }

        // 促销商品显示 <促销商品不支持退换货>
        if (mType == 4 || mType == 5 || mType == 6) {
            tv_sale_type.setVisibility(View.VISIBLE);
        }

        this.findViewById(R.id.rl_select_specifications).setVisibility(View.GONE);

        doBrowseTask();

    }

    /**
     * 倒计时
     */
    private Disposable mBrowseTaskDisposable;

    /**
     * 10s阅读任务
     */
    private void doBrowseTask() {

        CommonUtils.countdown(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mBrowseTaskDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("msg", "ShopProductDetailActivity -> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        NetComment.browseProduct(ShopVipDetailActivity.this, mProductNo);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBrowseTaskDisposable != null && !mBrowseTaskDisposable.isDisposed()) {
            mBrowseTaskDisposable.dispose();
        }
    }

    private void initAdapter() {
        // 产品图片
        mBannerItemList = new ArrayList<>();
        mBannerAdapter = new BaseQuickAdapter<BannerItem, BaseViewHolder>(
                R.layout.adapter_shop_product_image, mBannerItemList
        ) {
            @Override
            protected void convert(final BaseViewHolder helper, final BannerItem item) {
//                final ImageView iv = helper.getView(R.id.sdv_image);
                final SubsamplingScaleImageView ssiImage = helper.getView(R.id.ssi_image);
//                CommonUtils.showImage(iv, item.getImgUrl());
                Glide.with(mContext)
                        .download(CommonUtils.parseImageUrl(item.getImgUrl()))
                        .apply(RequestOptions.placeholderOf(R.mipmap.loading_place))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//                                float scale = ScaleImageViewUtils.getImageScale(mContext,resource.getAbsolutePath());
//                                ssiImage.setImage(ImageSource.uri(resource.getAbsolutePath()),
//                                        new ImageViewState(scale, new PointF(0, 0), 0));
                                ssiImage.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                ssiImage.setMaxScale(10f);
                            }
                        });

            }
        };

        // 评论数据
        mEvaluateList = new ArrayList<>();
        mEvaluateAdapter = new BaseQuickAdapter<ModelGoodsDetailGrade, BaseViewHolder>(
                R.layout.item_lv_goods_detail_comment, mEvaluateList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelGoodsDetailGrade model) {
                helper.setText(R.id.nameTV, model.getName())
                        .setText(R.id.describeTV, model.getDescribe())
                        .setText(R.id.dateTV, DateUtil.getAssignDate(UnitUtil.getLong(model.getDate()), 3))
                ;
                RatingStarView ratingScore = helper.getView(R.id.rating_score);
                ratingScore.setRating(UnitUtil.getFloat(model.getGrade()));


                CommonUtils.showImage(helper.getView(R.id.avatarSDV), model.getAvatarImg());

                helper.addOnClickListener(R.id.image1SDV)
                        .addOnClickListener(R.id.image2SDV)
                        .addOnClickListener(R.id.image3SDV);
                ImageView image1SDV = helper.getView(R.id.image1SDV);
                ImageView image2SDV = helper.getView(R.id.image2SDV);
                ImageView image3SDV = helper.getView(R.id.image3SDV);
                image1SDV.setVisibility(View.GONE);
                image2SDV.setVisibility(View.GONE);
                image3SDV.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(model.getImage1())) {
                    image1SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image1SDV, model.getImage1());
                }
                if (!TextUtils.isEmpty(model.getImage2())) {
                    image2SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image2SDV, model.getImage2());
                }
                if (!TextUtils.isEmpty(model.getImage3())) {
                    image3SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image3SDV, model.getImage3());
                }
            }
        };
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getProductEvaluate();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getProductEvaluate();
            }
        });

        mEvaluateAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int index = 0;
                if (view.getId() == R.id.image1SDV) {
                    index = 0;
                } else if (view.getId() == R.id.image2SDV) {
                    index = 1;
                } else {
                    index = 2;
                }
                ModelGoodsDetailGrade model = mEvaluateList.get(position);
                if (model == null) return;
                String images = model.getImage1() + "," + model.getImage2() + "," + model.getImage3();
                ActivityUtils.getInstance().jumpPhotoActivity(images, index);
            }
        });
    }

    @Override
    protected void loadData() {
        SimpleDialog.showLoadingHintDialog(this, 1);
        HttpParameterUtil.getInstance().requestQueryProductDetail(mProductNo, mHandler);
    }


    /**
     * 获取评价数据
     */
    private void getProductEvaluate() {
        recyclerView.setAdapter(mEvaluateAdapter);
        HttpParameterUtil.getInstance().requestProductComment(mPage, mProductNo, mHandler);
    }

    /**
     * 分享
     */
    @OnClick(R.id.tv_share)
    public void shareProduct() {
        CommonUtils.shareApp();
//        CommonUtils.shareAppUM(this);
    }

    @OnClick({R.id.tv_collect, R.id.tv_shopCart, R.id.btn_add_shopCart, R.id.btn_pay})
    public void onViewClicked(View view) {
        if (mProductDetail == null) {
            return;
        }
        Intent intent = new Intent();
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_collect: // 收藏
                if (IsCollect) {
                    //取消收藏
                    HttpParameterUtil.getInstance().requestDelGoodsCollect(mProductNo, mHandler);
                } else {
                    //收藏
                    HttpParameterUtil.getInstance().requestAddGoodsCollect(mProductNo, mHandler);
                }
                break;
            case R.id.tv_shopCart: // 前往购物车
                if (mType == 1) {
//                    EventBus.getDefault().post(new MainBtnChangeEvent("TabShopCartFragment"));
                } else if (mType == 2) {
                    // 店铺进详情
                    if (mProductDetail.getSelectCount() > 0) {
                        ShopProductUtils.getInstance().addProduct(mProductDetail);
                    }
                    finish();
                }


                break;
            case R.id.btn_add_shopCart: // 加入购物车
                if (color.equals("") && size.equals("")) {
                    //没有选择款式规格，弹出选择窗口
                    bundle.putString("productName", mProductDetail.getProductName());
                    bundle.putString("productNo", mProductDetail.getProductNo());
                    intent.putExtras(bundle);
                    intent.setClass(this, DialogSelectParameterActivity.class);
                    startActivityForResult(intent, 0x20);
                } else {
                    HttpParameterUtil.getInstance().requestAddShopCar(mProductDetail.getProductNo(), num, mProductDetail.getShopNo(), "", productSetNo, mHandler);
                }
                break;
            case R.id.btn_pay:
                if (mType == 1) {
                    //立即购买
                    if (color.equals("") && size.equals("")) {
                        //没有选择款式规格，弹出选择窗口
                        bundle.putString("productName", mProductDetail.getProductName());
                        bundle.putString("productNo", mProductDetail.getProductNo());
                        intent.putExtras(bundle);
                        intent.setClass(this, DialogSelectParameterActivity.class);
                        startActivityForResult(intent, 0x21);
                    } else {
                        toPayNow();
                    }
                } else if (mType == 2) {
                    if (ShopProductUtils.getInstance().isEmpty()) {
                        ToastUtils.showToast("请先选择商品吧!");
                        return;
                    }

                    String shopNo = CommonUtils.getMember().getTelePhone();
                    if (shopNo.equals(mShopNo)) {
                        ToastUtils.showToast("不能购买自己店铺的商品!");
                        return;
                    }
                    if (!mIsCanPay) {
                        ToastUtils.showToast("您当前所在位置不在店铺服务范围内!");
                        return;
                    }
                    bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putString("shopType", mModelShopDetail.getShopType());

                    if (!TextUtils.isEmpty(mProductDetail.getProductPlatNo())) {
                        // 促销平台商品
                        bundle.putBoolean("canSelectedCoupon", true);
                    }

                    ActivityUtils.getInstance().jumpActivity(OrderSettlementActivity.class, bundle);
                } else if (mType == 3) {
                    // 收藏进详情
                    ActivityUtils.getInstance().jumpShopDetailActivity(mShopNo, false);
                } else if (mType == 4) {
                    String shopNo = CommonUtils.getMember().getTelePhone();
                    if (shopNo.equals(mShopNo)) {
                        ToastUtils.showToast("不能购买自己店铺的商品!");
                        return;
                    }
                    if (!mIsCanPay) {
                        ToastUtils.showToast("您当前所在位置不在店铺服务范围内!");
                        return;
                    }

                    bundle = new Bundle();
                    bundle.putInt("type", 3);
                    bundle.putString("shopType", mModelShopDetail.getShopType());

                    if (!TextUtils.isEmpty(mProductDetail.getProductPlatNo())) {
                        // 促销平台商品
                        bundle.putBoolean("canSelectedCoupon", true);
                    }

                    ShopProductUtils.getInstance().setLastSaleProduct(mProductDetail);
                    ActivityUtils.getInstance().jumpActivity(OrderSettlementActivity.class, bundle);
                } else if (mType == 5) {
                    if (color.equals("") && size.equals("")) {
                        //没有选择款式规格，弹出选择窗口
                        bundle.putString("productName", mProductDetail.getProductName());
                        bundle.putString("productNo", mProductDetail.getProductNo());
                        // 购买任务，不可以选择数量
                        bundle.putBoolean("hideProductCount", true);
                        intent.putExtras(bundle);
                        intent.setClass(this, DialogSelectParameterActivity.class);
                        startActivityForResult(intent, 0x21);
                    } else {
                        TaskToPay();
                    }
                } else if (mType == 6) {
                    if (color.equals("") && size.equals("")) {
                        //没有选择款式规格，弹出选择窗口
                        bundle.putString("productName", mProductDetail.getProductName());
                        bundle.putString("productNo", mProductDetail.getProductNo());
                        // 购买任务，不可以选择数量
                        bundle.putBoolean("hideProductCount", true);
                        intent.putExtras(bundle);
                        intent.setClass(this, DialogSelectParameterActivity.class);
                        startActivityForResult(intent, 0x21);
                    } else {
                        Task3_0ToPay();
                    }
                }
                break;
        }
    }

    /**
     * 购买任务支付
     */
    private void TaskToPay() {
        if (mProductDetail == null) {
            loadData();
            return;
        }

        if (TextUtils.isEmpty(productSetNo)) {
            ToastUtils.showToast("请选择产品参数");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putBoolean("taskType", true);
        bundle.putString("shopNo", mTaskItemNo);
        bundle.putString("mProductSetNo", productSetNo);
        bundle.putString("selParams", selParams);
        mProductDetail.setSelectCount(1);
        ShopProductUtils.getInstance().setLastSaleProduct(mProductDetail);
        ActivityUtils.getInstance().jumpActivity(OrderSettlementActivity.class, bundle);
    }

    /**
     * 购买3.0任务支付
     */
    private void Task3_0ToPay() {
        if (mProductDetail == null) {
            loadData();
            return;
        }

        if (TextUtils.isEmpty(productSetNo)) {
            ToastUtils.showToast("请选择产品参数");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putBoolean("isTask", true);
        bundle.putString("shopNo", mTaskItemNo);
        bundle.putString("mProductSetNo", productSetNo);
        bundle.putString("selParams", selParams);
        bundle.putString("blockNo", mBlockNo);
        mProductDetail.setSelectCount(1);
        ShopProductUtils.getInstance().setLastSaleProduct(mProductDetail);
        ActivityUtils.getInstance().jumpActivity(OrderSettlementActivity.class, bundle);
    }

    /**
     * 立即付款
     */
    private void toPayNow() {
        if (mProductDetail == null) {
            loadData();
            return;
        }

        if (TextUtils.isEmpty(productSetNo) || TextUtils.isEmpty(num)) {
            ToastUtils.showToast("请选择产品参数");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("mProductNo", mProductDetail.getProductNo());
        intent.putExtra("mProductSetNo", productSetNo);
        intent.putExtra("mProductNum", num);
        intent.putExtra("type", mType);
        intent.putExtra("shopNo", mShopNo);

        //vip订单
        intent.putExtra("mVip", "vip");

        intent.setClass(this, PayNowActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.iv_product_minus, R.id.iv_product_add, R.id.tv_product_count})
    public void onProductSize(View view) {

        if (mProductDetail == null) {
            return;
        }
        if (mModelShopDetail == null) {
            return;
        }
//        if(mModelShopDetail.getShopType().equals("entity")) {
//            // 不在范围处理
//            // 判断店铺是否在当前定位的区域
//            double lat1 = Double.parseDouble(mModelShopDetail.getLatitude());
//            double lon1 = Double.parseDouble(mModelShopDetail.getLongitude());
//            double lat2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));
//            double lon2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
//            boolean notTheSameArea = false;
//            if (!mModelShopDetail.getCity().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_CITY, "深圳市")) ||
//                    !mModelShopDetail.getArea().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_AREA, "龙华区"))) {
//                notTheSameArea = true;
//            }
//            // 如果不在同一区域并且距离大于3000m则不能购买
//            if (notTheSameArea && CommonUtils.calcDistance(lat1, lon1, lat2, lon2) > 5000.00) {
//                // 隐藏提交按钮、价格，显示不在与服务区文字
//                ToastUtils.showToast("您当前所在位置不在店铺服务范围内!");
//                return;
//            }
//
//        }


        int storage = UnitUtil.getInt(mProductDetail.getStorage());
        /**
         * 商品数量
         */
        mProductCount = mProductDetail.getSelectCount();

        /**
         * 修改商品状态
         */
        boolean updateProductResult = false;

        // 弹窗产品加减
        if (view.getId() == R.id.iv_product_add) {
            // 增加
            if (storage <= mProductCount) {
                ToastUtils.showToast("库存不足");
                return;
            } else {
                mProductCount++;
            }
//            updateProductResult = ShopProductUtils.getInstance().addProduct(mProductDetail);
        } else if (view.getId() == R.id.iv_product_minus) {
            // 减少
            if (mProductCount <= 1) {
                mProductCount = 0;
            } else {
                mProductCount--;
            }
        } else if (view.getId() == R.id.tv_product_count) {
            if (mProductCount == 0) {
                mProductCount++;
            }
        }

        ModelProductDetail detail = mProductDetail;
        detail.setSelectCount(mProductCount);
        if (mProductCount > 0) {
            updateProductResult = ShopProductUtils.getInstance().addProduct(detail);
        } else {
            updateProductResult = ShopProductUtils.getInstance().removeProduct(detail);
        }

        if (updateProductResult) {
            if (mProductCount > 0) {
                ivProductAdd.setVisibility(View.VISIBLE);
                ivProductMinus.setVisibility(View.VISIBLE);
                tvProductCount.setTextColor(getResources().getColor(R.color.c_333333));
                tvProductCount.setBackgroundColor(Color.TRANSPARENT);
            }
            mProductDetail.setSelectCount(mProductCount);
            tvProductCount.setText(mProductCount + "");
        }
    }

    @OnClick({R.id.rl_select_specifications, R.id.tv_after_sale, R.id.tv_product_parameter})
    public void onDialogViewClicked(View view) {
        if (mProductDetail == null) {
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_select_specifications:// 尺寸
                if (!CommonUtils.checkLogin()) {
                    ActivityUtils.getInstance().jumpLoginActivity();
                    return;
                }
                //款式规格
                bundle.putString("productName", mProductDetail.getProductName());
                bundle.putString("productNo", mProductDetail.getProductNo());
                if (mType == 5 || mType == 6) {// 购买任务，不可以选择数量
                    bundle.putBoolean("hideProductCount", true);
                }
                intent.putExtras(bundle);

                intent.setClass(this, DialogSelectParameterActivity.class);
                intent.putExtra("price", tvProductPrice.getText().toString());
                startActivityForResult(intent, 0x01);
                break;
            case R.id.tv_after_sale: // 售后
                //售后服务
                intent.putExtra("customService", mProductDetail.getCustomService());
                intent.setClass(this, DialogAfterSalesServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_product_parameter: // 产品参数
                //产品参数
//                bundle.putSerializable("modelSwitchData", modelSwitchData);
//                intent.putExtras(bundle);
//                intent.putExtra("productParams", mProductDetail.getProductParams());
//                intent.setClass(this, DialogCheckParameterActivity.class);
//                startActivity(intent);
                showProductParamsWindow(view, ConstantsUrl.URL_PRODUCT_PARAMS + mProductNo);
                break;
        }
    }

    /**
     * 产品参数
     *
     * @param view
     * @param url  网页链接
     */
    private void showProductParamsWindow(View view, final String url) {
        Log.d("msg", "ShopProductDetailActivity -> showProductParamsWindow: 地址:" + url);
        View popupWindLayout = LayoutInflater.from(this).inflate(R.layout.popupwind_product_params_layout, null);
        final ProgressBar progressBar = popupWindLayout.findViewById(R.id.progressBar);
        new PopupWindowUtils(this, popupWindLayout)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewStyle(R.id.webView, new OnViewResultListener() {
                    @Override
                    public void getView(View view) {
                        final WebView webView = (WebView) view;
                        WebSettings settings = webView.getSettings();
                        // 支持JS
                        settings.setJavaScriptEnabled(true);
                        // 自适应屏幕
                        settings.setUseWideViewPort(true);
                        settings.setLoadWithOverviewMode(true);
                        settings.setSupportZoom(false);
                        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                        webView.setWebViewClient(new WebViewClient());
                        webView.setWebChromeClient(new WebChromeClient() {
                            @Override
                            public void onProgressChanged(WebView view, int newProgress) {
                                super.onProgressChanged(view, newProgress);
                                progressBar.setProgress(newProgress);
                                if (newProgress == 100) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                        webView.loadUrl(url);
                    }
                })
                .setStyle(R.style.popupWindowAsBottom)
                .setOnClickListenerByViewId(R.id.btn_cancel, null)
                .showAtLocation(this, view, Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.tv_tab_detail, R.id.tv_tab_evaluate})
    public void onTabSelect(View view) {
        if (view.getId() == R.id.tv_tab_detail) {
            // 产品详情
            showProductImages();
        } else {
            // 评价
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(true);
            mPage = 1;
            getProductEvaluate();
        }

//        if (tvTabDetail.isChecked()) {
//            tvTabDetail.setBackgroundResource(R.drawable.layer_list_product_tab_sel);
//        } else {
//            tvTabDetail.setBackgroundColor(Color.WHITE);
//        }
//        if (tvTabEvaluate.isChecked()) {
//            tvTabEvaluate.setBackgroundResource(R.drawable.layer_list_product_tab_sel);
//        } else {
//            tvTabEvaluate.setBackgroundColor(Color.WHITE);
//        }

    }

    /**
     * 产品详情
     */
    private void showProductImages() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        recyclerView.setAdapter(mBannerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_COMMON_DATA && data != null) {
            Bundle bundle = data.getExtras();
            productSetNo = bundle.getString("productSetNo");
            num = bundle.getString("num");
            color = bundle.getString("color");
            size = bundle.getString("size");
            selParams = "款式：" + color + "，规格：" + size;
            selectedParamTV.setText(color + " " + size);
            String productUrl = bundle.getString("productUrl");
            String productPrice = bundle.getString("productPrice");
            mProductDetail.setImagesPath(productUrl);
            mProductDetail.setPrice(productPrice);

            //0x20 加入购物车， 0x21 立即购买
            if (requestCode == 0x20) {
                HttpParameterUtil.getInstance().requestAddShopCar(mProductDetail.getProductNo(), num, mProductDetail.getShopNo(), "", productSetNo, mHandler);
            } else if (requestCode == 0x21) {
                if (mType == 5) {
                    TaskToPay();
                } else if (mType == 6) {
                    Task3_0ToPay();
                } else {
                    toPayNow();
                }
            }
        }
    }

    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                //获取商品信息
                mProductDetail = (ModelProductDetail) msg.obj;
                if (mProductDetail.getIsOnsale().equals("0")) {
                    ToastUtils.showToast(getString(R.string.good_not_on_sale));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                    return;
                }
                mProductDetail.setSelectCount(mNum);
                mShopNo = mProductDetail.getShopNo();
                if (!mShopNo.contains("SP")) {
                    HttpParameterUtil.getInstance().requestShopDetail(mShopNo, mHandler);
                }

                setProductInfo();
                //判断是否收藏
                IsCollect = Boolean.parseBoolean(mProductDetail.getCollect());
                tvCollect.setCompoundDrawables(null, IsCollect ? isCollect : unCollect, null, null);

                LinkedList<BannerItem> banner = new LinkedList<>();
                if (mProductDetail != null) {
                    String[] bannerList = (TextUtils.isEmpty(mProductDetail.getDetailImgPath())
                            ? mProductDetail.getImagesPath() : mProductDetail.getDetailImgPath()).split(",");
                    for (int i = 0; i < bannerList.length; i++) {
                        String logo = bannerList[i];
                        String name = "";
                        String other = "";
                        banner.add(new BannerItem(logo, name, other));
                    }
                }
                mBannerItemList.addAll(banner);
                mBannerAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_ADD_SHOP_CAR_SUCCESS:
                //加入购物车成功
                ToastUtils.showToast("加入购物车成功");
                break;
            case ConstantHandler.WHAT_ADD_SHOP_CAR_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_ADD_GOODS_COLLECT_SUCCESS:
                //收藏商品成功
//                ViewFactory.bind(mLike, FrescoUtil.getResUrl(R.drawable.icon_collected));
                tvCollect.setCompoundDrawables(null, isCollect, null, null);
                ToastUtils.showToast("收藏商品成功");
                IsCollect = true;
                break;
            case ConstantHandler.WHAT_DEL_GOODS_COLLECT_SUCCESS:
                //取消收藏成功
//                ViewFactory.bind(mLike, FrescoUtil.getResUrl(R.drawable.icon_like));
                tvCollect.setCompoundDrawables(null, unCollect, null, null);
                ToastUtils.showToast("取消收藏成功");
                IsCollect = false;
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
//                mCommentAdapter.mList = getGrades(msg);
//                mCommentAdapter.notifyDataSetChanged();
                if (!mEvaluateList.isEmpty()) mEvaluateList.clear();
                LinkedList<ModelGoodsDetailGrade> gradesList = getGrades(msg);
                if (gradesList.size() < Constants.MAX_LIMIT) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                mEvaluateList.addAll(gradesList);
                mEvaluateAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
//                mCommentAdapter.mList.addAll(getGrades(msg));
//                mCommentAdapter.notifyDataSetChanged();
                mEvaluateList.addAll(getGrades(msg));
                mEvaluateAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                // 点击图片
//                String image = (String) msg.obj;
//                if (!TextUtils.isEmpty(image)) {
//                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
//                    intent.putExtra("img_url", image);
//                    startActivity(intent);
//                }
                if (msg.arg1 == R.id.image1SDV || msg.arg1 == R.id.image2SDV || msg.arg1 == R.id.image3SDV) {
                    int position = 0;
                    if (msg.arg1 == R.id.image1SDV) {
                        position = 0;
                    } else if (msg.arg1 == R.id.image2SDV) {
                        position = 1;
                    } else {
                        position = 2;
                    }
                    ModelGoodsDetailGrade model = (ModelGoodsDetailGrade) msg.obj;
                    if (model == null) return;
                    String images = model.getImage1() + "," + model.getImage2() + "," + model.getImage3();
                    ActivityUtils.getInstance().jumpPhotoActivity(images, position);
                }
                break;
            case ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS:
                // 店铺详情
                mModelShopDetail = (ModelShopDetail) msg.obj;
                if (mModelShopDetail.getShopType().equals("entity")) {
//                    // 判断店铺是否在当前定位的区域
//                    double lat1 = Double.parseDouble(mModelShopDetail.getLatitude());
//                    double lon1 = Double.parseDouble(mModelShopDetail.getLongitude());
//                    double lat2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));
//                    double lon2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
//                    boolean notTheSameArea = false;
//                    if (mModelShopDetail.getCity().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_CITY, "深圳市")) &&
//                            mModelShopDetail.getArea().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_AREA, "龙华区"))) {
//                        notTheSameArea = true;
//                    }
//                    // 如果不在同一区域并且距离大于5000m则不能购买
//                    if (!notTheSameArea && CommonUtils.calcDistance(lat1, lon1, lat2, lon2) > 5000.00) {
//                        // 隐藏提交按钮、价格，显示不在与服务区文字
//                        // 线下实体店（entity）超过5公里不能购买，
//                        mIsCanPay = false;
//                    } else {
//                        // 同一区域解除禁止
//                        mIsCanPay = true;
//                    }

                    // 商品服务类型   0。线上（仅支持送货）1。到店（仅支持到店消费）2。都支持
                    int feeMode = mProductDetail.getFeeMode();
                    tv_product_feeMode_type.setVisibility(View.VISIBLE);
                    if (feeMode == 0) {
                        tv_product_feeMode_type.setText("仅支持送货");
                    } else if (feeMode == 1) {
                        tv_product_feeMode_type.setText("仅支持到店");
                    } else {
                        // 都支持
                        tv_product_feeMode_type.setText("支持送货");
                    }
                }

                break;
            default:

        }
    }


    /**
     * 设置商品数据
     */
    private void setProductInfo() {
        if (mProductDetail != null) {

            tvProductName.setText(mProductDetail.getProductName());
            tvProductDesc.setText(Html.fromHtml(mProductDetail.getDescription()));
            tvProductPrice.setText(UnitUtil.getMoney(mProductDetail.getPrice()));

            LinkedList<ModelProductParams> productParamsList = mProductDetail.getProductParamsList();//产品参数
            if (productParamsList != null && productParamsList.equals("")) {
                modelSwitchData.setmProductParamsList(productParamsList);//产品参数
            }

            //banner

            LinkedList<BannerItem> banner = new LinkedList<>();
            if (!bannerHasLoad) {
                String[] bannerList;
//                if(mType == 2 || mType == 4) {
//                    if (TextUtils.isEmpty(mProductDetail.getImagesPath())) {
//                        return;
//                    }
//                    bannerList = mProductDetail.getImagesPath().split(",");
//                }else {
//                    if (TextUtils.isEmpty(mProductDetail.getProductBanner())) {
//                        return;
//                    }
//                    bannerList = mProductDetail.getProductBanner().split(",");
//                }


                bannerList = (TextUtils.isEmpty(mProductDetail.getProductBanner()) ?
                        mProductDetail.getImagesPath() : mProductDetail.getProductBanner()).split(",");


                for (int i = 0; i < bannerList.length; i++) {
                    String logo = bannerList[i];
                    String name = "";
                    String other = "";
                    banner.add(new BannerItem(logo, name, other));
                    bannerHasLoad = true;
                }
            }
            sibMallBanner.setData(banner, null);
            sibMallBanner.setAdapter(new BGABanner.Adapter<ImageView, BannerItem>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable BannerItem model, int position) {
                    itemView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    CommonUtils.showImage(itemView, model.getImgUrl());
//                    Glide.with(ShopProductDetailActivity.this)
//                            .load(CommonUtils.parseImageUrl(model.getImgUrl()))
//                            .apply(new RequestOptions().centerCrop())
//                            .into(itemView);
                }
            });


        }
    }

    private LinkedList<ModelGoodsDetailGrade> getGrades(Message msg) {
        ModelComment modelComment = (ModelComment) msg.obj;
        String totalGrade = modelComment.getTotalGrade();
        totalGrade = TextUtils.isEmpty(totalGrade) ? "0" : totalGrade;
        LinkedList<ModelGoodsDetailGrade> grades = new LinkedList<>();
        for (int i = 0; i < modelComment.getModelRepairOrderCommentDetailList().size(); i++) {
            String id = modelComment.getModelRepairOrderCommentDetailList().get(i).getId();
            String name = modelComment.getModelRepairOrderCommentDetailList().get(i).getNickname();
            String avatarImg = modelComment.getModelRepairOrderCommentDetailList().get(i).getHeadPath();
            String grade = modelComment.getModelRepairOrderCommentDetailList().get(i).getDiscriptScore();
            String date = modelComment.getModelRepairOrderCommentDetailList().get(i).getCreateTime();
            String describe = modelComment.getModelRepairOrderCommentDetailList().get(i).getContent();

            String imgPath = modelComment.getModelRepairOrderCommentDetailList().get(i).getImgPath();
            String[] imageList = imgPath.split(",");
            String image1 = "";
            String image2 = "";
            String image3 = "";
            switch (imageList.length) {
                case 0:
                    image1 = "";
                    image2 = "";
                    image3 = "";
                    break;
                case 1:
                    image1 = imageList[0];
                    image2 = "";
                    image3 = "";
                    break;
                case 2:
                    image1 = imageList[0];
                    image2 = imageList[1];
                    image3 = "";
                    break;
                case 3:
                    image1 = imageList[0];
                    image2 = imageList[1];
                    image3 = imageList[2];
                    break;
                default:
                    break;
            }
            grades.add(new ModelGoodsDetailGrade(id, name, avatarImg, grade, date, describe, image1, image2, image3));
        }

        return grades;
    }

    private class MyHandler extends Handler {
        private WeakReference<ShopVipDetailActivity> mImpl;

        public MyHandler(ShopVipDetailActivity impl) {
            mImpl = new WeakReference<>(impl);
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
