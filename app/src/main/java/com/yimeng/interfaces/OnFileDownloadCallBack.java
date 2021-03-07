package com.yimeng.interfaces;

/**
 * Author : huiGer
 * Time   : 2018/10/13 0013 下午 03:29.
 * Desc   :
 */
public interface OnFileDownloadCallBack {
    void onSuccess(String filePath);
    void onFile(Exception e);
}
