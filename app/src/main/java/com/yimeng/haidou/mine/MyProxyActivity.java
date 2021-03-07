package com.yimeng.haidou.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.MineProxyAdapter;
import com.yimeng.entity.ProxyMemberBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
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
 *  Time   : 2019/4/13 6:57 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我是代理
 * </pre>
 */
public class MyProxyActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private int mPage = 1;
    private OkHttpCommon mOkHttpCommon;
    private List<ProxyMemberBean> mList;
    private BaseQuickAdapter<ProxyMemberBean, BaseViewHolder> mAdapter;
    private TextView mTvShopSum;
    private TextView mTvProxySum;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_proxy;
    }

    @Override
    protected void init() {

        mOkHttpCommon = new OkHttpCommon();

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MineProxyAdapter(mList);

        View headLayout = LayoutInflater.from(this).inflate(R.layout.head_proxy_layout, null);
        mTvShopSum = headLayout.findViewById(R.id.tv_shop_sum);
        mTvProxySum = headLayout.findViewById(R.id.tv_proxy_sum);
        mAdapter.setHeaderView(headLayout);
        recyclerView.setAdapter(mAdapter);



    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                ActivityUtils.getInstance().jumpActivity(ProxySettlementHistoryActivity.class);
            }
        });

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

    @Override
    protected void loadData() {
        getData();
    }

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        CommonUtils.addPageParams(params, mPage);

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_QUERY_SHARE_PROXY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("查询失败！");
                showSmartRefreshGetDataFail(smartRefresh, mPage);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败！"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                // 邀请的代理
                int otherData = data.get("otherData").getAsInt();
                mTvShopSum.setText("我邀请的店主（" + otherData +"）");

                // 邀请的代理
//                int total = data.get("total").getAsInt();
//                mTvProxySum.setText("我邀请的合伙人（"+total+"）");
//
//                List<ProxyMemberBean> list = GsonUtils.getGson().fromJson(data.get("rows").getAsJsonArray().toString(),
//                        new TypeToken<List<ProxyMemberBean>>(){}.getType());
//                setProxyItem(list);


            }
        });
    }

    /**
     * 设置代理数据
     * @param list
     */
    private void setProxyItem(List<ProxyMemberBean> list) {
        if(list == null || list.isEmpty()) {
            return;
        }

        if(list.size() < Constants.MAX_LIMIT) {
            smartRefresh.finishLoadMoreWithNoMoreData();
        }

        if(mPage == 1 && !mList.isEmpty()) {
            mList.clear();
        }
        mPage++;
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();

    }


}
