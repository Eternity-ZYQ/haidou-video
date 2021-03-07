package com.yimeng.haidou;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);

//        DecimalFormat df = new DecimalFormat("¥,###.00");
//        System.out.println(df.format(79.20));

//        System.out.println(UnitUtil.getRMBFormat(5687.1f));
//        for (int j = 0; j < 10; j++) {
//            int limit = j%3;
//            System.out.println(limit);
//        }

//        Pattern pattern = Pattern.compile("[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*");
//        Matcher match = pattern.matcher("1.0");
//        if (!match.matches()) {
//            System.out.println("充值金额输入格式有误");
//            return;
//        }


        new Runnable() {
            @Override
            public void run() {
                syso();
            }
        }.run();
    }

    private void syso() {
        long time = 1565669400000l;

        // s
        long result = (time - System.currentTimeMillis()) / 1000;
        // m
        long m = result / 60;
        long s = result % 60;
        System.out.println(result);
        System.out.println(m + ":" + s);
        try {
            Thread.sleep(1000);
            syso();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}