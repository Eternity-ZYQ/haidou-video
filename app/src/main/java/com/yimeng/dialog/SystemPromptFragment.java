package com.yimeng.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.StringUtils;


/**
 * 系统弹出框
 */
public class SystemPromptFragment extends DialogFragment {

    private OnSystemPromptFragmentListener onSystemPromptFragmentListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.system_tips_prompt_fragment, null);

        //标题
        TextView titleSystemPrompt = view.findViewById(R.id.titleSystemPrompt);
        String title = getArguments().getString("title");
        if (!StringUtils.isEmpty(title)) {
            titleSystemPrompt.setText(title);
        }

        //内容
        TextView contentSystemPrompt = view.findViewById(R.id.contentSystemPrompt);
        String content = getArguments().getString("content");
        if (!StringUtils.isEmpty(content)) {
            contentSystemPrompt.setText(content);
        }

        //获取dialog
        final Dialog dialog = new Dialog(getContext());
        // 关闭标题栏，setContentView() 之前调用
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        //取消按钮
        Button cancelButton = view.findViewById(R.id.cancelButtonSystemPrompt);
        Boolean isTips = getArguments().getBoolean("isTips");
        if (isTips) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        //确认按钮
        Button submitButton = view.findViewById(R.id.submitButtonSystemPrompt);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (null != onSystemPromptFragmentListener) {
                    onSystemPromptFragmentListener.OnSystemPromptFragment(v);
                }
            }
        });

        return dialog;
    }

    /**
     * 添加自定义事件
     * @param onSystemPromptFragmentListener
     */
    public void setOnSystemPromptFragmentListener(OnSystemPromptFragmentListener onSystemPromptFragmentListener) {
        this.onSystemPromptFragmentListener = onSystemPromptFragmentListener;
    }

    /**
     * 自定义事件
     */
    public interface OnSystemPromptFragmentListener {
        void OnSystemPromptFragment(View view);
    }
}
