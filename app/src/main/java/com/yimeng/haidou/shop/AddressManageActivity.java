package com.yimeng.haidou.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.AddressAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelAddress;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地址管理
 *
 * @author xp
 * @describe 地址管理.
 * @date 2017/10/19.
 */

public class AddressManageActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.ll_not_data)
    LinearLayout llNotData;
    @Bind(R.id.sdv_not_data_img)
    ImageView sdvNotDataImg;
    @Bind(R.id.tv_not_data_hint)
    TextView tvNotDataHint;

    private final int WHAT_HANDLER_CLICK = 0x01;
    private AddressAdapter mAdapter;
    private LinkedList<ModelAddress> mList;

    private String mFromActivity;// 来自哪个Activity
    private ModelAddress mModelAddress;// 当前选择的地址

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void init() {
        mFromActivity = getIntent().getStringExtra("activity");

        llNotData.setVisibility(View.GONE);
        sdvNotDataImg.setVisibility(View.GONE);
        tvNotDataHint.setText(R.string.empty_address_hint);

        mList = new LinkedList<>();
        mAdapter = new AddressAdapter(this, mHandler, mList, WHAT_HANDLER_CLICK);
        mListView.setAdapter(mAdapter);

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestAddressAll(mHandler);
            }
        });
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    /**
     * 添加地址
     */
    @OnClick(R.id.btn_add_address)
    public void addAddress(){
        ActivityUtils.getInstance().jumpActivity(UpdateAddressActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<AddressManageActivity> mImpl;

        public MyHandler(AddressManageActivity mImpl) {
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
            case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                // 所有地址
                mRefreshLayout.finishRefresh();
                mList = (LinkedList<ModelAddress>) msg.obj;
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getCount() > 0) {
                    llNotData.setVisibility(View.GONE);
                }
                break;
            case ConstantHandler.WHAT_ADDRESS_ALL_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case WHAT_HANDLER_CLICK:
                // 点击
                mModelAddress = (ModelAddress) msg.obj;
                switch (msg.arg1) {
                    case R.id.checkLL:
                        SimpleDialog.showLoadingHintDialog(this, 4);
                        String check = mModelAddress.getIsdefault().equals("1") ? "0" : "1";
                        mModelAddress.setIsdefault(check);
                        HttpParameterUtil.getInstance().requestAddressSave(mModelAddress.getAddressNo(),
                                mModelAddress.getLinkman(), mModelAddress.getMobileNo(), mModelAddress.getProvince(),
                                mModelAddress.getCity(), mModelAddress.getArea(), mModelAddress.getAddress(),
                                check, mHandler);
                        break;
                    case R.id.editClickTV:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mModelAddress", mModelAddress);
                        Intent intent = new Intent(this, UpdateAddressActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.delectClickTV:
                        SimpleDialog.showConfirmDialog(AddressManageActivity.this, null, "确定要删除？", null, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                // 删除地址
                                SimpleDialog.showLoadingHintDialog(AddressManageActivity.this, 4);
                                HttpParameterUtil.getInstance().requestAddressRemove(mModelAddress.getAddressNo(), mHandler);
                            }
                        });
                        break;
                    case R.id.ll_click:
                        mModelAddress = (ModelAddress) msg.obj;
//                        if (mFromActivity != null && mFromActivity.contains("PayNowActivity")) {
//                            Bundle dataBundle = new Bundle();
//                            dataBundle.putSerializable("mModelAddress", mModelAddress);
//                            Intent data = new Intent();
//                            data.putExtras(dataBundle);
//                            setResult(Constants.RESULT_CODE_COMMON_DATA, data);
//                            finish();
//                        } else if (mFromActivity != null && mFromActivity.contains("OrderSettlementActivity")) {
////                            Bundle dataBundle = new Bundle();
////                            dataBundle.putSerializable("mModelAddress", mModelAddress);
////                            Intent data = new Intent();
////                            data.putExtras(dataBundle);
////                            setResult(0x02, data);
////                            finish();
//                            EventBus.getDefault().post(mModelAddress, EventTags.PAY_SELECT_ADDRESS);
//                            finish();
//                        }

                        if(getIntent().getExtras() != null) {
                            if(getIntent().getExtras().getBoolean("isSelAddress")) {
                                EventBus.getDefault().post(mModelAddress, EventTags.PAY_SELECT_ADDRESS);
                                finish();
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ConstantHandler.WHAT_ADDRESS_REMOVE_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                mRefreshLayout.autoRefresh();
                break;
            case ConstantHandler.WHAT_ADDRESS_REMOVE_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_ADDRESS_SAVE_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (mAdapter.mList.get(i).getAddressNo().equals(mModelAddress.getAddressNo())) {
                        mAdapter.mList.get(i).setIsdefault(mModelAddress.getIsdefault());
                    } else {
                        mAdapter.mList.get(i).setIsdefault("0");
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_ADDRESS_SAVE_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

}
