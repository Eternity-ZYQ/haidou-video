package com.yimeng.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.yimeng.haidou.R;
import com.yimeng.entity.SeedBean;
import com.yimeng.interfaces.SeedItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/15 4:18 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 种子雨
 * </pre>
 */
public class SeedRainLayout extends FrameLayout {

    /**
     * 控制动画的快慢
     */
    private List<Float> mSpds = Arrays.asList(3f, 4.5f, 3.6f, 2f, 5f, 3f, 4f);
    /**
     * x最多可选取的随机数值
     */
    private static final List<Float> X_MAX_CHOSE_RANDOMS = Arrays.asList(
            0.1f, 0.22f, 0.31f, 0.5f, 0.6f, 0.75f);

    private int maxX, maxY;//子view的x坐标和y坐标的最大取值


    private Random mRandom = new Random();
    private List<View> mViews = new ArrayList<>();
    private LayoutInflater mInflater;
    private int mChildViewRes = R.layout.seed_item;//子view的资源文件
    private ValueAnimator mAnimator;
    private SeedItemClickListener mSeedItemClickListener;

    public SeedRainLayout(Context context) {
        this(context, null);
    }

    public SeedRainLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeedRainLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(getContext());

        initAnim();
    }

    /**
     * 界面销毁时回调
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxX = w;
        maxY = h;
    }

    private void initAnim() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if(mViews.isEmpty()) {
                    stopAnim();
                }
                for (View view : mViews) {
                    float x = view.getX();
                    float y = view.getY();
                    float spd = (float) view.getTag(R.id.seed_speed_key);
                    float translationY = y + spd * value;
                    if(translationY > maxY) {
                        translationY = 0;
                        reSetView(view);
                    }
                    view.setTranslationY(translationY);
                }
            }
        });

        // 无限循环
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(100);
    }


    /**
     * 重置View的起始点和速度
     *
     */
    private void reSetView(View view){
        float random = X_MAX_CHOSE_RANDOMS.get(mRandom.nextInt(X_MAX_CHOSE_RANDOMS.size()));
        view.setX(maxX * random);
        setSpd(view);
    }

    /**
     * 设置View的spd
     *
     * @param view
     */
    private void setSpd(View view) {
        float spd = mSpds.get(mRandom.nextInt(mSpds.size()));
        view.setTag(R.id.seed_speed_key, spd);
    }

    /**
     * 设置数据
     *
     * @param seedBeans
     */
    public void setSeedBeans(final List<SeedBean> seedBeans) {
        if (seedBeans == null || seedBeans.isEmpty()) {
            return;
        }

        //确保初始化完成
        post(new Runnable() {
            @Override
            public void run() {
                reset();
                addSeedBeanView(seedBeans);
                startAnim();
            }
        });

    }

    /**
     * 领取，删除view
     */
    public void removeSeed(View view){
        mViews.remove(view);
        removeView(view);
    }

    /**
     * 开始
     */
    public void startAnim() {
        mAnimator.start();
    }

    /**
     * 停止
     */
    public void stopAnim() {
        mAnimator.cancel();
    }



    private void addSeedBeanView(List<SeedBean> seedBeans) {
        if (seedBeans == null || seedBeans.isEmpty()) return;

        for (final SeedBean seedBean : seedBeans) {

            View view = mInflater.inflate(mChildViewRes, SeedRainLayout.this, false);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mSeedItemClickListener != null) mSeedItemClickListener.seedClick(view, seedBean);
                }
            });

            reSetView(view);
            mViews.add(view);
            addView(view);
        }
    }

    /**
     * 重置子view
     */
    private void reset() {
        for (int i = 0; i < mViews.size(); i++) {
            removeView(mViews.get(i));
        }
    }

    public void setOnSeedItemClickListener(SeedItemClickListener listener){
        this.mSeedItemClickListener = listener;
    }
}
