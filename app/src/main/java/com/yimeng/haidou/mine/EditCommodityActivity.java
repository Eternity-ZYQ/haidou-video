package com.yimeng.haidou.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.GoodsModel;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.TextViewUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;

import org.devio.takephoto.model.TResult;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author xp
 * 编辑商品（添加/修改商品）
 */

public class EditCommodityActivity extends BaseTakePhotoActivity {

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
    @Bind(R.id.tv_add_cuxiao_product)
    TextView tv_add_cuxiao_product;
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
    @Bind(R.id.ll_sale)
    LinearLayout ll_sale;
    @Bind(R.id.tv_tip_desc)
    TextView tv_tip_desc;


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
    /**
     * 图片type
     */
    private int uploadPicType;
    private AddGoodsListener mAddGoodsListener;
    private int type;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * entity 　自有商品
     * cuxiao   促销商品
     */
    private String mProductType = "entity";
    /**
     * 商品消费服务类型
     * 0。线上（仅支持送货）
     * 1。到店（仅支持到店消费）
     * 2。都支持
     */
    private int mFeeMode = 1;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_edit_commodity;
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        mShopNo = intent.getStringExtra("mShopNo");
        mProductType = intent.getStringExtra("productType");
        mProductCategoryNo = intent.getStringExtra("mProductCategoryNo");
        mGoodsModel = intent.getExtras() == null ?
                null : (GoodsModel) intent.getExtras().get("model");
        type = intent.getIntExtra("sort", -1);

        if (mProductType.equals("cuxiao")) {
//            priceET.setText("200");
//            priceET.setEnabled(false);

            tv_add_cuxiao_product.setVisibility(View.VISIBLE);
//            tv_add_cuxiao_product.setMovementMethod(LinkMovementMethod.getInstance());
//            tv_add_cuxiao_product.setText(
//                    SpannableStringUtils.getBuilder("若无货源，")
//                            .append("点此选择").setForegroundColor(getResources().getColor(R.color.c_link_color)).setClickSpan(new ClickableSpan() {
//                        @Override
//                        public void onClick(View widget) {
//                            // 添加平台商品
//                            Bundle bundle = new Bundle();
//                            bundle.putString("shopNo", mShopNo);
//                            bundle.putString("productCategoryNo", mProductCategoryNo);
//                            ActivityUtils.getInstance().jumpActivity(AddPlatformProductActivity.class, bundle);
//                        }
//                    })
//                            .append("供应商，极速添加促销商品")
//                            .create()
//            );
            tv_add_cuxiao_product.setText("若无货源，平台供货商一件代发，买家收货您即可赚得收益");
            ll_sale.setVisibility(View.VISIBLE);
            getTipMsg();
        }

