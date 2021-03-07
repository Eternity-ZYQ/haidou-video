package com.yimeng.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yimeng.haidou.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/6 4:43 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 加载状态
 * </pre>
 */
public class LoadingView extends View {

    private int mLoadingColor;
    private int mSuccessColor;
    private int mFailColor;

    private float progressWidth;    //进度宽度
    private float progressRadius;   //圆环半径

    private LoadingStatus mLoadingStatus;
    private Paint mPaint;

    private int startAngle = -90;
    private int minAngle = -90;
    private int sweepAngle = 120;
    private int curAngle = 0;

    //追踪Path的坐标
    private PathMeasure mPathMeasure;
    //画圆的Path
    private Path mPathCircle;
    //截取PathMeasure中的path
    private Path mPathCircleDst;
    private Path successPath;
    private Path failurePathLeft;
    private Path failurePathRight;

    private ValueAnimator circleAnimator;
    private float circleValue;
    private float successValue;
    private float failValueRight;
    private float failValueLeft;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mLoadingColor = typedArray.getColor(R.styleable.LoadingView_lv_loading_color, ContextCompat.getColor(context, R.color.theme_color));
        mSuccessColor = typedArray.getColor(R.styleable.LoadingView_lv_success_color, Color.parseColor("#469144"));
        mFailColor = typedArray.getColor(R.styleable.LoadingView_lv_fail_color, Color.parseColor("#c43133"));
        progressWidth = typedArray.getDimension(R.styleable.LoadingView_lv_progress_width, 10);
        progressRadius = typedArray.getDimension(R.styleable.LoadingView_lv_progress_radius, 100);
        typedArray.recycle();

        initPaint();
        initPath();
        initAnim();

    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLoadingColor);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(progressWidth);
        //设置画笔为圆角笔触
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initPath() {
        mPathCircle = new Path();
        mPathMeasure = new PathMeasure();
        mPathCircleDst = new Path();
        successPath = new Path();
        failurePathLeft = new Path();
        failurePathRight = new Path();
    }

    private void initAnim() {
        circleAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
    }

    public void loadLoading() {
        setStatus(LoadingStatus.LOADING);
        invalidate();
    }

    public void loadSuccess() {
        resetPath();
        setStatus(LoadingStatus.SUCCESS);
        startSuccessAnim();
    }

    public void loadFailure() {
        resetPath();
        setStatus(LoadingStatus.FAIL);
        startFailAnim();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getPaddingLeft(), getPaddingTop());   //将当前画布的点移到getPaddingLeft,getPaddingTop,后面的操作都以该点作为参照点
        if (mLoadingStatus == LoadingStatus.LOADING) {
            drawLoading(canvas);
        } else if (mLoadingStatus == LoadingStatus.SUCCESS) {
            drawSuccess(canvas);
        } else {
            drawFail(canvas);
        }

    }

    /**
     * 加载中
     */
    private void drawLoading(Canvas canvas) {
        mPaint.setColor(mLoadingColor);
        if (startAngle == minAngle) {
            sweepAngle += 6;
        }
        if (sweepAngle >= 300 || startAngle > minAngle) {
            startAngle += 6;
            if (sweepAngle > 20) {
                sweepAngle -= 6;
            }
        }

        if (startAngle > minAngle + 300) {
            startAngle %= 360;
            minAngle = startAngle;
            sweepAngle = 20;
        }
        canvas.rotate(curAngle += 4, progressRadius, progressRadius);  //旋转的弧长为4
        canvas.drawArc(new RectF(0, 0, progressRadius * 2, progressRadius * 2), startAngle, sweepAngle, false, mPaint);
        invalidate();
    }

    /**
     * 成功
     */
    private void drawSuccess(Canvas canvas) {
        mPaint.setColor(mSuccessColor);
        mPathCircle.addCircle(getWidth() / 2, getWidth() / 2, progressRadius, Path.Direction.CW);
        mPathMeasure.setPath(mPathCircle, false);
        //截取path并保存到mPathCircleDst中
        mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);

        if (circleValue == 1) {// 圈已转完
            successPath.moveTo(getWidth() / 8 * 3, getWidth() / 2);
            successPath.lineTo(getWidth() / 2, getWidth() / 5 * 3);
            successPath.lineTo(getWidth() / 3 * 2, getWidth() / 5 * 2);
            mPathMeasure.nextContour();
            mPathMeasure.setPath(successPath, false);
            mPathMeasure.getSegment(0, successValue * mPathMeasure.getLength(), mPathCircleDst, true);
            canvas.drawPath(mPathCircleDst, mPaint);
        }
    }

    /**
     * 失败
     */
    private void drawFail(Canvas canvas) {
        mPaint.setColor(mFailColor);
        mPathCircle.addCircle(getWidth() / 2, getWidth() / 2, progressRadius, Path.Direction.CW);
        mPathMeasure.setPath(mPathCircle, false);
        mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);
        if (circleValue == 1) {  //表示圆画完了,可以画叉叉的右边部分
            failurePathRight.moveTo(getWidth() / 3 * 2, getWidth() / 3);
            failurePathRight.lineTo(getWidth() / 3, getWidth() / 3 * 2);
            mPathMeasure.nextContour();
            mPathMeasure.setPath(failurePathRight, false);
            mPathMeasure.getSegment(0, failValueRight * mPathMeasure.getLength(), mPathCircleDst, true);
            canvas.drawPath(mPathCircleDst, mPaint);
        }
        if (failValueRight == 1) {    //表示叉叉的右边部分画完了,可以画叉叉的左边部分
            failurePathLeft.moveTo(getWidth() / 3, getWidth() / 3);
            failurePathLeft.lineTo(getWidth() / 3 * 2, getWidth() / 3 * 2);
            mPathMeasure.nextContour();
            mPathMeasure.setPath(failurePathLeft, false);
            mPathMeasure.getSegment(0, failValueLeft * mPathMeasure.getLength(), mPathCircleDst, true);
            canvas.drawPath(mPathCircleDst, mPaint);
        }
    }

    //重制路径
    private void resetPath() {
        successValue = 0;
        circleValue = 0;
        failValueLeft = 0;
        failValueRight = 0;
        mPathCircle.reset();
        mPathCircleDst.reset();
        failurePathLeft.reset();
        failurePathRight.reset();
        successPath.reset();
    }


    private void setStatus(LoadingStatus status) {
        mLoadingStatus = status;
    }

    private void startSuccessAnim() {
        ValueAnimator success = ValueAnimator.ofFloat(0f, 1.0f);
        success.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                successValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //组合动画,一先一后执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(success).after(circleAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void startFailAnim() {
        ValueAnimator failLeft = ValueAnimator.ofFloat(0f, 1.0f);
        failLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                failValueRight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator failRight = ValueAnimator.ofFloat(0f, 1.0f);
        failRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                failValueLeft = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //组合动画,一先一后执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(failLeft).after(circleAnimator).before(failRight);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) (progressRadius * 2 + progressWidth + getPaddingLeft() + getPaddingRight());
        }


        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) (progressRadius * 2 + progressWidth + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    public enum LoadingStatus {
        LOADING, SUCCESS, FAIL
    }

}
