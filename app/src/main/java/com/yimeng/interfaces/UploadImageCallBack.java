package com.yimeng.interfaces;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/24 0024 下午 04:14.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 图片上传回调
 * </pre>
 */
public interface UploadImageCallBack {
    void uploadSuccess(String url);
    void uploadFail(String msg);
}
