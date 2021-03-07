package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.huige.library.widget.CircleImageView;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/19 0019 下午 02:00.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class HomeMenuTypeView extends LinearLayout{

    private CircleImageView mImageView;
    private TextView mTextView;

    public HomeMenuTypeView(Context context) {
        this(context, null);
    }

    public HomeMenuTypeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeMenuTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_home_menu_type, this);
        mImageView = findViewById(R.id.iv_menu_type);
        mTextView = findViewById(R.id.tv_menu_type);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeMenuTypeView);
        int resId = typedArray.getResourceId(R.styleable.HomeMenuTypeView_HomeMenuIcon, R.mipmap.ico1);
        mImageView.setImageResource(resId);
        mTextView.setText(typedArray.getString(R.styleable.HomeMenuTypeView_HomeMenuType));
        typedArray.recycle();
    }

    /**
     * 设置图片
     * @param url 图片网络地址
     */
    public void setMenuImage(String url){
        CommonUtils.showImage(mImageView, url);
    }

    /**
     * 设置标识
     * @param title
     */
    public void setMenuTyep(String title){
        mTextView.setText(title);
    }


}
