package com.yimeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/27 0027 下午 06:15.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 上传图片
 * </pre>
 */
public class UploadImageView extends LinearLayout{

    private ImageView mIvUpload;
    private TextView mTvUpload;

    public UploadImageView(Context context) {
        this(context, null);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.upload_imageview_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UploadImageView);
        mIvUpload = findViewById(R.id.iv_upload);
        setImageView(typedArray.getResourceId(R.styleable.UploadImageView_upload_iv, R.mipmap.ic_upload));

        mTvUpload = findViewById(R.id.tv_upload);
        setDesc(typedArray.getString(R.styleable.UploadImageView_upload_desc));

        typedArray.recycle();
    }

    /**
     * 设置图片
     * @param icon
     */
    public void setImageView(@DrawableRes int icon){
        mIvUpload.setImageResource(icon);
    }

    /**
     * 设置图片
     * @param url
     */
    public void setImageView(String url){
        CommonUtils.showImage(mIvUpload, url);
    }

    /**
     * 图片描述
     * @param str
     */
    public void setDesc(String str){
        if(TextUtils.isEmpty(str)) {
            mTvUpload.setVisibility(GONE);
        }else{
            mTvUpload.setVisibility(VISIBLE);
            mTvUpload.setText(str);
        }
    }

    /**
     * 获取描述
     * @return
     */
    public String getDesc(){
        return mTvUpload.getText().toString();
    }

}
