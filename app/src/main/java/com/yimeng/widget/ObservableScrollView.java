package com.yimeng.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/25 0025 下午 03:52.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ObservableScrollView extends ScrollView {
    private ScrollViewListener mScrollViewListener;

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setOnScrollChangeListener(ScrollViewListener scrollViewListener){
        this.mScrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mScrollViewListener != null) {
            mScrollViewListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    public interface ScrollViewListener {
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
