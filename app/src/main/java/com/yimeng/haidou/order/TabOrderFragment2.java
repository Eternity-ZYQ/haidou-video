package com.yimeng.haidou.order;

import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.yimeng.base.BaseFragment;
import com.yimeng.haidou.R;
import com.yimeng.popupwind.PopupwindUtils;
import com.huige.library.popupwind.PopupWindowUtils;

import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/24 0024 下午 07:25.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class TabOrderFragment2 extends BaseFragment {


    /**
     * 显示商城订单
     */
    private boolean isShowMallOrder = false;
    private MallOrderFragment mMallOrderFragment;
    private ShopOrderFragment mShopOrderFragment;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_order2;
    }

    @Override
    protected void init() {
        checkFragment(isShowMallOrder);
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.iv_menu_check)
    public void showMenuDialog(View view){
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_order_sel_type, null);
        int[] windowPos = PopupwindUtils.calculatePopWindowPos(view, rootView);
        new PopupWindowUtils(getContext(), rootView)
                .setLayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                .setStyle(-1)
                .setOnClickListenerByViewId(R.id.tv_mall_order, new PopupWindowUtils.onPopupWindClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 商城订单
                        isShowMallOrder = true;
                        checkFragment(isShowMallOrder);

                    }
                })
                .setOnClickListenerByViewId(R.id.tv_shop_order, new PopupWindowUtils.onPopupWindClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 线下店铺订单
                        isShowMallOrder = false;
                        checkFragment(isShowMallOrder);
                    }
                })
                .showAtLocation(getActivity(), view, Gravity.TOP|Gravity.START, windowPos[0], windowPos[1] + 30);
    }

    /**
     *
     * @param flag true 商城订单
     *             false 店铺订单
     */
    private void checkFragment(boolean flag){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if(flag) {
            if(mMallOrderFragment == null) {
                mMallOrderFragment = new MallOrderFragment();
            }
            transaction.replace(R.id.layout_order, mMallOrderFragment);
        }else{
            if(mShopOrderFragment == null) {
                mShopOrderFragment = new ShopOrderFragment();
            }
            transaction.replace(R.id.layout_order, mShopOrderFragment);
        }
        transaction.commit();
    }
}
