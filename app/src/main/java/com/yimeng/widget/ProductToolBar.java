package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.haidou.R;


/**
 * Author : huiGer
 * Time   : 2018/8/10 0010 下午 12:03.
 * Desc   : 标题栏
 */
public class ProductToolBar extends Toolbar implements View.OnClickListener {

    private OnToolBarClick clickListener;
    private ImageView leftIcon, rightIcon, rightIcon2;
    private TextView tvTitle;
    private TextView tvRight;

    public ProductToolBar(Context context) {
        this(context, null);
    }

    public ProductToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.product_toolbar_layout, this);

        leftIcon = findViewById(R.id.toolBar_icon);
        leftIcon.setOnClickListener(this);
        tvRight = findViewById(R.id.tv_right);
        tvTitle = findViewById(R.id.toolBar_title);
        findViewById(R.id.right_layout).setOnClickListener(this);
        rightIcon = findViewById(R.id.right_icon);
        rightIcon2 = findViewById(R.id.iv_right_2);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProductToolBar);
        int color = R.styleable.ProductToolBar_p_toolbarBgColor;
        setBackgroundColor(typedArray.getColor(color, getResources().getColor(R.color.theme_color)));
        setTitle(typedArray.getString(R.styleable.ProductToolBar_p_title));
        setRightContent(typedArray.getString(R.styleable.ProductToolBar_p_rightContent));

        leftIcon.setImageResource(typedArray.getResourceId(R.styleable.ProductToolBar_p_leftIcon, R.mipmap.back_wihte));

        int titleTextColor = typedArray.getColor(R.styleable.ProductToolBar_p_titleTextColor, Color.WHITE);
        tvTitle.setTextColor(titleTextColor);
        tvRight.setTextColor(titleTextColor);
        tvRight.getPaint().setTextSize(typedArray.getDimensionPixelSize(R.styleable.ProductToolBar_p_rightTextSize, 14));
        leftIcon.setVisibility((typedArray.getInt(R.styleable.ProductToolBar_p_leftIconVisible, 1) == 1) ? VISIBLE : GONE);
        int rightIconResourceId = typedArray.getResourceId(R.styleable.ProductToolBar_p_rightIcon, -1);
        if (rightIconResourceId == -1) {
            rightIcon.setVisibility(GONE);
        } else {
            rightIcon.setVisibility(VISIBLE);
            rightIcon.setImageResource(rightIconResourceId);
        }
        int rightRes2 = typedArray.getResourceId(R.styleable.ProductToolBar_p_rightIcon2, -1);
        if (rightRes2 != -1) {
            rightIcon2.setImageResource(rightRes2);
        }

        findViewById(R.id.v_line).setVisibility(typedArray.getInt(R.styleable.ProductToolBar_p_lineVisible, 1) == 1 ? VISIBLE : GONE);

        typedArray.recycle();
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

    public ImageView getLeftIcon() {
        return leftIcon;
    }

    public void setOnToolBarClickListener(OnToolBarClick clickListener) {
        this.clickListener = clickListener;
    }

    public ImageView getRightImage2() {
        return rightIcon2;
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


    public static class OnToolBarClick {
        public void onLeftClick() {

        }

        public void onRightClick() {
        }
    }

}
