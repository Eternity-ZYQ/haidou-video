package com.yimeng.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.entity.ModelProductSetByProductNo;
import com.yimeng.entity.ModelSize;
import com.yimeng.haidou.R;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.FlowLayout;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * hym
 * 选择款式规格窗口
 */

public class DialogSelectParameterActivity extends AppCompatActivity {


    @Bind(R.id.tv_goods_cost_beans)
    TextView mGoodsCostBeans; //价格
    @Bind(R.id.tv_goods_inventory)
    TextView mGoodsInventory; //库存

    @Bind(R.id.sdv_image)
    ImageView ImageSDV;
    @Bind(R.id.fl_goods_pattern)
    FlowLayout mGoodsPatternFL; //款式
    @Bind(R.id.fl_goods_specifications)
    FlowLayout mGoodsSpecification; //规格
    @Bind(R.id.tv_shopping_cart_goods_count)
    TextView mCount;//数量
    @Bind(R.id.ll_count)
    LinearLayout ll_count;
    @Bind(R.id.btn_submit)
    Button btn_submit;

    @Bind(R.id.ll_select_param)
    LinearLayout selectParamLL;
    @Bind(R.id.tv_goods_spec)
    TextView tvGoodsSpec;

    private LinkedList<TextView> mColorTextViews;
    private LinkedList<ImageView> mColorImgViews;
    private LinkedList<TextView> mSizeTextViews;
    private LinkedList<ImageView> mSizeImgViews;
    private String defaultPrice;
    private LinkedList<ModelProductSetByProductNo> mList;
    private String productSetNo; //规格编号
    private String stock;//库存
    private String productNo;//商品编号
    private String productName;//商品名称
    private String color;//已选颜色
    private String size;//已选规格
    private int count;
    private boolean hideProductCount;   // 隐藏数量空间
    private String mProductUrl, mProductPrice;//选中规格的产品图片和价格

//    Drawable nomalDrawable;
//    Drawable ClickDrawable;
    private MyHandler mHandler = new MyHandler(this);
    private String selSpec = "";

