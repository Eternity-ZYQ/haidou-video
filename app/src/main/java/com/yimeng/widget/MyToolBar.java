package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.WindowUiUtil;


/**
 * Author : huiGer
 * Time   : 2018/8/10 0010 下午 12:03.
 * Desc   : 标题栏
 */
public class MyToolBar extends Toolbar implements View.OnClickListener {

    private OnToolBarClick clickListener;
    private ImageView leftIcon, rightIcon;
    private TextView tvTitle;
    private TextView tvRight;
    private ConstraintLayout linTitleBar;

    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this);

        leftIcon = findViewById(R.id.toolBar_icon);
        leftIcon.setOnClickListener(this);
        tvRight = findViewById(R.id.tv_right);
        tvTitle = findViewById(R.id.toolBar_title);
        linTitleBar = findViewById(R.id.layout_content);
        findViewById(R.id.right_layout).setOnClickListener(this);
        rightIcon = findViewById(R.id.right_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolBar);
        setTitle(typedArray.getString(R.styleable.MyToolBar_title));
        tvTitle.setTextColor(typedArray.getColor(R.styleable.MyToolBar_titleTextColor, Color.parseColor("#333333")));
        setRightContent(typedArray.getString(R.styleable.MyToolBar_rightContent));
        float textSize = typedArray.getDimension(R.styleable.MyToolBar_rightTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
        tvRight.getPaint().setTextSize(textSize);
        tvRight.setTextColor(typedArray.getColor(R.styleable.MyToolBar_rightTextColor, Color.parseColor("#333333")));

        Drawable bgDrawable = typedArray.getDrawable(R.styleable.MyToolBar_android_background);
        if (bgDrawable != null) {
            findViewById(R.id.v_line).setBackground(bgDrawable);
            linTitleBar.setBackground(bgDrawable);
        }
        //设置标题栏padding
        ViewGroup.LayoutParams linTitleBarLayoutParams = linTitleBar.getLayoutParams();
        linTitleBarLayoutParams.height = linTitleBarLayoutParams.height + WindowUiUtil.getStatusBarHeight(context);
        linTitleBar.setLayoutParams(linTitleBarLayoutParams);
        linTitleBar.setPadding(linTitleBar.getPaddingLeft(), linTitleBar.getPaddingTop() + WindowUiUtil.getStatusBarHeight(context), linTitleBar.getPaddingRight(), linTitleBar.getPaddingBottom());


        findViewById(R.id.line).setVisibility((typedArray.getInt(R.styleable.MyToolBar_lineVisible, 1) == 1) ? VISIBLE : GONE);
        setLeftIcon(typedArray.getResourceId(R.styleable.MyToolBar_leftIcon, R.mipmap.left_arrow));
        setLeftIconVisible((typedArray.getInt(R.styleable.MyToolBar_leftIconVisible, 1) == 1) ? VISIBLE : INVISIBLE);
        int rightIconResourceId = typedArray.getResourceId(R.styleable.MyToolBar_rightIcon, -1);
        if (rightIconResourceId == -1) {
            rightIcon.setVisibility(GONE);
        } else {
            rightIcon.setVisibility(VISIBLE);
            rightIcon.setImageResource(rightIconResourceId);
        }

        setStatusBarVisible(typedArray.getInt(R.styleable.MyToolBar_statusBarVisible, 1) == 1 ? VISIBLE : GONE);

        typedArray.recycle();
    }

    /**
     * 设置左边按钮图标
     *
     * @param idRes
     */
    public void setLeftIcon(@DrawableRes int idRes) {
        leftIcon.setImageResource(idRes);
        setLeftIconVisible(VISIBLE);
    }

    /**
     * 左边按钮显示状态
     *
     * @param visibility
     */
    public void setLeftIconVisible(int visibility) {
        leftIcon.setVisibility(visibility);
    }

    /**
     * 设置标题
     *
     * @param str
     */
    @Override
    public void setTitle(CharSequence str) {
        if (tvTitle == null) {
            return;
        }
        tvTitle.setText(str);
        tvTitle.setVisibility(VISIBLE);
    }

    /**
     * 设置右边文字
     *
     * @param str
     */
    public void setRightContent(CharSequence str) {
        if (tvRight == null || TextUtils.isEmpty(str)) {
            return;
        }
        tvRight.setText(str);
        tvRight.setVisibility(VISIBLE);
    }

    public void setRightIcon(@DrawableRes int idRes) {
        this.rightIcon.setImageResource(idRes);
        this.rightIcon.setVisibility(VISIBLE);
    }

    /**
     * 设置右边文字显示状态
     *
     * @param visible
     */
    public void setRightTextVisible(int visible) {
        tvRight.setVisibility(visible);
    }

    /**
     * 设置title颜色
     */
    public void setTvTitleColorWhite() {
        tvTitle.setTextColor(Color.WHITE);
    }

    public void setOnToolBarClickListener(OnToolBarClick clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolBar_icon:
                if (clickListener != null) {
                    clickListener.onLeftClick();
                }
                break;
            case R.id.right_layout:
                if (clickListener != null) {
                    clickListener.onRightClick();
                }
                break;
            default:
        }
    }

    /**
     * 显示状态
     *
     * @param visible
     */
    public void setStatusBarVisible(int visible) {
        findViewById(R.id.v_line).setVisibility(visible);
    }

    public static class OnToolBarClick {
        public void onLeftClick() {
            ActivityUtils.getInstance().finishActivity();
        }

        public void onRightClick() {
        }
    }

}
