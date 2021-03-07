package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.GoodsListAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.GoodsModel;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品销售中
 */
@SuppressLint("ValidFragment")
public class ClassifyGoodsFragment extends Fragment implements EditCommodityActivity.AddGoodsListener {

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ListView listView;

    /**
     * 类型 :0销售中,1仓库中,2审核中,3已驳回
     */
    private int mType;
    /**
     * 页码
     */
    private int mPage;
    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * 分类编号
     */
    private String mProductCategoryNo;
    /**
     * 当前商品数据
     */
    private GoodsModel mCurrentGoods;
    /**
     * 分类商品列表adapter
     */
    private GoodsListAdapter mGoodsListAdapter;
    /**
     * entity 　自有商品
     * cuxiao   促销商品
     */
    private String mProductType = "entity";

    @SuppressLint("ValidFragment")
    public ClassifyGoodsFragment(int mType, String mShopNo, String productType, String mProductCategoryNo) {
        this.mType = mType;
        this.mShopNo = mShopNo;
        this.mProductType = productType;
        this.mProductCategoryNo = mProductCategoryNo;
    }

    public ClassifyGoodsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fm_classify_goods, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onAutoRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        LinkedList<GoodsModel> goodData = new LinkedList<>();
        mGoodsListAdapter = new GoodsListAdapter(getActivity(), goodData, mProductType, mHandler, ConstantHandler.WHAT_HANDLER_CLICK, mType);
        listView.setAdapter(mGoodsListAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                if (mType == 0 || mType == 1) {
                    String isOnSale = mType == 0 ? "1" : "0";
                    int success = ConstantHandler.WHAT_QUERY_PRODUCT_LIST_SUCCESS;
                    int fail = ConstantHandler.WHAT_QUERY_PRODUCT_LIST_FAIL;
                    HttpParameterUtil.getInstance().requestQueryProductList(mShopNo, mProductType, isOnSale, mProductCategoryNo, mPage, mHandler, success, fail);
                } else {
                    String isOnSale = mType == 3 ? "0" : "1";
                    int success = ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_SUCCESS;
                    int fail = ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_FAIL;
                    HttpParameterUtil.getInstance().requestQueryProductAuditList(mShopNo, mProductType, isOnSale, mProductCategoryNo, mPage, mHandler, success, fail);

                }
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (mType == 0 || mType == 1) {
                    String isOnSale = mType == 0 ? "1" : "0";
                    int success = ConstantHandler.WHAT_QUERY_PRODUCT_LIST_MORE_SUCCESS;
                    int fail = ConstantHandler.WHAT_QUERY_PRODUCT_LIST_MORE_FAIL;
                    HttpParameterUtil.getInstance().requestQueryProductList(mShopNo, mProductType,isOnSale, mProductCategoryNo, mPage, mHandler, success, fail);
                } else {
                    String isOnSale = mType == 3 ? "0" : "1";
                    int success = ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_SUCCESS;
                    int fail = ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_FAIL;
                    HttpParameterUtil.getInstance().requestQueryProductAuditList(mShopNo, mProductType, isOnSale, mProductCategoryNo, mPage, mHandler, success, fail);

                }
            }
        });

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<ClassifyGoodsFragment> mImpl;

        public MyHandler(ClassifyGoodsFragment mImpl) {
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
            // 销售中和仓库中商品分页列表
            case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mGoodsListAdapter.datas = (LinkedList<GoodsModel>) msg.obj;
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                mGoodsListAdapter.datas.addAll((Collection<? extends GoodsModel>) msg.obj);
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            // 审核中和已驳回商品分页列表
            case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mGoodsListAdapter.datas = (LinkedList<GoodsModel>) msg.obj;
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                mGoodsListAdapter.datas.addAll((Collection<? extends GoodsModel>) msg.obj);
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                // 点击
                disposeClickData(msg);
                break;
            case ConstantHandler.WHAT_HANDLER_DELECT:
                // 删除
                if (msg.arg1 == 1) {
                    mCurrentGoods = (GoodsModel) msg.obj;
                    SimpleDialog.showLoadingHintDialog(getActivity(), 1);
                    if (mType == 1) {
                        HttpParameterUtil.getInstance().requestGoodsDel2(mShopNo, mProductCategoryNo, mCurrentGoods.getProductNo(), mHandler);
                    } else {
                        HttpParameterUtil.getInstance().requestGoodsDel(mShopNo, mProductCategoryNo, mCurrentGoods.getProductNo(), mHandler);

                    }
                }
                break;
            case ConstantHandler.WHAT_GOODS_DEL_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                mGoodsListAdapter.datas.remove(mCurrentGoods.getPosition());
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_GOODS_DEL_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_GOODS_PUTAWAY_SOLDOUT_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                mGoodsListAdapter.datas.remove(mCurrentGoods.getPosition());
                mGoodsListAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_GOODS_PUTAWAY_SOLDOUT_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    private void disposeClickData(Message msg) {
        final GoodsModel item = (GoodsModel) msg.obj;
        switch (msg.arg1) {
            case R.id.delBtn:
                delete(item);
                break;
            case R.id.editBtn:
                edit(item);
                break;
            case R.id.soldOutBtn:
                soldOut(item);
                break;
            case R.id.putawayBtn:
                put(item);
                break;
            case R.id.item_product_edit_view:
                // 弹框选择
                final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.dialog).create();
                RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_product_edit, null, false);
                dialog.setView(layout);
                dialog.show();
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setContentView(R.layout.dialog_product_edit);
                switch (mType) {
                    case 0:
                        window.findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.soldout_tv).setVisibility(View.VISIBLE);
//                        window.findViewById(R.id.edit_price_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.edit_storage_tv).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        window.findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.delete_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.putaway_tv).setVisibility(View.VISIBLE);
//                        window.findViewById(R.id.edit_price_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.edit_storage_tv).setVisibility(View.VISIBLE);
                        break;
                    case 2:
//                        window.findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.delete_tv).setVisibility(View.VISIBLE);
//                        window.findViewById(R.id.edit_price_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.edit_storage_tv).setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        window.findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.delete_tv).setVisibility(View.VISIBLE);
//                        window.findViewById(R.id.edit_price_tv).setVisibility(View.VISIBLE);
                        window.findViewById(R.id.edit_storage_tv).setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                window.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.edit_price_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePrice(item, dialog);
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.edit_storage_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStorage(item, dialog);
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.edit_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit(item);
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.soldout_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soldOut(item);
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.putaway_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        put(item);
                        dialog.dismiss();
                    }
                });
                window.findViewById(R.id.delete_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(item);
                        dialog.dismiss();
                    }
                });
                break;
            default:
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", item);
                Intent intent = new Intent(getActivity(), ClassifyCommodityDetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("mShopNo", mShopNo);
                intent.putExtra("mProductCategoryNo", mProductCategoryNo);
                intent.putExtra("sort", mType);
                startActivity(intent);
                break;
        }
    }

    /**
     * 上架商品
     *
     * @param item
     */
    private void put(final GoodsModel item) {
        SimpleDialog.showConfirmDialog(getActivity(), null, getString(R.string.confirm_putaway), null, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                mCurrentGoods = item;
                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                HttpParameterUtil.getInstance().requestGoodsPutawaySoldout(mShopNo, "1", mCurrentGoods.getProductNo(), mHandler);
            }
        });
    }

    /**
     * 下架商品
     *
     * @param item
     */
    private void soldOut(final GoodsModel item) {
        SimpleDialog.showConfirmDialog(getActivity(), null, getString(R.string.confirm_soldout), null, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                mCurrentGoods = item;
                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                HttpParameterUtil.getInstance().requestGoodsPutawaySoldout(mShopNo, "0", mCurrentGoods.getProductNo(), mHandler);
            }
        });
    }

    /**
     * 修改价格
     *
     * @param item
     * @param dialog
     */
    private void updatePrice(GoodsModel item, AlertDialog dialog) {
        Intent intent = new Intent(getActivity(), UpdateProductPriceOrStorageActivity.class);
        intent.putExtra("what", "price");
        intent.putExtra("productNo", item.getProductNo());
        intent.putExtra("price", item.getPrice());
        startActivity(intent);
        dialog.dismiss();
    }

    /**
     * 修改库存
     *
     * @param item
     * @param dialog
     */
    private void updateStorage(GoodsModel item, AlertDialog dialog) {
        Intent intent = new Intent(getActivity(), UpdateProductPriceOrStorageActivity.class);
        intent.putExtra("what", "storage");
        intent.putExtra("productNo", item.getProductNo());
        intent.putExtra("storage", item.getStorage());
        startActivity(intent);
        dialog.dismiss();
    }

    /**
     * 编辑商品
     *
     * @param item
     */
    private void edit(GoodsModel item) {
        if(!TextUtils.isEmpty(item.getProductPlatNo())) {
            ToastUtils.showToast("平台商品不能修改!");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", item);
        Intent intent = new Intent(getActivity(), EditCommodityActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("mShopNo", mShopNo);
        intent.putExtra("productType", mProductType);
        intent.putExtra("mProductCategoryNo", mProductCategoryNo);
        intent.putExtra("sort", mType);
        startActivity(intent);
    }

    /**
     * 删除商品
     *
     * @param item
     */
    private void delete(final GoodsModel item) {
        SimpleDialog.showConfirmDialog(getActivity(), null, getString(R.string.confirm_delect), null, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                mCurrentGoods = item;
                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                if (mType == 1) {
                    HttpParameterUtil.getInstance().requestGoodsDel2(mShopNo, mProductCategoryNo, mCurrentGoods.getProductNo(), mHandler);
                } else {
                    HttpParameterUtil.getInstance().requestGoodsDel(mShopNo, mProductCategoryNo, mCurrentGoods.getProductNo(), mHandler);

                }
            }
        });
    }

    @Override
    public void addGoodsSuccess() {
        if (mType == 2) {
            mRefreshLayout.autoRefresh();
        }
    }
}
