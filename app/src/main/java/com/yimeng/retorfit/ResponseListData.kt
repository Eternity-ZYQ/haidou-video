package com.yimeng.retorfit

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 18:26.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
open class ResponseBase {
    var status: Int = 0
    var msg: String = ""
}

class ResponseListData<T> : ResponseBase() {
    val data: DataList<T>? = null

    class DataList<T> {
        val start = 0
        val limit = 0
        val total = 0
        val rows = mutableListOf<T>()
    }
}

class ResponseData<T> : ResponseBase() {
    val data: T? = null
}