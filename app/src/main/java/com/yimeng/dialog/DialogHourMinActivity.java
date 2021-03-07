package com.yimeng.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.widget.wheelview.WheelView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 时间选择(时 : 分)
 */
public class DialogHourMinActivity extends Activity {

    @Bind(R.id.id_hour)
    WheelView mViewHour;
    @Bind(R.id.id_min)
    WheelView mViewMin;
    @Bind(R.id.id_min1)
    WheelView mViewMin1;

    String[] mHours = new String[25];
    String[] mMins = new String[60];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_hour_min);
        ButterKnife.bind(this);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setUpData();
        setUpListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void setUpListener() {
        mViewHour.setOffset(1);
        mViewHour.setItems(Arrays.asList(mHours));
        mViewHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                LogUtil.w("onSelected", "selectedIndex: " + selectedIndex + ", item: " + item);
                if (TextUtils.equals(item, "24")) {
                    mViewMin1.setVisibility(View.VISIBLE);
                    mViewMin.setVisibility(View.GONE);
                } else {
                    mViewMin1.setVisibility(View.GONE);
                    mViewMin.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewMin.setOffset(1);
        mViewMin.setItems(Arrays.asList(mMins));
        mViewMin.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                LogUtil.d("分钟onSelected", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });

        String[] mins = new String[]{"00"};
        mViewMin1.setOffset(1);
        mViewMin1.setItems(Arrays.asList(mins));

//        LogUtil.e("----", mHours.length + "-------" + mMins.length + "-----" + Arrays.asList(mMins));
    }

    private void setUpData() {

        for (int i = 0; i < 25; i++) {
            mHours[i] = i < 10 ? "0" + i : i + "";
        }
        for (int i = 0; i < 60; i++) {
            mMins[i] = i < 10 ? "0" + i : i + "";
        }
    }

    @OnClick({R.id.tv_cancel, R.id.tv_comfirm})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_comfirm:
                String hour = mViewHour.getSeletedItem();
                String min = mViewMin.getSeletedItem();
                min = TextUtils.equals(hour, "24") ? "00" : min;
                Intent intent = new Intent();
                intent.putExtra("hour", hour);
                intent.putExtra("min", min);
                setResult(ConstantHandler.RESULT_CODE_COMMON_DATA, intent);
                finish();
                break;
            default:
                break;
        }
    }

}
