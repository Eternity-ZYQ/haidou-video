package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/4 0004 下午 07:49.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MineItemLayout extends LinearLayout{

    TextView tvContent;

    public MineItemLayout(Context context) {
        this(context, null);
    }

    public MineItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_mine_item, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemLayout);
        int leftIconRes = typedArray.getResourceId(R.styleable.MineItemLayout_milLeftIcon, -1);
        ImageView leftIcon = findViewById(R.id.iv_left);
        if(leftIconRes != -1) {
            leftIcon.setImageResource(leftIconRes);
        }else{
            leftIcon.setVisibility(GONE);
        }

        tvContent = findViewById(R.id.tv_content);
        setText(typedArray.getString(R.styleable.MineItemLayout_milContent));

        typedArray.recycle();
    }

    public void setText(CharSequence str){
        tvContent.setText(str);
    }
}
