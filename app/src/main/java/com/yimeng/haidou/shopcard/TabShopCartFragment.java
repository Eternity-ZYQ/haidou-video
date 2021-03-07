//package com.yimeng.haidou.shopcard;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.yimeng.base.BaseFragment;
//import com.yimeng.config.ConstantHandler;
//import com.yimeng.dialog.SimpleDialog;
//import com.yimeng.entity.ModelShopCart;
//import com.yimeng.entity.ModelShopCartGoods;
//import com.yimeng.haidou.MainActivity;
//import com.yimeng.haidou.R;
//import com.yimeng.haidou.shopcard.adapter.ShopCartAdapter;
//import com.yimeng.net.lxmm_net.HttpParameterUtil;
//import com.yimeng.utils.ActivityUtils;
//import com.yimeng.utils.UnitUtil;
//import com.huige.library.utils.ToastUtils;
//import com.lxj.xpopup.interfaces.OnConfirmListener;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import java.lang.ref.WeakReference;
//import java.util.LinkedList;
//
//import butterknife.Bind;
//import butterknife.OnClick;
//
///**
// * 购物车
// */
//public class TabShopCartFragment extends BaseFragment {
//
//
//    @Bind(R.id.lv_shopping_cart)
//    ListView mListView; //没有数据不显示
//    @Bind(R.id.sdv_shop_cart_not_data)
//    ImageView mNotDataSDV;
//    @Bind(R.id.refreshLayout)
//    SmartRefreshLayout mRefreshLayout;
//    @Bind(R.id.tv_consumption_beans)
//    TextView beansTV;
//    @Bind(R.id.fl_select_all)
//    FrameLayout SelectAllFL;
//    @Bind(R.id.btn_select_all)
//    ImageView selectAllSDV;
//    @Bind(R.id.tv_buy)
//    TextView toBuyTV;
//
//    /**
//     * 当前点击的购物车商品
//     */
//    private ModelShopCartGoods mModelShopCartGoods;
//    /**
//     * 选中的商品总价格（分）
//     */
//    private int mTotalMoney;
//    /**
//     * 选中的商品购物车编号
//     */
//    private String mShopCarNo;
//    /**
//     * 全选
//     */
//    private boolean mAllChecked;
//
//    private ShopCartAdapter mAdapter;
//    private LinkedList<ModelShopCart> mList;
//    private MyHandler mHandler = new MyHandler(this);
//
//    public TabShopCartFragment() {
//        super();
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            mRefreshLayout.autoRefresh();
//        }
//    }
//
//    @Override
//    protected int setLayoutResId() {
//        return R.layout.fm_shopping_cart;
//    }
//
//    @Override
//    protected void init() {
//        mTotalMoney = 0;
//        mShopCarNo = "";
//        mAllChecked = false;
//        beansTV.setText("");
//        selectAllSDV.setImageResource(R.mipmap.noselect_circle);
//
//        mList = new LinkedList<>();
//        mAdapter = new ShopCartAdapter(mList, getActivity(), mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
//        mListView.setAdapter(mAdapter);
//
//        mRefreshLayout.autoRefresh();
//        mRefreshLayout.setEnableRefresh(true);
//        mRefreshLayout.setEnableLoadMore(false);
//        mRefreshLayout.setEnableAutoLoadMore(false);//开启自动加载功能
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(final RefreshLayout refreshlayout) {
//                HttpParameterUtil.getInstance().requestMyShopCar(mHandler);
//            }
//        });
//    }
//
//    @Override
//    protected void initListener() {
//
//    }
//
//    @Override
//    protected void loadData() {
//
//    }
//
//    //全选点击
//    @OnClick(R.id.fl_select_all)
//    void selectAll() {
//        setAllChecked();
//    }
//
//    private void setAllChecked() {
//        if (mAllChecked) {
//            selectAllSDV.setImageResource(R.mipmap.noselect_circle);
//            for (int i = 0; i < mList.size(); i++) {
//                mList.get(i).setShopIsSelected(false);
//                for (int j = 0; j < mList.get(i).getModelShopCartGoodses().size(); j++) {
//                    mList.get(i).getModelShopCartGoodses().get(j).setProductIsSelected(false);
//                }
//            }
//            mAllChecked = false;
//            mAdapter.mList = mList;
//            mAdapter.notifyDataSetChanged();
//        } else {
//            selectAllSDV.setImageResource(R.mipmap.select_circle);
//            for (int i = 0; i < mList.size(); i++) {
//                mList.get(i).setShopIsSelected(true);
//                for (int j = 0; j < mList.get(i).getModelShopCartGoodses().size(); j++) {
//                    ModelShopCartGoods shopCartGoods = mList.get(i).getModelShopCartGoodses().get(j);
//                    if (shopCartGoods.getIsOnsale().equals("1")) { // 上架状态
//                        shopCartGoods.setProductIsSelected(true);
//                    }
//                }
//            }
//            mAllChecked = true;
//            mAdapter.mList = mList;
//            mAdapter.notifyDataSetChanged();
//        }
//        setTotalMoney();
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void setTotalMoney() {
//        if (mAdapter == null || getActivity().isFinishing()) {
//            return;
//        }
//
//        mTotalMoney = 0;
//        mShopCarNo = "";
//        for (ModelShopCart shopCart : mAdapter.mList) {
//            for (ModelShopCartGoods goods : shopCart.getModelShopCartGoodses()) {
//                if (goods.isProductIsSelected()) {
//                    mTotalMoney += UnitUtil.getInt(goods.getPrice()) * UnitUtil.getInt(goods.getShopCarNum());
//                    mShopCarNo += goods.getShopCarNo() + ",";
//                }
//            }
//        }
//
//        mShopCarNo = mShopCarNo.contains(",") ? mShopCarNo.substring(0, mShopCarNo.length() - 1) : mShopCarNo;
//        beansTV.setText(UnitUtil.getMoney(mTotalMoney + ""));
//        String text = mAdapter.getCount() == 0 ? "逛一逛" : "结算";
//        toBuyTV.setText(text);
//    }
//
//    @OnClick(R.id.tv_buy)
//    void OnClick() {
//
//        if (mAdapter == null || mAdapter.getCount() == 0) {
//            ((MainActivity) getActivity()).bottomNvgView.setSelectedItemId(R.id.tab_shop);
//        } else if (mAdapter.getCount() != 0 && !TextUtils.isEmpty(mShopCarNo)) {
//            Bundle bundle = new Bundle();
//            Intent intent = new Intent();
//            intent.putExtra("mShopCarNo", mShopCarNo);
//            intent.putExtras(bundle);
//            intent.setClass(getActivity(), ShopCartSettleActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void disposeGoodsData(final Message msg) {
//        int fPosition = 0;
//        int position = 0;
//        int count = 0;
//        String shopNo;
//        switch (msg.what) {
//            case ConstantHandler.WHAT_MY_SHOP_CAR_SUCCESS:
//                //购物车列表
//                mRefreshLayout.finishRefresh();
//                mList = (LinkedList<ModelShopCart>) msg.obj;
//
//                if (mList.size() == 0) {
//                    mNotDataSDV.setVisibility(View.VISIBLE);
//                    mListView.setVisibility(View.GONE);
//                } else {
//                    mNotDataSDV.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
//                }
//                //把所有设置成选中
////                for (int i = 0; i < mList.size(); i++) {
////                    mList.get(i).setShopIsSelected(true);
////                    for (int j = 0; j < mList.get(i).getModelShopCartGoodses().size(); j++) {
////                        mList.get(i).getModelShopCartGoodses().get(j).setProductIsSelected(true);
////                    }
////                }
//
//                mAdapter.mList = mList;
//                mAdapter.notifyDataSetChanged();
//                setTotalMoney();
//                break;
//            case ConstantHandler.WHAT_MY_SHOP_CAR_FAIL:
//                mRefreshLayout.finishRefresh();
//                break;
//            case ConstantHandler.WHAT_UPDATE_SHOP_CAR_NUM_SUCCESS:
//                //修改数量
//                SimpleDialog.cancelLoadingHintDialog();
//                if (mModelShopCartGoods != null && mAdapter != null) {
//                    fPosition = mModelShopCartGoods.getfPosition();
//                    position = mModelShopCartGoods.getPosition();
//                    count = Integer.parseInt(mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNum());
//                    count = mModelShopCartGoods.getmStatus() == 1 ? ++count : count > 1 ? --count : 1;
//
//                    count = count > UnitUtil.getInt(mModelShopCartGoods.getSort()) ?
//                            UnitUtil.getInt(mModelShopCartGoods.getSort()) : count;
//                    mAdapter.mList.get(fPosition).getModelShopCartGoodses().get(position).setShopCarNum(String.valueOf(count));
//                    mAdapter.notifyDataSetChanged();
//
//                    setTotalMoney();
//                }
//                break;
//            case ConstantHandler.WHAT_UPDATE_SHOP_CAR_NUM_FAIL:
//                SimpleDialog.cancelLoadingHintDialog();
//                break;
//            case ConstantHandler.WHAT_DELETE_SHOP_CAR_SUCCESS:
//                //删除商品
//                mRefreshLayout.autoRefresh();
//                break;
//            case ConstantHandler.WHAT_DELETE_SHOP_CAR_FAIL:
//                //删除商品
//                ToastUtils.showToast((String) msg.obj);
//                break;
//            default:
//                break;
//        }
//
//        switch (msg.arg1) {
//            case R.id.click_RL:
//                ModelShopCartGoods model = (ModelShopCartGoods) msg.obj;
////                Intent intent = new Intent();
////                intent.putExtra("extra", model.getProductNo());
////                intent.putExtra("mShopNo", model.getmShopNo());
////                intent.setClass(getActivity(), GoodsDetailActivity.class);
////                startActivity(intent);
//                if (model.getIsOnsale().equals("1")) {
//                    ActivityUtils.getInstance().jumpShopProductActivity(1, model.getmShopNo(), model.getProductNo(), 0,"");
//                } else {
//                    ToastUtils.showToast(getString(R.string.good_not_on_sale));
//                }
//                break;
//            case R.id.btn_icon_minus_count:
//                //数量减1
//                ModelShopCartGoods goodsModel = (ModelShopCartGoods) msg.obj;
//                mModelShopCartGoods = goodsModel;
//                mModelShopCartGoods.setmStatus(2);
//                fPosition = goodsModel.getfPosition();
//                position = goodsModel.getPosition();
//                count = Integer.parseInt(mAdapter.mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNum());
//                count = count > 1 ? --count : 1;
//                if (count >= 1) {
//                    shopNo = mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNo();
//                    SimpleDialog.showLoadingHintDialog(getActivity(), 4);
//                    HttpParameterUtil.getInstance().requestUpdateShopCar(shopNo, String.valueOf(count), mHandler);
//                }
//                break;
//            case R.id.btn_icon_add_count:
//                //数量加1
//                goodsModel = (ModelShopCartGoods) msg.obj;
//                mModelShopCartGoods = goodsModel;
//                mModelShopCartGoods.setmStatus(1);
//                fPosition = goodsModel.getfPosition();
//                position = goodsModel.getPosition();
//                count = Integer.parseInt(mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNum());
//                if (count < UnitUtil.getInt(goodsModel.getSort())) {
//                    ++count;
//                    shopNo = mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNo();
//                    SimpleDialog.showLoadingHintDialog(getActivity(), 4);
//                    HttpParameterUtil.getInstance().requestUpdateShopCar(shopNo, String.valueOf(count), mHandler);
//                }
//                break;
//            case R.id.ll_delete_goods:
//                //删除商品
//                SimpleDialog.showConfirmDialog(getActivity(), null, "确定删除?", null, new OnConfirmListener() {
//                    @Override
//                    public void onConfirm() {
//                        ModelShopCartGoods goodsModel = (ModelShopCartGoods) msg.obj;
//                        int fPosition = goodsModel.getfPosition();
//                        int position = goodsModel.getPosition();
//                        String shopNo = mList.get(fPosition).getModelShopCartGoodses().get(position).getShopCarNo();
//                        HttpParameterUtil.getInstance().requestDeleteShopCar(shopNo, mHandler);
//                    }
//                });
//
//                break;
//            case R.id.fl_shop_select:
//                //选中商铺
//                ModelShopCart item = (ModelShopCart) msg.obj;
//                boolean checked = !item.isShopIsSelected();
//                mAdapter.mList.get(item.getPosition()).setShopIsSelected(checked);
//                for (int i = 0; i < item.getModelShopCartGoodses().size(); i++) {
//                    ModelShopCartGoods shopCartGoods = mAdapter.mList.get(item.getPosition())
//                            .getModelShopCartGoodses()
//                            .get(i);
//                    if (shopCartGoods.getIsOnsale().equals("1")) {
//                        shopCartGoods.setProductIsSelected(checked);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
//                setCheckedStatus();
//                setTotalMoney();
//                break;
//            case R.id.fl_select:
//                //选中商品
//                goodsModel = (ModelShopCartGoods) msg.obj;
//                // 当前商品
//                mAdapter.mList.get(goodsModel.getfPosition())
//                        .getModelShopCartGoodses()
//                        .get(goodsModel.getPosition())
//                        .setProductIsSelected(!goodsModel.isProductIsSelected());
//
//                // 关联的店铺
//                boolean shopChecked = false;
//                for (ModelShopCartGoods goods : mAdapter.mList.get(goodsModel.getfPosition()).getModelShopCartGoodses()) {
//                    if (goods.isProductIsSelected()) {
//                        shopChecked = true;
//                        break;
//                    }
//                }
//                mAdapter.mList.get(goodsModel.getfPosition()).setShopIsSelected(shopChecked);
//                mAdapter.notifyDataSetChanged();
//
//                setCheckedStatus();
//                setTotalMoney();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void setCheckedStatus() {
//        mAllChecked = true;
//        for (int i = 0; i < mList.size(); i++) {
//            for (int j = 0; j < mList.get(i).getModelShopCartGoodses().size(); j++) {
//                if (!mList.get(i).getModelShopCartGoodses().get(j).isProductIsSelected()) {
//                    mAllChecked = false;
//                    break;
//                }
//            }
//        }
//
//        int resId = mAllChecked ? R.mipmap.select_circle : R.mipmap.noselect_circle;
//        selectAllSDV.setImageResource(resId);
//    }
//
//    private static class MyHandler extends Handler {
//
//        private WeakReference<TabShopCartFragment> mImpl;
//
//        public MyHandler(TabShopCartFragment mImpl) {
//            this.mImpl = new WeakReference<>(mImpl);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (mImpl.get() != null) {
//                mImpl.get().disposeGoodsData(msg);
//            }
//        }
//
//    }
//
//}
//
