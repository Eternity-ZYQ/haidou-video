package com.yimeng.widget;

import android.content.Context;
import android.graphics.Typeface;
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
 * @author xp
 *         通用导航条
 */

public class NavCommonView extends LinearLayout {
    @Bind(R.id.ll_first)
    LinearLayout llItemFirst;
    @Bind(R.id.ll_sec)
    LinearLayout llItemSec;
    @Bind(R.id.ll_thr)
    LinearLayout llItemThr;
    @Bind(R.id.ll_four)
    LinearLayout llItemFour;
    @Bind(R.id.ll_five)
    LinearLayout llItemFive;

    @Bind(R.id.tv_first)
    TextView tvItemFirst;
    @Bind(R.id.tv_sec)
    TextView tvItemSec;
    @Bind(R.id.tv_thr)
    TextView tvItemThr;
    @Bind(R.id.tv_four)
    TextView tvItemFour;
    @Bind(R.id.tv_five)
    TextView tvItemFive;

    @Bind(R.id.line_first)
    View lineFirst;
    @Bind(R.id.line_sec)
    View lineSec;
    @Bind(R.id.line_thr)
    View lineThr;
    @Bind(R.id.line_four)
    View lineFour;
    @Bind(R.id.line_five)
    View lineFive;

    private OnClickListener mOnClickListener;

    public NavCommonView(Context context) {
        this(context, null);
    }

    public NavCommonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavCommonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_nav_common, this);
        ButterKnife.bind(this);
        setTextSeleted(0);
        positionLine(0);
    }

    @OnClick({R.id.ll_first, R.id.ll_sec, R.id.ll_thr, R.id.ll_four, R.id.ll_five})
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
            case R.id.ll_thr:
                setTextSeleted(2);
                positionLine(2);
                mOnClickListener.onClick(2);
                break;
            case R.id.ll_four:
                setTextSeleted(3);
                positionLine(3);
                mOnClickListener.onClick(3);
                break;
            case R.id.ll_five:
                setTextSeleted(4);
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
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.BOLD, 15);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemThr, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFour, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFive, "宋体", Typeface.NORMAL, 14);
                lineFirst.setVisibility(VISIBLE);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFive.setVisibility(INVISIBLE);
                break;
            case 1:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.BOLD, 15);
                TextViewUtil.setTypefaceAndSize(tvItemThr, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFour, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFive, "宋体", Typeface.NORMAL, 14);
                lineFirst.setVisibility(INVISIBLE);
                lineSec.setVisibility(VISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFive.setVisibility(INVISIBLE);
                break;
            case 2:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemThr, "宋体", Typeface.BOLD, 15);
                TextViewUtil.setTypefaceAndSize(tvItemFour, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFive, "宋体", Typeface.NORMAL, 14);
                lineFirst.setVisibility(INVISIBLE);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(VISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFive.setVisibility(INVISIBLE);
                break;
            case 3:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemThr, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFour, "宋体", Typeface.BOLD, 15);
                TextViewUtil.setTypefaceAndSize(tvItemFive, "宋体", Typeface.NORMAL, 14);
                lineFirst.setVisibility(INVISIBLE);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(VISIBLE);
                lineFive.setVisibility(INVISIBLE);
                break;
            case 4:
                TextViewUtil.setTypefaceAndSize(tvItemFirst, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemSec, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemThr, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFour, "宋体", Typeface.NORMAL, 14);
                TextViewUtil.setTypefaceAndSize(tvItemFive, "宋体", Typeface.BOLD, 15);
                lineFirst.setVisibility(INVISIBLE);
                lineSec.setVisibility(INVISIBLE);
                lineThr.setVisibility(INVISIBLE);
                lineFour.setVisibility(INVISIBLE);
                lineFive.setVisibility(VISIBLE);
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
            case 2:
                tar = tvItemThr;
                break;
            case 3:
                tar = tvItemFour;
                break;
            case 4:
                tar = tvItemFive;
                break;
            default:
                break;

        }
        tvItemFirst.setSelected(false);
        tvItemSec.setSelected(false);
        tvItemThr.setSelected(false);
        tvItemFour.setSelected(false);
        tvItemFive.setSelected(false);
        setTextColor(false, tvItemFirst, tvItemSec, tvItemThr, tvItemFour, tvItemFive);
        tar.setSelected(true);
        setTextColor(true, tar);
    }

    private void setTextColor(boolean isChecked, TextView... tvs){
        for (TextView tv : tvs) {
            tv.setTextColor(isChecked ? getResources().getColor(R.color.theme_color) : getResources().getColor(R.color.c_999999));
        }
    }

    public void setCurrentNavCount(int count) {
        llItemFirst.setVisibility(GONE);
        llItemSec.setVisibility(GONE);
        llItemThr.setVisibility(GONE);
        llItemFour.setVisibility(GONE);
        llItemFive.setVisibility(GONE);
        switch (count) {
            case 1:
                llItemFirst.setVisibility(VISIBLE);
                break;
            case 2:
                llItemFirst.setVisibility(VISIBLE);
                llItemSec.setVisibility(VISIBLE);
                break;
            case 3:
                llItemFirst.setVisibility(VISIBLE);
                llItemSec.setVisibility(VISIBLE);
                llItemThr.setVisibility(VISIBLE);
                break;
            case 4:
                llItemFirst.setVisibility(VISIBLE);
                llItemSec.setVisibility(VISIBLE);
                llItemThr.setVisibility(VISIBLE);
                llItemFour.setVisibility(VISIBLE);
                break;
            case 5:
                llItemFirst.setVisibility(VISIBLE);
                llItemSec.setVisibility(VISIBLE);
                llItemThr.setVisibility(VISIBLE);
                llItemFour.setVisibility(VISIBLE);
                llItemFive.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setNavType(String var1, String var2, String var3, String var4, String var5) {
        tvItemFirst.setText(var1);
        tvItemSec.setText(var2);
        tvItemThr.setText(var3);
        tvItemFour.setText(var4);
        tvItemFive.setText(var5);
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
