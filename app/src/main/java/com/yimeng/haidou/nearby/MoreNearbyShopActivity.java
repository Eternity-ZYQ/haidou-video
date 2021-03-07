package com.yimeng.haidou.nearby;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.offline.adapter.ShopItemAdapter;
import com.yimeng.entity.OfflineShopBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.SharedPreferencesUtils;
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
 *  Time   : 2019-3-20 17:36:38
 *  Email  : zhihuiail@163.com
 *  Desc   : 更多附近店铺
 */
public class MoreNearbyShopActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private int mPage = 1;
    private OkHttpCommon mOkHttpCommon;
    private List<OfflineShopBean> mList;
    private BaseQuickAdapter<OfflineShopBean, BaseViewHolder> mAdapter;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_more_nearby_shop;
    }

    @Override
    protected void init() {


        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShopItemAdapter(mList);
        recyclerView.setAdapter(mAdapter);
        mOkHttpCommon = new OkHttpCommon();

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
                mPage =  1;
                getData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.getInstance().jumpShopDetailActivity(mList.get(position).getShopNo(), false);
            }
        });
    }

    private void getData() {

        HashMap<String, String> params = CommonUtils.createParams();
//        params.put("token", CommonUtils.getToken());
        CommonUtils.addPageParams(params, mPage);
        params.put("Longitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, "114.039605"));
        params.put("Latitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, "22.66923"));
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_OFFLINE_SHOP, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast(getResources().getString(R.string.text_network_connected_error));
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(smartRefresh == null) return;
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败"));
                    return;
                }

                List<OfflineShopBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<OfflineShopBean>>(){}.getType()
                );
                if(list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }

                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mPage++;
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadData() {
        getData();
    }

}
