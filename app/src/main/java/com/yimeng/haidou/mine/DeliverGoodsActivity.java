package com.yimeng.haidou.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.LogisticsCompanyBean;
import com.yimeng.entity.ModelOrderGoods;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-27 14:17:01
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商家发货
 */
public class DeliverGoodsActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.tv_name)
    TextView tvUserName;
    @Bind(R.id.tv_phone)
    TextView tvUserPhone;
    @Bind(R.id.tv_district)
    TextView tvUserAddress;
    @Bind(R.id.tv_user_name_desc)
    TextView tvUserNameDesc;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.ll_logistics_type)
    LinearLayout ll_logistics_type;
    @Bind(R.id.tv_logistics_type)
    TextView tvLogisticsType;
    @Bind(R.id.layout_logistics_no)
    LinearLayout layoutLogisticsNo;
    @Bind(R.id.et_logistics)
    EditText etLogistics;
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.layout_address)
    ConstraintLayout layout_address;
    @Bind(R.id.tv_pay_type)
    TextView tv_pay_type;
    @Bind(R.id.tv_leave_msg)
    TextView tv_leave_msg;
    @Bind(R.id.ll_leave_msg)
    LinearLayout ll_leave_msg;
    @Bind(R.id.tv_postage)
    TextView tv_postage;
    @Bind(R.id.tv_order_price)
    TextView tv_order_price;
    @Bind(R.id.tv_coupon_amt)
    TextView tvCouponAmt;
    @Bind(R.id.rl_coupon)
    RelativeLayout rl_coupon;


    /**
     * 店铺订单信息
     */
    private ShopOrderModel mShopOrderModel;
    private BaseQuickAdapter mAdapter;
    private List<LogisticsCompanyBean> mList;
    private OkHttpCommon mOkHttpCommon;
    private OptionsPickerView<LogisticsCompanyBean> mPickerView;
    private String mType;
    /**
     * 选中的快递
     */
    private String selectedLogistcsType;
    private String selectedLogistcsName;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_deliver_goods;
    }

    @Override
    protected void init() {

        Bundle bundle = getIntent().getExtras();
        mShopOrderModel = (ShopOrderModel) bundle.get("mShopOrderModel");
        mType = bundle.getString("type");
        if (mShopOrderModel == null) {
            return;
        }

        // 厂促商品，隐藏地址信息
        if(mShopOrderModel.getIsPlatno().equals("1")) {
            layout_address.setVisibility(View.GONE);
        }

        //收货人
        tvUserName.setText(mShopOrderModel.getReceiver());
        tvUserPhone.setText(mShopOrderModel.getReceiverMobile());
        //收货地址
        tvUserAddress.setText( mShopOrderModel.getAddress());
        CommonUtils.showImage(ivUserHead, mShopOrderModel.getMember_headPath());
        tvUserNameDesc.setText(mShopOrderModel.getMember_nickname());

        // 代金券
        if(UnitUtil.getInt(mShopOrderModel.getCouponAmt()) > 0) {
            rl_coupon.setVisibility(View.VISIBLE);
            tvCouponAmt.setText("-"+UnitUtil.getMoney(mShopOrderModel.getCouponAmt()));
        }
        // 运费
        tv_postage.setText(UnitUtil.getMoney(mShopOrderModel.getLogisticsFee()));
        // 实付款
        tv_order_price.setText(UnitUtil.getMoney(mShopOrderModel.getDueAmt()));

        // 订单号:
        tvOrderNo.setText(mShopOrderModel.getOrderNo());
        // 订单时间
        tvOrderTime.setText(DateUtil.getAssignDate(mShopOrderModel.getCreateTime(), 3));

        // 支付方式
        tv_pay_type.setText(mShopOrderModel.getPayTypeStr());
        // 买家留言
        if (!TextUtils.isEmpty(mShopOrderModel.getLeaveMsg())) {
            ll_leave_msg.setVisibility(View.VISIBLE);
            tv_leave_msg.setText(mShopOrderModel.getLeaveMsg());
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<ModelOrderGoods, BaseViewHolder>(R.layout.adapter_shop_order_item,
                mShopOrderModel.getGoodsModels()) {
            @Override
            protected void convert(BaseViewHolder helper, ModelOrderGoods item) {
                CommonUtils.showImage(helper.getView(R.id.iv_product_logo), item.getProductImg());
                helper.setText(R.id.tv_product_name, item.getProductName())
                        .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getRealAmt()))
                        .setText(R.id.tv_product_num, "x" + item.getProductNum());
            }
        };
        recyclerView.setAdapter(mAdapter);

        mOkHttpCommon = new OkHttpCommon();


        if (mType.equals("new")) {
            // 新订单
            mList = new ArrayList<>();
            mPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    LogisticsCompanyBean companyBean = mList.get(options1);
                    // 物流类型
                    tvLogisticsType.setText(companyBean.getLogisticsCompanyName());
                    selectedLogistcsType = companyBean.getLogisticsCompanyPinyin();
                    selectedLogistcsName = companyBean.getLogisticsCompanyName();

                }
            })
                    .setTitleText("请选择物流类型").build();

            tvLogisticsType.setText(SpannableStringUtils.getBuilder("请选择")
                    .setForegroundColor(Color.BLUE)
                    .create());

            if (mShopOrderModel.getIsPlatno().equals("1")) {
                ll_logistics_type.setVisibility(View.GONE);
                layoutLogisticsNo.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
            }

            // 上门取件商品，不需要发货
            if (TextUtils.isEmpty(mShopOrderModel.getReceiver())) {
                layout_address.setVisibility(View.GONE);
                ll_logistics_type.setVisibility(View.GONE);
                layoutLogisticsNo.setVisibility(View.GONE);
                btn.setText("确认订单");
            }

        } else {
            // 物流类型
            tvLogisticsType.setText(mShopOrderModel.getLogisticsName());
            etLogistics.setText(mShopOrderModel.getLogisticsNo());
            etLogistics.setEnabled(false);
            btn.setText("查看物流");
        }


    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        if (mType.equals("new")) {
            getLogistcsData();
        }

    }

    private void getLogistcsData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_LOGISTICS_TYPE, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("查询物流类型失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询物流类型失败!"));
                    return;
                }

                List<LogisticsCompanyBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonArray().toString(), new TypeToken<List<LogisticsCompanyBean>>() {
                        }.getType()
                );
                if (!mList.isEmpty()) {
                    mList.clear();
                }
                mList.addAll(list);
                mPickerView.setPicker(mList);
            }
        });
    }

    @OnClick({R.id.tv_logistics_type, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_logistics_type:    // 选择物流类型
                mPickerView.show();
                break;
            case R.id.btn:  // 确认发货
                if (mType.equals("new")) {
                    String content = "";
                    if (!TextUtils.isEmpty(mShopOrderModel.getReceiver())) {
                        content = "确认发货吗？";
                    } else {
                        content = "确认订单吗？";
                    }

                    SimpleDialog.showConfirmDialog(this, null, content,
                            null, new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    send();
                                }
                            });

                } else {
                    String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                            mShopOrderModel.getLogisticsType() + "&postid=" + mShopOrderModel.getLogisticsNo();
                    ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                }
                break;
        }
    }

    /**
     * 发货
     */
    private void send() {
        HashMap<String, String> params = CommonUtils.createParams();
        // 非上门取件商品需要填入物流单号
        if (!TextUtils.isEmpty(mShopOrderModel.getReceiver())) {
            if (TextUtils.isEmpty(selectedLogistcsType)) {
                ToastUtils.showToast("请选择快递类型");
                return;
            }
            String logisticsNo = etLogistics.getText().toString().trim();
            if (TextUtils.isEmpty(logisticsNo)) {
                ToastUtils.showToast("请输入运单编号");
                return;
            }
            params.put("logisticsNo", logisticsNo);
            params.put("logisticsType", selectedLogistcsType);
            params.put("logisticsName", selectedLogistcsName);
        }

        params.put("orderNo", mShopOrderModel.getOrderNo());

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SHOP_SEND_GOODS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("提交失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "提交失败!"));
                    return;
                }

                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "提交成功"));
                finish();
            }
        });

    }
}
