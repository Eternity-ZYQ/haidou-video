package com.yimeng.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.XJConfig;
import com.yimeng.entity.AddressBean;
import com.yimeng.entity.Member;
import com.yimeng.entity.ProvinceBean;
import com.yimeng.interfaces.OnAddressIDPickerListener;
import com.yimeng.interfaces.OnAddressPickerListener;
import com.yimeng.interfaces.onBitmapGetColorListener;
import com.yimeng.net.NetComment;
import com.yimeng.widget.InputItemLayout;
import com.yimeng.haidou.App;
import com.yimeng.haidou.R;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.yanzhenjie.permission.runtime.PermissionRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * Author : huiGer
 * Time   : 2018/8/10 0010 下午 05:14.
 * Desc   :
 */
public class CommonUtils {


    /**
     * 地区选择器
     */
    private static OptionsPickerView pickerView = null;
    private static OptionsPickerView pickerAddressView = null;


    /**
     * 根据银行中文名字, 取英文简写
     *
     * @param bankZh
     * @return
     */
    public static String getBankEn(String bankZh) {
        try {
            Map<String, String> map = GsonUtils.getGson().fromJson(getAssetsString("bankName.txt"), Map.class);
            for (String key : map.keySet()) {
                if (map.get(key).equals(bankZh)) {
                    return key;
                }
            }
            return bankZh;
        } catch (Exception e) {
            e.printStackTrace();
            return bankZh;
        }
    }

    /**
     * 读取assets文件内容
     *
     * @return
     */
    public static String getAssetsString(String fileName) {
        InputStream is = null;
        String msg = null;
        try {
            is = App.getContext().getResources().getAssets().open(fileName);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            msg = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    /**
     * 根据银行英文简写, 取中文名字
     *
     * @return
     */
    public static String getBankZh(String bankEn) {
        String bankName = "";
        try {
            JSONObject bankNameJson = new JSONObject(getAssetsString("bankName.txt"));
            bankName = bankNameJson.getString(bankEn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bankName;
    }

    /**
     * 获取图片主色调
     *
     * @param bitmap
     * @return
     */
    public static void getBitmapColor(final Bitmap bitmap, final onBitmapGetColorListener listener) {

        Palette.from(bitmap).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                List<Palette.Swatch> list = palette.getSwatches();
                int colorSize = 0;
                Palette.Swatch maxSwatch = null;
                for (int i = 0; i < list.size(); i++) {
                    Palette.Swatch swatch = list.get(i);
                    if (swatch != null) {
                        int population = swatch.getPopulation();
                        if (colorSize < population) {
                            colorSize = population;
                            maxSwatch = swatch;
                        }
                    }
                }
                if (maxSwatch != null) {
                    listener.getColor(maxSwatch.getRgb());
                }
//                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
//                listener.getColor(vibrantSwatch.getRgb());
            }
        });
    }

    /**
     * 显示图片
     *
     * @param iv
     * @param imgUrl
     */
    public static void showImage(ImageView iv, String imgUrl) {
        showImage(iv, imgUrl, -1);
    }

    /**
     * 显示图片
     *
     * @param iv
     * @param imgUrl
     */
    public static void showImage(ImageView iv, String imgUrl, int errorIcon) {
        RequestBuilder<Drawable> builder = Glide.with(App.getContext())
                .load(parseImageUrl(imgUrl));
        if (errorIcon != -1) {
            builder.apply(new RequestOptions().error(errorIcon));
        }
        builder.transition(GenericTransitionOptions.with(R.anim.show_image_anim));
        builder.apply(new RequestOptions().placeholder(R.mipmap.xj_logo));
//        builder.transition(new DrawableTransitionOptions().crossFade());
        builder.into(iv);
    }

    /**
     * 处理图片地址
     */
    public static String parseImageUrl(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.contains(",")) {
            String[] paths = url.split(",");
            url = paths[0];
        }
        // 不是http开头,
        if (!TextUtils.isEmpty(url) && !url.startsWith("http") && (url.startsWith("/fileupload") || url.contains("/image"))) {
            url = ConstantsUrl.UPLOAD_HEAD_URL + url;
        }
        return url;
    }

