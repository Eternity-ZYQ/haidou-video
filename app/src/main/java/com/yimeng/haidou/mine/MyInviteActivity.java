package com.yimeng.haidou.mine;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelCompanion;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/8 0008 下午 03:04.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的人脉圈
 * </pre>
 */
public class MyInviteActivity extends BaseActivity {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRLayout)
    SmartRefreshLayout smartRLayout;

    ConstraintLayout layoutBigBaby;
    CircleImageView ivUserHead;
    TextView tvUserType;
    TextView tvUserName;
    TextView tvUserMobile;
    TextView tvTime;
    TextView tvUserGrade;
    TextView tvShopName;

    private int mPage = 1;
    private BaseQuickAdapter<ModelCompanion, BaseViewHolder> mAdapter;
    private List<ModelCompanion> mList;
    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_my_invite;
    }

    @Override
    protected void init() {
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<ModelCompanion, BaseViewHolder>(
                R.layout.adapter_my_invite_item, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelCompanion item) {
                CommonUtils.showImage(helper.getView(R.id.iv_user_head), item.getHeadPath());
                helper.setText(R.id.tv_user_name, item.getNickname())
                        .setText(R.id.tv_user_mobile, item.getMobileNo())
                        .setTextColor(R.id.tv_user_mobile, ContextCompat.getColor(mContext, R.color.c_link_color))
                        .setText(R.id.tv_time, DateUtil.getAssignDate(item.getCreateTime(), 3))
                        .setText(R.id.tv_user_grade, item.getTuijianMemberName().equals("common") ? "可销售" : "可消费")
                        .setText(R.id.tv_shop_name, TextUtils.isEmpty(item.getShopNames()) ? "未开店" : item.getShopNames())
                        .setGone(R.id.iv, true)
                        .addOnClickListener(R.id.tv_user_mobile)

                ;
            }
        };
        recyclerView.setNestedScrollingEnabled(false);

        View headLayout = LayoutInflater.from(this).inflate(R.layout.head_invite_layout, null);
        layoutBigBaby = headLayout.findViewById(R.id.layout_big_baby);
        ivUserHead = headLayout.findViewById(R.id.iv_user_head);
        tvUserType = headLayout.findViewById(R.id.tv_user_type);
        tvUserName = headLayout.findViewById(R.id.tv_user_name);
        tvUserMobile = headLayout.findViewById(R.id.tv_user_mobile);
        tvTime = headLayout.findViewById(R.id.tv_time);
        tvUserGrade = headLayout.findViewById(R.id.tv_user_grade);
        tvShopName = headLayout.findViewById(R.id.tv_shop_name);
        mAdapter.setHeaderView(headLayout);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

        layoutBigBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopNo = (String) view.getTag();
                if (!TextUtils.isEmpty(shopNo)) {
                    ActivityUtils.getInstance().jumpShopDetailActivity(shopNo, false);
                }
            }
        });

        smartRLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                loadData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModelCompanion modelCompanion = mList.get(position);
//                String shopNo = modelCompanion.getTelephone();
//                if (!TextUtils.isEmpty(shopNo)) {
//                    ActivityUtils.getInstance().jumpShopDetailActivity(shopNo, false);
//                }

                Bundle bundle = new Bundle();
                bundle.putString("nickname", modelCompanion.getNickname());
                bundle.putString("memberNo", modelCompanion.getMemberNo());
                ActivityUtils.getInstance().jumpActivity(InviteShareActivity.class, bundle);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ModelCompanion modelCompanion = mList.get(position);
                if (view.getId() == R.id.tv_user_mobile) {
                    // 拨打电话
                    ActivityUtils.getInstance().diallPhone(MyInviteActivity.this, modelCompanion.getMobileNo());
                }
            }
        });

    }


    @Override
    protected void loadData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        CommonUtils.addPageParams(params, mPage);
        if (mOkHttpCommon == null) {
            mOkHttpCommon = new OkHttpCommon();
        }
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_PARTNER_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mPage == 1) {
                    smartRLayout.finishRefresh(false);
                } else {
                    smartRLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (mPage == 1) {
                    smartRLayout.finishRefresh();
                } else {
                    smartRLayout.finishLoadMore();
                }
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                    return;
                }

                JsonArray data = jsonObject.get("data").getAsJsonArray();
                setBigBaby(GsonUtils.getGson().fromJson(data.get(0).getAsJsonObject().toString(), ModelCompanion.class));

                JsonArray array = data.get(1).getAsJsonObject().get("rows").getAsJsonArray();
                List<ModelCompanion> list = GsonUtils.getGson().fromJson(array.toString(), new TypeToken<List<ModelCompanion>>() {
                }.getType());
                if (mPage == 1 && !mList.isEmpty()) {
                    mList.clear();
                }
                mList.addAll(list);
                mPage++;
                if (list.size() < Constants.MAX_LIMIT) {
                    smartRLayout.finishLoadMoreWithNoMoreData();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 设置大宝数据
     *
     * @param bean
     */
    private void setBigBaby(ModelCompanion bean) {
        if (TextUtils.isEmpty(bean.getMobileNo())) {
            // 没有大宝数据
            layoutBigBaby.setVisibility(View.GONE);
        } else {
            layoutBigBaby.setVisibility(View.VISIBLE);
            CommonUtils.showImage(ivUserHead, bean.getHeadPath());
            tvUserName.setText(bean.getNickname());
            tvUserMobile.setText(bean.getMobileNo());
//            if(bean.getCertType().equals("agent")) {
//                // 合伙人
//                tvUserType.setVisibility(View.VISIBLE);
//                tvUserType.setText("合伙人");
//            }
            tvTime.setText(DateUtil.getAssignDate(bean.getCreateTime(), 3));
            tvUserGrade.setText(bean.getTuijianMemberName().equals("common") ? "可销售" : "可消费");
            tvShopName.setText(TextUtils.isEmpty(bean.getShopNames()) ? "未开店" : bean.getShopNames());
            layoutBigBaby.setTag(bean.getTelephone());
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackClick() {
        finish();
    }


}
