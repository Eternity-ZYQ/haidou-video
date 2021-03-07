package com.yimeng.haidou.nearby;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelShopDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.ShopProductUtils;
import com.yimeng.widget.ProductToolBar;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/18 0018 下午 02:58.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺详情
 * </pre>
 */
public class ShopDetailActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    ProductToolBar toolBar;
    @Bind(R.id.iv_shop_logo)
    ImageView ivShopLogo;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.tv_shop_notice)
    TextView tvShopNotice;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_shop_address)
    TextView tvShopAddress;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.iv_shop_logo_bg)
    ImageView ivShopLogoBg;
    @Bind(R.id.tv_linkman)
    TextView tvLinkman;

    /**
     * 店铺编号
     */
    private String mShopNo;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles;
    private ShopDetailProductFragment mShopSaleProductFragment;
    private ShopDetailProductFragment mShopDetailProductFragment;
    private ShopDetailEvaluateFragment mDetailEvaluateFragment;
    private ShopDetailMerchantFragment mShopDetailMerchantFragment;
    /**
     * 店铺详情
     */
    private ModelShopDetail mModelShopDetail;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * 是否来自任务页面
     */
    private boolean mIsFromTask = false;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            ToastUtils.showToast("数据异常!");
            finish();
            return;
        }

        mShopNo = bundle.getString("shopNo");
        mIsFromTask = bundle.getBoolean("isTask", false);

        if (mIsFromTask) { // 来自任务页面
            titles = Arrays.asList("促销", "评价", "商家");
            mShopSaleProductFragment = ShopDetailProductFragment.getNewInstance(mShopNo, true);
            fragments.add(mShopSaleProductFragment);


        } else { // 非来自任务
            titles = Arrays.asList("商品", "评价", "商家");
            mShopDetailProductFragment = ShopDetailProductFragment.getNewInstance(mShopNo, false);
            fragments.add(mShopDetailProductFragment);


        }

        mDetailEvaluateFragment = ShopDetailEvaluateFragment.getNewInstance(mShopNo);
        mShopDetailMerchantFragment = new ShopDetailMerchantFragment();
        fragments.add(mDetailEvaluateFragment);
        fragments.add(mShopDetailMerchantFragment);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new ProductToolBar.OnToolBarClick() {
            @Override
            public void onLeftClick() {
                super.onLeftClick();
                finish();
            }

            @Override
            public void onRightClick() {
                super.onRightClick();
                // 分享
//                ActivityUtils.shareActivity(ShopDetailActivity.this);
                CommonUtils.shareApp();
            }
        });

        toolBar.getRightImage2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.checkLogin()) {
                    ActivityUtils.getInstance().jumpLoginActivity();
                    return;
                }
                // 收藏
                if (mModelShopDetail == null) {
                    return;
                }
                if (mModelShopDetail.isCollected()) {
                    HttpParameterUtil.getInstance().requestDelShopCollect(mShopNo, mHandler);
                } else {
                    HttpParameterUtil.getInstance().requestAddShopCollect(mShopNo, mHandler);
                }
            }
        });

    }

    @Override
    protected void loadData() {
        SimpleDialog.showLoadingHintDialog(this, 1);
        HttpParameterUtil.getInstance().requestShopDetail(mShopNo, mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopProductUtils.getInstance().getShopProductSelectList().clear();
    }

    private static class MyHandler extends Handler {

        private WeakReference<ShopDetailActivity> mImpl;

        public MyHandler(ShopDetailActivity mImpl) {
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
        SimpleDialog.cancelLoadingHintDialog();
        switch (msg.what) {
            case ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS:
                // 店铺详情
                mModelShopDetail = (ModelShopDetail) msg.obj;
                setShopInfo();
                break;
            case ConstantHandler.WHAT_DEL_SHOP_COLLECT_SUCCESS:
                // 删除店铺收藏
                mModelShopDetail.setCollected(false);
                toolBar.getRightImage2().setImageResource(R.mipmap.ico_collect_black);
                ToastUtils.showToast("取消收藏成功!");
                break;
            case ConstantHandler.WHAT_ADD_SHOP_COLLECT_SUCCESS:
                // 添加店铺收藏
                mModelShopDetail.setCollected(true);
                toolBar.getRightImage2().setImageResource(R.mipmap.ico_collect_black_hl);
                ToastUtils.showToast("收藏店铺成功!");
                break;
            default:

        }
    }

    /**
     * 店铺信息
     */
    private void setShopInfo() {
        if (mModelShopDetail == null) return;

        String logoPath = mModelShopDetail.getLogoPath();
        CommonUtils.showBlurImage(ivShopLogoBg, logoPath, 5);
//        ivShopLogoBg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        CommonUtils.showRadiusImage(ivShopLogo, logoPath, DeviceUtils.dp2px(this, 10), true, true, true, true);
        tvShopName.setText(mModelShopDetail.getShopName());
        tvShopAddress.setText(mModelShopDetail.getAddressStr());
        tvShopNotice.setText("公告: " + mModelShopDetail.getIntroduce());
        tvTime.setText("营业时间: " + mModelShopDetail.getOpenTimeStr() + " - " + mModelShopDetail.getCloseTimeStr());
        tvLinkman.setText("联系方式: " + mModelShopDetail.getTelephone());
        // 是否收藏
        toolBar.getRightImage2().setImageResource(mModelShopDetail.isCollected() ? R.mipmap.ico_collect_black_hl : R.mipmap.ico_collect_black);

        String[] images = mModelShopDetail.getImagesPath().split(",");
        LinkedList<String> grades = new LinkedList<>();
        Collections.addAll(grades, images);
        mShopDetailMerchantFragment.setImages(grades);

//        // 线下实体店（entity）超过5公里不能购买，
//        if(mModelShopDetail.getShopType().equals("entity")) {
//            // 在判断完是否不在一个区域内之前先禁用提交按钮
//            mShopSaleProductFragment.canGoPay(false);
//            mShopDetailProductFragment.canGoPay(false);
//            // 判断店铺是否在当前定位的区域
//            double lat1 = Double.parseDouble(mModelShopDetail.getLatitude());
//            double lon1 = Double.parseDouble(mModelShopDetail.getLongitude());
//            double lat2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));
//            double lon2 = Double.parseDouble((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
//            boolean notTheSameArea = false;
//            if (mModelShopDetail.getCity().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_CITY, "深圳市")) &&
//                    mModelShopDetail.getArea().equals((String) SharedPreferencesUtils.get(Constants.APP_LOCATION_AREA, "龙华区"))) {
//                notTheSameArea = true;
//            }
//            // 如果不在同一区域并且距离大于5000m则不能购买
//            if (!notTheSameArea && CommonUtils.calcDistance(lat1, lon1, lat2, lon2) > 5000.00) {
//                // 隐藏提交按钮、价格，显示不在与服务区文字
////            mShopDetailProductFragment.shopIsScope(false);
//                // 线下实体店（entity）超过5公里不能购买，
//                mShopSaleProductFragment.shopIsScope(false);
//                mShopDetailProductFragment.shopIsScope(false);
//            } else {
//                // 同一区域解除禁止
//                mShopSaleProductFragment.canGoPay(true);
//                mShopDetailProductFragment.canGoPay(true);
//            }
//
//        }


        // 店铺类型
        if (mIsFromTask)
            mShopSaleProductFragment.setShopType(mModelShopDetail.getShopType());
        else
            mShopDetailProductFragment.setShopType(mModelShopDetail.getShopType());

        // 商家评分
        mDetailEvaluateFragment.setScore(mModelShopDetail.getShopScore());


    }

}