    /**
     * 显示圆角图片
     *
     * @param iv     图片控件
     * @param url    图片地址
     * @param radius 圆角
     */
    public static void showRadiusImage(ImageView iv, String url, int radius, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
//        showRadiusImage(iv, url, R.mipmap.loading_place, radius, leftTop, rightTop, leftBottom, rightBottom);
        showRadiusImage(iv, url, R.mipmap.xj_logo, radius, leftTop, rightTop, leftBottom, rightBottom);
    }

    /**
     * 显示圆角图片
     *
     * @param iv     图片控件
     * @param url    图片地址
     * @param radius 圆角
     */
    public static void showRadiusImage(ImageView iv, String url, int errorIcon, int radius, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        CornerTransform cornerTransform = new CornerTransform(App.getContext(), radius);
        cornerTransform.setExceptCorner(leftTop, rightTop, leftBottom, rightBottom);
        RequestBuilder<Drawable> requestBuilder = Glide.with(App.getContext())
                .load(parseImageUrl(url));
        if (errorIcon != -1) {
            requestBuilder.apply(RequestOptions.errorOf(errorIcon));
        }
        requestBuilder
                .apply(RequestOptions.bitmapTransform(cornerTransform))
//                .apply(RequestOptions.placeholderOf(R.mipmap.loading_place))
//                .apply(RequestOptions.placeholderOf(R.mipmap.xj_logo))
                .into(iv);
    }

