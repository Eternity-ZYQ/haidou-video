package com.yimeng.haidou.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yimeng.adapter.MsgAdapter;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.MsgBean;
import com.yimeng.haidou.R;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * Author : huiGer
 * Time   : 2018/8/5 0005 下午 12:40.
 * Desc   : 消息中心
 */
public class MsgActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    @Bind(R.id.smRefresh)
    SmartRefreshLayout smartRefreshLayout;
    private List<MsgBean> mList;
    private MsgAdapter msgAdapter;
    private boolean isRefresh = true;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void init() {
        initRecyclerView();


        getData(0);
    }

    @Override
    protected void initListener() {

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                getData(mList.size());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                getData(0);
            }
        });
    }


    @Override
    protected void loadData() {

    }

    private void initRecyclerView() {
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MsgAdapter(this, mList);
        recyclerView.setAdapter(msgAdapter);
    }


    public void getData(final int start) {
        String token = CommonUtils.getToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("limit", "10");
        params.put("start", start + "");

        new OkHttpCommon().postLoadData(this, ConstantsUrl.APP_MSG, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                try {
                    if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                        Toast.makeText(MsgActivity.this, "暂无消息!", Toast.LENGTH_SHORT).show();
                        if(isRefresh) smartRefreshLayout.finishRefresh();
                        else smartRefreshLayout.finishLoadMore();
                        return;
                    }

                    if (start == 0 && mList != null) mList.clear();

                    JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                    JsonArray rows = dataJson.getAsJsonArray("rows");
                    Gson gson = new Gson();
                    for (int i = 0; i < rows.size(); i++) {
                        mList.add(gson.fromJson(rows.get(i).toString(), MsgBean.class));
                    }
                    msgAdapter.notifyDataSetChanged();

                    if (isRefresh) {
                        smartRefreshLayout.finishRefresh(true);
                    } else {
                        if (rows.size() == 0)
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        else
                            smartRefreshLayout.finishLoadMore(true);
                    }

                    if (mList.isEmpty())
                        setEmptyLayout(true);
                    else
                        setEmptyLayout(false);

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    if (isRefresh)
                        smartRefreshLayout.finishRefresh(false);
                    else
                        smartRefreshLayout.finishLoadMore(false);
                    setEmptyLayout(true);
                }
            }
        });

    }

    /**
     * 布局切换
     *
     * @param isEmpty
     */
    private void setEmptyLayout(boolean isEmpty) {
        if (isEmpty) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
