package com.ramo.air.utils;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

/**
 * 大气走向趋势图
 * Created by ramo on 2017/1/22.
 */
public class DrawAirTowardsTrendChartUtil {
    private String[] dataStrings;
    public DrawAirTowardsTrendChartUtil(String[] dataStrings){
        this.dataStrings=dataStrings;
    }
    public  XYMultipleSeriesRenderer buildRenderer(int[] colors,
                                                         PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    public void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
                                   PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);

        renderer.setPointSize(8);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        renderer.setMargins(new int[] { 20, 30, 15, 20 });// 设置图表的外边框(上/左/下/右)
        renderer.setXLabelsAngle(-25); // 设置 X 轴标签倾斜角度 (clockwise degree)
        renderer.setXLabels(0); // 设置 X 轴不显示数字（改用我们手动添加的文字标签）
        int j = 1;

        for (String str : dataStrings) {
            j++;
            renderer.addTextLabel(j, str);
        }

        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setDisplayChartValues(true);// 将点的值显示出来
            r.setChartValuesSpacing(20);// 显示的点的值与图的距离
            r.setChartValuesTextSize(16);// 点的值的文字大小
            renderer.addSeriesRenderer(r);
        }
    }

    public void setChartSettings(XYMultipleSeriesRenderer renderer,
                                        String title, String xTitle, String yTitle, double xMin,
                                        double xMax, double yMin, double yMax, int axesColor,
                                        int labelsColor) {
        // renderer.setChartTitle(title);
        // renderer.setXTitle(xTitle);
        // renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    public XYMultipleSeriesDataset buildDataset(String[] titles,
                                                       List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
                                    List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }
}
