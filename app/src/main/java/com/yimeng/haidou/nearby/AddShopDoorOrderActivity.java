package com.yimeng.haidou.nearby;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.entity.ModelSettlement;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.ShopProductUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-2-18 15:02:25
 *  Email  : zhihuiemail@163.com
 *  Desc   : 上门取件申请
 */
public class AddShopDoorOrderActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.tv_name)
    TextView mName;
    @Bind(R.id.tv_phone)
    TextView mPhone;
    @Bind(R.id.tv_district)
    TextView mDistrict;
    @Bind(R.id.rl_default_address)
    RelativeLayout mDefalutRL;//有默认地址
    @Bind(R.id.rl_not_data_address)
    RelativeLayout mNotDataAddressRL;//没有设置地址
    @Bind(R.id.tv_select_address)
    TextView mSelectAddressTV;//有地址，没有设置默认地址,去选择地址
    @Bind(R.id.rl_select_address)
    RelativeLayout mselectAddressRL;//有地址，没有设置默认地址
    @Bind(R.id.shopNameTV)
    TextView shopNameTV;
    @Bind(R.id.imageSDV)
    ImageView imageSDV;
    @Bind(R.id.nameTV)
    TextView nameTV;
    @Bind(R.id.describeTV)
    TextView describeTV;
    @Bind(R.id.goodsMoneyTV)
    TextView goodsMoneyTV;
    @Bind(R.id.clickRL)
    RelativeLayout clickRL;
    @Bind(R.id.et_tip)
    EditText etTip;

    private ModelAddress mModelAddress;// 当前选择的地址
    private LinkedList<ModelAddress> mAddressList;
    private MyHandler mHandler = new MyHandler(this);
    private String linkman;//联系人
    private String phone;
    private String address;
    private List<ModelSettlement> mModelSettlementList;
    /**
     * 订单结算数据
     */
    private ModelSettlement mModelSettlement;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_add_shop_door_order;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestAddressAll(mHandler);
        List<ModelProductDetail> productSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
        mModelSettlementList = new ArrayList<>();
        String products = "";
        for (ModelProductDetail detail : productSelectList) {
            products += detail.getProductNo() + ",";
        }
        HttpParameterUtil.getInstance().requestOrderSettlementList(products, mHandler);

    }

    @OnClick({R.id.btn_submit, R.id.rl_not_data_address, R.id.rl_select_address, R.id.rl_default_address,})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_not_data_address:
                //没有地址，添加 地址
            case R.id.rl_select_address:
            case R.id.rl_default_address:
                ActivityUtils.getInstance().jumpAddressManager(true);
                break;
            case R.id.btn_submit: // 提交
                submitApply();
                break;
            default:

        }
    }

    /**
     * 提交申请
     */
    private void submitApply() {
        HashMap<String, String> params = CommonUtils.createParams();
        StringBuffer ss = new StringBuffer();
        ss.append("[");
        List<ModelProductDetail> shopProductSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
        for (ModelProductDetail productDetail : shopProductSelectList) {
            if (!productDetail.isNotChecked()) {
                ss.append("{\"productNo\":\"")
                        .append(productDetail.getProductNo())
                        .append("\",\"count\":\"")
                        .append(productDetail.getSelectCount())
                        .append("\"},");
            }
        }
        ss.append("]");
        String addressNo = mModelAddress == null ? "" : mModelAddress.getAddressNo();
//        params.put("addressNo", addressNo);
        params.put("settleProducts", ss.toString());
        String Longitude = (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE);
        String Latitude = (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE);
        String phone = mPhone.getText().toString().trim();
        String name = mName.getText().toString().trim();
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
            ToastUtils.showToast("请填写取件地址");
            return;
        }
        name = name.replace("寄件人:", "");
        String district = mDistrict.getText().toString().trim();
        district = district.replace("取件地址:", "");
        String tip = etTip.getText().toString().trim();
        params.put("sendInfo", Longitude + "," + Latitude + "," + phone + "," + name + "," + district + "," + tip);
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_CREATE_EXPRESS_ORDER, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "提交订单失败"));
                    return;
                }
                ToastUtils.showToast("提交成功!");
