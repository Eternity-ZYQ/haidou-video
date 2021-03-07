package com.yimeng.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.yimeng.haidou.R;


/**
 * 输入弹出框
 */
public class InputPopupWindow extends PopupWindow implements View.OnClickListener {

    private Button cancelButton, confirmButton;
    private EditText inputEditText;
    private View mMenuView;
    private PopupWindow popupWindow;
    private OnSelectedListener mOnSelectedListener;

    public InputPopupWindow(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_input_selector, null);
        cancelButton = mMenuView.findViewById(R.id.cancelButtonInputSelector);
        confirmButton = mMenuView.findViewById(R.id.confirmButtonInputSelector);
        inputEditText = mMenuView.findViewById(R.id.inputEditTextPopupWindow);

        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    /**
     * 把一个View控件添加到popupWindow上并显示
     * @param activity
     */
    public void showPopupWindow(Activity activity) {
        popupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 必须要设置背景，播放动画有一个前提 就是窗体必须有背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        // 设置窗口显示的动画效果
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.update();
    }

    /**
     * 移除PopupWindow
     */
    public void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButtonInputSelector:
                dismissPopupWindow();
                break;
            case R.id.confirmButtonInputSelector:
                if (null != mOnSelectedListener) {
                    String code = inputEditText.getText().toString();
                    mOnSelectedListener.OnSelected(v, code);
                }
                break;
        }
    }

    /**
     * 设置选择监听
     * @param l
     */
    public void setOnSelectedListener(OnSelectedListener l) {
        this.mOnSelectedListener = l;
    }

    /**
     * 选择监听接口
     */
    public interface OnSelectedListener {
        void OnSelected(View v, String code);
    }

}
