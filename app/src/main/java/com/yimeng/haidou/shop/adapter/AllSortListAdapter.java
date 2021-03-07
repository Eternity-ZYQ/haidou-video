package com.yimeng.haidou.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.entity.ModelProductCategories;
import com.yimeng.haidou.R;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018/6/13.
 */

public class AllSortListAdapter extends BaseAdapter {

    public LinkedList<ModelProductCategories> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public AllSortListAdapter(LinkedList<ModelProductCategories> mList, Context mContext, Handler mHandler, int mWhat) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
    }

    Typeface Boldtf = Typeface.create("宋体", Typeface.BOLD);
    Typeface Normaltf = Typeface.create("宋体", Typeface.NORMAL);

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
        ModelProductCategories model = (ModelProductCategories) getItem(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_sort_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(model, holder,i);
        return view;
    }

    private void setData(ModelProductCategories model, ViewHolder holder, int i ) {
        holder.mTitle.setText(model.getName());
        model.setPosition(i);
        for (int j = 0; j < mList.size(); j++) {
            if (mList.get(i).isSelected()){
                holder.mLine.setVisibility(View.VISIBLE);
                holder.mTitle.setTypeface(Boldtf);
                holder.mTitle.setTextSize(16);
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                holder.mRL.setBackgroundColor(Color.WHITE);
            } else {
                holder.mLine.setVisibility(View.INVISIBLE);
                holder.mTitle.setTypeface(Normaltf);
                holder.mTitle.setTextSize(15);
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.c_333333));
                holder.mRL.setBackgroundColor(mContext.getResources().getColor(R.color.c_f5f5f5));
            }
        }
        holder.mRL.setTag(model);

    }


    class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTitle;
        @Bind(R.id.line)
        View mLine;
        @Bind(R.id.rl_click)
        RelativeLayout mRL;

        @OnClick({R.id.rl_click})
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
