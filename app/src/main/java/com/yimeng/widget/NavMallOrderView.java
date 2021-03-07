package com.yimeng.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * xp
 * 商城订单头部导航
 */

public class NavMallOrderView extends LinearLayout {

    @Bind(R.id.rl_sec)
    RelativeLayout rlItemSec;
    @Bind(R.id.rl_thr)
    RelativeLayout rlItemThr;
    @Bind(R.id.rl_four)
    RelativeLayout rlItemFour;
    @Bind(R.id.rl_fif)
    RelativeLayout rlItemFif;
    @Bind(R.id.rl_six)
    RelativeLayout rlItemSix;

    @Bind(R.id.tv_sec)
    TextView tvItemSec;
    @Bind(R.id.tv_thr)
    TextView tvItemThr;
    @Bind(R.id.tv_four)
    TextView tvItemFour;
    @Bind(R.id.tv_fif)
    TextView tvItemFif;
    @Bind(R.id.tv_six)
    TextView tvItemSix;

    @Bind(R.id.line_sec)
    View lineSec;
    @Bind(R.id.line_thr)
    View lineThr;
    @Bind(R.id.line_four)
    View lineFour;
    @Bind(R.id.line_fif)
    View lineFif;
    @Bind(R.id.line_six)
    View lineSix;

    private OnClickListener mOnClickListener;
    Typeface Boldtf = Typeface.create("宋体", Typeface.BOLD);
    Typeface Normaltf = Typeface.create("宋体", Typeface.NORMAL);

    public NavMallOrderView(Context context) {
        this(context, null);
    }

    public NavMallOrderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavMallOrderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.include_mall_order_view, this);
        ButterKnife.bind(this);
        setTextSelected(0);
        positionLine(0);
    }

    @OnClick({R.id.rl_sec, R.id.rl_thr, R.id.rl_four, R.id.rl_fif, R.id.rl_six})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sec:
                setTextSelected(0);
                positionLine(0);
                mOnClickListener.onClick(0);
                break;
            case R.id.rl_thr:
                setTextSelected(1);
                positionLine(1);
                mOnClickListener.onClick(1);
                break;
            case R.id.rl_four:
                setTextSelected(2);
                positionLine(2);
                mOnClickListener.onClick(2);
                break;
            case R.id.rl_fif:
                setTextSelected(3);
                positionLine(3);
                mOnClickListener.onClick(3);
                break;
            case R.id.rl_six:
                setTextSelected(4);
                positionLine(4);
                mOnClickListener.onClick(4);
                break;
            default:
                break;
        }
    }

    private void positionLine(int position) {
        switch (position) {
            case 0:
                //设置字体粗体
                tvItemSec.setTypeface(Boldtf);
                tvItemThr.setTypeface(Normaltf);
                tvItemFour.setTypeface(Normaltf);
                tvItemFif.setTypeface(Normaltf);
                tvItemSix.setTypeface(Normaltf);
                tvItemSec.setTextSize(16);
                tvItemThr.setTextSize(15);
                tvItemFour.setTextSize(15);
                tvItemFif.setTextSize(15);
                tvItemSix.setTextSize(15);
                lineSec.setVisibility(VISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFif.setVisibility(INVISIBLE);
                lineSix.setVisibility(INVISIBLE);
                break;
            case 1:
                tvItemSec.setTypeface(Normaltf);
                tvItemThr.setTypeface(Boldtf);
                tvItemFour.setTypeface(Normaltf);
                tvItemFif.setTypeface(Normaltf);
                tvItemSix.setTypeface(Normaltf);
                tvItemSec.setTextSize(15);
                tvItemThr.setTextSize(16);
                tvItemFour.setTextSize(15);
                tvItemFif.setTextSize(15);
                tvItemSix.setTextSize(15);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(VISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFif.setVisibility(INVISIBLE);
                lineSix.setVisibility(INVISIBLE);
                break;
            case 2:
                tvItemSec.setTypeface(Normaltf);
                tvItemThr.setTypeface(Normaltf);
                tvItemFour.setTypeface(Boldtf);
                tvItemFif.setTypeface(Normaltf);
                tvItemSix.setTypeface(Normaltf);
                tvItemSec.setTextSize(15);
                tvItemThr.setTextSize(15);
                tvItemFour.setTextSize(16);
                tvItemFif.setTextSize(15);
                tvItemSix.setTextSize(15);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(VISIBLE);
                lineFif.setVisibility(INVISIBLE);
                lineSix.setVisibility(INVISIBLE);
                break;
            case 3:
                tvItemSec.setTypeface(Normaltf);
                tvItemThr.setTypeface(Normaltf);
                tvItemFour.setTypeface(Normaltf);
                tvItemFif.setTypeface(Boldtf);
                tvItemSix.setTypeface(Normaltf);
                tvItemSec.setTextSize(15);
                tvItemThr.setTextSize(15);
                tvItemFour.setTextSize(15);
                tvItemFif.setTextSize(16);
                tvItemSix.setTextSize(15);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFif.setVisibility(VISIBLE);
                lineSix.setVisibility(INVISIBLE);
                break;
            case 4:
                tvItemSec.setTypeface(Normaltf);
                tvItemThr.setTypeface(Normaltf);
                tvItemFour.setTypeface(Normaltf);
                tvItemFif.setTypeface(Normaltf);
                tvItemSix.setTypeface(Boldtf);
                tvItemSec.setTextSize(15);
                tvItemThr.setTextSize(15);
                tvItemFour.setTextSize(15);
                tvItemFif.setTextSize(15);
                tvItemSix.setTextSize(16);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFif.setVisibility(INVISIBLE);
                lineSix.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setTextSelected(int position) {
        TextView tar = tvItemSec;
        switch (position) {
            case 0:
                tar = tvItemSec;
                break;
            case 1:
                tar = tvItemThr;
                break;
            case 2:
                tar = tvItemFour;
                break;
            case 3:
                tar = tvItemFif;
                break;
            case 4:
                tar = tvItemSix;
            default:
                break;

        }
        tvItemSec.setSelected(false);
        setTextColor(tvItemSec, false);
        tvItemThr.setSelected(false);
        setTextColor(tvItemThr, false);
        tvItemFour.setSelected(false);
        setTextColor(tvItemFour, false);
        tvItemFif.setSelected(false);
        setTextColor(tvItemFif, false);
        tvItemSix.setSelected(false);
        setTextColor(tvItemSix, false);
        tar.setSelected(true);
        setTextColor(tar, true);
    }

    private void setTextColor(TextView tv, boolean isChecked){
        tv.setTextColor(isChecked ? getResources().getColor(R.color.theme_color) : getResources().getColor(R.color.c_999999));
    }

    public void setCurrentPosition(int position) {
        setTextSelected(position);
        positionLine(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public interface OnClickListener {

        void onClick(int position);

    }
}
