package com.yimeng.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yimeng.haidou.R;
import com.yimeng.entity.SystemMsgBean;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/25 10:33 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 首页系统提示
 * </pre>
 */
public class SystemTipDialog extends CenterPopupView {

    private ImageView mIv;
    private SystemMsgBean mSystemMsgBean;

    public SystemTipDialog(@NonNull Context context, SystemMsgBean msgBean) {
        super(context);
        this.mSystemMsgBean = msgBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.system_message_dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        mIv = findViewById(R.id.sys_message_image_iv);
        Glide.with(getContext()).load(CommonUtils.parseImageUrl(mSystemMsgBean.getLogo())).into(mIv);
        mIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSystemMsgBean != null) {
                    boolean canJump = mSystemMsgBean.getSort() == 1;
                    if (canJump) {
                        ActivityUtils.getInstance().jumpH5Activity(mSystemMsgBean.getName(), mSystemMsgBean.getIntroduction());
                    }
                    dismiss();
                }
            }
        });

        findViewById(R.id.sys_message_close_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

}
