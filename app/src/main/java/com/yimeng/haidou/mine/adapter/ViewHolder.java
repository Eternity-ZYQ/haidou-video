package com.yimeng.haidou.mine.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gsfh on 2017.06.13.
 */


public class ViewHolder {

    private int position;
    private View mConvertView;
    private SparseArray<View> viewArray;

    public View getConvertView() {
        return mConvertView;
    }

    private void setmConvertView(View mConvertView) {
        this.mConvertView = mConvertView;
    }

    private int getPosition() {
        return position;
    }

    private void setPosition(int position) {
        this.position = position;
    }

    private ViewHolder(Context context, int position, int layoutId, ViewGroup parent) {
        setPosition(position);
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);//将ViewHolder对象设置到convertView的Tag里，方便获取
        viewArray = new SparseArray<>();
        viewArray.put(layoutId, mConvertView);//将item布局自身加入子控件数组里，可在converted()方法里对item进行设置
    }

    public static ViewHolder getViewHolder(Context context, int position, int layoutId, View convertView, ViewGroup parent) {
        if (convertView == null) {
            return new ViewHolder(context, position, layoutId, parent);
        } else {
//            viewHolder.position = position;
            return (ViewHolder) convertView.getTag();
        }
    }

    /**
     * 获取View的方法，供convert()里绑定数据时使用
     *
     * @param viewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {

        View view = viewArray.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            viewArray.put(viewId, view);
        }
        return (T) view;
    }
}