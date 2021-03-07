/*
 * ************************************************
 * 文件：ArticleListAdapter.java
 * Author：huiGer
 * 当前修改时间：2019年04月27日 14:18:02
 * 上次修改时间：2019年04月27日 14:18:01
 *
 * Copyright (c) 2019.
 * ************************************************
 */

package com.yimeng.haidou.home.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ArticleBean;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/27 2:18 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 头条列表
 * </pre>
 */
public class ArticleListAdapter extends BaseMultiItemQuickAdapter<ArticleBean, BaseViewHolder> {

    /**
     * 头部数据
     */
    public static final int ARTICLE_TOP = 1;

    /**
     * 头部数据
     */
    public static final int ARTICLE_TOP_ONLY = 11;

    /**
     * 中间数据
     */
    public static final int ARTICLE_CENTER = 2;

    /**
     * 底部数据
     */
    public static final int ARTICLE_BOTTOM = 3;


    public ArticleListAdapter(List<ArticleBean> data) {
        super(data);
        addItemType(ARTICLE_TOP, R.layout.adapter_article_item_1);
        addItemType(ARTICLE_TOP_ONLY, R.layout.adapter_article_item_1_1);
        addItemType(ARTICLE_CENTER, R.layout.adapter_article_item_2);
        addItemType(ARTICLE_BOTTOM, R.layout.adapter_article_item_3);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {

        if(item.getItemType() == ARTICLE_TOP_ONLY) {
            // 只存在一条
            CommonUtils.showRadiusImage(helper.getView(R.id.iv_article_pic), item.getArticleImg(),
                    DeviceUtils.dp2px(mContext, 15), true, true, true, true);

        }else if(item.getItemType() == ARTICLE_TOP) {
            CommonUtils.showRadiusImage(helper.getView(R.id.iv_article_pic), item.getArticleImg(),
                    DeviceUtils.dp2px(mContext, 15), true, true, false, false);

        }else{
            CommonUtils.showImage(helper.getView(R.id.iv_article_pic), item.getArticleImg());
        }

        helper.setText(R.id.tv_article_title, item.getArticleTitle());

    }
}
