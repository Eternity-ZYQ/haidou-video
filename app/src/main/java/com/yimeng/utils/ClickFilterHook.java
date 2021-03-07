package com.yimeng.utils;

import android.util.Log;
import android.view.View;

import com.yimeng.haidou.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 5:46 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 过滤重复点击
 * </pre>
 */
@Aspect
public class ClickFilterHook {
    // 是否可重复点击
    private boolean isDoubleClick = false;
    private View mLastView;

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (isDoubleClick || !NoDoubleClickUtils.isDoubleClick()) {
//            joinPoint.proceed();
//            isDoubleClick = false;
//            Log.e("ClickFilterHook", "ClickFilterHook -> clickFilterHook: " + "执行点击");
//        } else {
//            Log.e("ClickFilterHook", "重复点击,已过滤");
//        }


        Object[] objects = joinPoint.getArgs();
        View view = objects.length == 0 ? null : (View) objects[0];
//        if (view != mLastView || isDoubleClick || !NoDoubleClickUtils.isDoubleClick()) {
//            joinPoint.proceed();
//            isDoubleClick = false;
//            LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "执行");
//        } else {
//            LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "拦截");
//        }


//        if (!isDoubleClick) {// 不允许，未设置
//            if (!NoDoubleClickUtils.isDoubleClick()) {
//                joinPoint.proceed();
//                LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "未设置，超时可点击，执行");
//            } else {
//                LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "未设置，未到时间，拦截");
//            }
//        } else {// 允许重复点击
//            joinPoint.proceed();
//            LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "可重复点击，执行");
////            if (mLastView != view) {
////                isDoubleClick = false;
////                LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: " + "可重复点击，执行，并设置后面点击不可重复");
////            }
//        }

        if(view != null) {
            Object tag = view.getTag(R.id.click_filter_key);
            if(tag != null && (boolean) tag) {
                joinPoint.proceed();
                LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: "+"设置重复点击，执行");
                return;
            }
        }

        if(!NoDoubleClickUtils.isDoubleClick()){
            joinPoint.proceed();
            LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickFilterHook: "+"未设置，到时间，执行");
        }else{
            Log.d("msg", "ClickFilterHook -> clickFilterHook: " + "未设置，未到时间，拦截");
        }

        mLastView = view;
    }

    /**
     * 点击之后
     */
    @After("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickAfter() {
        LogSimple.Companion.instance().e("msg", "ClickFilterHook -> clickAfter: "+"点击结束");

    }



}
