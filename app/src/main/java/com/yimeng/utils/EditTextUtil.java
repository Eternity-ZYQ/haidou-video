package com.yimeng.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextUtil {
    /*** 小数点后的位数 */
    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    private static String number;
    private static int curSelection;

    /***
     * 保留两位小数
     * @param etWeight
     * @param length 整数数字长度
     */
    @SuppressLint("SetTextI18n")
    public static void keepTwoDecimals(final EditText etWeight, final int length) {
        etWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //删除.后面超过两位的数字
                if (s.toString().contains(".")) {
                    int pointIndex = s.toString().indexOf(".");
                    if (s.length() - 1 - pointIndex > length) {
                        s = s.toString().subSequence(0,
                                pointIndex + 3);
                        etWeight.setText(s);
                        etWeight.setSelection(s.length());
                    }
                } else {
                    if (s.length() > 7) {
                        s = s.toString().subSequence(0, s.length() - 2);
                    }
                }

                //如果.在起始位置,则起始位置自动补0
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    etWeight.setText(s);
                    etWeight.setSelection(2);
                }

                //如果起始位置为0并且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etWeight.setText(s.subSequence(0, 1));
                        etWeight.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }


    /***
     * 保留两位小数
     * @param etWeight edit
     * @param decimalLen 小数数字长度
     * @param integerLen 整数数字长度
     */
    @SuppressLint("SetTextI18n")
    public static void keepTwoDecimals(final EditText etWeight, final int decimalLen,int integerLen) {
        etWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //删除.后面超过两位的数字
                if (s.toString().contains(".")) {
                    int pointIndex = s.toString().indexOf(".");
                    if (s.length() - 1 - pointIndex > decimalLen) {
                        s = s.toString().subSequence(0,
                                pointIndex + 3);
                        etWeight.setText(s);
                        etWeight.setSelection(s.length());
                    }
                } else {
                    if (s.length() > integerLen) {
                        s = s.toString().subSequence(0, s.length() - 1);
                        etWeight.setText(s);
                        etWeight.setSelection(s.length());
                    }
                }

                //如果.在起始位置,则起始位置自动补0
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    etWeight.setText(s);
                    etWeight.setSelection(2);
                }

                //如果起始位置为0并且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etWeight.setText(s.subSequence(0, 1));
                        etWeight.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }
}
