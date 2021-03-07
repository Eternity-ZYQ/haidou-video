package com.yimeng.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yimeng.interfaces.OnViewPagerListener;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-06 20:37.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ViewPagerLayoutManager extends LinearLayoutManager {
    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;
    private RecyclerView mRecyclerView;
    private int mDeltaY;

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        public void onChildViewAttachedToWindow(View view) {
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onInitComplete();
            }
        }

        public void onChildViewDetachedFromWindow(View view) {
            if (mDeltaY >= 0) {
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageRelease(true, getPosition(view));
                }
            } else if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageRelease(false, getPosition(view));
            }
        }
    };

    public ViewPagerLayoutManager(Context context) {
        this(context, LinearLayoutManager.VERTICAL);
    }

    public ViewPagerLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View curView = mPagerSnapHelper.findSnapView(this);
            int curPos = getPosition(curView);
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onPageSelected(curPos, curPos == getItemCount() - 1);
            }
        }
    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDeltaY = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    public void setOnViewPagerListener(OnViewPagerListener listener) {
        mOnViewPagerListener = listener;
    }
}


