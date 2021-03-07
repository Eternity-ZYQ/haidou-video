package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yimeng.haidou.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/14 0014 下午 03:33.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 首页圆弧背景图片
 * </pre>
 */
public class ArcImageView extends AppCompatImageView{

    /**
     * 圆弧高度
     */
    private int mArcHeight;

    /**
     * 凹
     */
    private final int OU = 1;
    /**
     * 凸
     */
    private final int TU = -1;
    private int mArcType;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);
        mArcType = typedArray.getInt(R.styleable.ArcImageView_arcType, TU);
        typedArray.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        Path path = new Path();
        path.moveTo(0, 0);
        if(mArcType == OU) {
            path.lineTo(0, height);
            path.quadTo(width / 2, height - mArcHeight, width, height);
        }else {
            path.lineTo(0, height - mArcHeight);
            path.quadTo(width / 2, height + mArcHeight, width, height - mArcHeight);
        }
        path.lineTo(width, 0);
        path.close();
        canvas.clipPath(path);
        super.draw(canvas);
    }
}
