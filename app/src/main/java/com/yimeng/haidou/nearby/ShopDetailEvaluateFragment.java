package com.yimeng.haidou.nearby;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.haidou.nearby.adapter.DiscussAdapter;
import com.yimeng.entity.ModelGrade;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/18 0018 下午 04:43.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商店详情评价页面
 * </pre>
 */
public class ShopDetailEvaluateFragment extends BaseFragment {


    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private String mShopNo;
    private int mPage = 1;
    private MyHandler mHandler = new MyHandler(this);
    private DiscussAdapter mDiscussAdapter;
    private List<ModelGrade> mList;

    public static ShopDetailEvaluateFragment getNewInstance(String shopNo) {
        ShopDetailEvaluateFragment fragment = new ShopDetailEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shopNo", shopNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_detail_child_evaluate;
    }

    @Override
    protected void init() {
        mShopNo = getArguments().getString("shopNo");
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDiscussAdapter = new DiscussAdapter(mList);
        mDiscussAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mDiscussAdapter);

        mPage=1;
        HttpParameterUtil.getInstance().requestShopComment(mShopNo, mPage, mHandler);
    }

    /**
     * 商家评分
     * @param score 分数
     */
    public void setScore(String score){
        if(tvScore != null) {
            tvScore.setText(score + "分");
        }
    }

    @Override
    protected void initListener() {
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestShopComment(mShopNo, mPage, mHandler);
            }


        });

        mDiscussAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ModelGrade grade = mList.get(position);
                // 点击图片
                String image = "";
//                if(view.getId() == R.id.iv_1) {
//                    image = FrescoUtil.getNetUrl(grade.getImage1());
//                }else if(view.getId() == R.id.iv_2) {
//                    image = FrescoUtil.getNetUrl(grade.getImage2());
//                }else if(view.getId() == R.id.iv_3) {
//                    image = FrescoUtil.getNetUrl(grade.getImage3());
//                }
                image = grade.getImage1() +","+grade.getImage2()+","+grade.getImage3();


                if (!TextUtils.isEmpty(image)) {
                    ActivityUtils.getInstance().jumpPhotoActivity(image, position);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_SHOP_COMMENT_SUCCESS:
                // 评论列表
                mPage = 2;
//                mList = (LinkedList<ModelGrade>) msg.obj;
//                mDiscussAdapter.notifyDataSetChanged();
                LinkedList<ModelGrade> list =  (LinkedList<ModelGrade>) msg.obj;
                if(list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mList.addAll(list);
                mDiscussAdapter.setNewData(mList);
                break;
            case ConstantHandler.WHAT_SHOP_COMMENT_FAIL:
                smartRefresh.finishRefresh();
                break;
            case ConstantHandler.WHAT_SHOP_COMMENT_MORE_SUCCESS:
                smartRefresh.finishLoadMore();
                mPage++;
                list =  (LinkedList<ModelGrade>) msg.obj;
                if(list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mList.addAll(list);
                mDiscussAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_SHOP_COMMENT_MORE_FAIL:
                smartRefresh.finishLoadMore();
                break;
            default:

        }

    }

    private class MyHandler extends Handler {
        private WeakReference<ShopDetailEvaluateFragment> mIml;

        public MyHandler(ShopDetailEvaluateFragment impl) {
            mIml = new WeakReference<>(impl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mIml.get() != null) {
                mIml.get().disposeData(msg);
            }
        }
    }

}
