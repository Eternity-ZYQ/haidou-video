package com.yimeng.haidou.circle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.circle.adapter.FriendCircleAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ParentChildCircleDetail;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/2 0002 δΈε 05:42.
 *  Email  : zhihuiemail@163.com
 *  Desc   :    εε
 * </pre>
 */
public class TabCircleFragment extends BaseFragment {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private FriendCircleAdapter mAdapter;
    private List<ParentChildCircleDetail> mData;
    private int mPage = 1;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * ηΉθ΅ηδΈζ 
     */
    private int startIndex = 0;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_circle;
    }

    @Override
    protected void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mAdapter = new FriendCircleAdapter(mData);
        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.getItemAnimator().setChangeDuration(0);

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {

            @Override
            public void onRightClick() {
                // εεΈ
                if (CommonUtils.checkLogin()) {
                    // εεΈ
                    startActivity(new Intent(getActivity(), ReleaseActivity.class));
                } else {
                    ActivityUtils.getInstance().jumpLoginActivity();
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ParentChildCircleDetail circleDetail = mData.get(position);
                if (circleDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("shuoshuoNo", circleDetail.getShuoshuoNo());
                    Intent intent = new Intent(getActivity(), ReleaseDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
               final ParentChildCircleDetail circleDetail = mData.get(position);
                if (view.getId() == R.id.cb_praise_num) {
                    if (CommonUtils.checkLogin()) {
                        // ηΉθ΅
                        startIndex = position;
                        HttpParameterUtil.getInstance().parentChildCircleStart(circleDetail.getShuoshuoNo(), mHandler);
                    } else {
                        ((CheckBox)view).setChecked(false);
                        ActivityUtils.getInstance().jumpLoginActivity();
                    }
                } else if (view.getId() == R.id.ll_article && circleDetail.getIsReprinted() == 1) {
                    // θ½¬θ½½ζη« ηΉε»
                    ActivityUtils.getInstance().jumpH5Activity(circleDetail.getReprintedTitle(), circleDetail.getReprintedUrl());
                } else if (view.getId() == R.id.tv_del) {
                    // ε ι€ε¨ζ
                    SimpleDialog.showConfirmDialog(getActivity(), null, "η‘?ε?θ¦ε ι€εοΌ", null, new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            del(circleDetail.getShuoshuoNo(), position);
                        }
                    });
                }else if(view.getId() == R.id.tv_share) {
                    // εδΊ«
                    CommonUtils.shareApp(ConstantsUrl.URL_SHARE_CIRCLE_DETAIL + circleDetail.getShuoshuoNo());
                }else if(view.getId() == R.id.layout_images) {
                    // ηΉε»ζ₯ηε€§εΎ
                    ActivityUtils.getInstance().jumpPhotoActivity(circleDetail.getImages(), 0);
                }
            }
        });

        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                HttpParameterUtil.getInstance().requestGetParentChildCircleList(mPage, mHandler);
                getData(mPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                HttpParameterUtil.getInstance().requestGetParentChildCircleList(mPage = 1, mHandler);
                getData(mPage = 1);
            }
        });
    }

    /**
     * ε ι€ε¨ζ
     *
     * @param no
     */
    private void del(String no, final int position) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("shuoshuoNo", no);
        new OkHttpCommon().postLoadData(getActivity(), ConstantsUrl.PARENT_CHILD_DEL_CIRCLE_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("ε ι€ε€±θ΄₯οΌθ―·η¨ειθ―οΌ");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "ε ι€ε€±θ΄₯οΌθ―·η¨ειθ―οΌ"));
                    return;
                }

                ToastUtils.showToast("ε ι€ζε");
                mData.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void getData(int page) {
        HttpParameterUtil.getInstance().requestGetParentChildCircleList(page, mHandler);
    }

    @Override
    protected void loadData() {
        getData(mPage = 1);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        smartRefresh.autoRefresh(500);
//    }

    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_SUCCESS: // ε·ζ°ζε
                smartRefresh.finishRefresh();
                List<ParentChildCircleDetail> list = (List<ParentChildCircleDetail>) msg.obj;
                if (list == null || list.isEmpty()) {
                    return;
                }
                if (list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                if (!mData.isEmpty()) {
                    mData.clear();
                }
                mPage++;
                mData.addAll(list);
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_FAIL:// ε·ζ°ε€±θ΄₯
                ToastUtils.showToast((String) msg.obj);
                smartRefresh.finishRefresh(false);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_SUCCESS: // ε θ½½ζε
                smartRefresh.finishLoadMore();
                list = (List<ParentChildCircleDetail>) msg.obj;
                if (list == null || list.isEmpty()) {
                    return;
                }
                if (list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mPage++;
                mData.addAll(list);
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_FAIL:// ε θ½½ε€±θ΄₯
                ToastUtils.showToast((String) msg.obj);
                smartRefresh.finishLoadMore(false);
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_SUCCESS: // ηΉθ΅ζε
                if (mAdapter != null && mData != null && !mData.isEmpty()) {
                    ParentChildCircleDetail circleDetail = mData.get(startIndex);
                    boolean memberFabulous = circleDetail.isMemberFabulous();
                    String msgStr = (String) msg.obj;
                    ToastUtils.showToast(TextUtils.isEmpty(msgStr) ? (memberFabulous ? "εζΆζε!" : "ηΉθ΅ζε!") : msgStr);
                    circleDetail.setGiveNum(circleDetail.getGiveNum() + (memberFabulous ? -1 : 1));
                    circleDetail.setMemberFabulous(!memberFabulous);
                    mAdapter.notifyItemChanged(startIndex);
                }
                break;
            case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            default:

        }
    }

    private class MyHandler extends Handler {
        private WeakReference<TabCircleFragment> mImpl;

        public MyHandler(TabCircleFragment impl) {
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
