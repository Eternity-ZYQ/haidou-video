package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.TextViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * xp
 * 我的收藏导航
 */

public class NavMyFavoriteView extends LinearLayout {
    @Bind(R.id.ll_first)
    LinearLayout llItemFirst;
    @Bind(R.id.ll_sec)
    LinearLayout llItemSec;

    @Bind(R.id.tv_first)
    TextView tvItemFirst;
    @Bind(R.id.tv_sec)
    TextView tvItemSec;

    @Bind(R.id.line_first)
    View lineFirst;
    @Bind(R.id.line_sec)
    View lineSec;

    private OnClickListener mOnClickListener;

    public NavMyFavoriteView(Context context) {
        this(context, null);
    }

    public NavMyFavoriteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavMyFavoriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_nav_my_favorite, this);
        ButterKnife.bind(this);
        setTextSeleted(0);
        positionLine(0);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavMyFavoriteView);
        String navText1 = typedArray.getString(R.styleable.NavMyFavoriteView_nav_text_one);
        if(!TextUtils.isEmpty(navText1)) {
            tvItemFirst.setText(navText1);
        }
        String navText2 = typedArray.getString(R.styleable.NavMyFavoriteView_nav_text_two);
        if(!TextUtils.isEmpty(navText2)) {
            tvItemSec.setText(navText2);
        }
        typedArray.recycle();
    }

    @OnClick({R.id.ll_first, R.id.ll_sec,})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_first:
                setTextSeleted(0);
                positionLine(0);
                mOnClickListener.onClick(0);
                break;
            case R.id.ll_sec:
                setTextSeleted(1);
                positionLine(1);
                mOnClickListener.onClick(1);
                break;
            default:
                break;
        }
    }

    private void positionLine(int position) {
        switch (position) {
            case 0:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.BOLD, 15);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.NORMAL, 14);
                lineFirst.setVisibility(VISIBLE);
                lineSec.setVisibility(INVISIBLE);
                break;
            case 1:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.BOLD, 15);
                lineFirst.setVisibility(INVISIBLE);
                lineSec.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setTextSeleted(int position) {
        TextView tar = tvItemFirst;
        switch (position) {
            case 0:
                tar = tvItemFirst;
                break;
            case 1:
                tar = tvItemSec;
                break;
            default:
                break;

        }
        tvItemFirst.setSelected(false);
        tvItemSec.setSelected(false);
        tar.setSelected(true);
    }

    /**
     * 设置标题1
     * @param str
     */
    public void setTitle1(CharSequence str){
        tvItemFirst.setText(str);
    }

    /**
     * 设置标题2
     * @param str
     */
    public void setTitle2(CharSequence str){
        tvItemSec.setText(str);
    }

    public void setCurrentPosition(int position) {
        setTextSeleted(position);
        positionLine(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public interface OnClickListener {

        void onClick(int position);

    }
}
