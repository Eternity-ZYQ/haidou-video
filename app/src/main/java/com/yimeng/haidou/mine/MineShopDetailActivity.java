package com.yimeng.haidou.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.collection.QrCodeCollectionActivity;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelShop1;
import com.yimeng.entity.ModelShopDetail;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ActionView;
import com.yimeng.widget.ObservableScrollView;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.widget.ItemLayout;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author lijb
 * 我的店铺详情
 */

public class MineShopDetailActivity extends BaseActivity {

    @Bind(R.id.imageSDV)
    ImageView imageSDV;
    @Bind(R.id.shopNameTV)
    TextView shopNameTV;
    @Bind(R.id.tv_shop_address)
    TextView tvShopAddress;
    @Bind(R.id.statusTV)
    TextView statusTV;
    @Bind(R.id.actionCommodity)
    ActionView actionCommodity;
    @Bind(R.id.actionOrderManage)
    ActionView actionOrder;
    @Bind(R.id.shopTypeTV)
    TextView shopTypeTV;
    @Bind(R.id.tradingStatementsTV)
    TextView tradingStatementsTV;
    @Bind(R.id.tv_money_today)
    TextView tvMoneyToday;
    @Bind(R.id.tv_order_today)
    TextView tvOrderToday;
    @Bind(R.id.item_money)
    ItemLayout itemMoney;
    @Bind(R.id.tv_member_grade)
    TextView tvMemberGrade;

    @Bind(R.id.tv_tip_title)
    TextView tv_tip_title;
    @Bind(R.id.tv_tip_desc)
    TextView tv_tip_desc;
    @Bind(R.id.layout_sale)
    ConstraintLayout layoutSale;
    @Bind(R.id.layout_head)
    ConstraintLayout layoutHead;
    @Bind(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    @Bind(R.id.tv_predict_money_sum)
    TextView tvPredictMoneySum;
    @Bind(R.id.tv_predict_money_today)
    TextView tvPredictMoneyToday;

    /**
     * 店铺信息
     */
    private ModelShop1 mModelShop;
    /**
     * 店铺详情
     */
    private ModelShopDetail mModelShopDetail;
    private int total = 0;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_mine_shop;
    }

    @Override
    protected void init() {

        actionCommodity.setActionName("商品管理");
        actionCommodity.setActionImg(-1);
        actionCommodity.setCountInfo("0");
        actionOrder.setActionName("订单管理");
        actionOrder.setActionImg(-1);


        tradingStatementsTV.setVisibility(View.VISIBLE);
        // 店铺新订单
        HttpParameterUtil.getInstance().requestMyShopOrderInfo(1, "new", mHandler);

        Member member = CommonUtils.getMember();
        if (member.getMemberGrade() >= 1) {
            layoutSale.setVisibility(View.GONE);
        } else {
            layoutSale.setVisibility(View.VISIBLE);
        }
        tvMemberGrade.setText(member.getMemberGradeAlias());
    }


    @Override
    protected void initListener() {


        scrollView.setOnScrollChangeListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = layoutHead.getMeasuredHeight() - layoutTitle.getMeasuredHeight() - imageSDV.getMeasuredHeight();
                if (scrollY >= height) {
                    layoutTitle.setBackgroundColor(Color.parseColor("#2d201c"));
                } else {
                    layoutTitle.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });


