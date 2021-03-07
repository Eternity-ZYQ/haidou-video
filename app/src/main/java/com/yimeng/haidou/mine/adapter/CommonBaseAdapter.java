package com.yimeng.haidou.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by GSFH on 2016-6-21.
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {

    protected Context context;
    private List<T> list;
    private int layoutId;

    public CommonBaseAdapter(Context context, int layoutId, List<T> list) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(context, position, layoutId, convertView, parent);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    /**
     * 子类需要实现的抽象方法，进行数据与控件的绑定
     * @param holder
     * @param bean
     */
    public abstract void convert(ViewHolder holder, T bean, int position);

}
