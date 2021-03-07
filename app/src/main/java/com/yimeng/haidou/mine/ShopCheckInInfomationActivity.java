package com.yimeng.haidou.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.AMapSearchActivity;
import com.yimeng.haidou.R;
import com.yimeng.dialog.DialogHourMinActivity;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelShopDetail;
import com.yimeng.entity.ProvinceBean;
import com.yimeng.entity.ShopDetailCache;
import com.yimeng.interfaces.OnAddressIDPickerListener;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.NetComment;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.UploadImageView;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;

import org.devio.takephoto.model.TResult;
import org.litepal.LitePal;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lijb
 * 入驻申请（店铺信息编辑）
 */

public class ShopCheckInInfomationActivity extends BaseTakePhotoActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.shopNameET)
    EditText shopNameET;
    @Bind(R.id.contactsET)
    EditText contactsET;
    @Bind(R.id.phoneET)
    EditText phoneET;
    @Bind(R.id.districtET)
    EditText districtET;
    @Bind(R.id.addressET)
    EditText addressET;
    @Bind(R.id.noticeET)
    EditText noticeET;
    @Bind(R.id.startTimeTV)
    TextView startTimeTV;
    @Bind(R.id.endTimeTV)
    TextView endTimeTV;
    @Bind(R.id.frontIPV)
    UploadImageView frontIPV;
    @Bind(R.id.backIPV)
    UploadImageView backIPV;
    @Bind(R.id.handheldIPV)
    UploadImageView handheldIPV;
    @Bind(R.id.displayIPV)
    UploadImageView displayIPV;
    @Bind(R.id.shopPhoto1IPV)
    UploadImageView shopPhoto1IPV;
    @Bind(R.id.shopPhoto2IPV)
    UploadImageView shopPhoto2IPV;
    @Bind(R.id.shopPhoto3IPV)
    UploadImageView shopPhoto3IPV;
    @Bind(R.id.hintTextTV)
    TextView hintTextTV;
    @Bind(R.id.shop_position_et)
    EditText shop_position_et;
    @Bind(R.id.send_to_door_rl)
    RelativeLayout send_to_door_rl;
    @Bind(R.id.send_to_door_checkbox_sdv)
    ImageView send_to_door_checkbox_sdv;
    @Bind(R.id.tv_other_title)
    TextView tvOtherTitle;

    /**
     * 正面照
     */
    private String mFrontPhoto;
    /**
     * 反面照
     */
    private String mBackPhoto;
    /**
     * 手持照
     */
    private String mHandheldPhoto;
    /**
     * 显示照
     */
    private String mDisplayPhoto;
    /**
     * 店铺照片1
     */
    private String mShop1Photo;
    /**
     * 店铺照片2
     */
    private String mShop2Photo;
    /**
     * 店铺照片3
     */
    private String mShop3Photo;
    /**
     * 区id
     */
    private String areaId;
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
     * 类型
     * 个人微商 sale，
     * 有店铺实体店 entity，
     * 无店铺 noshop
     */
    private String mType;
    /**
     * 店铺详情
     */
    private ModelShopDetail mModelShopDetail;
    /**
     * 纬度
     */
    private String mLatitude;
    /**
     * 经度
     */
    private String mLongitude;
    /**
     * 店铺状态 nopass 未通过，重新审核
     */
    private String mShopStatus;
    /**
     * 是否送货上门
     */
    private boolean isSendToDoor = false;
    /**
     * 是否能编辑区域
     * 查看详情时不能编辑
     */
