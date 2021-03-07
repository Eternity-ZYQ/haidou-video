package com.yimeng.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by user on 2018/6/15.
 */

public class ClickNotSlipScrollView extends ScrollView {
    public ClickNotSlipScrollView(Context context) {
        super(context);
    }

    public ClickNotSlipScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickNotSlipScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
