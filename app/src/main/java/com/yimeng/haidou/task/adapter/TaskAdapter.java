package com.yimeng.haidou.task.adapter;

import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.TaskItemBean;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/23 0023 下午 05:54.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务适配器
 * </pre>
 */
public class TaskAdapter extends BaseMultiItemQuickAdapter<TaskItemBean, BaseViewHolder> {

    /**
     * 未开始
     */
    public static final int ITEM_NO_START = 0;
    /**
     * 已完成
     */
    public static final int ITEM_COMPLETE = 1;
    /**
     * 进度条
     */
    public static final int ITEM_WAIT = 2;

    /**
     * 每日任务item
     */
    public static final int ITEM_DAILY = 3;

    /**
     * 体验任务
     */
    public static final int ITEM_EXP = 4;

    /**
     * 推荐任务
     */
    public static final int ITEM_RECOMMEND = 5;

    /**
     * 下载任务
     */
    public static final int ITEM_DOWNLOAD = 6;

    public TaskAdapter(List<TaskItemBean> data) {
        super(data);
        addItemType(ITEM_NO_START, R.layout.task_item_no_start);
        addItemType(ITEM_COMPLETE, R.layout.task_item_complete);
        addItemType(ITEM_WAIT, R.layout.task_item_wait);
        addItemType(ITEM_DAILY, R.layout.task_item_daily);
        addItemType(ITEM_EXP, R.layout.task_item_exp);
        addItemType(ITEM_RECOMMEND, R.layout.task_item_recommend);
        addItemType(ITEM_DOWNLOAD, R.layout.task_item_download);
    }

    @Override
    protected void convert(final BaseViewHolder helper, TaskItemBean taskItemBean) {
        switch (taskItemBean.getItemType()) {
            case ITEM_NO_START:     // 未开始
                helper
//                        .setText(R.id.tv_task_name, taskItemBean.getShopName())
                        .setText(R.id.tv_task_name, "任意购买一件促销商品")
                        .addOnClickListener(R.id.btn);
                break;
            case ITEM_COMPLETE:     // 已完成
//                helper.setText(R.id.tv_task_name, taskItemBean.getShopName());
                helper.setText(R.id.tv_task_name, "任意购买一件促销商品");
                break;
            case ITEM_WAIT:         // 进度条
                helper.setText(R.id.tv_task_name, taskItemBean.getTitle());
                double hasRecommend = taskItemBean.getHasRecommend();
                double lowRecommend = taskItemBean.getLowRecommend();
                ProgressBar progressBar = helper.getView(R.id.progressBar);
                int limit = (int) ((hasRecommend / lowRecommend) * 100);
                progressBar.setProgress(limit);
                helper.setText(R.id.tv_loading, (int) hasRecommend + "/" + (int) lowRecommend);
                if(taskItemBean.getTitle().contains("销售")) {
                    helper.setVisible(R.id.btn_share, true)
                            .addOnClickListener(R.id.btn_share);
                }
                break;
            case ITEM_RECOMMEND:    //推荐任务
                hasRecommend = taskItemBean.getHasRecommend();
                lowRecommend = taskItemBean.getLowRecommend();
                helper.setText(R.id.tv_loading, (int) hasRecommend + "/" + (int) lowRecommend);
                helper.setText(R.id.tv_task_name, "自己开店后，推荐1人注册，好友完成商家任务");
                helper
                        .setText(R.id.btn, "立即邀请")
                        .addOnClickListener(R.id.btn);

                break;
            case ITEM_DAILY:        // 每日任务
                String btnStr = "";
                if (taskItemBean.getAdType().equals("share")) {
                    // 分享任务
                    btnStr = "立即邀请";
                } else if (taskItemBean.getAdType().equals("download")) {
                    // 下载任务
                    btnStr = "立即下载";
                } else if (taskItemBean.getAdType().equals("moments")) {
                    btnStr = "晒圈赚分";
                }else if(taskItemBean.getAdType().equals("cps")) {
                    btnStr = "分享赚钱";
                }
                helper.setText(R.id.tv_task_name, taskItemBean.getTitle())
                        .setText(R.id.tv_task_desc, taskItemBean.getDescription())
                        .setText(R.id.btn, btnStr)
                        .addOnClickListener(R.id.btn);
                break;
            case ITEM_EXP:      // 体验任务
                if (taskItemBean.getComplete() == 1) {
                    btnStr = "查看";
                } else {
                    btnStr = "去完成";
                }
                helper.setText(R.id.tv_task_name, taskItemBean.getTitle())
                        .setText(R.id.btn, btnStr)
                        .addOnClickListener(R.id.btn);
                break;
            case ITEM_DOWNLOAD:
                CommonUtils.showRadiusImage(helper.getView(R.id.iv),
                        taskItemBean.getTitle(), R.mipmap.loading_place_h,
                        DeviceUtils.dp2px(mContext, 10), true, true,
                        true, true);
                break;
            default:

        }
    }
}
