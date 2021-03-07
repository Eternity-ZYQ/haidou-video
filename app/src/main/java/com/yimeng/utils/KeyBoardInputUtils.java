package com.yimeng.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/29 7:21 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 解决webView键盘遮挡问题
 * </pre>
 */
public class KeyBoardInputUtils {

    private Activity activity;

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private static KeyBoardInputUtils keyBoardListener;


    public static KeyBoardInputUtils getInstance(Activity activity) {
// if(keyBoardListener==null){
        keyBoardListener = new KeyBoardInputUtils(activity);
// }
        return keyBoardListener;
    }


    private KeyBoardInputUtils(Activity activity) {
        super();
        this.activity = activity;
    }


    public void init() {

        FrameLayout content = activity
                .findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        possiblyResizeChildOfContent();
                    }
                });

        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
                .getLayoutParams();


    }


    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView()
                    .getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard
                        - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }


    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);

    }
}