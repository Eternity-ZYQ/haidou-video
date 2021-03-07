/*
 * ************************************************
 * 文件：ArticleTopActivity.java
 * Author：huiGer
 * 当前修改时间：2019年04月27日 11:52:48
 * 上次修改时间：2019年04月27日 11:52:46
 *
 * Copyright (c) 2019.
 * ************************************************
 */

package com.yimeng.haidou.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.home.adapter.ArticleListAdapter;
import com.yimeng.entity.ArticleBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
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
 *  Time   : 2019/4/27 11:53 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 头条数据
 * </pre>
 */
public class ArticleTopActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private int mPage = 1;

    private OkHttpCommon mOkHttpCommon;
    private List<ArticleBean> mList;
    private ArticleListAdapter mAdapter;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_article_top;
    }

    @Override
    protected void init() {

        mOkHttpCommon = new OkHttpCommon();

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleListAdapter(mList);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                loadData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleBean articleBean = mList.get(position);
                if (articleBean != null) {
                    ActivityUtils.getInstance().jumpH5Activity("促销王头条",
                            ConstantsUrl.ARTICLE_TOP_DETAIL_URL + articleBean.getArticleNo() +"&from=app");
                }
            }
        });
    }

    @Override
    protected void loadData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("start", ((mPage - 1) * 5) + "");
        params.put("limit", "5");

        params.put("projectNo", "sharing");
        mOkHttpCommon.postLoadData(this, ConstantsUrl.ARTICLE_TOP_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast("获取数据失败！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);

                if (smartRefresh == null || mAdapter == null) {
                    return;
                }

                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败！"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                final List<ArticleBean> list = GsonUtils.getGson().fromJson(
                        data.get("rows").getAsJsonArray().toString(), new TypeToken<List<ArticleBean>>() {
                        }.getType()
                );

                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mPage++;

                if (list.isEmpty() || list.size() < 5) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }

                addData(list);


            }
        });
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    private void addData(List<ArticleBean> list) {
        if (list.isEmpty()) {
            return;
        }


        for (int i = 0; i < list.size(); i++) {
            ArticleBean articleBean = list.get(i);
            if(list.size() == 1) {
                //  只存在一个
                articleBean.setItemType(ArticleListAdapter.ARTICLE_TOP_ONLY);
            }else if (i > 0 && i == list.size() - 1) {
                // 最后一个
                articleBean.setItemType(ArticleListAdapter.ARTICLE_BOTTOM);
            }else if (i == 0 || i % 6 == 0) {
                // top
                articleBean.setItemType(ArticleListAdapter.ARTICLE_TOP);
            } else if (i % 5 == 0) {
                // bottom
                articleBean.setItemType(ArticleListAdapter.ARTICLE_BOTTOM);
            } else {
                // center
                articleBean.setItemType(ArticleListAdapter.ARTICLE_CENTER);
            }
        }

        mList.addAll(list);

        mAdapter.notifyDataSetChanged();
    }

}
