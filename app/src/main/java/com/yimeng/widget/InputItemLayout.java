package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yimeng.haidou.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/15 0015 上午 10:21.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 输入条
 * </pre>
 */
public class InputItemLayout extends ConstraintLayout{

    private View rootView;
    private EditText mEditText;
    private TextView mTvType;

    public InputItemLayout(Context context) {
        this(context, null);
    }

    public InputItemLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rootView = LayoutInflater.from(context).inflate(R.layout.input_item_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputItemLayout);
        mTvType = findViewById(R.id.tv_type);
        mTvType.setText(typedArray.getString(R.styleable.InputItemLayout_android_text));
        float testSize = typedArray.getDimension(R.styleable.InputItemLayout_android_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
        mTvType.getPaint().setTextSize(testSize);

        mEditText = findViewById(R.id.et_value);
        mEditText.getPaint().setTextSize(testSize);
        mEditText.setInputType(typedArray.getInt(R.styleable.InputItemLayout_android_inputType, EditorInfo.TYPE_CLASS_TEXT));

        boolean isEditor = typedArray.getBoolean(R.styleable.InputItemLayout_editor, true);
        mEditText.setFocusable(isEditor);
        mEditText.setFocusableInTouchMode(isEditor);

        mEditText.setTextColor(typedArray.getColor(R.styleable.InputItemLayout_EditTextColor, Color.parseColor("#333333")));

        mEditText.setGravity(typedArray.getInt(R.styleable.InputItemLayout_android_gravity, Gravity.RIGHT));

        String hintStr = typedArray.getString(R.styleable.InputItemLayout_android_hint);
        mEditText.setHint(TextUtils.isEmpty(hintStr) ? context.getString(R.string.please_input) : hintStr);

        int maxLength = typedArray.getInt(R.styleable.InputItemLayout_android_maxLength, -1);
        if(maxLength > 0) {
            mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
        }


        typedArray.recycle();
    }

    /**
     * @return 输入的内容
     */
    public String getInputText(){
        return mEditText.getText().toString().trim();
    }

    public void setEditText(CharSequence str){
        mEditText.setText(str);
    }

    public void setLeftText(CharSequence sequence){
        mTvType.setText(sequence);
    }

    public void setOnItemClickListener(final View.OnClickListener listener){
        if(listener != null) {
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);

            mEditText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(R.id.click_filter_key, true);
                    listener.onClick(rootView);
                }
            });
            setOnClickListener(listener);
        }
    }

    public EditText getmEditText() {
        return mEditText;
    }
}
