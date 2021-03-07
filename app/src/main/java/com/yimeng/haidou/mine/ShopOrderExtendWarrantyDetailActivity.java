package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.OrderGoodsAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.NetComment;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.UploadImageView;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.devio.takephoto.model.TResult;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lib
 * 订单详情-（新订单）
 *
 *
 * 确认订单
 */

public class ShopOrderExtendWarrantyDetailActivity extends BaseTakePhotoActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.order_icon)
    ImageView order_icon;
    @Bind(R.id.nameTV)
    TextView nameTV;
    @Bind(R.id.phoneTV)
    TextView phoneTV;
    @Bind(R.id.totalPriceTV)
    TextView totalPriceTV;
    @Bind(R.id.backMoneyTV)
    TextView backMoneyTV;
    @Bind(R.id.order_goods_list)
    ExpandListView goodlist;
    @Bind(R.id.image1IPV)
    UploadImageView image1IPV;// 手机正面照
    @Bind(R.id.image2IPV)
    UploadImageView image2IPV;// IMEI照片
    @Bind(R.id.order_detail_address_info_rl)
    RelativeLayout order_detail_address_info_rl;
    @Bind(R.id.order_detail_send_name_tv)
    TextView order_detail_send_name_tv;
    @Bind(R.id.order_detail_send_address_tv)
    TextView order_detail_send_address_tv;
    @Bind(R.id.order_detail_send_phone_tv)
    TextView order_detail_send_phone_tv;
    @Bind(R.id.submitBTN)
    Button submitBtn;
    @Bind(R.id.rl_user_info)
    RelativeLayout rl_user_info;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;

    // 自取订单,取件地址
    @Bind(R.id.rl_default_address)
    RelativeLayout rl_default_address;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.tv_district)
    TextView tv_district;
    @Bind(R.id.tv_order_tip)
    TextView tv_order_tip;
    @Bind(R.id.btn_cancel)
    Button btnCancel;

    /**
     * 正面照
     */
    private String mFrontPhoto;
    /**
     * 发票照
     */
    private String mImeiPhoto;


    /**
     * 店铺订单信息
     */
    private ShopOrderModel mShopOrderModel;
    private int uploadPicType;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_order_extend_warranty_detail;
    }

    @Override
    protected void init() {
        mShopOrderModel = (ShopOrderModel) getIntent().getExtras().get("mShopOrderModel");
        if (mShopOrderModel == null) {
            return;
        }
        CommonUtils.showImage(order_icon, mShopOrderModel.getMember_headPath());
        nameTV.setText(mShopOrderModel.getMember_nickname());
        phoneTV.setText(mShopOrderModel.getMember_mobileNo());
        OrderGoodsAdapter adapter = new OrderGoodsAdapter(this, mHandler, "0", mShopOrderModel.getGoodsModels(), mShopOrderModel, 0x02);
        goodlist.setAdapter(adapter);
        totalPriceTV.setText(UnitUtil.getMoney(mShopOrderModel.getRealPrice()));
//        backMoneyTV.setText("(返" + UnitUtil.getMoney(mShopOrderModel.getScore(), false) + "元)");
        tvOrderNo.setText("订单号: " + mShopOrderModel.getOrderNo());
        tvOrderTime.setText("订单时间: " + DateUtil.getAssignDate(mShopOrderModel.getCreateTime(), 3));


        // 判断是否支持配送
        if (mShopOrderModel.getIsDelivery() == 1) {
            order_detail_address_info_rl.setVisibility(View.VISIBLE);

            order_detail_send_name_tv.setText("收货人：" + mShopOrderModel.getReceiver());
            order_detail_send_phone_tv.setText(mShopOrderModel.getReceiverMobile());
            order_detail_send_address_tv.setText("收货地址：" + mShopOrderModel.getAddress());
        }

        // 是否为快递物流店
        String shopType = CommonUtils.getMineShopType();
        if(!TextUtils.isEmpty(shopType) && (shopType.equals("express") || shopType.equals("logistics"))) {
            submitBtn.setText("确认收件");
            image2IPV.setDesc("运单照片");
            rl_default_address.setVisibility(View.VISIBLE);
            rl_user_info.setVisibility(View.GONE);

            try {
                String sendInfo = mShopOrderModel.getSendInfo();
                if(!TextUtils.isEmpty(sendInfo)) {
                    String[] split = sendInfo.split(",");
                    // 寄件人
                    tv_name.setText("寄件人:" +split[3]);
                    // 寄件人手机
                    tv_phone.setText(split[2]);
                    // 取件地址
                    tv_district.setText("取件地址:" + split[4]);
                    // 买家留言
                    tv_order_tip.setVisibility(View.VISIBLE);
                    tv_order_tip.setText("买家留言: "  + (split.length >=6 ?split[5] : ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        image1IPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicType = 1;
                showSelPopupWind(v, 1);
            }
        });
        image2IPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicType = 2;
                showSelPopupWind(v, 1);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String path = getTakeSuccessPath(tResult);
        NetComment.uploadPic(this, path, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                if (uploadPicType == 1) {
                    mFrontPhoto = url;
                    image1IPV.setImageView(url);
                } else {
                    mImeiPhoto = url;
                    image2IPV.setImageView(url);
                }
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast(msg);
            }
        });
    }

    @OnClick({R.id.submitBTN, R.id.btn_cancel})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:    // 确认订单
                confirmOrderToPay();
                break;
            case R.id.btn_cancel:   // 取消订单
                SimpleDialog.showConfirmDialog(this, null, "确定要取消订单吗?", null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        SimpleDialog.showLoadingHintDialog(ShopOrderExtendWarrantyDetailActivity.this, 4);
                        HttpParameterUtil.getInstance().requestCancelShopOrder(mShopOrderModel.getOrderNo(), mHandler);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void confirmOrderToPay() {
        if (mShopOrderModel == null) {
            return;
        }

        if (mFrontPhoto == null) {
            ToastUtils.showToast("请上传商品照片");
            return;
        }
        if (mImeiPhoto == null) {
            ToastUtils.showToast("请上传发票照片");
            return;
        }

        String realPrice = mShopOrderModel.getInsuranceOrder_realPrice();
        SimpleDialog.showLoadingHintDialog(this, 4);
        HttpParameterUtil.getInstance().requesConfirmExtendedWarrantyOrder(mShopOrderModel.getInsuranceOrder_orderNo(),
                realPrice, "", "", "", "", mFrontPhoto, "", "", mImeiPhoto, mHandler);
        submitBtn.setClickable(false);
    }

    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_CONFIRM_INSURANCE_ORDER_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交成功");

                Bundle bundle = new Bundle();
                bundle.putSerializable("mShopOrderModel", mShopOrderModel);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(ConstantHandler.RESULT_CODE_COMMON_DATA, intent);

                finish();
                break;
            case ConstantHandler.WHAT_CONFIRM_INSURANCE_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                submitBtn.setClickable(true);
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS:
                //取消订单成功
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast(getString(R.string.tv_cancel_mall_order_success));
                finish();
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL:
                // 取消订单失败
                ToastUtils.showToast("取消订单失败");
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<ShopOrderExtendWarrantyDetailActivity> mImpl;

        public MyHandler(ShopOrderExtendWarrantyDetailActivity mImpl) {
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
