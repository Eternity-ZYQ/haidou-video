package com.yimeng.haidou.mine;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.entity.GoodsModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.TextViewUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.DeviceUtils;

import butterknife.Bind;

/**
 * @author xp
 *         分类商品详情
 */

public class ClassifyCommodityDetailActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.nameET)
    EditText nameET;
    @Bind(R.id.describeET)
    EditText describeET;
    @Bind(R.id.image1IPV)
    ImageView image1IPV;
    @Bind(R.id.image2IPV)
    ImageView image2IPV;
    @Bind(R.id.image3IPV)
    ImageView image3IPV;
    @Bind(R.id.priceET)
    EditText priceET;
    @Bind(R.id.unitET)
    EditText unitET;
    @Bind(R.id.tv_product_original_price)
    TextView tvProductOriginalPrice;
    @Bind(R.id.ll_shop_type)
    LinearLayout ll_shop_type;
    @Bind(R.id.layout_to_shop)
    RelativeLayout layout_to_shop;
    @Bind(R.id.layout_to_send)
    RelativeLayout layout_to_send;
    @Bind(R.id.cb_shop)
    CheckBox cbShop;
    @Bind(R.id.cb_send)
    CheckBox cbSend;


    /**
     * 商品照片1
     */
    private String mGoods1Photo;
    /**
     * 店铺照片2
     */
    private String mGoods2Photo;
    /**
     * 商品照片3
     */
    private String mGoods3Photo;
    /**
     * 商品数据
     */
    private GoodsModel mGoodsModel;
    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * 商品分类编号
     */
    private String mProductCategoryNo;
    private int type;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_commodity_detail;
    }

    @Override
    protected void init() {
        mShopNo = getIntent().getStringExtra("mShopNo");
        mProductCategoryNo = getIntent().getStringExtra("mProductCategoryNo");
        mGoodsModel = getIntent().getExtras() == null ?
                null : (GoodsModel) getIntent().getExtras().get("model");
        type = getIntent().getIntExtra("sort", -1);
        int mWidth = (DeviceUtils.getWindowWidth(this) - DeviceUtils.dp2px(this, 36)) / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mWidth, mWidth);
        image1IPV.setLayoutParams(layoutParams);
        image2IPV.setLayoutParams(layoutParams);
        image3IPV.setLayoutParams(layoutParams);

        TextViewUtil.setLimitTwoDecimalPlaces(priceET);

        // 到店消费/送货服务
        if (CommonUtils.getMineShopType().equals("entity")) {
            // 线下实体店
            ll_shop_type.setVisibility(View.VISIBLE);
            if(mGoodsModel != null) {
                int feeMode = mGoodsModel.getFeeMode();
                if(feeMode == 0) {
                    cbSend.setChecked(true);
                    cbShop.setChecked(false);
                }else if(feeMode == 1){
                    cbSend.setChecked(false);
                    cbShop.setChecked(true);
                }else{
                    cbSend.setChecked(true);
                    cbShop.setChecked(true);
                }
            }
        }

        // 编辑商品
        if (mGoodsModel != null) {
            nameET.setText(mGoodsModel.getProductName());
            describeET.setText(mGoodsModel.getDescription());
            priceET.setText(UnitUtil.getMoney(mGoodsModel.getPrice()));
            unitET.setText(mGoodsModel.getUnits());
            if(!TextUtils.isEmpty(mGoodsModel.getProductPlatNo())) {
                // 平台商品显示进货价
                tvProductOriginalPrice.setVisibility(View.VISIBLE);
                tvProductOriginalPrice.setText(
                        SpannableStringUtils.getBuilder("商品进价\t\t")
                                .append(UnitUtil.getMoney(mGoodsModel.getVipPrice()))
                                .setForegroundColor(getResources().getColor(R.color.c_999999))
                                .create()
                );
            }


            String[] otherImgs = mGoodsModel.getImagesPath().split(",");
            for (int i = 0; i < otherImgs.length; i++) {
                switch (i) {
                    case 0:
                        CommonUtils.showImage(image1IPV, otherImgs[0]);
                        break;
                    case 1:
                        CommonUtils.showImage(image2IPV, otherImgs[1]);
                        break;
                    case 2:
                        CommonUtils.showImage(image3IPV, otherImgs[2]);
                        break;
                    default:
                        break;
                }
            }
        }

        if (type == 3 && mGoodsModel.getRemark() != null && !mGoodsModel.getRemark().isEmpty()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("商品驳回原因");
            builder.setMessage(mGoodsModel.getRemark());
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

}
