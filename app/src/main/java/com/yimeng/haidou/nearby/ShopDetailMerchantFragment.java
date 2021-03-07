package com.yimeng.haidou.nearby;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.haidou.R;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/18 0018 下午 04:43.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商店详情商家页面
 * </pre>
 */
public class ShopDetailMerchantFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<String> mList;
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_detail_child_merchant;
    }

    @Override
    protected void init() {

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(
                R.layout.adapter_shop_pic, mList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                CommonUtils.showRadiusImage(helper.getView(R.id.iv_logo), item, DeviceUtils.dp2px(mContext, 10),
                        true, true, true, true);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                String imgUrl = mList.get(position);
                String imgUrl = "";
                for (String s : mList) {
                    imgUrl += (s + ",");
                }

                ActivityUtils.getInstance().jumpPhotoActivity(imgUrl, position);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    /**
     * 设置图片
     *
     * @param images
     */
    public void setImages(List<String> images) {
        if (mList != null) {
            if (!mList.isEmpty()) {
                mList.clear();
            }
            mList.addAll(images);
            mAdapter.notifyDataSetChanged();
        }
    }

}
