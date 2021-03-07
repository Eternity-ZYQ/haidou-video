package com.yimeng.haidou.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelMemberBankcard;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.BankCardAdapter;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * 银行卡列表
 *
 * @author xp
 * @describe 银行卡列表.
 * @date 2018/6/29.
 */

public class BankListActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private final int WHAT_HANDLER_CLICK = 0x01;
    private final int WHAT_HANDLER_BANKCARD_REMOVE = 0x02;
    private BankCardAdapter mAdapter;
    private LinkedList<ModelMemberBankcard> mList;

    private ModelMemberBankcard mModelMemberBankcard;// 当前选择的地址


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_banklist;
    }

    @Override
    protected void init() {
        mList = new LinkedList<>();
        mAdapter = new BankCardAdapter(this, mHandler, mList, WHAT_HANDLER_CLICK);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                // 判断是否实名认证
                if (!CommonUtils.checkAuth()) { // 未实名认证弹框提示

                    final AlertDialog.Builder builder = new AlertDialog.Builder(BankListActivity.this);
                    builder.setMessage("请完成实名认证");
                    builder.setCancelable(true);
                    builder.setPositiveButton("认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                    return;
                }
                ActivityUtils.getInstance().jumpActivity(UpdateBankActivity.class);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestMemberBankcard(mHandler);
            }
        });
    }

    @Override
    protected void loadData() {
        mRefreshLayout.autoRefresh();

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<BankListActivity> mImpl;

        public MyHandler(BankListActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_MEMBER_BANKCARD_SUCCESS:
                mRefreshLayout.finishRefresh();
                mList = (LinkedList<ModelMemberBankcard>) msg.obj;
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_MEMBER_BANKCARD_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case WHAT_HANDLER_CLICK:
                // 点击
                mModelMemberBankcard = (ModelMemberBankcard) msg.obj;
                switch (msg.arg1) {
                    case R.id.editBtn:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mModelMemberBankcard", mModelMemberBankcard);
                        Intent intent = new Intent(this, UpdateBankActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.delBtn:
                        SimpleDialog.showConfirmDialog(BankListActivity.this, null, "确定要删除该银行卡吗?", null, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                SimpleDialog.showLoadingHintDialog(BankListActivity.this, 4);
                                HttpParameterUtil.getInstance().requestDelMemberBankcard(mModelMemberBankcard.getBankcardNo(), mHandler);
                            }
                        });
                        break;
                    case R.id.clickRL:
                        bundle = new Bundle();
                        bundle.putSerializable("mModelMemberBankcard", mModelMemberBankcard);
                        intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        break;
                }
                break;
            case ConstantHandler.WHAT_DEL_WITHDRAW_APPLY_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                mRefreshLayout.autoRefresh();
                break;
            case ConstantHandler.WHAT_DEL_WITHDRAW_APPLY_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String)msg.obj);
                break;
            default:
                break;
        }
    }

}