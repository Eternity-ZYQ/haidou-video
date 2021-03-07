package com.yimeng.haidou.shop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-22 18:37:34
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商家查看促销商品
 */
public class SaleProduct2ShopActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.nameET)
    TextView nameET;
    @Bind(R.id.describeET)
    TextView describeET;
    @Bind(R.id.image1IPV)
    ImageView image1IPV;
    @Bind(R.id.image2IPV)
    ImageView image2IPV;
    @Bind(R.id.image3IPV)
    ImageView image3IPV;
    @Bind(R.id.priceTV)
    TextView priceTV;
    @Bind(R.id.priceET)
    TextView priceET;
    @Bind(R.id.unitTV)
    TextView unitTV;
    @Bind(R.id.unitET)
    TextView unitET;
    @Bind(R.id.tv_product_original_price)
    TextView tvProductOriginalPrice;
    @Bind(R.id.tv_tip_desc)
    TextView tv_tip_desc;

    /**
     * 产品编号
     */
    private String mProductNo;
    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_sale_product2_shop;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mProductNo = bundle.getString("productNo");
        }
        mOkHttpCommon = new OkHttpCommon();

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("productNo", mProductNo);
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_QUERY_PRODUCT_DETAIL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("查询失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast("查询失败");
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                ModelProductDetail productDetail = GsonUtils.getGson().fromJson(data.toString(), ModelProductDetail.class);

                setData(productDetail);

            }
        });

        getTipMsg();

    }

    /**
     * 开启促销活动标题
     */
    private void getTipMsg() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SALE_SHOP_TIP, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() == 1) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
//                    tv_tip_title.setText(data.get("name").getAsString());
                    tv_tip_desc.setText(data.get("introduction").getAsString());
                }
            }
        });
    }

    private void setData(ModelProductDetail productDetail) {
        if (productDetail == null) return;

        nameET.setText(productDetail.getProductName());
        describeET.setText(productDetail.getDescription());
        tvProductOriginalPrice.setText(
                SpannableStringUtils.getBuilder("商品进价\t\t")
                        .append(UnitUtil.getMoney(productDetail.getVipPrice()))
                        .setForegroundColor(getResources().getColor(R.color.c_999999))
                        .create()
        );

        String productBanner = productDetail.getProductBanner();
        if (!TextUtils.isEmpty(productBanner)) {
            if (productBanner.contains(",")) {
                String[] split = productBanner.split(",");
                if (split.length == 1) {
                    image1IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image1IPV, split[0]);
                } else if (split.length == 2) {
                    image1IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image1IPV, split[0]);
                    image2IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image2IPV, split[1]);
                } else if (split.length >= 3) {
                    image1IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image1IPV, split[0]);
                    image2IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image2IPV, split[1]);
                    image3IPV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image3IPV, split[2]);
                }
            } else {
                image1IPV.setVisibility(View.VISIBLE);
                CommonUtils.showImage(image1IPV, productBanner);
            }
        }

        priceET.setText(UnitUtil.getMoney(productDetail.getPrice()));
        unitET.setText(productDetail.getUnits());
    }

}
