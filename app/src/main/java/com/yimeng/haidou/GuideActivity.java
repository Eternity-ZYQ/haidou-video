package com.yimeng.haidou;

import android.widget.ImageView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.utils.ActivityUtils;
import com.huige.library.utils.SharedPreferencesUtils;

import butterknife.Bind;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/13 0013 下午 05:28.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 引导页
 * </pre>
 */
public class GuideActivity extends BaseActivity {


    @Bind(R.id.banner)
    BGABanner banner;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        banner.setData(localImageSize, ImageView.ScaleType.FIT_XY, R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3);
    }

    @Override
    protected void initListener() {
        banner.setEnterSkipViewIdAndDelegate(R.id.tv_enter, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                SharedPreferencesUtils.put(Constants.APP_IS_FIRST, true);
                ActivityUtils.getInstance().jumpActivity(MainActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
