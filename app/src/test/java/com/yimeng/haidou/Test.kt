package com.yimeng.haidou

import org.junit.Test

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-15 09:28.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class Test {

    @Test
    fun forTest() {

        val limit = 5
        val list = mutableListOf("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh", "iii", "jjj", "kkk", "lll", "mmm", "nnn", "ooo", "ppp", "qqq","aaa", "bbb", "ccc", "ddd")
        val list2 = mutableListOf("赵", "钱", "孙", "李")
        var index = 0
        list2.forEachIndexed { i, it ->
            if (i == 0) {
                index = limit
            } else {
                index += limit + 1
            }
            list.add(index, it)
            print("$index ,")

        }
        print("\n")
        print(list)

    }
}