package com.yimeng.utils;

import android.annotation.SuppressLint;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.yimeng.utils.Preconditions.checkNotNull;
import static com.yimeng.utils.Preconditions.checkUiThread;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/12 0012 下午 02:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class RxView {
    /**
     * 防止重复点击
     *
     * @param target 目标view
     * @param action 监听器
     */
    @SuppressLint("CheckResult")
    public static void setOnClickListeners(final Action1<View> action, @NonNull View... target) {
        for (View view : target) {
            RxView.onClick(view).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<View>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull View view) throws Exception {
                    action.onClick(view);
                }
            });
        }
    }

    /**
     * 监听onclick事件防抖动
     *
     * @param view
     * @return
     */
    @CheckResult
    @NonNull
    private static Observable<View> onClick(@NonNull View view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ViewClickOnSubscribe(view));
    }

    /**
     * onclick事件防抖动
     * 返回view
     */
    private static class ViewClickOnSubscribe implements ObservableOnSubscribe<View> {
        private View view;

        public ViewClickOnSubscribe(View view) {
            this.view = view;
        }

        @Override
        public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<View> e) throws Exception {
            checkUiThread();

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!e.isDisposed()) {
                        e.onNext(view);
                    }
                }
            };
            view.setOnClickListener(listener);
        }
    }

    /**
     * A one-argument action. 点击事件转发接口
     *
     * @param <T> the first argument type
     */
    public interface Action1<T> {
        void onClick(T t);
    }
}
