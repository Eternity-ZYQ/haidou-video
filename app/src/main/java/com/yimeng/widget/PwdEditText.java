package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.yimeng.haidou.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/4 3:15 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class PwdEditText extends AppCompatEditText implements TextWatcher {

    // 圆点半径
    private int mDotSize = 4;
    // 圆点颜色
    private int mDotColor = Color.BLACK;
    // 圆点画笔
    private Paint mDotPaint;
    // 边框颜色
    private int mRectColor = Color.BLACK;
    // 边框圆角
    private int mRectCorner = 0;
    // 分割线颜色
    private int mLineColor = Color.parseColor("#cccccc");
    // 分割线和边框宽度
    private int mLineWidth = 1;
    // 密码位数
    private int mPwdCount = 6;
    // 一个密码宽度
    private int mPwdItemWidth;
    private Paint mRectPaint;
    private Paint mLinePaint;

    private onPwdResultListener mOnPwdResultListener;

    public PwdEditText(Context context) {
        this(context, null);
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initAttributeSet(context, attrs);
        // 不显示光标
        setCursorVisible(false);
        initPaint();

        addTextChangedListener(this);
    }

    public void setOnPwdResultListener(onPwdResultListener onPwdResultListener){
        this.mOnPwdResultListener = onPwdResultListener;
    }

    private void initPaint() {
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(mDotColor);
        mDotPaint.setDither(true);
        mDotPaint.setStyle(Paint.Style.FILL);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(mRectColor);
        mRectPaint.setDither(true);
        mRectPaint.setStrokeWidth(mLineWidth);
        mRectPaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText);
        mDotSize = (int) typedArray.getDimension(R.styleable.PwdEditText_pwdet_dot_radius, dp2px(mDotSize));
        mDotColor = typedArray.getColor(R.styleable.PwdEditText_pwdet_dot_color, mDotColor);

        mRectColor = typedArray.getColor(R.styleable.PwdEditText_pwdet_rect_color, mRectColor);
        mRectCorner = (int) typedArray.getDimension(R.styleable.PwdEditText_pwdet_rect_corner, 0);
        mLineColor = typedArray.getColor(R.styleable.PwdEditText_pwdet_line_color, mLineColor);

        mPwdCount = typedArray.getInt(R.styleable.PwdEditText_pwdet_count, mPwdCount);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPwdCount)});

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        mPwdItemWidth = (getWidth() - (mPwdCount + 1) * mLineWidth) / mPwdCount;
        drawRect(canvas);
        drawLine(canvas);
        drawDot(canvas);
    }

    /**
     * 密码圆点
     *
     * @param canvas
     */
    private void drawDot(Canvas canvas) {
        int length = getText().toString().length();
        for (int i = 0; i < length; i++) {
            int cx = mLineWidth + i * mLineWidth + i * mPwdItemWidth + mPwdItemWidth / 2;
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, mDotSize, mDotPaint);
        }

    }

    /**
     * 分割线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        for (int i = 0; i < mPwdCount - 1; i++) {
            int startX = mLineWidth + (i + 1) * mPwdItemWidth + mLineWidth;
            int startY = 0;
            int endX = startX;
            int entY = getHeight() - mLineWidth;
            canvas.drawLine(startX, startY, endX, entY, mLinePaint);
        }
    }

    /**
     * 边框
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        RectF rect = new RectF(mLineWidth, mLineWidth, getWidth() - mLineWidth, getHeight() - mLineWidth);
        if (mRectCorner == 0) {
            canvas.drawRect(rect, mRectPaint);
        } else {
            canvas.drawRoundRect(rect, mRectCorner, mRectCorner, mRectPaint);
        }
    }

    public void addPassword(String number) {
        Log.d("msg", "PwdEditText -> addPassword: " + number);
        if (TextUtils.isEmpty(number)) {
            return;
        }
        //把密码取取出来
        String password = getText().toString().trim();
        if (password.length() <= mPwdCount) {
            //密码叠加
            password += number;
            setText(password);
        }
    }

    /**
     * 删除密码
     */
    public void deletePassword() {
        String password = getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            return;
        }
        password = password.substring(0, password.length() - 1);
        setText(password);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private long lastTime = 0;

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if(text.length() == mPwdCount && mOnPwdResultListener != null) {
            if(System.currentTimeMillis() - lastTime >= 1000) {
                mOnPwdResultListener.onResult(this, text.toString());
                lastTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface onPwdResultListener{
        void onResult(View view, String pwd);
    }
}