    @OnClick(R.id.rl_finish)
    void closeDialog() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initView() {

        setContentView(R.layout.dialog_select_parameter);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    protected void initData() {

//        Resources resources = getResources();
//        nomalDrawable = resources.getDrawable(R.drawable.btn_bg_wihte_stroke_gray_aaaaaa);
//        ClickDrawable = resources.getDrawable(R.drawable.shadow_linear_1);
        count = 1;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        productName = bundle.getString("productName");
        productNo = bundle.getString("productNo");
        hideProductCount = bundle.getBoolean("hideProductCount", false);
        if(hideProductCount) {
            // 影藏数量控件
            ll_count.setVisibility(View.GONE);
        }

        defaultPrice = intent.getStringExtra("price");

        HttpParameterUtil.getInstance().requestProductSetByProductNo(productNo, mHandler);

        mColorTextViews = new LinkedList<>();
        mColorImgViews = new LinkedList<>();
        mSizeTextViews = new LinkedList<>();
        mSizeImgViews = new LinkedList<>();
        mList = new LinkedList<>();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.rl_finish, R.id.rl_transparent, R.id.sdv_image, R.id.rl_not_finish,
            R.id.btn_icon_minus_count, R.id.btn_icon_add_count, R.id.btn_submit, R.id.iv_close})
    public void myOnClick(View view) {
        count = Integer.parseInt(String.valueOf(mCount.getText()));
        switch (view.getId()) {
            case R.id.rl_finish:
            case R.id.rl_transparent:
                finish();
                break;
            case R.id.sdv_image:
            case R.id.rl_not_finish:
                break;
            case R.id.btn_icon_minus_count:
                //减号
                if (count > 1) {
                    count--;
                    mCount.setText(String.valueOf(count));
                }
                break;
            case R.id.btn_icon_add_count:
                //加号
                count = count >= UnitUtil.getInt(stock) ? UnitUtil.getInt(stock) : ++count;
                count = count == 0 ? 1 : count;
                mCount.setText(String.valueOf(count));
                break;
            case R.id.btn_submit:
                //确认按钮(判断商品编号，库存)
                if (productSetNo != null && TextUtils.isEmpty(productSetNo) || size == null) {
                    ToastUtils.showToast("请选择商品款式规格");
                    return;
                }

                if (stock == null || TextUtils.isEmpty(stock)) {
                    return;
                }

                if (UnitUtil.getInt(stock) >= count) {
                    //库存大于等于购买数量   productSetNo  num
                    Bundle bundle = new Bundle();
                    bundle.putString("productSetNo", productSetNo);
                    bundle.putString("num", String.valueOf(count));
                    bundle.putString("color", color);
                    bundle.putString("size", size);
                    bundle.putString("productUrl", mProductUrl);
                    bundle.putString("productPrice", mProductPrice);
                    Intent data = new Intent();
                    data.putExtras(bundle);
                    setResult(Constants.RESULT_CODE_COMMON_DATA, data);
                    finish();
                } else {
                    ToastUtils.showToast("商品库存不足");
                }
                break;
            case R.id.iv_close: // 关闭
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_PRODUCT_SET_BY_PRODUCT_NO_SUCCESS:
                boolean hasAdd = false;
                mList = (LinkedList<ModelProductSetByProductNo>) msg.obj;
                if (mList == null || mList.size() == 0) {
                    return;
                }
                ModelProductSetByProductNo modelProductSetByProduct = mList.get(0);
                color = modelProductSetByProduct.getColor();
                //获取款式
                if (!hasAdd) {
                    int i = 0;
                    for (i = 0; i < mList.size(); i++) {
                        View view = LayoutInflater.from(this).inflate(
                                R.layout.adapter_product_spec, mGoodsPatternFL, false);
                        final TextView colorTV = view.findViewById(R.id.tv_spec);
                        final ImageView colorIV = view.findViewById(R.id.iv_spec);
                        colorTV.setTag(i);
                        mColorTextViews.add(colorTV);
                        mColorImgViews.add(colorIV);

                        colorTV.setText(mList.get(i).getColor());
                        colorTV.setTextSize(12);
                        if (i == 0) {
//                            colorTV.setTextColor(getResources().getColor(R.color.activity_bg));
//                            colorTV.setBackgroundDrawable(ClickDrawable);
                            colorTV.setEnabled(true);
                            colorIV.setVisibility(View.VISIBLE);
                            setData(colorTV, colorIV, 0, true);
                        } else {
//                            colorTV.setTextColor(getResources().getColor(R.color.color_tv_2));
//                            colorTV.setBackgroundDrawable(nomalDrawable);
                            colorTV.setEnabled(false);
                            colorIV.setVisibility(View.GONE);
                        }
                        mGoodsPatternFL.addView(view);
                        final int finalI = i;
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击颜色，重新获取对应的规格
                                setData(colorTV, colorIV, finalI, true);
                            }
                        });
                    }

                    String image;
                    String price;
                    //获取图片 库存 价格（默认选第一个规格的）
                    if (modelProductSetByProduct.getSizeList() == null || modelProductSetByProduct.getSizeList().size() == 0) {
                        stock = "0";
                        mGoodsInventory.setText("库存" + stock);
                        btn_submit.setEnabled(false);
                        return;
                    }
                    selSpec = modelProductSetByProduct.getColor();
                    image = modelProductSetByProduct.getSizeList().get(0).getImgPath();
                    CommonUtils.showImage(ImageSDV, image);
                    mGoodsCostBeans.setText(UnitUtil.getMoney(modelProductSetByProduct.getSizeList().get(0).getPrice()));
                    tvGoodsSpec.setText("款式: " + selSpec + " " + modelProductSetByProduct.getSizeList().get(0).getSize());
                    // 规格图片
                    mProductUrl = image;
                    // 规格价格
                    mProductPrice = modelProductSetByProduct.getSizeList().get(0).getPrice();
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(TextView colorTV, ImageView colorIV, final int finalI, boolean defaultSpec) {
        for (TextView textView : mColorTextViews) {
//            textView.setBackgroundDrawable(nomalDrawable);
            textView.setEnabled(false);
//            textView.setTextColor(getResources().getColor(R.color.color_tv_2));
        }
        for (ImageView colorImgView : mColorImgViews) {
            colorImgView.setVisibility(View.GONE);
        }
//        colorTV.setBackgroundDrawable(ClickDrawable);
//        colorTV.setTextColor(getResources().getColor(R.color.activity_bg));
        colorTV.setEnabled(true);
        colorIV.setVisibility(View.VISIBLE);
        mGoodsSpecification.removeAllViews();//清除所有子view
        // 没有规格设置库存为零、价格为默认
        stock = "0";
        mGoodsInventory.setText("库存" + stock);
        mGoodsCostBeans.setText(defaultPrice == null ? "0" : defaultPrice);
        size = null;
        for (int j = 0; j < mList.get(finalI).getSizeList().size(); j++) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.adapter_product_spec, mGoodsPatternFL, false);
            final TextView sizeTV = view.findViewById(R.id.tv_spec);
            final ImageView sizeIV = view.findViewById(R.id.iv_spec);
//            final TextView sizeTV = (TextView) LayoutInflater.from(getApplicationContext()).inflate(
//                    R.layout.search_label_tv, mGoodsPatternFL, false);
            sizeTV.setText(mList.get(finalI).getSizeList().get(j).getSize());
            sizeTV.setTextSize(12);
//            sizeTV.setTextColor(getResources().getColor(R.color.color_tv_2));
//            sizeTV.setBackgroundDrawable(nomalDrawable);
            sizeTV.setEnabled(false);
            sizeIV.setVisibility(View.GONE);

