package com.yimeng.haidou.mine;

import android.text.TextUtils;
import android.widget.EditText;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.StringUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/16 0016 下午 02:26.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 意见反馈
 * </pre>
 */
public class FeedbackActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.feedbackTypeEditText)
    EditText feedbackTypeEditText;
    @Bind(R.id.feedbackContentEditText)
    EditText feedbackContentEditText;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        Map<String, String> parameterMap = new HashMap<String, String>();
        String token = CommonUtils.getToken();
        if (StringUtils.isEmpty(token)) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        parameterMap.put("token", token);

        //主题
        String feedbackType = feedbackTypeEditText.getText().toString();
        if(TextUtils.isEmpty(feedbackType)) {
            ToastUtils.showToast("请填写主题!");
            return;
        }
        parameterMap.put("feedbackType", feedbackType);

        //内容
        String feedbackContent = feedbackContentEditText.getText().toString();
        if(TextUtils.isEmpty(feedbackContent)) {
            ToastUtils.showToast("请填写内容!");
            return;
        }
        parameterMap.put("feedbackContent", feedbackContent);

        OkHttpCommon okHttpCommon = new OkHttpCommon();
        okHttpCommon.postLoadData(FeedbackActivity.this, ConstantsUrl.ADD_FEEDBACK_URL, parameterMap, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                    String msg = jsonObject.get("msg").isJsonNull() ? "添加反馈信息失败" : jsonObject.get("msg").getAsString();
                    ToastUtils.showToast(msg);
                    return;
                }

                //返回
                ToastUtils.showToast("提交成功！感谢你提的宝贵意见或建议");
                finish();
            }
        });
    }
}
