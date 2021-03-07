package com.yimeng.haidou.shop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ProvinceBean;
import com.yimeng.interfaces.OnAddressIDPickerListener;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加地址
 *
 * @author xp
 * @describe 添加地址.
 * @date 2017/10/20.
 */

public class UpdateAddressActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.et_consignee)
    EditText etConsignee;// 收货人
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;// 电话
    @Bind(R.id.et_zipcode)
    EditText etZipcode;// 邮政编码
    @Bind(R.id.tv_region)
    TextView tvRegion;// 所在区域
    @Bind(R.id.et_detail_address)
    EditText etDetailAddress;// 详情
    @Bind(R.id.checkFL)
    FrameLayout checkFL;
    @Bind(R.id.checkSDV)
    ImageView checkSDV;

    /**
     * 县区
     */
    private String mArea;
    /**
     * 城市
     */
    private String mCity;
    /**
     * 省份
     */
    private String mProvince;
    /**
     * 地址数据
     */
    private ModelAddress mModelAddress;
    private OptionsPickerView mAddressPicker;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void init() {
        checkSDV.setTag("0");
        checkSDV.setImageResource(R.mipmap.noselect_circle);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            toolBar.setTitle("编辑地址");
            mModelAddress = (ModelAddress) bundle.get("mModelAddress");
            if (mModelAddress != null) {
                etConsignee.setText(mModelAddress.getLinkman());
                etPhoneNum.setText(mModelAddress.getMobileNo());
                mProvince = mModelAddress.getProvince();
                mCity = mModelAddress.getCity();
                mArea = mModelAddress.getArea();
                tvRegion.setText(mProvince + " " + mCity + " " + mArea);
                etDetailAddress.setText(mModelAddress.getAddress());
                int resId = mModelAddress.getIsdefault().equals("1") ? R.mipmap.select_circle : R.mipmap.noselect_circle;
                checkSDV.setImageResource(resId);
                checkSDV.setTag(mModelAddress.getIsdefault());

                etConsignee.setSelection(mModelAddress.getLinkman().length());
                etPhoneNum.setSelection(mModelAddress.getMobileNo().length());
                etDetailAddress.setSelection(mModelAddress.getAddress().length());
            }
        }

        mAddressPicker = CommonUtils.getAddressPicker(this, new OnAddressIDPickerListener() {
            @Override
            public void onResult(View v, ProvinceBean province, ProvinceBean.CitysBean city, ProvinceBean.CitysBean.DistrictsBean area) {
                mProvince = province.getCityName();
                mCity = city.getCityName();
                mArea = area.getCityName();
                tvRegion.setText(getString(R.string.address_format, mProvince, mCity, mArea));
            }
        });

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                super.onRightClick();
                // 保存
                String name = etConsignee.getText().toString();
                String detail = etDetailAddress.getText().toString();
                String mobile = etPhoneNum.getText().toString();
                String is_default = (String) checkSDV.getTag();

                if (checkData(name, mobile, mProvince, detail)) {
                    SimpleDialog.showLoadingHintDialog(UpdateAddressActivity.this, 4);
                    // 判断添加还是修改
                    if (mModelAddress == null) {
                        HttpParameterUtil.getInstance().requestAddressAdd(name,
                                mobile, mProvince, mCity, mArea, detail, is_default, mHandler);
                    } else {
                        HttpParameterUtil.getInstance().requestAddressSave(mModelAddress.getAddressNo(),
                                name, mobile, mProvince, mCity, mArea, detail, is_default, mHandler);
                    }
                }
            }
        });

    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.rl_region, R.id.checkFL, R.id.checkSDV})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.checkFL:
            case R.id.checkSDV:
                // 选中
                if (checkSDV.getTag().toString().equals("1")) {
                    checkSDV.setImageResource(R.mipmap.noselect_circle);
                    checkSDV.setTag("0");
                } else {
                    checkSDV.setImageResource(R.mipmap.select_circle);
                    checkSDV.setTag("1");
                }
                break;
            case R.id.rl_region:
                // 地区选择
//                Intent intent = new Intent(this, DialogDistrictSelectActivity.class);
//                startActivityForResult(intent, 0x01);
                KeyboardUtils.hideKeyBoard(view);
                mAddressPicker.show();
                break;
            default:
                break;
        }
    }


    private boolean checkData(String name, String mobile, String district_code, String detail) {

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(getString(R.string.input_consignee_name));
            return false;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToast(getString(R.string.input_phone));
            return false;
        }
        if (TextUtils.isEmpty(district_code)) {
            ToastUtils.showToast(getString(R.string.select_address));
            return false;
        }
        if (TextUtils.isEmpty(detail)) {
            ToastUtils.showToast(getString(R.string.input_detail_address));
            return false;
        }

        return true;
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<UpdateAddressActivity> mImpl;

        public MyHandler(UpdateAddressActivity mImpl) {
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

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_ADDRESS_ADD_SUCCESS:
            case ConstantHandler.WHAT_ADDRESS_SAVE_SUCCESS:
                // 添加地址成功
                SimpleDialog.cancelLoadingHintDialog();
                finish();
                break;
            case ConstantHandler.WHAT_ADDRESS_ADD_FAIL:
            case ConstantHandler.WHAT_ADDRESS_SAVE_FAIL:
                ToastUtils.showToast((String) msg.obj);
                SimpleDialog.cancelLoadingHintDialog();
                break;
            default:
                break;
        }
    }
}
