package com.yimeng.haidou.task_3_0

import android.support.v4.content.ContextCompat
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.SeedChartBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_trend.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/13 3:58 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 趋势图
 * </pre>
 */
class SeedChartActivity : BaseActivity() {

    // X轴
    private lateinit var mXAxis: XAxis
    // 左侧Y轴
    private lateinit var mLeftYAxis: YAxis
    // 右侧Y轴
    private lateinit var mRightYaxis: YAxis
    // 图例
    private lateinit var mLegend: Legend

    private var mOkHttpCommon = OkHttpCommon()

    override fun setLayoutResId(): Int = R.layout.activity_trend

    override fun init() {

        lineChart.apply {

            // 是否展示网格背景
            setDrawGridBackground(false)
            // 是否显示边界
            setDrawBorders(true)
            // 是否可拖动
            isDragEnabled = true
            // 是否有触摸事件
            setTouchEnabled(true)
            // 设置XY轴动画效果
            animateY(2500)
            animateX(1500)

            /***XY轴的设置***/
            mXAxis = lineChart.xAxis
            mLeftYAxis = lineChart.axisLeft
            mRightYaxis = lineChart.axisRight
            //X轴设置显示位置在底部
            mXAxis.position = XAxis.XAxisPosition.BOTTOM
            mXAxis.axisMinimum = 0f
            mXAxis.granularity = 1f
            //保证Y轴从0开始，不然会上移一点
            mLeftYAxis.axisMinimum = 0f
            mLeftYAxis.axisMaximum = 1f
            mRightYaxis.axisMinimum = 0f
            mRightYaxis.axisMaximum = 1f
            mRightYaxis.isDrawLabelsEnabled

            /***折线图例 标签 设置***/
            mLegend = lineChart.legend
            //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
            mLegend.form = Legend.LegendForm.LINE
            mLegend.textSize = 12f
            //显示位置 左下方
            mLegend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            mLegend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            mLegend.orientation = Legend.LegendOrientation.HORIZONTAL
            //是否绘制在图表里面
            mLegend.setDrawInside(false)

            // 右下角描述
            description = Description().apply { isEnabled = false }
        }


    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        rb_day.setOnClickListener { getData("day") }
        rb_week.setOnClickListener { getData("week") }
        rb_month.setOnClickListener { getData("month") }
        rb_quarter.setOnClickListener { getData("quarter") }
        rb_year.setOnClickListener { getData("year") }

    }

    override fun loadData() {
        getData("day")
    }


    /**
     * 查询数据
     */
    private fun getData(type: String) {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SEED_PRICE_CHART, CommonUtils.createParams().apply {
            put("qType", type)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var jsonArray = it.get("data").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<SeedChartBean>>(jsonArray.toString(), object : TypeToken<List<SeedChartBean>>() {}.type)

                    setChartData(list)
                }
            }
        })
    }

    private fun setChartData(list: List<SeedChartBean>) {
        if (list.isEmpty()) return

        var entryList = mutableListOf<Entry>()


        for (i in 0 until list.size) {
            var seedChartBean = list[i]
            entryList.add(Entry((i + 1).toFloat(), seedChartBean.triggerAmt.toFloat(), seedChartBean))

        }

        // 一个LineDataSet 代表一条曲线
        var lineDataSet = LineDataSet(entryList, "种子趋势").apply {
            color = ContextCompat.getColor(this@SeedChartActivity, R.color.theme_color)
            setCircleColor(color)
            lineWidth = 1f
            circleRadius = 3f
            // 曲线圆点是否为空空心
            setDrawCircleHole(false)
            valueTextSize = 10f

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = "￥$value"
            }
        }

        mXAxis.valueFormatter = object :ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
//                Log.d("msg", "${list[value.toInt() % list.size].triggerDay}");
//                return value.toString()
                if(value.toInt() == list.size && value.toInt() != 0) {
                    // 最后一个
                    return list[value.toInt()-1].triggerDay
                }

                var position = value.toInt() % list.size
                if(position == 0) {
                    return "0"
                }
                return list[position-1].triggerDay
            }
        }

        var lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // 清除并刷新
        lineChart.fitScreen()
    }
}
