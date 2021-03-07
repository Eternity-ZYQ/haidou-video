package com.yimeng.utils;

import android.os.Looper;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/12 0012 下午 02:52.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class Preconditions {
    public static void checkArgument(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    public static void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }

    private Preconditions() {
        throw new AssertionError("No instances.");
    }
}
