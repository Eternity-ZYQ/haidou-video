package com.yimeng.haidou.shop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * 大图显示
 *
 * @author xp
 * @describe 大图显示.
 * @date 2018/6/22.
 */

/**
 * 多张显示
 * <p>
 * Update by huiGer on 2018/11/5 0005
 */
public class PhotoViewsActivity extends Activity {
    ViewPager viewPager;
    private List<String> images = new ArrayList<>();
    private List<Integer> imageRess = new ArrayList<>();
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(this);
        setContentView(R.layout.activity_photos_view);
        initView();
        initData();
    }

    private void initView() {

        viewPager = findViewById(R.id.viewPager);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        mType = bundle.getInt("type", 1);
        int defaultPosition = bundle.getInt("defaultPosition", 0);
        if (mType == 1) {
            String img_url = bundle.getString("img_url");
            if (!TextUtils.isEmpty(img_url)) {
                if (img_url.contains(",")) {
                    Collections.addAll(images, img_url.split(","));
                } else {
                    images.add(img_url);
                }
            }
            defaultPosition = defaultPosition >= images.size() ? 0 : defaultPosition;
        } else {
            int[] img_urls = bundle.getIntArray("img_url");
            for (int i : img_urls) {
                imageRess.add(i);
            }
            defaultPosition = defaultPosition >= imageRess.size() ? 0 : defaultPosition;
        }



        viewPager.setAdapter(new PagerAdapter() {

            private LinkedList<View> mViewCache = new LinkedList<>();//缓存view

            @Override
            public int getCount() {
                if(mType == 1) {
                    return images.size();
                }else{
                   return imageRess.size();
                }
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
//                    return super.instantiateItem(container, position);
                View convertView = null;
                PhotoView photoView = null;
                if (mViewCache.size() == 0) {
                    photoView = new PhotoView(PhotoViewsActivity.this);
                    // 单击图片，返回
                    photoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    // 开启图片缩放功能
                    photoView.enable();
                    // 设置缩放类型
                    photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    // 设置最大缩放倍数
                    photoView.setMaxScale(2.5f);

                    convertView = photoView;
                } else {
                    convertView = mViewCache.removeFirst();
                }

                photoView = (PhotoView) convertView;
                if(mType == 1) {
                    // 加载图片
                    CommonUtils.showImage(photoView, images.get(position));
                }else{
                    Glide.with(PhotoViewsActivity.this).load(imageRess.get(position)).into(photoView);
                }
                container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return photoView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                    super.destroyItem(container, position, object);
                View contentView = (View) object;
                container.removeView(contentView);
                this.mViewCache.add(contentView);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        viewPager.setCurrentItem(defaultPosition);
    }


}
