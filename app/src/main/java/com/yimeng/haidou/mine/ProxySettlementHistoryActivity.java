package com.yimeng.haidou.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.MineProxyAdapter;
import com.yimeng.entity.ProxyMemberBean;
import com.yimeng.entity.ProxyMemberScoreBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
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
 *  Time   : 2019/4/15 10:02 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代理结算历史
 * </pre>
 */
public class ProxySettlementHistoryActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @Bind(R.id.rl_score)
    RelativeLayout rlScore;
    @Bind(R.id.tv_score_title)
    TextView tvScoreTitle;
    @Bind(R.id.iv_score)
    ImageView ivScore;

    private int mPage = 1;
    private MineProxyAdapter mAdapter;
    private List<ProxyMemberBean> mList;
    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_proxy_settlement_history;
    }

    @Override
    protected void init() {

        mOkHttpCommon = new OkHttpCommon();
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MineProxyAdapter(mList);


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

    }

    @Override
    protected void loadData() {
        getData();
    }

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        CommonUtils.addPageParams(params, mPage);

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_PROXY_HORSTORY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast("查询失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询失败，请稍后重试！"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();

                if(!data.get("otherData").isJsonNull()) {
                    // 店主结算
                    JsonObject otherData = data.get("otherData").getAsJsonObject();
                    ProxyMemberScoreBean scoreBean = GsonUtils.getGson().fromJson(otherData.toString(), ProxyMemberScoreBean.class);
                    if(scoreBean != null) {
                        rlScore.setVisibility(View.VISIBLE);
                        tvScoreTitle.setText(scoreBean.getLogName());
                    }
                }


                // 代理结算
                List<ProxyMemberBean> proxyList = GsonUtils.getGson().fromJson(
                        data.get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<ProxyMemberBean>>() {
                        }.getType()
                );
                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mPage++;
                if (proxyList.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mList.addAll(proxyList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
