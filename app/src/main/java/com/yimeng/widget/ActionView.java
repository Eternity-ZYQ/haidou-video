package com.yimeng.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;


/**
 * @author lijb
 *         功能条封装
 */

public class ActionView extends LinearLayout {
    private ImageView actionImg;
    private TextView actionTag;
    private TextView infoTV;

    public ActionView(Context context) {
        this(context, null);
    }

    public ActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_action, this);
        initView();
    }

    private void initView() {
        actionImg = findViewById(R.id.actionImg);
        actionTag = findViewById(R.id.actionTag);
        infoTV = findViewById(R.id.infoTV);
    }

    public void setActionName(@StringRes int actionName) {
        actionTag.setText(actionName);
    }
    public void setActionName(String actionName) {
        actionTag.setText(actionName);
    }

    public void setCountInfo(String countInfo) {
        infoTV.setText(countInfo);
    }

    public void setActionImg(@DrawableRes int actionIma) {
        if (actionIma == -1) {
            actionImg.setVisibility(INVISIBLE);
            return;
        }
        actionImg.setImageResource(actionIma);

    }

    public void setTextColor(int color) {
        infoTV.setTextColor(color);
    }
}