//                ActivityUtils.getInstance().jumpActivity(MyOrderActivity.class);
//                finish();
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.shopCart));
                ActivityUtils.getInstance().jumpMainActivity();
            }
        });
    }

    private void disposeData(Message msg) {
        boolean hasDefault = false;
        switch (msg.what) {
            case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                // 所有地址
                mAddressList = (LinkedList<ModelAddress>) msg.obj;
                if (mAddressList.size() > 0) {
                    //有地址
                    // 查找默认地址
                    for (int i = 0; i < mAddressList.size(); i++) {
                        String isdefault = mAddressList.get(i).getIsdefault();
                        if (isdefault.equals(String.valueOf(1))) {
                            hasDefault = true;
                            mModelAddress = mAddressList.get(i);
                            mDefalutRL.setVisibility(View.VISIBLE);
                            mselectAddressRL.setVisibility(View.GONE);
                            mNotDataAddressRL.setVisibility(View.GONE);
                            mName.setText("寄件人: " + mAddressList.get(i).getLinkman());
                            mPhone.setText(mAddressList.get(i).getMobileNo());
                            mDistrict.setText("取件地址: "+mAddressList.get(i).getProvince() + mAddressList.get(i).getCity() + mAddressList.get(i).getArea() + mAddressList.get(i).getAddress());
                        }
                    }
                    //有地址，但没有默认地址
                    if (!hasDefault) {
                        mDefalutRL.setVisibility(View.GONE);
                        mselectAddressRL.setVisibility(View.VISIBLE);
                        mNotDataAddressRL.setVisibility(View.GONE);
                    }
                } else if (mAddressList.size() == 0) {
                    //没有地址
                    mDefalutRL.setVisibility(View.GONE);
                    mselectAddressRL.setVisibility(View.GONE);
                    mNotDataAddressRL.setVisibility(View.VISIBLE);
                }
                break;
            case ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_SUCCESS:
                // 列表商品信息
                mModelSettlementList.addAll((List<ModelSettlement>) msg.obj);
                mModelSettlement = mModelSettlementList.get(0);
                setShopInfo();

                break;
            case ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_FAIL:
                SimpleDialog.showTipDialog(this, (String) msg.obj, new SimpleDialog.OnSimpleDialogClickListener() {
                    @Override
                    public void onClick(View v, AlertDialog dialog) {
                        finish();
                    }
                });
                break;
        }
    }

    /**
     * 设置店铺默认数据
     */
    private void setShopInfo() {
        if (mModelSettlement != null) {
            shopNameTV.setText(mModelSettlement.getShopName());
            CommonUtils.showImage(imageSDV, mModelSettlement.getImagesPath());
            nameTV.setText(mModelSettlement.getProductName());
            goodsMoneyTV.setText(UnitUtil.getMoney(mModelSettlement.getPrice(), false));

        }
    }

    /**
     * 选择地址回调
     * @param address
     */
    @Subscriber(tag = EventTags.PAY_SELECT_ADDRESS)
    public void selectAddress(ModelAddress address){
        mModelAddress = address;
        if (mModelAddress != null) {
            linkman = mModelAddress.getLinkman();
            phone = mModelAddress.getMobileNo();
            String addressStr = mModelAddress.getProvince() + mModelAddress.getCity() + mModelAddress.getArea() + mModelAddress.getAddress();
            mDefalutRL.setVisibility(View.VISIBLE);
            mselectAddressRL.setVisibility(View.GONE);
            mNotDataAddressRL.setVisibility(View.GONE);
            mName.setText("寄件人:" +linkman);
            mPhone.setText(phone);
            mDistrict.setText("取件地址:" + addressStr);
        }
    }

//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Constants.RESULT_CODE_COMMON_DATA && data != null) {
//            mModelAddress = (ModelAddress) data.getExtras().get("mModelAddress");
//            assert mModelAddress != null;
//            linkman = mModelAddress.getLinkman();
//            phone = mModelAddress.getMobileNo();
//            address = mModelAddress.getProvince() + mModelAddress.getCity() + mModelAddress.getArea() + mModelAddress.getAddress();
//            mDefalutRL.setVisibility(View.VISIBLE);
//            mselectAddressRL.setVisibility(View.GONE);
//            mNotDataAddressRL.setVisibility(View.GONE);
//            mName.setText("寄件人:" +linkman);
//            mPhone.setText(phone);
//            mDistrict.setText("取件地址:" + address);
//        }
//    }




    private static class MyHandler extends Handler {

        private WeakReference<AddShopDoorOrderActivity> mImpl;

        public MyHandler(AddShopDoorOrderActivity mImpl) {
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
