package com.yimeng.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.yimeng.haidou.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/22 0022 下午 06:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 文件操作
 * </pre>
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileUtil {

    /**
     * 创建目录
     *
     * @param dirName 目录名称
     * @return flag 0 已经存在，1 创建成功， -1 创建失败
     */
    public static int createDir(String dirName) {

        int flag;
        String directory = getInnerSDCardPath() + "/" + dirName;
        File file = new File(directory);

        if (!file.exists()) {
            file.mkdirs();
            flag = 1;
        } else {
            flag = 0;
        }

        return flag;
    }

    /**
     * 创建文件
     *
     * @param dirPath  目录路径
     * @param fileName 文件名称
     * @param fileType 文件类型
     * @return flag 0 已经存在，1 创建成功， -1 创建失败
     */
    public static int createFile(String dirPath, String fileName, String fileType) {

        int flag = 0;
        File file = new File(dirPath + "/" + fileName + fileType);
        if (!file.exists()) {
            try {
                file.createNewFile();
                flag = 1;
            } catch (IOException e) {
                flag = -1;
                e.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * 是否存在该文件
     *
     * @param filePath 文件路径
     * @return boolean false不存在，true存在
     */
    private boolean isExistFile(String filePath) {

        boolean flag = false;
        File file = new File(filePath);
        if (file.exists()) {
            flag = true;
        }

        return flag;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file          要删除的根目录
     * @param deleteDirFlag true 删除目录，false 不删除目录
     */
    public static void deleteFile(File file, boolean deleteDirFlag) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                if (deleteDirFlag) {
                    file.delete();
                }
                return;
            }
            for (File f : childFile) {
                deleteFile(f, deleteDirFlag);
            }

            file.delete();
        }
    }

    /**
     * 写文件
     *
     * @param fileName  文件名
     * @param write_str 信息
     * @param flag      是否为追加标记
     * @throws IOException
     */
    public static void writeSDFile(String fileName, String write_str, boolean flag) throws IOException {

        File file = new File(fileName);

        FileOutputStream fos = new FileOutputStream(file, flag);

        byte[] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }

    /**
     * 获取内置SD卡路径
     *
     * @return path
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 保存图片到系统相册, 带时间
     * @param context
     * @param bitmap
     */
    public static File saveBitmap(Context context, Bitmap bitmap){
        String dirName = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + context.getString(R.string.app_name);
        File fileDir = new File(dirName);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        // 系统时间
        long mImageTime = System.currentTimeMillis();
        long dateSeconds = mImageTime / 1000;

        // 文件名
        String mImageFileName = "qrcode" + mImageTime + ".png";
        // 文件路径
        String mImageFilePath = dirName + File.separator + mImageFileName;

        File file = new File(mImageFilePath);
        if(file.exists()) {// 图片已存在
            return file;
        }

        // 保存截屏到系统MediaStore
        ContentValues values = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, mImageFilePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, mImageTime);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        try {
            OutputStream out = resolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);// bitmap转换成输出流，写入文件
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        }
        // update file size in the database
        values.clear();
        values.put(MediaStore.Images.ImageColumns.SIZE,
                new File(mImageFilePath).length());
        resolver.update(uri, values, null, null);

        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse(Environment.getExternalStorageDirectory().getPath())));
        return file;
    }
}
