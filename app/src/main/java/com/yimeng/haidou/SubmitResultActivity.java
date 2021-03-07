package com.yimeng.haidou;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.widget.MyToolBar;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/30 0030 下午 06:15.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 提交成功页面
 * </pre>
 */
public class SubmitResultActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.lookOrderTV)
    TextView lookOrderTV;
    @Bind(R.id.tv_desc)
    TextView tvDesc;

    /**
     * 1. 商城支付成功
     * 2. 充值成功
     * 3. 店铺提交成功
     * 4. 附近店铺支付成功
     */
    private int mType = 1;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getInt("type");
            if (mType == 1) {

            }else if(mType == 2) {
                lookOrderTV.setVisibility(View.GONE);
            }else if(mType == 3){
                toolBar.setTitle("店铺入驻");
                lookOrderTV.setText("随便逛逛");
                tvDesc.setText("您的店铺申请提交成功，预计1天内审核通过");
            }
        }

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.lookOrderTV})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.lookOrderTV:
                if(mType == 1) {
                    // 商城订单
//                    ActivityUtils.getInstance().jumpActivity(MallOrderActivity.class);
                    ActivityUtils.getInstance().jumpOrderActivity(1, 0);
                }else if(mType == 3) {
                    // 店铺提交成功
                    EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.home));
                    ActivityUtils.getInstance().jumpMainActivity();
                }else if(mType == 4) {
                    //附近店铺订单
//                    ActivityUtils.getInstance().jumpActivity(ShopOrderActivity.class);
                    ActivityUtils.getInstance().jumpOrderActivity(0, 0);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