//    private boolean canNotEditArea = false;
    private OptionsPickerView mPickerView;

    private int mPicType;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_checkin_infomation;
    }

    @Override
    protected void init() {
//        String where = getIntent().getStringExtra("where");
//        if (where != null && where.equals("myShopDetail")) {
//            canNotEditArea = true;
//        }
        mType = getIntent().getStringExtra("type");
        mShopStatus = getIntent().getStringExtra("mShopStatus");
        mModelShopDetail = getIntent().getExtras() == null ?
                null : (ModelShopDetail) getIntent().getExtras().get("model");

        String titleStr = "";
        if (mType.equals("entity")) {
            titleStr = "有公司/线下实体店";
            shopPhoto1IPV.setDesc("营业执照");
            shopPhoto2IPV.setDesc("公司/店铺门头照");
            shopPhoto3IPV.setDesc("内部环境照");
            tvOtherTitle.setText("环境照片和营业执照（3张）");
        } else if (mType.equals("noshop")) {
            titleStr = "无公司场地无实体店";
            shopPhoto1IPV.setDesc("营业执照");
            shopPhoto2IPV.setDesc("产品照");
            shopPhoto3IPV.setDesc("产品照");
            tvOtherTitle.setText("营业执照，产品照（3张）");
        } else if (mType.equals("sale")) {
            titleStr = "个人微商";
            shopPhoto1IPV.setDesc("产品照片");
            shopPhoto2IPV.setDesc("产品照片");
            shopPhoto3IPV.setDesc("产品照片");
            tvOtherTitle.setText("产品照片（3张）");
        }

        // 编辑店铺
        if (mModelShopDetail != null) {
            toolBar.setTitle("资料修改(" + titleStr + ")");
            hintTextTV.setText("注: 保存后资料需要重新审核");

            if (!TextUtils.isEmpty(mModelShopDetail.getRemark())
                    && null != mShopStatus && TextUtils.equals(mShopStatus, "nopass")) {
                SimpleDialog.showSimpleRemarkWithTitleDialog(this, "驳回原因", mModelShopDetail.getRemark());
            }

            shopNameET.setText(mModelShopDetail.getShopName());
            contactsET.setText(mModelShopDetail.getMobileNo());
            phoneET.setText(mModelShopDetail.getTelephone());
            mProvince = mModelShopDetail.getProvince();
            mCity = mModelShopDetail.getCity();
            mArea = mModelShopDetail.getArea();
//            areaId = mModelShopDetail.
            districtET.setText(mProvince + " " + mCity + " " + mArea);
            addressET.setText(mModelShopDetail.getAddress());
            noticeET.setText(mModelShopDetail.getIntroduce());
            startTimeTV.setText(mModelShopDetail.getOpenTimeStr());
            endTimeTV.setText(mModelShopDetail.getCloseTimeStr());
            mLatitude = mModelShopDetail.getLatitude();
            mLongitude = mModelShopDetail.getLongitude();
            isSendToDoor = mModelShopDetail.getLogisticsType().equals("all");
            send_to_door_checkbox_sdv.setBackground(getResources().getDrawable(isSendToDoor ? R.mipmap.select_circle : R.mipmap.noselect_circle));

            // 身份证,证件照片
            String[] idCardImgs = mModelShopDetail.getIdentityPath().split(",");
            for (int i = 0; i < idCardImgs.length; i++) {
                switch (i) {
                    case 0:
                        mFrontPhoto = idCardImgs[i];
                        frontIPV.setImageView(mFrontPhoto);
                        break;
                    case 1:
                        mBackPhoto = idCardImgs[i];
                        backIPV.setImageView(mBackPhoto);
                        break;
                    case 2:
                        mHandheldPhoto = idCardImgs[i];
                        handheldIPV.setImageView(mHandheldPhoto);
                        break;
                    default:
                        break;
                }
            }
            // 显示照片
            mDisplayPhoto = mModelShopDetail.getLogoPath();
            displayIPV.setImageView(mDisplayPhoto);
            // 环境照片
            String[] otherImgs = mModelShopDetail.getImagesPath().split(",");
            for (int i = 0; i < otherImgs.length; i++) {
                switch (i) {
                    case 0:
                        mShop1Photo = otherImgs[i];
                        shopPhoto1IPV.setImageView(mShop1Photo);
                        break;
                    case 1:
                        mShop2Photo = otherImgs[i];
                        shopPhoto2IPV.setImageView(mShop2Photo);
                        break;
                    case 2:
                        mShop3Photo = otherImgs[i];
                        shopPhoto3IPV.setImageView(mShop3Photo);
                        break;
                    default:
                        break;
                }
            }

        } else {
            toolBar.setTitle("商家入驻(" + titleStr + ")");

            // 输入记录
            ShopDetailCache shopDetailCache = LitePal.findLast(ShopDetailCache.class);
            if (shopDetailCache != null) {
                shopNameET.setText(shopDetailCache.getShopName());
                contactsET.setText(shopDetailCache.getMobileNo());
                phoneET.setText(shopDetailCache.getTelephone());
                mProvince = shopDetailCache.getProvince();
                mCity = shopDetailCache.getCity();
                mArea = shopDetailCache.getArea();
                districtET.setText(mProvince + " " + mCity + " " + mArea);
                addressET.setText(shopDetailCache.getAddress());
                noticeET.setText(shopDetailCache.getIntroduce());
            }
        }

        mPickerView = CommonUtils.getAddressPicker(this, new OnAddressIDPickerListener() {
            @Override
            public void onResult(View v, ProvinceBean province, ProvinceBean.CitysBean city, ProvinceBean.CitysBean.DistrictsBean area) {
                mProvince = province.getCityName();
                mCity = city.getCityName();
                mArea = area.getCityName();
                if (area.getId() == 0) {
                    areaId = null;
                    ToastUtils.showToast("请重新选择区域");
                    return;
                }
                areaId = area.getId() + "";
                districtET.setText(getString(R.string.address_format, mProvince, mCity, mArea));
            }
        });
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        // 送货上门
        send_to_door_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSendToDoor) {
                    isSendToDoor = false;
                    send_to_door_checkbox_sdv.setBackground(getResources().getDrawable(R.mipmap.noselect_circle));
                    return;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShopCheckInInfomationActivity.this);
                dialog.setMessage("您确定开启免费送货上门服务？系统默认配送到当地区/县。请根据自己店铺情况考量是否开启，开启后可关闭。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isSendToDoor = false;
                                send_to_door_checkbox_sdv.setBackground(getResources().getDrawable(R.mipmap.noselect_circle));
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isSendToDoor = true;
                                send_to_door_checkbox_sdv.setBackground(getResources().getDrawable(R.mipmap.select_circle));
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == ConstantOther.REQUEST_CODE_DISTRICT_SELECT && data.getExtras() != null) {
//            DistrictModel districtModel = (DistrictModel) data.getExtras().get("model");
//            assert districtModel != null;
//            String[] areas = districtModel.getName().split("--");
//            mProvince = areas[0];
//            mCity = areas[1];
//            mArea = areas[2];
//            districtET.setText(districtModel.getName().replace("--", " "));
//        } else
        if (requestCode == ConstantHandler.RESULT_CODE_SELECT_ADDRESS && data != null) {
            mLatitude = data.getStringExtra("lat");
            mLongitude = data.getStringExtra("lon");
            shop_position_et.setText("已定位");
            shop_position_et.setTextColor(Color.BLACK);
        } else if (resultCode == ConstantHandler.RESULT_CODE_COMMON_DATA && data != null) {
            String hour = data.getStringExtra("hour");
            String min = data.getStringExtra("min");
            if (requestCode == 0x02) {
                startTimeTV.setText(hour + ":" + min);
            } else {
                endTimeTV.setText(hour + ":" + min);
            }
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);

        String path = getTakeSuccessPath(tResult);
        NetComment.uploadPic(this, path, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                switch (mPicType) {
                    case 1:// 正面照
                        frontIPV.setImageView(url);
                        mFrontPhoto = url;
                        break;
                    case 2:// 背面照
                        backIPV.setImageView(url);
                        mBackPhoto = url;
                        break;
                    case 3:// 手持照
                        handheldIPV.setImageView(url);
                        mHandheldPhoto = url;
                        break;
                    case 4:// 门店LOGO照
                        displayIPV.setImageView(url);
                        mDisplayPhoto = url;
                        break;
                    case 5:// 门头照
                        shopPhoto1IPV.setImageView(url);
                        mShop1Photo = url;
                        break;
                    case 6:// 店内照
                        shopPhoto2IPV.setImageView(url);
                        mShop2Photo = url;
                        break;
                    case 7:// 营业执照
                        shopPhoto3IPV.setImageView(url);
                        mShop3Photo = url;
                        break;
                    default:

                }
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast(msg);
            }
        });

    }

    @OnClick({R.id.submitBTN, R.id.districtRL, R.id.districtET, R.id.startTimeTV,
            R.id.endTimeTV, R.id.addressFL})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                submitApplyShop();
                break;
            case R.id.districtRL:
            case R.id.districtET:
