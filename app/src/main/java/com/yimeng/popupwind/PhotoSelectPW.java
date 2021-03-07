package com.yimeng.popupwind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yimeng.haidou.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片选择
 */
public class PhotoSelectPW extends PopupWindow {

    private int mWhat;
    private Handler mHandler;
    private Context mContext;
    private int mType;

    public PhotoSelectPW(Context mContext, Handler mHandler, int mWhat, int mType) {
        super(mContext);
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
        this.mType = mType;
        initView(mContext);
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void initView(Context context) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.pw_photo_select, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

    }

    @OnClick({R.id.takePhotosTV, R.id.photoTV, R.id.cancelTV})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.cancelTV:
                dismiss();
                break;
            case R.id.takePhotosTV:
            case R.id.photoTV:
                Message msg = mHandler.obtainMessage();
                msg.what = mWhat;
                msg.arg1 = view.getId();
                msg.arg2 = mType;
                mHandler.sendMessage(msg);
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
