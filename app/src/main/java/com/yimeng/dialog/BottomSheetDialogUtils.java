package com.yimeng.dialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/29 0029 上午 11:24.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 通用BottomSheetDialog
 * </pre>
 */
public class BottomSheetDialogUtils {

    private final BottomSheetDialog mDialog;
    private final View mRootView;
    private final Context mContext;
    private BaseQuickAdapter mDefaultAdapter;

    public BottomSheetDialogUtils(@NonNull Context context) {
        mContext = context;
        mDialog = new BottomSheetDialog(mContext);
        mRootView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_layout, null);
        mDialog.setContentView(mRootView);
    }

    public BottomSheetDialogUtils setData(List<String> data) {
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView_city);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDefaultAdapter = new BaseQuickAdapter<String, BaseViewHolder>(
                R.layout.bottom_sheet_dialog_city_text_layout, data
        ) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_city, item)
                        .addOnClickListener(R.id.tv_city);
            }
        };
        recyclerView.setAdapter(mDefaultAdapter);
        return this;
    }

    public BottomSheetDialogUtils setData(BaseQuickAdapter adapter) {
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView_city);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDefaultAdapter = adapter;
        recyclerView.setAdapter(adapter);
        return this;
    }

    public BottomSheetDialogUtils setOnDialogItemClickListener(BaseQuickAdapter.OnItemChildClickListener listener) {
        mDefaultAdapter.setOnItemChildClickListener(listener);
        return this;
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public BottomSheetDialogUtils setOnClickListenerByViewId(@IdRes int id, final onDialogChildClickListener listener) {
        mRootView.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                mDialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 根据id返回该view
     *
     * @param id       id
     * @param callBack 返回就该view
     * @return BottomSheetDialogUtils
     */
    public BottomSheetDialogUtils setViewStyle(@IdRes int id, CustomViewCallBack callBack) {
        callBack.onCallBack(mRootView.findViewById(id));
        return this;
    }

    public interface onDialogChildClickListener {
        void onClick(View view);
    }

    public interface CustomViewCallBack {
        void onCallBack(View v);
    }
}
