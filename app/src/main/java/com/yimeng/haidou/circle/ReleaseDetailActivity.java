package com.yimeng.haidou.circle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.circle.adapter.ParentChildDiscussAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ParentChildCircleDetail;
import com.yimeng.entity.ParentChildDiscussBean;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.sensitive.SensitiveFilter;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.StatusBarUtil;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/22 0022 下午 07:38.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 动态详情
 * </pre>
 */
public class ReleaseDetailActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.civ_user_head)
    CircleImageView civUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_discuss)
    TextView tvDiscuss;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.layout_images)
    LinearLayout layoutImages;
    @Bind(R.id.tv_discuss_num)
    TextView tvDiscussNum;
    @Bind(R.id.cb_praise_num)
    CheckBox cbPraiseNum;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.ll_article)
    LinearLayout ll_article;
    @Bind(R.id.iv_article_pic)
    ImageView iv_article_pic;
    @Bind(R.id.tv_article_title)
    TextView tv_article_title;
    @Bind(R.id.btn_send)
    Button btn_send;


    private String mShuoshuoNo;
    private ParentChildCircleDetail mCircleDetail;
    private List<ParentChildDiscussBean> mList;
    private ParentChildDiscussAdapter mAdatper;
    private int mPage = 1;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_release_detail;
    }

    @Override
    protected void init() {
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
        StatusBarUtil.StatusBarLightMode(this, false);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            ToastUtils.showToast("数据异常!");
            finish();
            return;
        }

        mShuoshuoNo = bundle.getString("shuoshuoNo");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdatper = new ParentChildDiscussAdapter(mList);
        mAdatper.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mAdatper);


    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onLeftClick() {
                super.onLeftClick();
                finish();
            }
        });
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpParameterUtil.getInstance().getParentChildDiscussList(mPage, mShuoshuoNo, mHandler);
            }
        });

    }

    @Override
    protected void loadData() {

        HttpParameterUtil.getInstance().getParentChildCircleDetail(mShuoshuoNo, mHandler);
        HttpParameterUtil.getInstance().getParentChildDiscussList(mPage, mShuoshuoNo, mHandler);

    }

    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3})
    public void onPicClick(View view) {
        if (mCircleDetail != null) {
            int position = 0;
            if (view.getId() == R.id.iv_1) {
                position = 0;
            } else if (view.getId() == R.id.iv_2) {
                position = 1;
            } else {
                position = 2;
            }

            String imageUrls = "";
            String[] split = mCircleDetail.getImages().split(",");
            if (split.length >= 1) {
                imageUrls = split[0];
                if (split.length >= 2) {
                    imageUrls += "," + split[1];
                    if (split.length >= 3) {
                        imageUrls += "," + split[2];
                    }
                }
            }
            ActivityUtils.getInstance().jumpPhotoActivity(imageUrls, position);

        }

    }

    /**
     * 查看转载内容详情
     */
    @OnClick(R.id.ll_article)
    public void lookArticleDetail() {
        if (mCircleDetail == null) return;
        ActivityUtils.getInstance().jumpH5Activity(mCircleDetail.getReprintedTitle(), mCircleDetail.getReprintedUrl());
    }

    @OnClick(R.id.tv_share)
    public void shareContent(){
        if(mCircleDetail == null) return;
        // 分享
        CommonUtils.shareApp(ConstantsUrl.URL_SHARE_CIRCLE_DETAIL + mCircleDetail.getShuoshuoNo());
    }

    @OnClick({R.id.cb_praise_num, R.id.btn_send})
    public void onClick(View v) {
        if (CommonUtils.checkLogin()) {
            switch (v.getId()) {
                case R.id.cb_praise_num:
                    // 点赞
                    HttpParameterUtil.getInstance().parentChildCircleStart(mShuoshuoNo, mHandler);
                    break;
                case R.id.btn_send:
                    // 评论
                    String content = etContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showToast("请输入评论内容!");
                        return;
                    }

                    SensitiveFilter filter = SensitiveFilter.DEFAULT;
                    String resultStr = filter.filter(content, '*');
                    if(!resultStr.equals(content)) {
                        SimpleDialog.showSimpleRemarkWithTitleDialog(this, "包含敏感词",
                                resultStr);
                        return;
                    }
                    btn_send.setEnabled(false);
                    HttpParameterUtil.getInstance().parentChildAddDiscuss(mShuoshuoNo, content, mHandler);
                    break;
                default:

            }
        } else {
            if(v.getId() == R.id.cb_praise_num) {
                ((CheckBox)v).setChecked(false);
            }
            ActivityUtils.getInstance().jumpLoginActivity();
        }
    }


    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DETAIL_SUCCESS:
                // 亲子圈详情
                setCircleDetail(msg);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DISCUSS_SUCCESS:
                // 评论列表
                smartRefresh.finishLoadMore();
                List<ParentChildDiscussBean> discussBeanList = (List<ParentChildDiscussBean>) msg.obj;
                if (mAdatper != null && mList != null && discussBeanList != null) {
                    if (mPage == 1 && !mList.isEmpty()) {
                        mList.clear();
                    }
                    if (discussBeanList.size() < Constants.MAX_LIMIT) {
                        smartRefresh.finishLoadMoreWithNoMoreData();
                    }

                    mPage++;
                    mList.addAll(discussBeanList);
                    mAdatper.notifyDataSetChanged();
                }
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_SUCCESS:
                // 点赞成功
                boolean memberFabulous = mCircleDetail.isMemberFabulous();
                String msgStr = (String) msg.obj;
                ToastUtils.showToast(TextUtils.isEmpty(msgStr) ? (memberFabulous ? "取消成功!" : "点赞成功!") : msgStr);

                int fiveNum = mCircleDetail.getGiveNum() + (memberFabulous ? -1 : 1);

                mCircleDetail.setGiveNum(fiveNum);
                mCircleDetail.setMemberFabulous(!memberFabulous);

                cbPraiseNum.setText(String.valueOf(fiveNum));
                cbPraiseNum.setChecked(!memberFabulous);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_SUCCESS:
                // 评论成功
                btn_send.setEnabled(true);
                String str = (String) msg.obj;
                ToastUtils.showToast(TextUtils.isEmpty(str) ? "评论成功!" : str);

                HttpParameterUtil.getInstance().getParentChildDiscussList(mPage = 1, mShuoshuoNo, mHandler);

                mCircleDetail.setCommentNum(mCircleDetail.getCommentNum() + 1);
                tvDiscussNum.setText(mCircleDetail.getCommentNum() + "");

                etContent.setText("");
                KeyboardUtils.hideKeyBoard(etContent);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DETAIL_FAIL:
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DISCUSS_FAIL:
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_FAIL:
                ToastUtils.showToast((String) msg.obj);
                btn_send.setEnabled(true);
                break;
            default:
                ToastUtils.showToast((String) msg.obj);
        }
    }

    /**
     * 设置详情
     */
    private void setCircleDetail(Message msg) {
        ParentChildCircleDetail circleDetail = (ParentChildCircleDetail) msg.obj;
        if (circleDetail == null) return;
        this.mCircleDetail = circleDetail;

        CommonUtils.showImage(civUserHead, circleDetail.getHeadPath());
        tvUserName.setText(circleDetail.getNickname());
        tvTime.setText(DateUtil.getAssignDate(circleDetail.getCreateTime(), 3));
        tvDiscuss.setText(circleDetail.getContent());

        if (circleDetail.getIsReprinted() == 1) {
            // 转载内容
            layoutImages.setVisibility(View.GONE);
            ll_article.setVisibility(View.VISIBLE);
            CommonUtils.showImage(iv_article_pic, circleDetail.getImages());
            tv_article_title.setText(circleDetail.getReprintedTitle());

        } else {
            String images = circleDetail.getImages();
            if (!TextUtils.isEmpty(images)) {
                String[] split = images.split(",");
                if (split.length >= 1) {
                    iv1.setVisibility(View.VISIBLE);
//                    CommonUtils.showRadiusImage(iv1, split[0], DeviceUtils.dp2px(this, 5), true, true, true, true);
                    CommonUtils.showImage(iv1, split[0]);
                    if (split.length >= 2) {
                        iv2.setVisibility(View.VISIBLE);
//                        CommonUtils.showRadiusImage(iv2, split[1], DeviceUtils.dp2px(this, 5), true, true, true, true);
                        CommonUtils.showImage(iv2, split[1]);
                        if (split.length >= 3) {
                            iv3.setVisibility(View.VISIBLE);
//                            CommonUtils.showRadiusImage(iv3, split[2], DeviceUtils.dp2px(this, 5), true, true, true, true);
                            CommonUtils.showImage(iv3, split[2]);
                        }
                    }
                }
            } else {
                layoutImages.setVisibility(View.GONE);
            }
        }


        tvDiscussNum.setText(String.valueOf(circleDetail.getCommentNum()));
        cbPraiseNum.setText(String.valueOf(circleDetail.getGiveNum()));
        cbPraiseNum.setChecked(circleDetail.isMemberFabulous());
    }

    private class MyHandler extends Handler {
        private WeakReference<ReleaseDetailActivity> mImpl;

        public MyHandler(ReleaseDetailActivity impl) {
            mImpl = new WeakReference<>(impl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

}