    /**
     * 显示毛玻璃效果图片
     *
     * @param iv
     * @param url
     */
    public static void showBlurImage(ImageView iv, String url, float blurRadius) {
        Glide.with(App.getContext())
                .load(parseImageUrl(url))
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(App.getContext(), blurRadius)))
                .into(iv);
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 倒计时
     *
     * @param time 时长
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int finalTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        return finalTime - aLong.intValue();
                    }
                })
                .take(finalTime + 1);
    }

    /**
     * 获取手机设备号
     */
    public static Observable<String> getIMEI(final Context context) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<String> e) throws Exception {
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

                LogUtils.v("REQUEST_READ_PHONE_STATE:" + permissionCheck);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    getPermission(context, new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                            @SuppressLint("MissingPermission") String bindNo = telephonyManager.getDeviceId();
                            SharedPreferencesUtils.put(Constants.MOBILE_ID, bindNo);
                            e.onNext(bindNo);
                        }
                    }, Manifest.permission.READ_PHONE_STATE);
                } else {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                    String bindNo = telephonyManager.getDeviceId();
                    if (bindNo == null) {
                        //android.provider.Settings;
                        bindNo = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                    SharedPreferencesUtils.put(Constants.MOBILE_ID, bindNo);
                    e.onNext(bindNo);
                }

            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取权限
     *
     * @param ctx
     * @param granted
     * @param permissions
     */
    public static void getPermission(final Context ctx, Action<List<String>> granted, final String... permissions) {
        getPermission(ctx, granted, null, permissions);
    }

    public static void getPermission(final Context ctx, Action<List<String>> granted,
                                     Action<List<String>> denied, final String... permissions) {
        PermissionRequest permissionRequest = AndPermission.with(ctx)
                .runtime()
                .permission(permissions)
                .onGranted(granted);
        if (denied == null) {
            permissionRequest.onDenied(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    if (AndPermission.hasAlwaysDeniedPermission(ctx, data)) {
                        showSettingDialog(ctx, data);
                    }
                }
            });
        } else {
            permissionRequest.onDenied(denied);
        }
        permissionRequest.start();
    }

    /**
     * Display setting dialog.
     */
    private static void showSettingDialog(final Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    public static void setPermission(final Context ctx) {
        AndPermission.with(ctx)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        ToastUtils.showToast("授权失败!");
                    }
                })
                .start();
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getCellphoneKey() {
        TelephonyManager tm = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取城市选择期
     *
     * @param context  上下文
     * @param listener 选择返回
     * @return 选择器
     */
    @SuppressLint("CheckResult")
    public static OptionsPickerView getAddressPicker(Context context, final OnAddressPickerListener listener) {

        try {
            final List<AddressBean> options1Items = new ArrayList<>();
            final List<List<String>> options2Items = new ArrayList<>();
            final List<List<List<String>>> options3Items = new ArrayList<>();

            pickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    listener.onResult(v, options1Items.get(options1).getName(),
                            options2Items.get(options1).get(options2),
                            options3Items.get(options1).get(options2).get(options3));
                }
            })
                    .setTitleText("城市选择")
                    .build();

            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                    e.onNext(CommonUtils.getLocationJsonData());
                }
            }).map(new Function<String, ArrayList<AddressBean>>() {
                @Override
                public ArrayList<AddressBean> apply(@NonNull String s) throws Exception {
                    return GsonUtils.getGson().fromJson(s, new TypeToken<ArrayList<AddressBean>>() {
                    }.getType());
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<AddressBean>>() {
                        @Override
                        public void accept(List<AddressBean> addressBeen) throws Exception {
                            options1Items.addAll(addressBeen);
                            for (int i = 0; i < addressBeen.size(); i++) {//遍历省份
                                List<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                                List<List<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
                                for (int c = 0; c < addressBeen.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                                    String CityName = addressBeen.get(i).getCity().get(c).getName();
                                    CityList.add(CityName);//添加城市
                                    List<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                                    if (addressBeen.get(i).getCity().get(c).getArea() == null
                                            || addressBeen.get(i).getCity().get(c).getArea().size() == 0) {
                                        City_AreaList.add("");
                                    } else {
                                        City_AreaList.addAll(addressBeen.get(i).getCity().get(c).getArea());
                                    }
                                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                                }

                                /**
                                 * 添加城市数据
                                 */
                                options2Items.add(CityList);

                                /**
                                 * 添加地区数据
                                 */
                                options3Items.add(Province_AreaList);
                            }

                            pickerView.setPicker(options1Items, options2Items, options3Items);
                        }
                    });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return pickerView;
    }

    /**
     * 获取json字符串
     *
     * @return
     */
    public static String getLocationJsonData() throws IOException {
        File file = new File(App.getContext().getExternalFilesDir(null), "province.txt");
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        String result = new String(bytes);
        fis.close();
        return result;
    }

    /**
     * 获取城市选择期带ID
     *
     * @param context  上下文
     * @param listener 选择返回
     * @return 选择器
     */
    @SuppressLint("CheckResult")
    public static OptionsPickerView getAddressPicker(Context context, final OnAddressIDPickerListener listener) {

        try {
            // 地区数据
            final List<ProvinceBean> provinceList = new ArrayList<>();
            final List<List<ProvinceBean.CitysBean>> cityList = new ArrayList<>();
            final List<List<List<ProvinceBean.CitysBean.DistrictsBean>>> districtsList = new ArrayList<>();

            pickerAddressView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    listener.onResult(v, provinceList.get(options1),
                            cityList.get(options1).get(options2),
                            districtsList.get(options1).get(options2).get(options3));
                }
            })
                    .setTitleText("城市选择")
                    .build();

            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                    e.onNext((String) SharedPreferencesUtils.get(Constants.APP_SJMM_LOCATION_INFO, ""));
                }
            }).map(new Function<String, ArrayList<ProvinceBean>>() {
                @Override
                public ArrayList<ProvinceBean> apply(@NonNull String s) throws Exception {
                    return GsonUtils.getGson().fromJson(s, new TypeToken<ArrayList<ProvinceBean>>() {
                    }.getType());
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ProvinceBean>>() {
                        @Override
                        public void accept(List<ProvinceBean> addressBeen) throws Exception {
                            if (!provinceList.isEmpty()) provinceList.clear();
                            provinceList.addAll(addressBeen);
                            for (ProvinceBean provinceBean : provinceList) {
                                List<ProvinceBean.CitysBean> citys = provinceBean.getCitys();
                                List<List<ProvinceBean.CitysBean.DistrictsBean>> districts = new ArrayList<>();

                                for (ProvinceBean.CitysBean citysBean : citys) {
                                    districts.add(citysBean.getDistricts());
                                }
                                cityList.add(citys);
                                districtsList.add(districts);
                            }

                            pickerAddressView.setPicker(provinceList, cityList, districtsList);

                        }
                    });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return pickerAddressView;
    }

    /**
     * 缓存用户信息
     *
     * @param member
     */
    public static void saveMember(Member member) {
        SharedPreferencesUtils.put(Constants.USER_TOKEN, member.getToken());
        SharedPreferencesUtils.put(Constants.USER_HEAD_PATH, member.getHeadPath());
        SharedPreferencesUtils.put(Constants.USER_MOBILE, member.getMobileNo());
        SharedPreferencesUtils.put(Constants.MOBILE_ID, member.getBindNo());
        SharedPreferencesUtils.put(Constants.USER_AUTH_STATUS, member.getNameAuthFlag());
    }

    /**
     * 清除用户信息
     */
    public static void cleanMember() {
        SharedPreferencesUtils.put(Constants.USER_INFO, "");
        SharedPreferencesUtils.put(Constants.USER_TOKEN, "");
        SharedPreferencesUtils.put(Constants.USER_HEAD_PATH, "");
        SharedPreferencesUtils.put(Constants.USER_MOBILE, "");
        SharedPreferencesUtils.put(Constants.MOBILE_ID, "");
        SharedPreferencesUtils.put(Constants.IS_LOGIN, false);
        SharedPreferencesUtils.put(Constants.USER_AUTH_STATUS, "");
        SharedPreferencesUtils.put(Constants.MINE_SHOP_APPLY_LOADING, false);
        SharedPreferencesUtils.put(Constants.TASK_DIALOG_STATUS, false);
        SharedPreferencesUtils.remove(Constants.TASK_NEW_IS_ACTIVATE);
        SPUtils.getInstance().remove(XJConfig.LAST_SHOW_ACTIVATE);
    }

    /**
     * @return 用户实名状况
     */
    public static boolean checkAuth() {
        return getMember().getNameAuthFlag().equals("1");
    }

    /**
     * 返回用户信息
     *
     * @return
     */
    public static Member getMember() {
        String userInfo = (String) SharedPreferencesUtils.get(Constants.USER_INFO, "");
        if (TextUtils.isEmpty(userInfo)) return null;
        return GsonUtils.getGson().fromJson(userInfo, Member.class);
    }

    /**
     * @return 是否开店
     */
    public static boolean checkOpenShop() {
        return getMember().getJob().equals("open");
    }

    /**
     * @return 获取全部通讯录列表
     */
    public static Map<String, String> getAllPhoneContacts(Context ctx) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Map<String, String> map = new HashMap<>();
        //得到ContentResolver对象
        ContentResolver cr = ctx.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                String phoneStr = "";
                if (phoneCursor != null) {
                    phoneCursor.moveToFirst();
                    if (phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) > 0) {
                        phoneStr = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    } else {
                        phoneStr = "";
                    }
                }
                if (!TextUtils.isEmpty(phoneStr) && !TextUtils.isEmpty(name)) {
                    Log.i("通讯录：", name + ":" + phoneStr);
                    phoneStr.replaceAll(" ", "");
                    map.put(phoneStr, EmojiUtil.cleanEmoji(name));
                }
                phoneCursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        cursor.close();
        return map;
    }

    /**
     * 页码公共参数
     */
    public static void addPageParams(HashMap<String, String> params, int page) {
        params.put("start", ((page - 1) * 10) + "");
        params.put("limit", Constants.MAX_LIMIT + "");
    }

    /**
     * 创建Map
     *
     * @return
     */
    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        return params;
    }

    /**
     * @return 获取token
     */
    public static String getToken() {
        String token = (String) SharedPreferencesUtils.get(Constants.USER_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        return token;
    }

    /**
     * 判空
     *
     * @param isNullToast 当输入为空时, toast
     * @return 是否为空
     */
    public static boolean checkInputData(InputItemLayout layout, String isNullToast) {
        boolean isEmpty = TextUtils.isEmpty(layout.getInputText());
        if (isEmpty) {
            ToastUtils.showToast(isNullToast);
        }
        return isEmpty;
    }

    /**
     * 判空
     *
     * @param isNullToast 当输入为空时, toast
     * @return 是否为空
     */
    public static boolean checkInputData(EditText layout, String isNullToast) {
        boolean isEmpty = TextUtils.isEmpty(layout.getText().toString());
        if (isEmpty) {
            ToastUtils.showToast(isNullToast);
        }
        return isEmpty;
    }

    /**
     * 获取处理后的银行卡编号
     *
     * @return **** **** **** 2222
     */
    public static String getBankCardNo(String cardNo) {
        if (cardNo.length() < 4) {
            return cardNo;
        }

        String text = cardNo.substring(cardNo.length() - 4);
        text = "**** **** **** " + text;

        return text;
    }

    /**
     * 友盟分享
     *
     * @param activity
     * @param shareBitmap
     * @param content
     */
    public static void shareAppUM(final Activity activity, Bitmap shareBitmap, String title, String content, final String taskNo) {
        if (CommonUtils.checkLogin()) {
            if (shareBitmap != null) {
                UMImage image = new UMImage(activity, shareBitmap);//bitmap文件
                new ShareAction(activity).withMedia(image)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {
                                ToastUtils.showToast("正在启动分享,请稍后!");
                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                if (!TextUtils.isEmpty(taskNo)) {
                                    // 分享任务
                                    NetComment.shareAddActivite(activity, taskNo);
                                }
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                ToastUtils.showToast("分享失败");
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                ToastUtils.showToast("您已取消分享");
                            }
                        }).open();
            } else {
                Member member = CommonUtils.getMember();
                String memberNo = member.getMemberNo();
                if (!TextUtils.isEmpty(memberNo)) {
                    String url = ConstantsUrl.SHARE_URL_HEADER + "memberNo=" + memberNo;
                    UMWeb umWeb = new UMWeb(url);
                    umWeb.setTitle(title);//标题
                    umWeb.setThumb(new UMImage(activity, R.mipmap.icon_make_money));  //缩略图
                    umWeb.setDescription(content);
                    new ShareAction(activity).withMedia(umWeb)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .setCallback(new UMShareListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {
                                    ToastUtils.showToast("正在启动分享,请稍后!");
                                }

                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                    if (!TextUtils.isEmpty(taskNo)) {
                                        // 分享任务
                                        NetComment.shareAddActivite(activity, taskNo);
                                    }
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    ToastUtils.showToast("分享失败");
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                    ToastUtils.showToast("您已取消分享");
                                }
                            }).open();
                } else {
                    ToastUtils.showToast(R.string.please_login_do);
                    ActivityUtils.getInstance().jumpLoginActivity();
                }
            }
        } else {
            ToastUtils.showToast(R.string.please_login_do);
            ActivityUtils.getInstance().jumpLoginActivity();
        }
    }

    /**
     * @return 登录状态
     */
    public static boolean checkLogin() {
        return (boolean) SharedPreferencesUtils.get(Constants.IS_LOGIN, false);
    }

    /**
     * 分享
     */
    public static void shareApp() {
        if (CommonUtils.checkLogin()) {
            Member member = CommonUtils.getMember();
            if (member != null && !member.getMemberNo().isEmpty()) {
                String memberNo = member.getMemberNo();
                String inviteCode = member.getInviteCode();
                shareApp(ConstantsUrl.SHARE_URL_HEADER + "memberNo=" + memberNo + "&inviteCode=" + inviteCode);
            } else {
                ToastUtils.showToast(R.string.please_login_do);
                ActivityUtils.getInstance().jumpLoginActivity();
            }
        } else {
            ToastUtils.showToast(R.string.please_login_do);
            ActivityUtils.getInstance().jumpLoginActivity();
        }
    }

    /**
     * 分享内容
     *
     * @param shareContent
     */
    public static void shareApp(String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, App.getContext().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TITLE, App.getContext().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        ActivityUtils.getInstance().currentActivity().startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * 分享图片
     *
     * @param bitmap 图片文件
     */
    public static void shareBitmap(Context context, Bitmap bitmap) {
        File file = FileUtil.saveBitmap(context, bitmap);
        if (file != null && file.exists() && file.isFile()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, App.getContext().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TITLE, App.getContext().getString(R.string.app_name));
            ActivityUtils.getInstance().currentActivity().startActivity(Intent.createChooser(intent, "分享到"));
        }

    }

    /**
     * 限制分享的渠道
     *
     * @param client 微信/QQ/微博
     */
    private static void shareApp(String memberNo, String... client) {
        if (client.length == 0) {
            client = new String[]{"微信", "QQ", "微博"};
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 查询所有可以分享的Activity
        List<ResolveInfo> resInfo = App.getContext().getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (!resInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resInfo) {
                Intent targeted = new Intent(Intent.ACTION_SEND);
                targeted.setType("text/plain");
                targeted.putExtra(Intent.EXTRA_SUBJECT, App.getContext().getString(R.string.app_name));
                targeted.putExtra(Intent.EXTRA_TITLE, App.getContext().getString(R.string.app_name));
                targeted.putExtra(Intent.EXTRA_TEXT, ConstantsUrl.API_HOST + "member/shareRegister?memberNo=" + memberNo);
                ActivityInfo activityInfo = info.activityInfo;
                Log.v("logcat", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name);

                targeted.setPackage(activityInfo.packageName);
                targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                PackageManager pm = App.getContext().getPackageManager();

                for (String clientStr : client) {
                    String pmStr = info.activityInfo.applicationInfo.loadLabel(pm).toString();
                    if (pmStr.equals(clientStr)) {
                        targetedShareIntents.add(targeted);
                    }
                }
            }
            // 选择分享时的标题
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "分享到");
            if (chooserIntent == null) {
                return;
            }
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
            try {

                ActivityUtils.getInstance().currentActivity().startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                ToastUtils.showToast("找不到该分享应用组件");
            }
        }
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在控间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    KeyboardUtils.hideKeyBoard(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片转string
     *
     * @param filePath 文件路径
     * @return Base64String
     */
    public static String imageToBase64Binary(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, 0);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    @SuppressWarnings("unchecked")
    public static boolean isMobile(String str) {

        if (str == null || str.length() != 11) {
            return false;
        }

        Pattern p = null;
        Matcher m = null;
        boolean b = false;
//        p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * @return 我的店铺类型是否为快递物流类型
     */
    public static String getMineShopType() {
        return (String) SharedPreferencesUtils.get(Constants.MINE_SHOP_TYPE, "");
    }

    /**
     * 获取距离
     *
     * @param distance
     * @return
     */
    public static String getDistance(String distance) {
        int dist = UnitUtil.getInt(distance);
        if (dist > 1000) {
            distance = UnitUtil.get2DecimalPointData((dist / 1000.0)) + "km";
        } else {
            distance += "m";
        }
        return distance;
    }

    /**
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return calcDistance
     */
    public static Double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        Double R = 6370996.81;  //地球的半径
        /*     * 获取两点间x,y轴之间的距离     */
        Double x = (lng2 - lng1) * Math.PI * R * Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
        Double y = (lat2 - lat1) * Math.PI * R / 180;
        Double distance = Math.hypot(x, y);
        //得到两点之间的直线距离
        LogUtils.d("距离：" + distance);
        return distance;
    }

    /**
     * url 转 map
     *
     * @param paramStr
     * @return
     */
    public static Map<String, String> urlToMap(String paramStr) {
        String[] params = paramStr.split("&");
        Map<String, String> resMap = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                resMap.put(key, value);
            }
        }
        return resMap;
    }

    /**
     * 设置webView参数
     *
     * @param webView
     * @return
     */
    public static void setWebSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
//       // 配置支持domstorage
        settings.setDomStorageEnabled(true);//启用或禁用DOM缓存
        settings.setAppCacheEnabled(false);//关闭/启用应用缓存
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }


}
