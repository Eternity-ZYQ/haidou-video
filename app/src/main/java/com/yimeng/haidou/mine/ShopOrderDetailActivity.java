package com.yimeng.haidou.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.OrderGoodsAdapter;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.huige.library.utils.DeviceUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lib
 * 订单详情（已完成订单）
 */

public class ShopOrderDetailActivity extends BaseActivity {
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
    @Bind(R.id.tv_postage)
    TextView tv_postage;
    @Bind(R.id.backMoneyTV)
    TextView backMoneyTV;
    @Bind(R.id.orderNoTV)
    TextView orderNoTV;
    @Bind(R.id.gradeRSV)
    RatingStarView gradeRSV;
    @Bind(R.id.gradeTV)
    TextView gradeTV;
    @Bind(R.id.order_goods_list)
    ExpandListView goodlist;
    @Bind(R.id.image1SDV)
    ImageView image1SDV;
    @Bind(R.id.image2SDV)
    ImageView image2SDV;
    @Bind(R.id.image3SDV)
    ImageView image3SDV;
    @Bind(R.id.gradeLL)
    LinearLayout gradeLL;
    @Bind(R.id.btn_affirm)
    Button btnAffirm;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_district)
    TextView tvDistrict;
    @Bind(R.id.tv_order_time)
    TextView tv_order_time;
    @Bind(R.id.tv_logistics_name)
    TextView tv_logistics_name;
    @Bind(R.id.ll_logistics_name)
    LinearLayout ll_logistics_name;
    @Bind(R.id.ll_logistics_no)
    LinearLayout ll_logistics_no;
    @Bind(R.id.tv_logistics_no)
    TextView tv_logistics_no;
    @Bind(R.id.tv_pay_type)
    TextView tv_pay_type;
    @Bind(R.id.tv_leave_msg)
    TextView tv_leave_msg;
    @Bind(R.id.ll_leave_msg)
    LinearLayout ll_leave_msg;
    @Bind(R.id.layout_address)
    ConstraintLayout layout_address;
    @Bind(R.id.tv_coupon_amt)
    TextView tvCouponAmt;
    @Bind(R.id.rl_coupon)
    RelativeLayout rl_coupon;

    /**
     * 店铺订单信息
     */
    private ShopOrderModel mShopOrderModel;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_order_detail;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        mShopOrderModel = (ShopOrderModel) bundle.get("mShopOrderModel");
        if (mShopOrderModel == null) {
            return;
        }
        String type = bundle.getString("type", "");
        if (type.equals("new")) {
            btnAffirm.setVisibility(View.VISIBLE);
            ll_logistics_name.setVisibility(View.GONE);
            ll_logistics_no.setVisibility(View.GONE);
        }

        // 厂促商品，隐藏地址信息
        if (mShopOrderModel.getIsPlatno().equals("1")) {
            layout_address.setVisibility(View.GONE);
        }

        CommonUtils.showImage(order_icon, mShopOrderModel.getMember_headPath());
        nameTV.setText(mShopOrderModel.getMember_memberName());
        phoneTV.setText(mShopOrderModel.getMember_mobileNo());
        OrderGoodsAdapter adapter = new OrderGoodsAdapter(this, mHandler, "0", mShopOrderModel.getGoodsModels(), mShopOrderModel, 0x02);
        goodlist.setAdapter(adapter);
        // 代金券
        if(UnitUtil.getInt(mShopOrderModel.getCouponAmt()) > 0) {
            rl_coupon.setVisibility(View.VISIBLE);
            tvCouponAmt.setText("-"+UnitUtil.getMoney(mShopOrderModel.getCouponAmt()));
        }
        // 运费
        tv_postage.setText(UnitUtil.getMoney(mShopOrderModel.getLogisticsFee()));
        //实付款
        totalPriceTV.setText(UnitUtil.getMoney(mShopOrderModel.getRealPrice()));
//        backMoneyTV.setText("(返" + UnitUtil.getMoney(mShopOrderModel.getScore(), false) + "元)");
        //订单号
        orderNoTV.setText(mShopOrderModel.getOrderNo());
        gradeRSV.setRating(UnitUtil.getFloat(mShopOrderModel.getModelGrade().getGrade()));
        gradeTV.setText(mShopOrderModel.getModelGrade().getDescribe());
        //订单时间
        tv_order_time.setText(DateUtil.getAssignDate(mShopOrderModel.getCreateTime(), 3));
        //物流类型
        tv_logistics_name.setText(mShopOrderModel.getLogisticsName());
        //运单号
        tv_logistics_no.setText(mShopOrderModel.getLogisticsNo());
        //支付方式
        tv_pay_type.setText(mShopOrderModel.getPayTypeStr());
        // 买家留言
        if (!TextUtils.isEmpty(mShopOrderModel.getLeaveMsg())) {
            ll_leave_msg.setVisibility(View.VISIBLE);
            tv_leave_msg.setText(mShopOrderModel.getLeaveMsg());
        }

        if (TextUtils.isEmpty(mShopOrderModel.getModelGrade().getDescribe()) &&
                TextUtils.isEmpty(mShopOrderModel.getModelGrade().getImage1()) &&
                TextUtils.isEmpty(mShopOrderModel.getModelGrade().getId())) {
            gradeLL.setVisibility(View.GONE);
        }

        int width = (DeviceUtils.getWindowWidth(this) - DeviceUtils.dp2px(this, 64)) / 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        image1SDV.setLayoutParams(lp);
        image2SDV.setLayoutParams(lp);
        image3SDV.setLayoutParams(lp);
        image1SDV.setVisibility(View.GONE);
        image2SDV.setVisibility(View.GONE);
        image3SDV.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mShopOrderModel.getModelGrade().getImage1())) {
            image1SDV.setVisibility(View.VISIBLE);
            CommonUtils.showImage(image1SDV, mShopOrderModel.getModelGrade().getImage1());
        }
        if (!TextUtils.isEmpty(mShopOrderModel.getModelGrade().getImage2())) {
            image2SDV.setVisibility(View.VISIBLE);
            CommonUtils.showImage(image2SDV, mShopOrderModel.getModelGrade().getImage2());
        }
        if (!TextUtils.isEmpty(mShopOrderModel.getModelGrade().getImage3())) {
            image3SDV.setVisibility(View.VISIBLE);
            CommonUtils.showImage(image3SDV, mShopOrderModel.getModelGrade().getImage3());
        }

        // 收货人
        tvName.setText(mShopOrderModel.getReceiver());
        tvPhone.setText(mShopOrderModel.getReceiverMobile());
        //收货地址
        tvDistrict.setText(mShopOrderModel.getAddress());

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    private MyHandler mHandler = new MyHandler(this);

    @OnClick(R.id.btn_affirm)
    public void onViewClicked() {
        // 确认订单
    }

    private static class MyHandler extends Handler {

        private WeakReference<ShopOrderDetailActivity> mImpl;

        public MyHandler(ShopOrderDetailActivity mImpl) {
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

    private void disposeData(Message msg) {

        switch (msg.what) {

            default:
                break;
        }
    }

}
