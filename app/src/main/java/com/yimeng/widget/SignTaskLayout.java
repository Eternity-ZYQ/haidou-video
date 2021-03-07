package com.yimeng.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.dialog.SignTaskDialog;
import com.lxj.xpopup.XPopup;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/26 5:33 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 签到任务中的每个item
 * </pre>
 */
public class SignTaskLayout extends ConstraintLayout implements View.OnClickListener {

    /**
     * 是否展开描述
     */
    private boolean isShow =false;
    private TextView mTvTaskHelp;
    //
    private ImageView mIvTaskClose;
    // 任务描述
    private TextView mTvTaskDesc;
    private Button mBtnDoTask;
    /**
     * 任务状态
     * 0. 未开始（领任务）
     * 1. 已开始（去完成）
     * 2. 已结束（领奖励）
     */
    private int mTaskStatus = 0;

    public SignTaskLayout(Context context) {
        this(context, null);
    }

    public SignTaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignTaskLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.layout_sign_task, this);

        mTvTaskDesc = findViewById(R.id.tv_task_desc);
        mTvTaskHelp = findViewById(R.id.tv_task_help);
        mIvTaskClose = findViewById(R.id.iv_task_close);
        mBtnDoTask = findViewById(R.id.btn_do_task);


        mTvTaskDesc.setOnClickListener(this);
        mIvTaskClose.setOnClickListener(this);
    }

    /**
     * 设置数据
     */
    public void setData(){

    }

    /**
     * 是否展开显示详情
     * @param isShow
     */
    public void changeViewStatus(boolean isShow){
        mTvTaskHelp.setVisibility(isShow ? VISIBLE : GONE);
        mIvTaskClose.setRotation(isShow ? 90 : -90);
    }

    /**
     * 任务按钮
     * @param onClickListener
     */
    public void setDoTaskClickListener(final OnClickListener onClickListener){
        mBtnDoTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .asCustom(new SignTaskDialog(getContext(), onClickListener))
                        .show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_task_desc:
            case R.id.iv_task_close:
                changeViewStatus(isShow = !isShow);
                break;
            default:

        }
    }
}
