package com.yimeng.haidou.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.Member;
import com.yimeng.haidou.R;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Author : huiGer
 * Time   : 2018/9/15 0015 下午 04:58.
 * Desc   : 添加代理
 */
public class RecomendJoinActivity extends BaseActivity {

    /**
     * 跳转到扫码
     */
    private final int JUMP_SCAN_TYPE = 0x11;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.layout_input)
    LinearLayout layoutInput;
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_user_mobile)
    TextView tvUserMobile;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.layout_user_info)
    ConstraintLayout layoutUserInfo;
    private Handler mHandle = new MyHandle(this);
    /**
     * 查询成功
     */
    private boolean isQuerySuccess = false;
    private OptionsPickerView<String> mPickerView;
    private List<String> mList;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_recomend_join;
    }

    @Override
    protected void init() {

        initPickerView();

    }

    /**
     * 要代理设置id
     */
    private String memberNo;

    private void initPickerView() {
        mPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                HttpParameterUtil.getInstance().setProxy(mHandle, memberNo, mList.get(options1));
            }
        })
                .setTitleText("请选择分润百分比")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        mList = Arrays.asList("40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50");
        mPickerView.setPicker(mList);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                ActivityUtils.getInstance().jumpActivityForResult(RecomendJoinActivity.this, ScanForResultActivity.class, JUMP_SCAN_TYPE, null);
            }
        });
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_submit,})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:   // next
                if (isQuerySuccess) {
                    // 查询成功
                    // 添加代理
                    mPickerView.show();
                } else {
                    String str = etInput.getText().toString().trim();
                    if (TextUtils.isEmpty(str)) {
                        ToastUtils.showToast("请输入账号");
                        return;
                    }
                    if (!CommonUtils.isMobile(str)) {
                        ToastUtils.showToast("手机号码格式错误");
                        return;
                    }
                    // 用户信息
                    HttpParameterUtil.getInstance().queryMemberByMobile(mHandle, str);
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JUMP_SCAN_TYPE && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("result");
            if (!TextUtils.isEmpty(result) && result.contains("memberNo")) {
                result = result.substring(result.indexOf("?"));
                String[] split = result.split("&");
                for (String s : split) {
                    if (s.contains("memberNo=")) {
                        String memberNo = s.split("=")[1];
                        Log.d("msg", "RecomendJoinActivity -> onActivityResult: memberNo====" + memberNo);
                        HttpParameterUtil.getInstance().queryMemberByMemberNo(mHandle, memberNo);
                        return;
                    }
                }
            }
        }
    }


    /**
     * 消息处理
     *
     * @param msg
     */
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_MEMBER_BY_MOBILE_SUCCESS:
                // 推荐查询成功
                isQuerySuccess = true;
                layoutUserInfo.setVisibility(View.VISIBLE);
                layoutInput.setVisibility(View.GONE);
                btnSubmit.setText("确定");
                Member userInfo = (Member) msg.obj;
                // 数据回填
                CommonUtils.showImage(ivUserHead, userInfo.getHeadPath());
                tvUserName.setText(userInfo.getMemberName());
                tvUserMobile.setText(userInfo.getMobileNo());
                tv.setVisibility(View.VISIBLE);
                tv.setText("确认账户信息。");
                memberNo = userInfo.getMemberNo();
                break;
            case ConstantHandler.WHAT_QUERY_MEMBER_BY_MOBILE_FAIL:
                isQuerySuccess = false;
                // 推荐入驻查询失败
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_SET_PROXY_SUCCESS:
                // 添加代理成功
                ToastUtils.showToast("添加成功!");
                setResult(RESULT_OK);
                finish();
                break;
            case ConstantHandler.WHAT_SET_PROXY_FAIL:
                // 添加代理失败
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_QUERY_MEMBER_BY_NO_SUCCESS:
                // 给店铺添加执行经理前,根据memberNo查询用户信息成功
                // 查询执行经理
                Member member = (Member) msg.obj;
                memberNo = member.getMemberNo();
                HttpParameterUtil.getInstance().queryMemberByMobile(mHandle, member.getMobileNo());
                break;
            case ConstantHandler.WHAT_QUERY_MEMBER_BY_NO_FAIL:
                // 给店铺添加执行经理前,根据memberNo查询用户信息失败
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
        }
    }

    private static class MyHandle extends Handler {
        private WeakReference<RecomendJoinActivity> mImpl;

        public MyHandle(RecomendJoinActivity mImpl) {
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
}
