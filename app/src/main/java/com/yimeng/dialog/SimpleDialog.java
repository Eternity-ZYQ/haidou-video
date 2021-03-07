package com.yimeng.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.huige.library.utils.DeviceUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class SimpleDialog {

    static AlertDialog loadingDlg;

    /**
     * 单提示
     *
     * @param ctx
     * @param content
     * @param listener
     */
    public static void showTipDialog(Context ctx, String content, final OnSimpleDialogClickListener listener) {
        final AlertDialog dlg = new AlertDialog.Builder(ctx, R.style.Theme_AppCompat_Light_Dialog_Alert).create();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_simple_hint, null);
        dlg.setView(layout);
        dlg.show();
        dlg.setCancelable(false);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);
        // 设置窗口的内容页面
        window.setContentView(R.layout.dialog_simple_hint);

        TextView tvHint = window.findViewById(R.id.tv_simple_hint);
        tvHint.setText(content);

        window.findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (listener != null) {
                    listener.onClick(v, dlg);
                }
            }
        });
    }


    /**
     * 简单提示， 标题，内容，
     */
    public static void showSimpleRemarkWithTitleDialog(final Context context, String title, String content) {

        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_shop_remark_with_title, null);
        dlg.setView(layout);
        dlg.show();

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);
        // 设置窗口的内容页面
        window.setContentView(R.layout.dialog_shop_remark_with_title);

        TextView tvHint = window.findViewById(R.id.contentTV);
        tvHint.setText(content);
        TextView titleTv = window.findViewById(R.id.titleTV);
        titleTv.setText(title);

        window.findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

    }

    /**
     * 确认窗
     *
     * @param ctx
     * @param title           不需要title可传入null
     * @param content
     * @param cancelListener
     * @param confirmListener
     */
    public static void showConfirmDialog(Context ctx, String title, String content, String cancelBtnText, String confirmBtnText,
                                         OnCancelListener cancelListener, OnConfirmListener confirmListener, boolean isHideCancel) {
        new XPopup.Builder(ctx)
                .maxWidth((int) (DeviceUtils.getWindowWidth(ctx) * 0.75))
                .autoDismiss(true)
                .dismissOnTouchOutside(false)
                .asConfirm(title, content, cancelBtnText, confirmBtnText, confirmListener, cancelListener, isHideCancel)
                .bindLayout(R.layout.xpopup_custom_layout)
                .show();
    }

    /**
     * 确认窗
     *
     * @param ctx
     * @param title           不需要title可传入null
     * @param content
     * @param cancelListener
     * @param confirmListener
     */
    public static void showConfirmDialog(Context ctx, String title, String content,
                                         OnCancelListener cancelListener, OnConfirmListener confirmListener) {
        showConfirmDialog(ctx, title, content, null, null, cancelListener, confirmListener, false);
    }

    private static BasePopupView mLoadingDialog;

    /**
     * 显示加载窗
     *
     * @param ctx
     */
    public static synchronized void showLoading(Context ctx, String title) {
        if (mLoadingDialog == null) {
            mLoadingDialog = createDialog(ctx, null,true,true, false);
        }
        mLoadingDialog.show();
        ((CustomLoadingDialog)mLoadingDialog).setLoadingMsg(title).startAnim();
    }

    /**
     * 延时消失
     * @param ctx
     * @param title
     * @param delayMillis
     */
    public static synchronized void showDialog(Context ctx, String title, long delayMillis){
        final BasePopupView dialog = createDialog(ctx, title, false, false, true).show();
        dialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayMillis);
    }

    /**
     * 创建dialog
     * @param ctx
     * @param dismissOnTouchOutside 点击外部是否关闭
     * @param hasRect 是否含有黑色背景
     * @param loadingIsShow 菊花是否转圈圈
     * @return
     */
    public static BasePopupView createDialog(Context ctx, String title,boolean dismissOnTouchOutside, boolean loadingIsShow, boolean hasRect){
        return new XPopup.Builder(ctx)
                .hasShadowBg(false)
                .dismissOnTouchOutside(dismissOnTouchOutside)
                .popupAnimation(PopupAnimation.NoAnimation)
                .asCustom(new CustomLoadingDialog(ctx, title,loadingIsShow, hasRect));
    }

    /**
     * 关闭加载窗
     */
    public static void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShow()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 耗时操作的提示框
     *
     * @param context context
     * @param type    1 简单的帧动画， 2 圆形进度条
     */
    public static void showLoadingHintDialog(Context context, int type) {

        loadingDlg = new AlertDialog.Builder(context).create();
        loadingDlg.show();
        loadingDlg.setCanceledOnTouchOutside(false);
        Window window = loadingDlg.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0f);
        // 设置窗口的内容页面
        window.setContentView(R.layout.dialog_loading_hint);
        if (type != 4) {
            window.setBackgroundDrawableResource(R.mipmap.iv_transparent_bg_round_2);
            window.setLayout(DeviceUtils.dp2px(context, 120), DeviceUtils.dp2px(context, 120));
        } else {
            window.setBackgroundDrawable(new BitmapDrawable());
        }

        ImageView pbar1 = window.findViewById(R.id.progressbar_1);
        ProgressBar pbar2 = window.findViewById(R.id.progressbar_2);
        if (type == 2) {
            // 系统圆形进度条
            pbar2.setVisibility(View.VISIBLE);
        } else {
            // 简单的帧动画
            pbar1.setImageResource(R.mipmap.loading);
            pbar1.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(window.getContext(), R.anim.iv_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            animation.setInterpolator(lin);
            pbar1.startAnimation(animation);
        }
    }

    public static synchronized void cancelLoadingHintDialog() {

        if (loadingDlg != null && loadingDlg.isShowing()) {
            try {
                loadingDlg.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnSimpleDialogClickListener {
        void onClick(View v, AlertDialog dialog);
    }
}
