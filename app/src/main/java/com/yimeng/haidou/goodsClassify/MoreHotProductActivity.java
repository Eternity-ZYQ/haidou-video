package com.yimeng.haidou.goodsClassify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.ModelProductByMenuNo;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.yimeng.haidou.R;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018-12-19 16:01:34
 *  Email  : zhihuiemail@163.com
 *  Desc   : 更多热卖产品
 */
public class MoreHotProductActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private String mMenuNo;
    private OkHttpCommon mOkHttpCommon;
    private BaseQuickAdapter<ModelProductByMenuNo, BaseViewHolder> mAdapter;
    private List<ModelProductByMenuNo> mList;
    private int mPage = 1;
    private String mVip;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_more_hot_product;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }

        mMenuNo = bundle.getString("menuNo");
        String title = bundle.getString("title");
        mVip = bundle.getString("mVip");
        toolBar.setTitle(title);

        mOkHttpCommon = new OkHttpCommon();
        smartRefresh.autoRefresh();
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new BaseQuickAdapter<ModelProductByMenuNo, BaseViewHolder>(
                R.layout.adapter_home_product_item, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelProductByMenuNo item) {
                CommonUtils.showRadiusImage(helper.getView(R.id.iv_product_logo),
                        item.getImagesPath(), SizeUtils.dp2px(10f), true,
                        true, false, false);
                helper.setText(R.id.tv_product_name, item.getProductName())
                        .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                        .setText(R.id.tv_product_pay, mContext.getResources().getString(R.string.personPay, UnitUtil.getInt(item.getHasSaled())))
                        .setText(R.id.tv_product_gift, UnitUtil.getJFMoneyStr(mContext, item.getPrice()));
            }
        };
        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModelProductByMenuNo modelProductByMenuNo = mList.get(position);
                if(null != mVip && mVip.equals("vip")){
                    ActivityUtils.getInstance().jumpShopVipActivity(1, modelProductByMenuNo.getShopNo(),
                            modelProductByMenuNo.getProductNo(), 0,"");
                }else{
                    ActivityUtils.getInstance().jumpShopProductActivity(1, modelProductByMenuNo.getShopNo(),
                            modelProductByMenuNo.getProductNo(), 0,"");
                }
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        CommonUtils.addPageParams(params, mPage);
        params.put("token", CommonUtils.getToken());
        params.put("menuNo", mMenuNo);
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_HOT_SALES_PRODUCT, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败!"));
                    return;
                }
                if(mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mPage++;
                List<ModelProductByMenuNo> list = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(), new TypeToken<List<ModelProductByMenuNo>>() {
                }.getType());
                if (list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
