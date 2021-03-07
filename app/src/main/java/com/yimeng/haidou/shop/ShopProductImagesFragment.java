package com.yimeng.haidou.shop;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.yimeng.base.BaseFragment;
import com.yimeng.haidou.R;
import com.yimeng.entity.BannerItem;
import com.yimeng.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/31 0031 下午 08:21.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商品详情图片列表
 * </pre>
 */
public class ShopProductImagesFragment extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<BannerItem> mList;
    private BaseQuickAdapter<BannerItem, BaseViewHolder> mAdapter;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_shop_product_images;
    }

    @Override
    protected void init() {

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BaseQuickAdapter<BannerItem, BaseViewHolder>(
                R.layout.adapter_shop_product_image, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, BannerItem item) {
//                ImageView iv = helper.getView(R.id.sdv_image);
//                CommonUtils.showImage(iv, item.getImgUrl());

                final SubsamplingScaleImageView ssiImage = helper.getView(R.id.ssi_image);
//                CommonUtils.showImage(iv, item.getImgUrl());
                Glide.with(mContext)
                        .download(CommonUtils.parseImageUrl(item.getImgUrl()))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                ssiImage.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                ssiImage.setMaxScale(10f);
                            }
                        });
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    /**
     * 设置图片
     * @param list
     */
    public void setImages(List<BannerItem> list){
        if(mList != null && list != null) {
            if(!mList.isEmpty()) {
                mList.clear();
            }
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

}
