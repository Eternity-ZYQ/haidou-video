package com.yimeng.utils;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;
import android.widget.TextView;

/**
 * TextViewUtil
 *
 * @author xp
 * @describe TextViewUtil.
 * @date 2017/4/5.
 */

public class TextViewUtil {

    /**
     * 设置EditText的Hint与正常的字体大小
     *
     * @param text     文字
     * @param size     字体大小
     * @param editText EditText
     */
    public static void setEditTextHint(String text, int size, EditText editText) {

        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(text);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss));
    }


    /**
     * 设置字体样式
     *
     * @param object     TextView,EditText
     * @param familyName 如：宋体
     * @param style      如: Typeface.BOLD
     */
    public static void setTypeface(Object object, String familyName, int style) {
        Typeface typeface = Typeface.create(familyName, style);
        if (object instanceof EditText) {
            EditText mEditText = (EditText) object;
            mEditText.setTypeface(typeface);
        } else if (object instanceof TextView) {
            TextView mTextView = (TextView) object;
            mTextView.setTypeface(typeface);
        }
    }

    /**
     * 设置字体样式，大小
     *
     * @param object     TextView,EditText
     * @param familyName 如：宋体
     * @param style      如: Typeface.BOLD
     */
    public static void setTypefaceAndSize(Object object, String familyName, int style, int sizeSp) {
        Typeface typeface = Typeface.create(familyName, style);
        if (object instanceof EditText) {
            EditText mEditText = (EditText) object;
            mEditText.setTypeface(typeface);
            mEditText.setTextSize(sizeSp);
        } else if (object instanceof TextView) {
            TextView mTextView = (TextView) object;
            mTextView.setTypeface(typeface);
            mTextView.setTextSize(sizeSp);
        }
    }

    /**
     * 限制输入俩位小数
     */
    public static void setLimitTwoDecimalPlaces(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });

    }
}
