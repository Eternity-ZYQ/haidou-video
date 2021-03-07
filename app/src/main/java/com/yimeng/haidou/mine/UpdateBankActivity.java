package com.yimeng.haidou.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.BottomSheetDialogUtils;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.AddressBean;
import com.yimeng.entity.ModelMemberBankcard;
import com.yimeng.haidou.R;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.ToastUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 添加，更新银行卡
 *
 * @author xp
 * @describe 添加，更新银行卡.
 * @date 2018/6/25.
 */

public class UpdateBankActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.accountNameET)
    EditText accountNameET;
    @Bind(R.id.bankET)
    EditText bankET;
    @Bind(R.id.bankAccountET)
    EditText bankAccountET;
    @Bind(R.id.submitBTN)
    Button submitBTN;
    @Bind(R.id.bank_province_rl)
    RelativeLayout bank_province_ll;
    @Bind(R.id.bank_province_et)
    EditText bank_province_et;
    @Bind(R.id.bank_city_rl)
    RelativeLayout bank_city_ll;
    @Bind(R.id.bank_city_et)
    EditText bank_city_et;
    @Bind(R.id.bank_name_rl)
    RelativeLayout bank_name_ll;
    @Bind(R.id.bank_name_et)
    EditText bank_name_et;
    @Bind(R.id.bank_sub_name_rl)
    RelativeLayout bank_sub_name_ll;
    @Bind(R.id.bank_sub_name_et)
    EditText bank_sub_name_et;
    @Bind(R.id.bankMobileET)
    EditText bankMobileET;

    /**
     * 用户银行卡
     */
    ModelMemberBankcard mModelMemberBankcard;
    private MyHandler mHandler = new MyHandler(this);

    /**
     * 地区选择插件
     */
    private ArrayList<AddressBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private OptionsPickerView pvOptions, cityOptions;
    private int pvIndex = -1;

    private OkHttpCommon mOkHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_bankcard;
    }

    @Override
    protected void init() {
        toolBar.setTitle("添加银行卡");
        Bundle bundle = getIntent().getExtras();
        mModelMemberBankcard = bundle == null ? null : (ModelMemberBankcard) bundle.get("mModelMemberBankcard");
        if (mModelMemberBankcard != null) {
            toolBar.setTitle(getString(R.string.change_bankcard));
            submitBTN.setText("确认修改");
            accountNameET.setText(mModelMemberBankcard.getBankcardName());
            String bankName = mModelMemberBankcard.getBankName();
            String[] nameList = bankName.split(",");
            if (nameList.length >= 3) {
                bank_province_et.setText(nameList[0]);
                bank_city_et.setText(nameList[1]);
                if (nameList.length == 4) {
                    bank_name_et.setText(nameList[2]);
                    bankET.setText(nameList[3]);
                } else {
                    bankET.setText(nameList[2]);
                }
            }
            bankAccountET.setText(mModelMemberBankcard.getBankcardNum());
        }

        // 监听手动输入银行支行
        bankET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    bank_sub_name_et.setText("");
                }
            }
        });

        mOkHttpCommon = new OkHttpCommon();
        initJsonData();
        initPickerView();
    }

    /**
     * 解析省市数据
     */
    private void initJsonData() {
        String JsonData = null;
        try {
            JsonData = CommonUtils.getLocationJsonData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (JsonData == null) {
            return;
        }

        ArrayList<AddressBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCity().get(c).getName();
                CityList.add(CityName);//添加城市
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
        }
    }

    /**
     * 城市滚轮
     */
    private void initPickerView() {
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                pvIndex = options1;
                cityOptions.setPicker(options2Items.get(pvIndex));
                bank_province_et.setText(options1Items.get(options1).getName());
            }
        })
                .setTitleText("请选择省份")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items);

        cityOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                bank_city_et.setText(options2Items.get(pvIndex).get(options1));
            }
        })
                .setTitleText("请选择所在城市")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
    }

    /**
     * Gson 解析
     *
     * @param result
     * @return
     */
    public ArrayList<AddressBean> parseData(String result) {
        ArrayList<AddressBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                AddressBean entity = gson.fromJson(data.optJSONObject(i).toString(), AddressBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.submitBTN, R.id.bank_province_rl, R.id.bank_city_rl, R.id.bank_name_rl, R.id.bank_sub_name_rl,
            R.id.bank_province_et, R.id.bank_city_et, R.id.bank_name_et, R.id.bank_sub_name_et})
    public void myOnClick(View view) {
        String accountName = accountNameET.getText().toString();
        String provinceName = bank_province_et.getText().toString();
        String cityName = bank_city_et.getText().toString();
        String bankName = bank_name_et.getText().toString();
        String subBankName = bank_sub_name_et.getText().toString();
        String bank = bankET.getText().toString();
        String bankAccount = bankAccountET.getText().toString();
        String bankMobile = bankMobileET.getText().toString();
        switch (view.getId()) {
            case R.id.bank_province_rl:
            case R.id.bank_province_et:
                KeyboardUtils.hideKeyBoard(view);
                pvOptions.show();
                break;
            case R.id.bank_city_rl:
            case R.id.bank_city_et:
                if (pvIndex == -1) {
                    ToastUtils.showToast("请先选择所在省份");
                    return;
                }
                cityOptions.show();
                break;
            case R.id.bank_name_rl:
            case R.id.bank_name_et: // 开户行
                checkBankCardNo();
                break;
            case R.id.bank_sub_name_rl:
            case R.id.bank_sub_name_et: // 开户支行
                getBankNameSub();
                break;
            case R.id.submitBTN:
                if (TextUtils.isEmpty(accountName)) {
                    ToastUtils.showToast(getString(R.string.input_account_name));
                    return;
                }
                if (provinceName.isEmpty()) {
                    ToastUtils.showToast("请选择所在省份");
                    return;
                }
                if (cityName.isEmpty()) {
                    ToastUtils.showToast("请选择所在城市");
                    return;
                }
                if (bankName.isEmpty()) {
                    ToastUtils.showToast("请选择开户银行");
                    return;
                }
                if (subBankName.isEmpty() && TextUtils.isEmpty(bank)) {
                    ToastUtils.showToast("请选择或手动输入支行/分行");
                    return;
                }
                if (TextUtils.isEmpty(bankAccount)) {
                    ToastUtils.showToast(getString(R.string.input_bank_account));
                    return;
                }
                if (!(bankAccount.length() >= 16 && bankAccount.length() <= 21)) {
                    ToastUtils.showToast(getString(R.string.bank_account_hint));
                    return;
                }

                if (TextUtils.isEmpty(bankMobile)) {
                    ToastUtils.showToast(getString(R.string.input_bank_mobile));
                    return;
                }
                if (bankMobile.length() < 11) {
                    ToastUtils.showToast("请输入正确手机号!");
                    return;
                }

                String fullBankName;
                if (!bank_sub_name_et.getText().toString().isEmpty()) {
                    fullBankName = provinceName + "," + cityName + "," + bankName + "," + subBankName;
                } else {
                    fullBankName = provinceName + "," + cityName + "," + bankName + "," + bankET.getText().toString().trim();
                }

                SimpleDialog.showLoadingHintDialog(this, 4);
                if (mModelMemberBankcard != null) {
                    HttpParameterUtil.getInstance().requestUpdateMemberBankcard(
                            mModelMemberBankcard.getBankcardNo(), bankAccount, fullBankName, accountName, bankMobile, mHandler);
                } else {
                    HttpParameterUtil.getInstance().requestAddMemberBankcard(
                            bankAccount, fullBankName, accountName, bankMobile, mHandler);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取所在地区的开户银行支行
     */
    private void getBankNameSub() {
        String province = bank_province_et.getText().toString();
        String city = bank_city_et.getText().toString();
        String bankName = bank_name_et.getText().toString();

        if (TextUtils.isEmpty(province)) {
            ToastUtils.showToast("请先选择所在省份");
            return;
        }

        if (TextUtils.isEmpty(city)) {
            ToastUtils.showToast("请先选择所在城市");
            return;
        }

        if (TextUtils.isEmpty(bankName)) {
            ToastUtils.showToast("请先选择开户银行");
            return;
        }
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("province", province);
        params.put("city", city);
        params.put("bankName", bankName);
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_ADD_CARD_SUB_BANK_NAME, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.not_bankname_sub);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.not_bankname_sub)));
                    return;
                }
                List<String> list = GsonUtils.getGson().fromJson(jsonObject.get("data").toString(), new TypeToken<List<String>>() {
                }.getType());
                showBottomSheetDialog(list);
            }
        });
    }

    /**
     * @param data 支行数据
     */
    private void showBottomSheetDialog(final List<String> data) {
        final BottomSheetDialogUtils dialogUtils = new BottomSheetDialogUtils(this);
        dialogUtils.setData(data)
                .setViewStyle(R.id.toolBar_city, new BottomSheetDialogUtils.CustomViewCallBack() {
                    @Override
                    public void onCallBack(View v) {
                        MyToolBar toolBar = (MyToolBar) v;
                        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
                            @Override
                            public void onLeftClick() {
                                dialogUtils.dismiss();
                            }
                        });
                    }
                })
                .setOnDialogItemClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        bank_sub_name_et.setText(data.get(position));
                        dialogUtils.dismiss();
                    }
                })
                .showDialog();
    }

    /**
     * 验证银行卡
     */
    private void checkBankCardNo() {
        String bankNo = bankAccountET.getText().toString();
        if (TextUtils.isEmpty(bankNo)) {
            ToastUtils.showToast("请输入正确卡号");
            return;
        }

        HashMap<String, String> params = new HashMap<>();//?_input_charset=utf-8&cardBinCheck=true&cardNo=
        params.put("_input_charset", "utf-8");
        params.put("cardBinCheck", "true");
        params.put("cardNo", bankNo);
        mOkHttpCommon.getLoadData(this, ConstantsUrl.CHECK_BANK_CARD, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                bank_name_et.setText("银行卡类型不匹配");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                try {
                    if (jsonObject.get("validated").getAsBoolean()) {
                        String bankName = CommonUtils.getBankZh(jsonObject.get("bank").getAsString());
                        bank_name_et.setText(bankName);
                    } else {
                        bank_name_et.setText("银行卡类型不匹配");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bank_name_et.setText("银行卡类型不匹配");
                }
            }
        });

    }


    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_UPDATE_MEMBER_BANKCARD_SUCCESS:
            case ConstantHandler.WHAT_ADD_MEMBER_BANKCARD_SUCCESS:
                ToastUtils.showToast((String) msg.obj);
                HttpParameterUtil.getInstance().requestMemberBankcard(mHandler);
                break;
            case ConstantHandler.WHAT_ADD_MEMBER_BANKCARD_FAIL:
            case ConstantHandler.WHAT_UPDATE_MEMBER_BANKCARD_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_MEMBER_BANKCARD_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                LinkedList<ModelMemberBankcard> mList = (LinkedList<ModelMemberBankcard>) msg.obj;
                if (mList.size() > 0) {
                    mModelMemberBankcard = mList.getFirst();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mModelMemberBankcard", mModelMemberBankcard);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case ConstantHandler.WHAT_MEMBER_BANKCARD_FAIL:
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<UpdateBankActivity> mImpl;

        public MyHandler(UpdateBankActivity mImpl) {
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
