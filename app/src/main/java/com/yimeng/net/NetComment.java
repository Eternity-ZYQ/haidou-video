package com.yimeng.net;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.App;
import com.yimeng.haidou.BuildConfig;
import com.yimeng.haidou.MainActivity;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SystemTipDialog;
import com.yimeng.entity.Member;
import com.yimeng.entity.ProvinceBean;
import com.yimeng.entity.SeedBean;
import com.yimeng.entity.SystemMsgBean;
import com.yimeng.interfaces.OnFileDownloadCallBack;
import com.yimeng.interfaces.OnGetMemberInfoCallBack;
import com.yimeng.interfaces.OnLocationDataInfoCallBack;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.receiver.DownloadCompleteReceiver;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.huige.library.dialog.CommonDialog;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.yanzhenjie.permission.Action;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/13 0013 下午 04:21.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class NetComment {

    /**
     * 版本检测
     */
    public static void checkVersion(final Activity ctx) {

        if (BuildConfig.DEBUG) {
            getSystemMessage(ctx);
            return;
        }

//        if (!ctx.toString().contains("MainActivity")) {
//            ToastUtils.showToast("已是最新版本!");
//        }


        HashMap<String, String> params = new HashMap<>();
        params.put("version", "androidVersion");
        new OkHttpCommon().postLoadData(ctx, ConstantsUrl.CHECK_VERSION, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {

                String localVersion = BuildConfig.VERSION_NAME;

                if (jsonObject.get("status").getAsInt() == 1) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    if (data != null) {
                        // 版本号
                        String versionNum = GsonUtils.parseJson(data, "versionNum", "");
                        if (!TextUtils.isEmpty(versionNum) && !versionNum.equals(localVersion)) {
                            // 强制更新
                            final int flash = data.get("flash").getAsInt();
                            final String versionContent = data.get("versionContent").getAsString();
                            final String updateUrl = data.get("versionName").getAsString();

                            // 是否强制
                            final boolean isFlash = (ctx instanceof MainActivity) && (flash == 1);

                            CommonDialog.getInstance()
                                    .init(ctx)
                                    .setTitle("更新提醒")
                                    .setCancelable(!isFlash)
                                    .setViewStyle(R.id.tv_dialog_msg, new CommonDialog.CustomViewCallBack() {
                                        @Override
                                        public void onCallBack(View v) {
                                            TextView tv = (TextView) v;
                                            tv.setText(Html.fromHtml(versionContent));
                                        }
                                    })
                                    .setSubmitListener(new CommonDialog.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Intent intent = new Intent();
//                                            intent.setAction("android.intent.action.VIEW");
//                                            Uri content_url = Uri.parse(updateUrl);
//                                            intent.setData(content_url);
//                                            ctx.startActivity(intent);
                                            ActivityUtils.getInstance().jumpInternetExplorer(updateUrl);
                                        }
                                    })
                                    .setCancelListener(new CommonDialog.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isFlash) {
                                                ctx.finish();
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            if (!ctx.toString().contains("MainActivity")) {
                                ToastUtils.showToast("已是最新版本!");
                            } else {
                                // 首页
                                getSystemMessage(ctx);
                            }


                        }
                    }
                }
            }
        });
    }

    /**
     * 获取系统提示
     */
    private static void getSystemMessage(final Activity activity) {
        new OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_SYSTEM_MESSAGE, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) return;
                if (jsonObject.get("data").isJsonNull()) return;

                String dataJsonArr = jsonObject.get("data").getAsJsonArray().toString();
                if (TextUtils.isEmpty(dataJsonArr)) return;

                List<SystemMsgBean> list = GsonUtils.getGson().fromJson(dataJsonArr, new TypeToken<List<SystemMsgBean>>() {
                }.getType());
                if (!list.isEmpty()) {
                    showSystemPopup(activity, list.get(0));
                }
            }
        });
    }

    /**
     * 系统消息
     *
     * @param msgBean
     */
    private static void showSystemPopup(final Activity activity, final SystemMsgBean msgBean) {
        new XPopup.Builder(activity)
                .asCustom(new SystemTipDialog(activity, msgBean))
                .show();
    }

    /**
     * 获取网络城市数据
     */
    public static void getLocationInfo(final OnFileDownloadCallBack callBack) {
        new OkHttpCommon().getLoadDataNoUI(ConstantsUrl.GET_LOCATION_INFO, new HashMap<String, String>(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null) {
                    callBack.onFile(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将返回结果转化为流，并写入文件
                int len;
                byte[] buf = new byte[1024];
                InputStream inputStream = response.body().byteStream();
                String fileName = "province.txt";

                File file = new File(App.getContext().getExternalFilesDir(null), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                while ((len = inputStream.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                if (callBack != null) {
                    callBack.onSuccess(file.getPath());
                }
            }
        });
    }

    /**
     * sjmm地区数据
     *
     * @param callBack
     */
    public static void getLXLocationInfo(final OnLocationDataInfoCallBack callBack) {
        if (TextUtils.isEmpty((String) SharedPreferencesUtils.get(Constants.APP_SJMM_LOCATION_INFO, ""))) {
            new OkHttpCommon().postLoadDataNoUI(ConstantsUrl.URL_QUERY_ADDRESS, CommonUtils.createParams(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callBack != null) {
                        callBack.onFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String resultInfo = response.body().string();
                        JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();
                        if (jsonObject.get("status").getAsInt() != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取地区失败"));
                            return;
                        }

                        String jsonStr = jsonObject.get("data").getAsJsonArray().toString();
                        List<ProvinceBean> list = GsonUtils.getGson().fromJson(jsonStr,
                                new TypeToken<List<ProvinceBean>>() {
                                }.getType());
                        SharedPreferencesUtils.put(Constants.APP_SJMM_LOCATION_INFO, jsonStr);
                        if (callBack != null) {
                            callBack.onSuccess(list);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 上传全部通讯录信息
     */
    public static void sendPhoneContactsHttp(final Context ctx) {
        CommonUtils.getPermission(ctx, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                final String token = CommonUtils.getToken();
                if (TextUtils.isEmpty(token)) {
                    return;
                }
                if ((boolean) SharedPreferencesUtils.get("savePhoneContacts", false)) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> parameterMap = new HashMap<String, String>();
                        Map<String, String> map = CommonUtils.getAllPhoneContacts(ctx);
                        String callRecord = new Gson().toJson(map);
                        parameterMap.put("callRecord", callRecord);

                        if (TextUtils.isEmpty(token)) {
                            ActivityUtils.getInstance().jumpLoginActivity();
                            return;
                        }
                        parameterMap.put("token", token);

                        OkHttpCommon okHttpCommon = new OkHttpCommon();
                        okHttpCommon.postLoadDataNoUI(ConstantsUrl.SAVE_CALL_RECORD, parameterMap, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //请求返回数据
                                String resultInfo = response.body().string();
                                JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();
                                if (jsonObject.get("status").getAsInt() == 1) {
                                    SharedPreferencesUtils.put("savePhoneContacts", true);
                                }
                            }
                        });
                    }
                }).start();
            }
        }, Manifest.permission.READ_CONTACTS);
//        if (!AndPermission.hasPermissions(ctx, Manifest.permission.READ_CONTACTS)) return;
    }

    /**
     * 保存地址信息
     *
     * @param activity
     * @param location
     */
    public static void sendLocation(Activity activity, BDLocation location) {
        String token = CommonUtils.getToken();
        if (TextUtils.isEmpty(token)) return;
        OkHttpCommon okHttpCommon = new OkHttpCommon();
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("longitude", location.getLongitude() + "");
        params.put("latitude", location.getLatitude() + "");
        params.put("address", location.getProvince() + "," + location.getCity() + "," + location.getDistrict() + "," + location.getStreet());
        Log.d("msg", "CommonUtils -> sendLocation: " + location.getProvince() + "," + location.getCity() + "," + location.getDistrict() + "," + location.getStreet());
        okHttpCommon.postLoadData(activity, ConstantsUrl.SAVE_LOCATION, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
            }
        });
    }

    /**
     * 系统下载
     *
     * @param url
     * @param contentDisposition
     * @param mimeType
     */
    public static void downloadBySystem(final String url, final String contentDisposition, final String mimeType) {
        CommonUtils.getPermission(App.getContext(), new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                ToastUtils.showToast("正在下载...");
                // 指定下载地址
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
                request.allowScanningByMediaScanner();
                // 设置通知的显示类型，下载进行时和完成后显示通知
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // 设置通知栏的标题，如果不设置，默认使用文件名
                // request.setTitle("This is title");
                // 设置通知栏的描述
                // request.setDescription("This is description");
                // 允许在计费流量下下载
                request.setAllowedOverMetered(false);
                // 允许该记录在下载管理界面可见
                request.setVisibleInDownloadsUi(false);
                // 允许漫游时下载
                request.setAllowedOverRoaming(true);
                // 允许下载的网路类型
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                // 设置下载文件保存的路径和文件名
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
                Log.d("msg", "CommonUtils -> downloadBySystem: fileName===" + fileName);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                // 另外可选一下方法，自定义下载路径
                // request.setDestinationUri()
                // request.setDestinationInExternalFilesDir()
                final DownloadManager downloadManager = (DownloadManager) App.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                // 添加一个下载任务
                long downloadId = downloadManager.enqueue(request);
                Log.d("msg", "CommonUtils -> downloadBySystem: " + downloadId);
                SharedPreferencesUtils.put(Constants.APP_DOWNLOAD_ID, downloadId);

                DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                App.getContext().registerReceiver(receiver, intentFilter);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 上传图片
     *
     * @param activity
     * @param filePath
     * @param callBack
     */
    public static void uploadPic(final Activity activity, final String filePath, final UploadImageCallBack callBack) {
        String baseStr = CommonUtils.imageToBase64Binary(filePath);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("id", "picsjbb00" + System.currentTimeMillis());
        params.put("imgBase64", "data:image/png;base64," + baseStr);
        Log.d("msg", "NetComment -> uploadPic: " + params.toString());
        new OkHttpCommon().postLoadData(activity, ConstantsUrl.UPLOAD_IMG_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.uploadFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                String url = GsonUtils.parseJson(jsonObject, "data", "");
                if (jsonObject.get("success").getAsBoolean() && !TextUtils.isEmpty(url)) {
                    callBack.uploadSuccess(url);
                } else {
                    callBack.uploadFail("上传失败!");
                }
            }
        });
    }

    /**
     * 表单多图片上传
     *
     * @param activity
     * @param filePathList
     * @param callback
     */
    public static void uploadPicFrom(Activity activity, List<String> filePathList, final UploadImageCallBack callback) {
        new OkHttpCommon().uploadFile(activity, filePathList, callback);
    }

    /**
     * 浏览商品增加活跃度
     *
     * @param activity
     * @param productNo
     */
    public static void browseProduct(Activity activity, String productNo) {
        if (!CommonUtils.checkLogin()) return;
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("productNo", productNo);
        new OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_BROWSE_PRODUCT, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) { }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException { }
        });
    }

    /**
     * 分享增加活跃度
     *
     * @param activity
     * @param taskNo
     */
    public static void shareAddActivite(final Activity activity, String taskNo) {
        if (!CommonUtils.checkLogin()) return;
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("taskNo", taskNo);
        new OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_SHARE_ADD_ACTIVITE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", activity.getString(R.string.net_error)));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "分享成功"));
            }
        });
    }

    /**
     * 联网获取用户信息
     */
    public static void getMemberInfo(final OnGetMemberInfoCallBack onGetMemberInfoCallBack){
        if(!CommonUtils.checkLogin()) {
            ToastUtils.showToast(R.string.please_login_str);
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        Activity activity = ActivityUtils.getInstance().currentActivity();
        new OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_GET_MEMBER_INFO, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取信息失败!"));
                    return;
                }

                String jsonStr = jsonObject.get("data").getAsJsonObject().toString();
                SharedPreferencesUtils.put(Constants.USER_INFO, jsonStr);
                if(onGetMemberInfoCallBack != null) {
                    Member member = GsonUtils.getGson().fromJson(jsonStr, Member.class);
                    onGetMemberInfoCallBack.getMemberInfo(member);
                }
            }
        });
    }

    /**
     * 获取可抢种子数据
     */
    public static void getGrabSeedInfo(){
        // 最后保存时间
        long lastTime = (long) SharedPreferencesUtils.get(Constants.LAST_SAVE_SEED_INFO_TIME, 0l);
        boolean isToday = DateUtil.isToday(lastTime);
        if(isToday) return;

        // 清空数据
        LitePal.deleteAll(SeedBean.class);

        new OkHttpCommon().postLoadData(ActivityUtils.getInstance().currentActivity(), ConstantsUrl.URL_GET_ALL_SEED,
                CommonUtils.createParams(), new CallbackCommon() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                        if(jsonObject.get("status").getAsInt() == 1) {
                            SharedPreferencesUtils.put(Constants.LAST_SAVE_SEED_INFO_TIME, System.currentTimeMillis());

                            JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                            List<SeedBean> list = GsonUtils.getGson().fromJson(jsonArray.toString(), new TypeToken<List<SeedBean>>(){}.getType());
                            LitePal.saveAll(list);

                        }
                    }
                });
    }

}
