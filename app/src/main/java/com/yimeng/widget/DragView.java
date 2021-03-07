package com.yimeng.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/27 10:32 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 可拖动的悬浮按钮
 * </pre>
 */
public class DragView implements View.OnTouchListener {

    private int mStatusBarHeight, mScreenWidth, mScreenHeight;
    // 手指按下位置
    private int mStartX, mStartY, mLastX, mLastY;
    private boolean mTouchResult = false;

    private DragView.Builder mBuilder;

    private DragView(DragView.Builder builder) {
        this.mBuilder = builder;
        initDragView();
    }

    public View getDragView() {
        return mBuilder.view;
    }

    public Activity getActivity() {
        return mBuilder.activity;
    }

    public boolean getNeedNearEdge() {
        return mBuilder.needNearEdge;
    }

    public void setNeedNearEdge(boolean needNearEdge) {
        mBuilder.needNearEdge = needNearEdge;
        if (needNearEdge) {
            moreNearEdge();//贴边
        }
    }

    /**
     * 自动贴边
     */
    private void moreNearEdge() {
        int left = getDragView().getLeft();
        int lastX;
        if(left + getDragView().getWidth() / 2 <= mScreenWidth / 2) {
            lastX = 0;
        } else {
          lastX = mScreenWidth - getDragView().getWidth();
        }

        final ValueAnimator animator = ValueAnimator.ofInt(left, lastX);
        animator.setDuration(1000);
        animator.setRepeatCount(0);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int left = (int) animator.getAnimatedValue();
                getDragView().setLayoutParams(createLayoutParams(left, getDragView().getTop(), 0, 0));
            }
        });
        animator.start();
    }

    private void initDragView() {
        if (null == getActivity()) {
            throw new NullPointerException("the activity is null");
        }
        if (null == mBuilder.view) {
            throw new NullPointerException("the dragView is null");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mBuilder.activity.isDestroyed()) {
            return;
        }

        // 屏幕宽高
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        if (null != windowManager) {
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            mScreenWidth = displayMetrics.widthPixels;
            mScreenHeight = displayMetrics.heightPixels;
        }

        // 状态栏高度
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        mStatusBarHeight = rect.top;
        if (mStatusBarHeight <= 0) {
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                Object obj = cls.newInstance();
                Field statusBarHeightField = cls.getField("status_bar_height");
                int x = Integer.parseInt(statusBarHeightField.get(obj).toString());
                mStatusBarHeight = getActivity().getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        int left = mBuilder.needNearEdge ? 0 : mBuilder.defaultLeft;
        int left =  mBuilder.defaultLeft;
        FrameLayout.LayoutParams lp = createLayoutParams(left, mStatusBarHeight + mBuilder.defaultTop, 0, 0);
        FrameLayout rootLayout = (FrameLayout) getActivity().getWindow().getDecorView();
        rootLayout.addView(getDragView(), lp);
        getDragView().setOnTouchListener(this);
    }

    /**
     * 设置该控件大小，以及左右边距
     */
    private FrameLayout.LayoutParams createLayoutParams(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mBuilder.size, mBuilder.size);
        lp.setMargins(left, top, right, bottom);
        return lp;
    }


    private static DragView createDragView(DragView.Builder builder) {
        if (null == builder) {
            throw new NullPointerException("the param builder is null when execute method createDragView");
        }

        if (null == builder.activity) {
            throw new NullPointerException("the activity is null");
        }

        if (null == builder.view) {
            throw new NullPointerException("the view is null");
        }
        return new DragView(builder);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
                mTouchResult = false;
                mStartX = mLastX = (int) event.getRawX();
                mStartY = mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:// 移动
                int left, top, right, bottom;
                // 移动距离
                int dx = (int) (event.getRawX() - mLastX);
                int dy = (int) (event.getRawY() - mLastY);
                left = view.getLeft() + dx;
                if (left < 0) {
                    left = 0;
                }

                right = left + view.getWidth();
                if (right > mScreenWidth) {
                    right = mScreenWidth;
                    left = right - view.getWidth();
                }

                top = view.getTop() + dy;
                if (top < mStatusBarHeight + dp2px(30)) {
                    top = mStatusBarHeight + dp2px(30);
                }

                bottom = top + dy;
                if (bottom > mScreenHeight - dp2px(120)) {
                    bottom = mScreenHeight - dp2px(120);
                    top = bottom - view.getHeight();
                }
                view.layout(left, top, right, bottom);
                mLastX = (int) event.getRawX();
                mLastY = (int) event.getRawY();
                view.setLayoutParams(createLayoutParams(view.getLeft(), view.getTop(), 0, 0));
                mTouchResult = true;
                break;
            case MotionEvent.ACTION_UP:// 抬起
                //这里需设置LayoutParams，不然按home后回再到页面等view会回到原来的地方
                float endX = event.getRawX();
                float endY = event.getRawY();

                view.setLayoutParams(createLayoutParams(view.getLeft(), view.getTop(), 0, 0));

                if(Math.abs(endX - mStartX) < 10 || Math.abs(endY - mStartY) < 10) {
                    // 防止点击的时候稍微有点移动点击事件被拦截了
                    mTouchResult = false;
                }

                if(mTouchResult && mBuilder.needNearEdge) {
                    //是否每次都移至屏幕边沿
                    moreNearEdge();
                }


                break;
            default:
        }
        return mTouchResult;
    }

    private int dp2px(int dp){
        float density = getActivity().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    public static class Builder {
        private Activity activity;
        // 盖控件大小
        private int size = FrameLayout.LayoutParams.WRAP_CONTENT;
        // 默认位置
        private int defaultTop = 0;
        private int defaultLeft = 0;
        // 是否贴边
        private boolean needNearEdge = false;
        private View view;


        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setDefaultLeft(int defaultLeft) {
            this.defaultLeft = defaultLeft;
            return this;
        }

        public Builder setDefaultTop(int defaultTop) {
            this.defaultTop = defaultTop;
            return this;
        }

        public Builder setNeedNearEdge(boolean needNearEdge) {
            this.needNearEdge = needNearEdge;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public DragView build() {
            return createDragView(this);
        }
    }

}