            if (j == 0 && defaultSpec) {
//                sizeTV.setBackgroundDrawable(ClickDrawable);
//                sizeTV.setTextColor(getResources().getColor(R.color.activity_bg));
                sizeTV.setEnabled(true);
                sizeIV.setVisibility(View.VISIBLE);
                //点击修改图片 库存 价格，获取规格编号
                ModelSize modelSize = mList.get(finalI).getSizeList().get(0);
                stock = modelSize.getStock();
                CommonUtils.showImage(ImageSDV, modelSize.getImgPath());
                String price = modelSize.getPrice();
                mGoodsCostBeans.setText(UnitUtil.getMoney(price));
                mGoodsInventory.setText("库存" + stock);
                productSetNo = modelSize.getProductSetNo();
                size = modelSize.getSize();
                selSpec = mList.get(finalI).getColor();
                tvGoodsSpec.setText("款式: " + selSpec + " " + size);
            }

            mSizeTextViews.add(sizeTV);
            mSizeImgViews.add(sizeIV);
            mGoodsSpecification.addView(view);
            color = mList.get(finalI).getColor();
            final int finalJ = j;
            view.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    for (TextView textView : mSizeTextViews) {
//                        textView.setBackgroundDrawable(nomalDrawable);
//                        textView.setTextColor(getResources().getColor(R.color.color_tv_2));
                        textView.setEnabled(false);
                    }
                    for (ImageView sizeImg : mSizeImgViews) {
                        sizeImg.setVisibility(View.GONE);
                    }
//                    sizeTV.setBackgroundDrawable(ClickDrawable);
//                    sizeTV.setTextColor(getResources().getColor(R.color.activity_bg));
                    sizeTV.setEnabled(true);
                    sizeIV.setVisibility(View.VISIBLE);
                    //点击修改图片 库存 价格，获取规格编号
                    ModelSize modelSize = mList.get(finalI).getSizeList().get(finalJ);
                    stock = modelSize.getStock();
                    CommonUtils.showImage(ImageSDV, modelSize.getImgPath());
                    String price = modelSize.getPrice();
                    mGoodsCostBeans.setText(UnitUtil.getMoney(price));
                    mGoodsInventory.setText("库存" + modelSize.getStock());
                    productSetNo = modelSize.getProductSetNo();
                    size = modelSize.getSize();
                    tvGoodsSpec.setText("款式: " + selSpec + " " + size);
                    // 规格图片
                    mProductUrl = modelSize.getImgPath();
                    // 规格价格
                    mProductPrice = price;

                    // 无库存
                    btn_submit.setEnabled(UnitUtil.getInt(stock) != 0);
                }
            });
        }

        // 无库存
        btn_submit.setEnabled(UnitUtil.getInt(stock) != 0);

    }

    private static class MyHandler extends Handler {

        private WeakReference<DialogSelectParameterActivity> mImpl;

        public MyHandler(DialogSelectParameterActivity mImpl) {
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
