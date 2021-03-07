package com.yimeng.haidou.mine;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.ProxyPersonAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.AgencyBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/30 0030 下午 08:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的代理
 * </pre>
 */
public class MyAgencyActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private List<AgencyBean> mList;
    private OkHttpCommon mOkHttpCommon;
    private int mPage = 1;
    private ProxyPersonAdapter mAdapter;
    private List<String> extractsList;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_agency;
    }

    @Override
    protected void init() {

        extractsList = Arrays.asList("40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50");
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProxyPersonAdapter(mList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        mOkHttpCommon = new OkHttpCommon();
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
                // 添加代理
                ActivityUtils.getInstance().jumpActivity(RecomendJoinActivity.class);
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

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final AgencyBean agencyBean = mList.get(position);
                if (view.getId() == R.id.tv_del) {
                    // 删除代理
                    SimpleDialog.showConfirmDialog(MyAgencyActivity.this, null, getString(R.string.proxy_del_person, agencyBean.getMemberName()), null, new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            deleteProxy(agencyBean.getMemberNo());
                        }
                    });
                } else if (view.getId() == R.id.tv_update) {
                    // 修改分润
                    showPickerView(new OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            updateMemberExtracts(agencyBean.getMemberNo(), extractsList.get(options1));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void getData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        CommonUtils.addPageParams(params, mPage);
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_MY_PROXY_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
                showSmartRefreshGetDataFail(smartRefresh, mPage);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                showSmartRefreshGetDataFail(smartRefresh, mPage);
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "加载失败!"));
                    return;
                }

                List<AgencyBean> list = GsonUtils.getGson().fromJson(jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(),
                        new TypeToken<List<AgencyBean>>() {
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

    /**
     * 删除代理
     *
     * @param memberNo
     */
    private void deleteProxy(String memberNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("memberNo", memberNo);

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_DEL_PROXY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "删除失败!"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "删除成功!"));
                smartRefresh.autoRefresh();
            }
        });

    }

    private void showPickerView(OnOptionsSelectListener listener) {
        OptionsPickerView<String> mPickerView = new OptionsPickerBuilder(this, listener)
                .setTitleText("请选择分润百分比")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();

        mPickerView.setPicker(extractsList);
        mPickerView.show();
    }

    /**
     * 修改分润
     *
     * @param memberNo
     */
    private void updateMemberExtracts(String memberNo, String extracts) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("memberNo", memberNo);
        params.put("extracts", extracts);

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_UPDATE_PROXY, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改失败!"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "修改成功!"));
                smartRefresh.autoRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smartRefresh.autoRefresh();
    }
}