        itemMoney.setOnItemClickListener(new ItemLayout.OnSingleItemClickListener() {
            @Override
            public void onItemClick(View v) {
                // 账户余额
                Bundle bundle = new Bundle();
                bundle.putString("walletType", "shop");
                ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3Activity.class, bundle);
            }
        });
    }


    @Override
    protected void loadData() {
        getTipMsg();
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
                if (jsonObject.get("status").getAsInt() == 1) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    tv_tip_title.setText(data.get("name").getAsString());
                    tv_tip_desc.setText(data.get("introduction").getAsString());
                }
            }
        });
    }

    /**
     * 交易列表
     */
    private void jumpBusinessList() {
        Intent intent = new Intent(this, BusinessListActivty.class);
        intent.putExtra("shopNo", mModelShop.getShopNo());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpParameterUtil.getInstance().requestShopInfo(mHandler);
        if (total > 0) {
            // 店铺新订单
            HttpParameterUtil.getInstance().requestMyShopOrderInfo(1, "new", mHandler);
        }

    }

    @OnClick({R.id.actionCommodity, R.id.actionOrderManage, R.id.iv_back, R.id.iv_shop_qrcode,
            R.id.view_upload, R.id.layout_sale, R.id.tv_help, R.id.tvIWantCollection,
            R.id.tradingStatementsTV, R.id.layout_today})
    public void myOnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tvIWantCollection:
                Intent intent1=new Intent(this,QrCodeCollectionActivity.class);
                intent1.putExtra(Constants.INTENT_PARAM_SHOPNO,mModelShopDetail.getShopNo());
                startActivity(intent1);
                break;
            case R.id.iv_shop_qrcode:   // 二维码
                Member member = CommonUtils.getMember();
                if (member == null || mModelShop == null || mModelShopDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
//                intent = new Intent(MineShopDetailActivity.this, QrCodeActivity.class);
//                bundle.putString("title", mModelShop.getShopName());
//                bundle.putString("mType", "shop");
//                bundle.putString("mImage", mModelShopDetail.getLogoPath());
//                bundle.putString("mQRUrl", QRCodeUtil.getQRCodeUrl(member.getMemberNo(), mModelShop.getShopNo()));
//                intent.putExtras(bundle);
//                startActivity(intent);
                ActivityUtils.getInstance().jumpMyQrCode(2);
                break;
            case R.id.layout_today: // 今日收入/今日订单数
                jumpBusinessList();
                break;
            case R.id.actionCommodity:// 商品管理
                ActivityUtils.getInstance().jumpActivity(MineShopProductManagerActivity.class);
                break;
            case R.id.actionOrderManage:// 订单管理
                startActivity(new Intent(this, OrderManagActivity.class));
                break;
            case R.id.view_upload:  // 修改店铺
                if (mModelShopDetail == null) {
                    return;
                }
                bundle = new Bundle();
                bundle.putSerializable("model", mModelShopDetail);
                intent = new Intent(this, ShopCheckInInfomationActivity.class);
                intent.putExtra("type", mModelShopDetail.getShopType());
                intent.putExtra("where", "myShopDetail");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tradingStatementsTV: // 交易报表
                intent = new Intent(this, TradingStatementsActivity.class);
                intent.putExtra("shopNo", mModelShopDetail.getShopNo());
                startActivity(intent);
                break;
            case R.id.layout_sale:  // 开启权限
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.task));
                ActivityUtils.getInstance().jumpMainActivity();
                break;
            case R.id.tv_help: // 收益教程
                ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_SHOP_HELP);
                break;

            default:
                break;
        }
    }


    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_SHOP_INFO_SUCCESS:
                mModelShop = (ModelShop1) msg.obj;
                SharedPreferencesUtils.put(Constants.MINE_SHOP_TYPE, mModelShop.getShopType());
                setShopData();
                mModelShopDetail = mModelShop.getmModelShopDetail();
                if (mModelShopDetail != null) {
                    CommonUtils.showImage(imageSDV, mModelShopDetail.getLogoPath());
                }
                break;
            case ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS:
                break;
            case ConstantHandler.WHAT_MY_SHOP_ORDER_SUCCESS:// 店铺新订单
                total = msg.arg2;
                if (total > 0) {
                    actionOrder.setCountInfo(total + "个新订单");
                    actionOrder.setTextColor(Color.RED);
                } else {
                    actionOrder.setCountInfo("");
                }
                break;
            default:
                break;
        }
    }

    private void setShopData() {
        if (mModelShop == null) {
            return;
        }

        shopTypeTV.setText(mModelShop.getShopName());

        shopNameTV.setText(mModelShop.getShopName());
//        tvShopAddress.setText(mModelShop.getProvince() + mModelShop.getCity() + mModelShop.getArea() + mModelShop.getAddress());
        tvShopAddress.setText(mModelShop.getAddress());
        statusTV.setText(mModelShop.getStatusText());
        actionCommodity.setCountInfo(mModelShop.getProductCount());

        // 今日收入
        tvMoneyToday.setText(UnitUtil.getMoney(mModelShop.getDayAmtTotal()));
        // 今日订单数
        tvOrderToday.setText("今日订单\t" + mModelShop.getDayOrderCount());
        // 账户余额
        itemMoney.setRightText(UnitUtil.getMoney(mModelShop.getBalance()));

        // 累计收益
        tvPredictMoneySum.setText(UnitUtil.getMoney(mModelShop.getTotalIncome()));
        tvPredictMoneyToday.setText("预计收益\t" + UnitUtil.getMoney(mModelShop.getExpectIncome()));
    }

    private static class MyHandler extends Handler {

        private WeakReference<MineShopDetailActivity> mImpl;

        public MyHandler(MineShopDetailActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                try {
                    mImpl.get().disposeData(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
