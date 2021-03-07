package com.yimeng.haidou.shop.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yimeng.entity.ModelProductParams;
import com.yimeng.haidou.R;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2018/6/13.
 */

public class CheckParameterAdapter extends BaseAdapter {

    public LinkedList<ModelProductParams> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public CheckParameterAdapter(LinkedList<ModelProductParams> mList, Context mContext, Handler mHandler, int mWhat) {
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
        ModelProductParams model = (ModelProductParams) getItem(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gv_check_parameter, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(model, holder);
        return view;
    }

    private void setData(ModelProductParams model, ViewHolder holder) {
        if (model != null) {
            holder.mTitle.setText(model.getKey());
            holder.mName.setText(model.getValue());
        }
    }


    class ViewHolder {
        @Bind(R.id.tv_parameter_name)
        TextView mTitle;
        @Bind(R.id.tv_parameter_content)
        TextView mName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
