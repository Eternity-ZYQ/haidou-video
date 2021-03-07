package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelComment;
import com.yimeng.entity.ModelGoodsDetailGrade;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.yimeng.haidou.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品评价
 */

@SuppressLint("ValidFragment")
public class GoodsCommentFragment extends Fragment {

    @Bind(R.id.viewLine)
    View viewLine;
    @Bind(R.id.gradeTV)
    TextView gradeTV;
    //    @Bind(R.id.listview)
//    ListView listView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private String mShopNo;
    private ModelComment modelComment;

    /**
     * 用户评价
     */
//    private GoodsDetailCommentAdapter mCommentAdapter;
    private LinkedList<ModelGoodsDetailGrade> mList;
    private BaseQuickAdapter<ModelGoodsDetailGrade, BaseViewHolder> mAdapter;
    private int mPage;
    private MyHandler mHandler = new MyHandler(this);

    public GoodsCommentFragment() {
    }

    public GoodsCommentFragment(String mShopNo) {
        this.mShopNo = mShopNo;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fm_goods_comment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        mList = new LinkedList<>();
//        mCommentAdapter = new GoodsDetailCommentAdapter(getActivity(), mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK);
//        listView.setAdapter(mCommentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseQuickAdapter<ModelGoodsDetailGrade, BaseViewHolder>(
                R.layout.item_lv_goods_detail_comment, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelGoodsDetailGrade model) {
                helper.setText(R.id.nameTV, model.getName())
                        .setText(R.id.describeTV, model.getDescribe())
                        .setText(R.id.dateTV, DateUtil.getAssignDate(UnitUtil.getLong(model.getDate()), 3))
                ;
                RatingStarView ratingScore = helper.getView(R.id.rating_score);
                ratingScore.setRating(UnitUtil.getFloat(model.getGrade()));


                CommonUtils.showImage(helper.getView(R.id.avatarSDV), model.getAvatarImg());

                helper.addOnClickListener(R.id.image1SDV)
                        .addOnClickListener(R.id.image2SDV)
                        .addOnClickListener(R.id.image3SDV);
                ImageView image1SDV = helper.getView(R.id.image1SDV);
                ImageView image2SDV = helper.getView(R.id.image2SDV);
                ImageView image3SDV = helper.getView(R.id.image3SDV);
                image1SDV.setVisibility(View.GONE);
                image2SDV.setVisibility(View.GONE);
                image3SDV.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(model.getImage1())) {
                    image1SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image1SDV, model.getImage1());
                }
                if (!TextUtils.isEmpty(model.getImage2())) {
                    image2SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image2SDV, model.getImage2());
                }
                if (!TextUtils.isEmpty(model.getImage3())) {
                    image3SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(image3SDV, model.getImage3());
                }
            }
        };
        recyclerView.setAdapter(mAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestProductComment(mPage, mShopNo, mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestProductComment(mPage, mShopNo, mHandler);
            }
        });


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int index = 0;
                if (view.getId() == R.id.image1SDV) {
                    index = 0;
                } else if (view.getId() == R.id.image2SDV) {
                    index = 1;
                } else {
                    index = 2;
                }
                ModelGoodsDetailGrade model = mList.get(position);
                if (model == null) return;
                String images = model.getImage1() + "," + model.getImage2() + "," + model.getImage3();
                ActivityUtils.getInstance().jumpPhotoActivity(images, index);
            }
        });
    }

    private void disposeData(Message msg) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_SUCCESS:
                //获取商品信息
                ModelProductDetail item = (ModelProductDetail) msg.obj;
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
//                mCommentAdapter.mList = getGrades(msg);
//                mCommentAdapter.notifyDataSetChanged();
                if (!mList.isEmpty()) mList.clear();
                mList.addAll(getGrades(msg));
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
//                mCommentAdapter.mList.addAll(getGrades(msg));
//                mCommentAdapter.notifyDataSetChanged();
                mList.addAll(getGrades(msg));
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                // 点击图片
//                String image = (String) msg.obj;
//                if (!TextUtils.isEmpty(image)) {
//                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
//                    intent.putExtra("img_url", image);
//                    startActivity(intent);
//                }
                if (msg.arg1 == R.id.image1SDV || msg.arg1 == R.id.image2SDV || msg.arg1 == R.id.image3SDV) {
                    int position = 0;
                    if (msg.arg1 == R.id.image1SDV) {
                        position = 0;
                    } else if (msg.arg1 == R.id.image2SDV) {
                        position = 1;
                    } else {
                        position = 2;
                    }
                    ModelGoodsDetailGrade model = (ModelGoodsDetailGrade) msg.obj;
                    if (model == null) return;
                    String images = model.getImage1() + "," + model.getImage2() + "," + model.getImage3();
                    ActivityUtils.getInstance().jumpPhotoActivity(images, position);
                }

                break;
            default:
                break;
        }
    }

    private LinkedList<ModelGoodsDetailGrade> getGrades(Message msg) {
        modelComment = (ModelComment) msg.obj;
        String totalGrade = modelComment.getTotalGrade();
        totalGrade = TextUtils.isEmpty(totalGrade) ? "0" : totalGrade;
        LinkedList<ModelGoodsDetailGrade> grades = new LinkedList<>();
        for (int i = 0; i < modelComment.getModelRepairOrderCommentDetailList().size(); i++) {
            String id = modelComment.getModelRepairOrderCommentDetailList().get(i).getId();
            String name = modelComment.getModelRepairOrderCommentDetailList().get(i).getNickname();
            String avatarImg = modelComment.getModelRepairOrderCommentDetailList().get(i).getHeadPath();
            String grade = modelComment.getModelRepairOrderCommentDetailList().get(i).getDiscriptScore();
            String date = modelComment.getModelRepairOrderCommentDetailList().get(i).getCreateTime();
            String describe = modelComment.getModelRepairOrderCommentDetailList().get(i).getContent();

            String imgPath = modelComment.getModelRepairOrderCommentDetailList().get(i).getImgPath();
            String[] imageList = imgPath.split(",");
            String image1 = "";
            String image2 = "";
            String image3 = "";
            switch (imageList.length) {
                case 0:
                    image1 = "";
                    image2 = "";
                    image3 = "";
                    break;
                case 1:
                    image1 = imageList[0];
                    image2 = "";
                    image3 = "";
                    break;
                case 2:
                    image1 = imageList[0];
                    image2 = imageList[1];
                    image3 = "";
                    break;
                case 3:
                    image1 = imageList[0];
                    image2 = imageList[1];
                    image3 = imageList[2];
                    break;
                default:
                    break;
            }
            grades.add(new ModelGoodsDetailGrade(id, name, avatarImg, grade, date, describe, image1, image2, image3));
        }

        return grades;
    }

    public void autoRefresh(String mShopNo) {
        this.mShopNo = mShopNo;
//        mRefreshLayout.autoRefresh();
        mPage = 1;
        HttpParameterUtil.getInstance().requestProductComment(mPage, mShopNo, mHandler);
    }

    private static class MyHandler extends Handler {

        private WeakReference<GoodsCommentFragment> mImpl;

        public MyHandler(GoodsCommentFragment mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }
}
