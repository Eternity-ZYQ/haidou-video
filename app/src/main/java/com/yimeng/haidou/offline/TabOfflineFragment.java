package com.yimeng.haidou.offline;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.offline.adapter.ShopItemAdapter;
import com.yimeng.entity.OfflineBannerBean;
import com.yimeng.entity.OfflineShopBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
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
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/27 0027 上午 11:17.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 线下店铺
 * </pre>
 */
public class TabOfflineFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private OkHttpCommon mOkHttpCommon;
    private BaseQuickAdapter<OfflineShopBean, BaseViewHolder> mAdapter;
    private int mPage = 1;
    private List<OfflineShopBean> mList;
    private BGABanner mBannerLayout;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_offline;
    }

    @Override
    protected void init() {


        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new BaseQuickAdapter<OfflineShopBean, BaseViewHolder>(
//                R.layout.adapter_offline_item, mList
//        ) {
//            @Override
//            protected void convert(BaseViewHolder helper, OfflineShopBean item) {
//                helper.setText(R.id.tv_start, "店铺评分: " + item.getShopScore())
//                        .setText(R.id.tv_shop_name, item.getShopName())
//                        .setText(R.id.tv_shop_address, "地址:" + item.getCity() + item.getArea() + item.getAddress())
//                        .setText(R.id.tv_shop_distance, CommonUtils.getDistance(item.getDistance() + ""))
//                ;
//                CommonUtils.showRadiusImage((ImageView) helper.getView(R.id.iv_logo), item.getLogoPath(),
//                        DeviceUtils.dp2px(mContext, 5), true, true, true, true);
//
//            }
//        };
        mAdapter = new ShopItemAdapter(mList);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_offline_layout, null);
        mBannerLayout = headView.findViewById(R.id.bannerLayout);

        mBannerLayout.setAdapter(new BGABanner.Adapter<ImageView, OfflineBannerBean>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable OfflineBannerBean model, int position) {
                if (model != null) {
                    CommonUtils.showImage(itemView, model.getLogo());
                    itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        mAdapter.setHeaderView(headView);
        recyclerView.setAdapter(mAdapter);

        mOkHttpCommon = new OkHttpCommon();
        setData((String) SharedPreferencesUtils.get(Constants.OFFLINE_DATA_CACHE, ""));
    }

    @Override
    protected void initListener() {
        smartRefresh.autoRefresh();
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
    }

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
//        params.put("token", CommonUtils.getToken());
        CommonUtils.addPageParams(params, mPage);
        params.put("Longitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, "114.039605"));
        params.put("Latitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, "22.66923"));
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_OFFLINE_SHOP, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast(getResources().getString(R.string.text_network_connected_error));
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败"));
                    return;
                }

                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mPage++;
                String jsonStr = jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString();
                SharedPreferencesUtils.put(Constants.OFFLINE_DATA_CACHE, jsonStr);
                setData(jsonStr);
            }
        });

        HashMap<String, String> params1 = CommonUtils.createParams();
        params1.put("menuType", "bannerType");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_OFFLINE_BANNER, params1, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(getResources().getString(R.string.text_network_connected_error));
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取Banner失败!"));
                    return;
                }

                List<OfflineBannerBean> list = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonArray().toString(),
                        new TypeToken<List<OfflineBannerBean>>() {
                        }.getType());
                mBannerLayout.setData(list, null);

            }
        });
    }

    @Override
    protected void loadData() {

    }

    /**
     * 设置数据
     *
     * @param jsonStr
     */
    private void setData(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;
        List<OfflineShopBean> list = GsonUtils.getGson().fromJson(jsonStr,
                new TypeToken<List<OfflineShopBean>>() {
                }.getType());
        if (list.size() < Constants.MAX_LIMIT) {
            smartRefresh.finishLoadMoreWithNoMoreData();
        }
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

}