        TextViewUtil.setLimitTwoDecimalPlaces(priceET);
        priceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp.length() == 1 && temp.equals(".")) {
                    s.delete(0, 1);
                }
                int posDot = temp.indexOf(".");
                if (temp.length() == 2 && temp.substring(0, 1).equals("0") && (temp.substring(0, 1)).equals(temp.substring(1, 2))) {
                    s.delete(1, 2);
                }
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });

        // 编辑商品
        if (mGoodsModel != null) {
            toolBar.setTitle("编辑商品");
            nameET.setText(mGoodsModel.getProductName());
            describeET.setText(mGoodsModel.getDescription());
            priceET.setText(UnitUtil.getMoney(mGoodsModel.getPrice(), false));
            unitET.setText(mGoodsModel.getUnits());

//            nameET.setSelection(mGoodsModel.getProductName().length());
//            describeET.setSelection(mGoodsModel.getDescription().length());
//            priceET.setSelection(UnitUtil.getMoney(mGoodsModel.getPrice()).length());
//            unitET.setSelection(mGoodsModel.getUnits().length());

            String[] otherImgs = mGoodsModel.getImagesPath().split(",");
            for (int i = 0; i < otherImgs.length; i++) {
                setImageUrl(i + 1, otherImgs[i]);
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

            // 商品服务类型
            if (CommonUtils.getMineShopType().equals("entity")) {
                if (mGoodsModel.getFeeMode() == 0) {
                    cbSend.setChecked(true);
                    cbShop.setChecked(false);
                } else if (mGoodsModel.getFeeMode() == 1) {
                    cbSend.setChecked(false);
                    cbShop.setChecked(true);
                } else {
                    cbSend.setChecked(true);
                    cbShop.setChecked(true);
                }
            }

        }

        if (CommonUtils.getMineShopType().equals("entity")) {
            // 线下实体店
            ll_shop_type.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开启促销活动标题
     */
    private void getTipMsg() {
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_SALE_SHOP_TIP, CommonUtils.createParams(), new CallbackCommon() {
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

    /**
     * 商品服务类型选择
     *
     * @param view
     */
    @OnClick({R.id.layout_to_shop, R.id.layout_to_send})
    public void productTypeServiceType(View view) {
        if (view.getId() == R.id.layout_to_shop) {
            cbShop.setChecked(!cbShop.isChecked());
        } else {
            cbSend.setChecked(!cbSend.isChecked());
        }
    }


    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
                submitGoodsInfo();
            }
        });

    }

    @Override
    protected void loadData() {

    }


    private void submitGoodsInfo() {
        String productName = nameET.getText().toString();//商品名称
        String description = describeET.getText().toString();//商品描述
        String units = unitET.getText().toString();//商品规格单位
        String image1 = TextUtils.isEmpty(mGoods1Photo) ? "" : mGoods1Photo + ",";
        String image2 = TextUtils.isEmpty(mGoods2Photo) ? "" : mGoods2Photo + ",";
        String image3 = TextUtils.isEmpty(mGoods3Photo) ? "" : mGoods3Photo;
        String imagesPath = image1 + image2 + image3;//商品图片
        String price = priceET.getText().toString();//价格
        price = UnitUtil.getInt((UnitUtil.getDouble(price) * 100) + "") + "";//元转分

        if (TextUtils.isEmpty(productName)) {
            ToastUtils.showToast(getString(R.string.input_commodity_name));
            return;
        }
        if (TextUtils.isEmpty(description)) {
            ToastUtils.showToast(getString(R.string.input_commodity_describe));
            return;
        }
        if (TextUtils.isEmpty(imagesPath)) {
            ToastUtils.showToast(getString(R.string.shoot_goods_photo));
            return;
        }
        if (TextUtils.isEmpty(price)) {
            ToastUtils.showToast(getString(R.string.input_price));
            return;
        }
        if (mProductType.equals("cuxiao")) {
            // 促销商品在200-300之间
//            if(UnitUtil.getInt(price) < 20000 || UnitUtil.getInt(price) > 30000) {
            if (UnitUtil.getInt(price) < 20000) {
                ToastUtils.showToast("促销商品价格最少200");
                return;
            }
        }

        if (TextUtils.isEmpty(units)) {
            ToastUtils.showToast(getString(R.string.input_commodity_unit));
            return;
        }

        if (ll_shop_type.getVisibility() == View.VISIBLE) {
            if (cbSend.isChecked() && !cbShop.isChecked()) {
                mFeeMode = 0;
            } else if (!cbSend.isChecked() && cbShop.isChecked()) {
                mFeeMode = 1;
            } else if (cbSend.isChecked() && cbShop.isChecked()) {
                mFeeMode = 2;
            } else {
                ToastUtils.showToast("请至少选择一种商品服务类型");
                return;
            }


        }


        SimpleDialog.showLoadingHintDialog(this, 4);
        if (mGoodsModel == null) {
            HttpParameterUtil.getInstance().requestAddCommodity(mShopNo, mProductType, mProductCategoryNo,
                    productName, description, units, imagesPath, price, mFeeMode, mHandler);
        } else if (type == 3) {
            HttpParameterUtil.getInstance().requestRejectEditCommodity(mShopNo, mProductType, mProductCategoryNo,
                    productName, description, units, imagesPath, price, mFeeMode, mGoodsModel.getProductNo(), mHandler);
        } else {
            HttpParameterUtil.getInstance().requestEditCommodity(mShopNo, mProductType, mProductCategoryNo,
                    productName, description, units, imagesPath, price, mFeeMode, mGoodsModel.getProductNo(), mHandler);
        }
    }

    private void setImageUrl(int type, String url) {
        switch (type) {
            case 1:
                mGoods1Photo = url;
                CommonUtils.showImage(image1IPV, url);
                break;
            case 2:
                mGoods2Photo = url;
                CommonUtils.showImage(image2IPV, url);
                break;
            case 3:
                mGoods3Photo = url;
                CommonUtils.showImage(image3IPV, url);
                break;
            default:
                break;
        }
    }

    private void calcMoney() {
        if (priceET.getText().toString().isEmpty()) return;
        if (Double.parseDouble(priceET.getText().toString()) > 100000) {
            priceET.setText("99999");
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String path = getTakeSuccessPath(tResult);
        NetComment.uploadPic(this, path, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                setImageUrl(uploadPicType, url);
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast(msg);
            }
        });
    }

    @OnClick({R.id.image1IPV, R.id.image2IPV, R.id.image3IPV})
    public void uploadPic(View view) {
        switch (view.getId()) {
            case R.id.image1IPV:
                uploadPicType = 1;
                break;
            case R.id.image2IPV:
                uploadPicType = 2;
                break;
            case R.id.image3IPV:
                uploadPicType = 3;
                break;
            default:
        }
        showSelPopupWind(view, 1);
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        SimpleDialog.cancelLoadingHintDialog();
        switch (msg.what) {
            case ConstantHandler.WHAT_GOODS_ADD_SUCCESS:
            case ConstantHandler.WHAT_GOODS_EDIT_SUCCESS:
            case ConstantHandler.WHAT_REJECT_GOODS_EDIT_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("商品信息已提交，请耐心等待审核结果");
                finish();
                break;
            case ConstantHandler.WHAT_GOODS_ADD_FAIL:
            case ConstantHandler.WHAT_GOODS_EDIT_FAIL:
            case ConstantHandler.WHAT_REJECT_GOODS_EDIT_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    public void setAddGoodsListener(AddGoodsListener listener) {
        mAddGoodsListener = listener;
    }

    public interface AddGoodsListener {
        void addGoodsSuccess();
    }

    private static class MyHandler extends Handler {

        private WeakReference<EditCommodityActivity> mImpl;

        public MyHandler(EditCommodityActivity mImpl) {
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

    @Subscriber(tag = "addProductToSale")
    public void onResult(boolean flag) {
        if (flag) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 200);

        }
    }
}
