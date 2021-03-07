package com.yimeng.haidou.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.entity.CashLogsBean;
import com.yimeng.enums.ScoreLogType;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
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
 *  Time   : 2018-12-20 17:08:24
 *  Email  : zhihuiemail@163.com
 *  Desc   : 流水
 */
public class ScoreLogsActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    /**
     * {@link com.yimeng.enums.ScoreLogType}
     */
    private @ScoreLogType int mType = 1;
    private OkHttpCommon mOkHttpCommon;
    private List<CashLogsBean> mList;
    private BaseQuickAdapter<CashLogsBean, BaseViewHolder> mAdapter;
    private int mPage = 1;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_cash_logs;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getInt("type", 1);
        }
        toolBar.setRightIcon(R.mipmap.icon_score_tip);
        if(mType == ScoreLogType.active) {
            toolBar.setTitle("活跃度记录");
        }else if(mType == ScoreLogType.score) {
            toolBar.setTitle("积分记录");
        }

        mOkHttpCommon = new OkHttpCommon();
        smartRefresh.autoRefresh();

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<CashLogsBean, BaseViewHolder>(
                R.layout.adapter_cash_logs_item_layout, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, CashLogsBean item) {
//                int color = item.getScoreInOut().contains("out") ? Color.RED : mContext.getResources().getColor(R.color.c_333333);
                String unitStr = item.getScoreInOut().contains("out") ? "-" : "+";
                String log = item.getScore();
                helper.setText(R.id.titleTV, item.getLogName())
                        .setText(R.id.describeTV, DateUtil.getAssignDate((item.getCreateTime()), 3))
                        .setText(R.id.otherTV, unitStr + log)
//                        .setTextColor(R.id.otherTV, color)
                ;
            }
        };
        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                if(mType == ScoreLogType.active) {
                    ActivityUtils.getInstance().jumpH5Activity("活跃度规则", ConstantsUrl.URL_PROTOCOL + "活跃度规则");
                }else if(mType == ScoreLogType.score) {
                    ActivityUtils.getInstance().jumpH5Activity("积分规则", ConstantsUrl.URL_PROTOCOL + "积分规则");
                }
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

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        CommonUtils.addPageParams(params, mPage);
        if(mType ==  ScoreLogType.active) {
            params.put("category", "active");
        }else if(mType ==  ScoreLogType.score) {
            params.put("category", "wealth");
        }
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_CASH_LOG, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                    return;
                }
                List<CashLogsBean> list = GsonUtils.getGson().fromJson(jsonObject.get("data")
                        .getAsJsonObject().get("rows").getAsJsonArray().toString(), new TypeToken<List<CashLogsBean>>() {
                }.getType());


                if (list.size() < Constants.MAX_LIMIT) {
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

    }



}
