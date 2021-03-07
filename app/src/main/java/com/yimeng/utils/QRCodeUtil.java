package com.yimeng.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.yimeng.config.ConstantsUrl;
import com.yimeng.widget.zxingnew.qrcode.core.BGAQRCodeUtil;
import com.yimeng.widget.zxingnew.qrcode.zxing.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;


/**
 * 类名称
 *
 * @author xp
 * @describe 类详细描述.
 * @date 2018/6/20.
 */

public class QRCodeUtil {

    public static void createChineseQRCodeWithLogo(final Context mContext, final String mQRUrl,
                                                   final Bitmap logoBitmap, final ImageView qrcodeSDV) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(mQRUrl,
                        BGAQRCodeUtil.dp2px(mContext, 150), Color.parseColor("#333333"), logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrcodeSDV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    qrcodeSDV.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }

    public static void createChineseQRCode(final Context mContext, final String mQRUrl, final ImageView qrcodeSDV) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(mQRUrl, BGAQRCodeUtil.dp2px(mContext, 150), Color.parseColor("#333333"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrcodeSDV.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }

    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width   位图宽度(单位:px)
     * @param height  位图高度(单位:px)
     * @return
     * @author huiGer
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height) {
        return createQRCodeBitmap(content, width, height, "UTF-8", "H", "2", Color.BLACK, Color.WHITE);
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content          字符串内容
     * @param width            位图宽度,要求>=0(单位:px)
     * @param height           位图高度,要求>=0(单位:px)
     * @param character_set    字符集/字符转码格式 (支持格式:{@link CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin           空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black      黑色色块的自定义颜色值
     * @param color_white      白色色块的自定义颜色值
     * @return
     * @author huiGer
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            @Nullable String character_set, @Nullable String error_correction, @Nullable String margin,
                                            @ColorInt int color_black, @ColorInt int color_white) {

        /** 1.参数合法性判断 */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }

        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }

        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();

            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }

            if (!TextUtils.isEmpty(error_correction)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置
            }

            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
            }
            BitMatrix bitMatrix = createQRCode(content, width, height);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据内容生成二维码数据
     *
     * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密]
     * @param width   二维码照片宽度
     * @param height  二维码照片高度
     * @return
     */
    public static BitMatrix createQRCode(String content, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        //设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    /**
     * 将View转为Bitmap
     *
     * @param addViewContent View
     * @return Bitmap
     */
    public static Bitmap getViewBitmap(View addViewContent) {

//        addViewContent.setDrawingCacheEnabled(true);
//        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
//        addViewContent.buildDrawingCache();
//        Bitmap cacheBitmap = addViewContent.getDrawingCache();
//        return Bitmap.createBitmap(cacheBitmap);


//        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
//        addViewContent.buildDrawingCache();
////        return addViewContent.getDrawingCache();
//        Bitmap bitmapResult = Bitmap.createBitmap(addViewContent.getDrawingCache());
//        return bitmapResult;

//        addViewContent.measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        );
//        addViewContent.layout(0, 0, DeviceUtils.getWindowWidth(addViewContent.getContext()), addViewContent.getMeasuredHeight());  //根据字符串的长度显示view的宽度
//        addViewContent.buildDrawingCache();
//        return addViewContent.getDrawingCache();

        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.buildDrawingCache();
        Bitmap bitmapResult = Bitmap.createBitmap(addViewContent.getDrawingCache());
        addViewContent.setDrawingCacheEnabled(false);
        return bitmapResult;


//        if (addViewContent == null) return null;
//        Bitmap ret =
//                Bitmap.createBitmap(addViewContent.getWidth(), addViewContent.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(ret);
//        Drawable bgDrawable = addViewContent.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        addViewContent.draw(canvas);
//        return ret;

    }

    /**
     * 获取bitmap， 适用于 LayoutInflater
     *
     * @param activity
     * @param view
     * @return
     */
    public static Bitmap getViewBitmap(Activity activity, View view) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        // 整个View的大小 参数是左上角 和右下角的坐标
        view.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        view.layout(0, 0, w, h);
        view.draw(c);
        return bmp;
    }

    /**
     * 获取二维码链接
     *
     * @return url
     */
    public static String getQRCodeUrl(String memberNo, String shopNo) {
        String url = ConstantsUrl.SHARE_URL_HEADER;
        memberNo = TextUtils.isEmpty(memberNo) ? "" : "memberNo=" + memberNo;
        shopNo = TextUtils.isEmpty(shopNo) ? "" : "&shopNo=" + shopNo;
        url += memberNo + shopNo;
        return url;
    }


}
