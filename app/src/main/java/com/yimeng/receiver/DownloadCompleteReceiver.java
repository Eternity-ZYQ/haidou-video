package com.yimeng.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.yimeng.config.Constants;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/13 0013 下午 03:07.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 下载通知
 * </pre>
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("onReceive. intent:{}", intent != null ? intent.toUri(0) : null);
        if (intent != null) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                Log.d("msg", "DownloadCompleteReceiver -> onReceive: downloadId===" + "");
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                Log.d("msg", "DownloadCompleteReceiver -> onReceive: type===" + type);
                if (TextUtils.isEmpty(type)) {
                    type = "*/*";
                }

                long refernece = (long)SharedPreferencesUtils.get(Constants.APP_DOWNLOAD_ID, (long)0);
                if (refernece != downloadId) {
                    return;
                }


                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                Log.d("msg", "DownloadCompleteReceiver -> onReceive: uri===" + uri);
//                if (uri != null) {
//                    Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
//                    handlerIntent.setDataAndType(uri, type);
//                    handlerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(handlerIntent);
//                }

                if(uri != null) {
                    installAPK(context, uri);
                }

//                Cursor cursor = downloadManager.query(new DownloadManager.Query());
//                String localFilename;
//                // 下载文件在本地保存的路径（Android 7.0 以后 COLUMN_LOCAL_FILENAME 字段被弃用, 需要用 COLUMN_LOCAL_URI 字段来获取本地文件路径的 Uri）
//                if(Build.VERSION.SDK_INT >= 24) {
//                    localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                }else {
//                    localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
//                }
//                notifyToInstall(context, localFilename);
            }
        }
    }

    private void installAPK(Context context,Uri apk ) {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intents);
        } else {
            File file = queryDownloadedApk(context);
            if (file.exists()) {
                openFile(file, context);
            }
        }
    }

    /**
     * 通过downLoadId查询下载的apk，解决6.0以后安装的问题
     * @param context
     * @return
     */
    public static File queryDownloadedApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = (long)SharedPreferencesUtils.get(Constants.APP_DOWNLOAD_ID, -1L);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;

    }

    private void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            ToastUtils.showToast("没有找到打开此类文件的程序");
        }

    }

    private String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }


    private void notifyToInstall(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }

        Log.d("msg", "DownloadCompleteReceiver -> notifyToInstall: 文件地址:" + apkPath);

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) { // 7.0以上
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "com.yimeng.sjbb.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else { // 7.0以下
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

}
