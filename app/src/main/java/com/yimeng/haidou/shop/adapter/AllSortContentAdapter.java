package com.yimeng.haidou.shop.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.entity.ModelProductCategoriesContent;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.DeviceUtils;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018/6/13.
 */

public class AllSortContentAdapter extends BaseAdapter {

    public LinkedList<ModelProductCategoriesContent> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public AllSortContentAdapter(LinkedList<ModelProductCategoriesContent> mList, Context mContext, Handler mHandler, int mWhat) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        int type = getItemViewType(i);
        ModelProductCategoriesContent model = (ModelProductCategoriesContent) getItem(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_sort_content, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(model, holder);
        return view;
    }

    private void setData(ModelProductCategoriesContent model, ViewHolder holder) {
        CommonUtils.showImage(holder.mImage, model.getLogo());
        holder.mTitle.setText(model.getName());

        int w = ((DeviceUtils.getWindowWidth(mContext) - DeviceUtils.dp2px(mContext, 140))) / 3;
        int h = (int) (w / 1.03947);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
        holder.mImage.setLayoutParams(layoutParams);
        holder.mTitle.setGravity(Gravity.CENTER | Gravity.BOTTOM);

        holder.clickView.setTag(model);
        holder.mRL.setTag(model);

    }

    class ViewHolder {
        @Bind(R.id.sdv_image)
        ImageView mImage;
        @Bind(R.id.tv_sort_content_title)
        TextView mTitle;
        @Bind(R.id.click_rl)
        RelativeLayout mRL;
        @Bind(R.id.clickView)
        View clickView;

        @OnClick({R.id.click_rl, R.id.clickView})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
