package com.ramo.air.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.ramo.air.utils.DrawAirTowardsTrendChartUtil;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2017/1/22.
 */
public class MainFragmentPresenter {
    private Context context;
    private DrawAirTowardsTrendChartUtil drawAirTowardsTrendChartUtil;

    public MainFragmentPresenter(Context context) {
        this.context = context;
    }

    public View drawLine(String[] titles,String[] dataStrings, double[] aqiDoubles) {
        if (drawAirTowardsTrendChartUtil == null)
            drawAirTowardsTrendChartUtil = new DrawAirTowardsTrendChartUtil(dataStrings);

        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28});// 每个序列中点的X坐标
        }
        List<double[]> values = new ArrayList<double[]>();

        values.add(aqiDoubles);// 序列1中点的y坐标

        int[] colors = new int[]{Color.WHITE};// 每个序列的颜色设置
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE};// 每个序列中点的形状设置
        XYMultipleSeriesRenderer renderer = drawAirTowardsTrendChartUtil.buildRenderer(colors, styles);// 调用AbstractDemoChart中的方法设置renderer.

        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
                    .setFillPoints(true);// 设置图上的点为实心
        }

        // renderer, String title, String xTitle, String yTitle, double xMin,
        // double xMax, double yMin, double yMax, int axesColor, int labelsColor
        drawAirTowardsTrendChartUtil.setChartSettings(renderer, "空气污染趋势", "时间", "等级", 0, 9, 0, 250,
                Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(28);// 设置x轴显示12个点,根据setChartSettings的最大值和最小值自动计算点的间
        renderer.setYLabels(5);// 设置y轴显示10个点,根据setChartSettings的最大值和最小值自动计算点的间
        renderer.setShowGrid(true);// 是否显示网格
        renderer.setXLabelsAlign(Paint.Align.RIGHT);// 刻度线与刻度标注之间的相对位置关系
        renderer.setYLabelsAlign(Paint.Align.RIGHT);// 刻度线与刻度标注之间的相对位置关系

        renderer.setZoomButtonsVisible(false);// 是否显示放大缩小按钮
        renderer.setPanLimits(new double[]{0, 28, 0, 500});// 设置拖动时X轴Y轴允许的最大值最小值.
        renderer.setZoomLimits(new double[]{0, 28, 0, 500});// 设置放大缩小时X轴Y轴允许的最大最小值.
        renderer.setApplyBackgroundColor(true);
        renderer.setMarginsColor(Color.parseColor("#3f000000"));

        return ChartFactory.getLineChartView(context,
                drawAirTowardsTrendChartUtil.buildDataset(titles, x, values), renderer);
    }
}