//                Intent intent = new Intent(this, DialogDistrictSelectActivity.class);
//                startActivityForResult(intent, 0x01);

//                if (canNotEditArea) {
//                    ToastUtils.showToast("地区不能修改");
//                    return;
//                }
                mPickerView.show();
                break;
            case R.id.addressFL:
//                Intent intent = new Intent(this, BaiduLBS_SearchActivity.class);
//                intent.putExtra("latitude", mLatitude);
//                intent.putExtra("longitude", mLongitude);
//                startActivityForResult(intent, 0x01);
                ActivityUtils.getInstance().jumpActivityForResult(ShopCheckInInfomationActivity.this, AMapSearchActivity.class,
                        ConstantHandler.RESULT_CODE_SELECT_ADDRESS, null);
                break;
            case R.id.startTimeTV:
                startActivityForResult(new Intent(this, DialogHourMinActivity.class), 0x02);
                break;
            case R.id.endTimeTV:
                startActivityForResult(new Intent(this, DialogHourMinActivity.class), 0x03);
                break;
            default:
                break;
        }
    }

    private void submitApplyShop() {
        String identityPath = mFrontPhoto + "," + mBackPhoto + "," + mHandheldPhoto;
        String imagesPath = mShop1Photo + "," + mShop2Photo + "," + mShop3Photo;
        String logoPath = mDisplayPhoto;
        String closeTimeStr = endTimeTV.getText().toString();
        String openTimeStr = startTimeTV.getText().toString();
        String introduce = noticeET.getText().toString();
        String address = addressET.getText().toString();
        String area = mArea;
        String city = mCity;
        String province = mProvince;
        String latitude = mLatitude;
        String longitude = mLongitude;
        String telephone = phoneET.getText().toString();
        String mobileNo = contactsET.getText().toString();
        String shopName = shopNameET.getText().toString();
        String shopType = mType;
        String logisticsType = isSendToDoor ? "all" : "take";

        if (TextUtils.isEmpty(shopName)) {
            ToastUtils.showToast(getString(R.string.input_shopname));
            return;
        }
        if (TextUtils.isEmpty(mobileNo)) {
            ToastUtils.showToast(getString(R.string.input_contacts));
            return;
        }
        if (TextUtils.isEmpty(telephone)) {
            ToastUtils.showToast(getString(R.string.input_contacts_phone));
            return;
        }
        if (TextUtils.isEmpty(area) || TextUtils.isEmpty(city) || TextUtils.isEmpty(province)) {
            ToastUtils.showToast(getString(R.string.select_address));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showToast(getString(R.string.input_detail_address));
            return;
        }
        if (mLatitude == null || TextUtils.isEmpty(mLatitude)) {
            ToastUtils.showToast("请定位店铺地址");
            return;
        }
        if (TextUtils.isEmpty(introduce)) {
            ToastUtils.showToast(getString(R.string.input_shop_notice));
            return;
        }
        if (TextUtils.isEmpty(openTimeStr)) {
            ToastUtils.showToast("开始时间");
            return;
        }
        if (TextUtils.isEmpty(closeTimeStr)) {
            ToastUtils.showToast("结束时间");
            return;
        }
        if (TextUtils.isEmpty(mFrontPhoto)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + getString(R.string.front_photo));
            return;
        }
        if (TextUtils.isEmpty(mBackPhoto)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + "反面照");
            return;
        }
        if (TextUtils.isEmpty(mHandheldPhoto)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + "手持照");
            return;
        }
        if (TextUtils.isEmpty(mDisplayPhoto)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + displayIPV.getDesc());
            return;
        }
        if (TextUtils.isEmpty(mShop1Photo)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + shopPhoto1IPV.getDesc());
            return;
        }
        if (TextUtils.isEmpty(mShop2Photo)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + shopPhoto2IPV.getDesc());
            return;
        }
        if (TextUtils.isEmpty(mShop3Photo)) {
            ToastUtils.showToast(getString(R.string.please_shoot) + shopPhoto3IPV.getDesc());
            return;
        }

        SimpleDialog.showLoadingHintDialog(this, 4);
        if (mModelShopDetail == null) {
            // 申请店铺
            HttpParameterUtil.getInstance().requestApplyShop(identityPath, imagesPath, logoPath,
                    closeTimeStr, openTimeStr, introduce, address, area, city, province, areaId, latitude,
                    longitude, telephone, mobileNo, shopName, shopType, mHandler, logisticsType);
        } else if (mShopStatus != null && mShopStatus.equals("nopass")) {
            // 店铺重审
            HttpParameterUtil.getInstance().requestAnewApplyShop(identityPath, imagesPath, logoPath,
                    closeTimeStr, openTimeStr, introduce, address, area, city, province, areaId, latitude,
                    longitude, telephone, mobileNo, shopName, shopType, mModelShopDetail.getShopNo(), mHandler, logisticsType);
        } else {
            // 更新店铺
            HttpParameterUtil.getInstance().requestUpdateShop(identityPath, imagesPath, logoPath,
                    closeTimeStr, openTimeStr, introduce, address, area, city, province, areaId, latitude,
                    longitude, telephone, mobileNo, shopName, shopType, mModelShopDetail.getShopNo(), mHandler, logisticsType);
        }
    }

    /**
     * 图片点击
     *
     * @param view
     */
    @OnClick({R.id.frontIPV, R.id.backIPV, R.id.handheldIPV, R.id.displayIPV, R.id.shopPhoto1IPV, R.id.shopPhoto2IPV, R.id.shopPhoto3IPV,})
    public void onPicClick(View view) {
        switch (view.getId()) {
            case R.id.frontIPV:         // 正面照
                mPicType = 1;
                break;
            case R.id.backIPV:          // 背面照
                mPicType = 2;
                break;
            case R.id.handheldIPV:      // 手持照
                mPicType = 3;
                break;
            case R.id.displayIPV:       // 门店LOGO照
                mPicType = 4;
                break;
            case R.id.shopPhoto1IPV:    // 门头照
                mPicType = 5;
                break;
            case R.id.shopPhoto2IPV:    // 店内照
                mPicType = 6;
                break;
            case R.id.shopPhoto3IPV:    // 营业执照
                mPicType = 7;
                break;
            default:
        }

        showSelPopupWind(view, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 保存记录
        ShopDetailCache shopDetailCache = new ShopDetailCache();
        shopDetailCache.setShopName(shopNameET.getText().toString().trim());
        shopDetailCache.setMobileNo(contactsET.getText().toString().trim());
        shopDetailCache.setTelephone(phoneET.getText().toString().trim());
        shopDetailCache.setProvince(mProvince);
        shopDetailCache.setCity(mCity);
        shopDetailCache.setArea(mArea);
        shopDetailCache.setAddress(addressET.getText().toString().trim());
        shopDetailCache.setIntroduce(noticeET.getText().toString().trim());
        shopDetailCache.save();
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_UPDATE_SHOP_SUCCESS:
                // 修改成功
//                SimpleDialog.cancelLoadingHintDialog();
//                ToastUtils.showToast("修改成功");
//                finish();
//                break;
            case ConstantHandler.WHAT_APPLY_SHOP_SUCCESS:
                SharedPreferencesUtils.put(Constants.MINE_SHOP_APPLY_LOADING, true);
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交成功");
                setResult(RESULT_OK);
                ActivityUtils.getInstance().jumpSubmitResult(3);
                finish();
                break;
            case ConstantHandler.WHAT_APPLY_SHOP_FAIL:
            case ConstantHandler.WHAT_UPDATE_SHOP_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<ShopCheckInInfomationActivity> mImpl;

        public MyHandler(ShopCheckInInfomationActivity mImpl) {
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
