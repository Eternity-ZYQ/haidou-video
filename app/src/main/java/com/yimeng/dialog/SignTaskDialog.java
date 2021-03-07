package com.yimeng.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.yimeng.haidou.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/27 3:55 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 签到任务弹窗
 * </pre>
 */
public class SignTaskDialog extends CenterPopupView {

    private Button mBtnSubmit;
    private OnClickListener mOnClickListener;

    public SignTaskDialog(@NonNull Context context, @NonNull OnClickListener clickListener) {
        super(context);
        this.mOnClickListener = clickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_sign_task;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view);
                dismiss();
            }
        });
    }
}
