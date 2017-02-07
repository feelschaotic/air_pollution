package com.ramo.air;

import android.app.Activity;

import com.ramo.air.bean.AirQuality;
import com.ramo.air.jsonparsing.AirResultParseBean;

import java.util.Map;

public class LineActivity extends Activity {
	private Map<Integer, AirQuality> twoAirMap;
	private String dataStrings[];
	private double[] aqiDoubles;
	private AirResultParseBean air;
/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 air = (AirResultParseBean) MyPreferenceUtils
				.readObject("air_quality");
		// 趋势图的绘制开始
				if (air != null)
					twoAirMap = air.getLastTwoWeeks();
				if (twoAirMap != null) {
					int i = 0;
					aqiDoubles = new double[twoAirMap.size()];
					dataStrings = new String[twoAirMap.size()];

					for (Integer id : twoAirMap.keySet()) {
						AirQuality air = twoAirMap.get(id);
						aqiDoubles[i] = Double.parseDouble(air.getAQI());
						dataStrings[i++] = air.getDate().substring(5);
					}
				}

				String[] titles = new String[] { "最近28天的空气质量" };
				List<double[]> x = new ArrayList<double[]>();
				for (int i = 0; i < titles.length; i++) {
					x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
							15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 });// 每个序列中点的X坐标
				}
				List<double[]> values = new ArrayList<double[]>();

				values.add(aqiDoubles);// 序列1中点的y坐标

				int[] colors = new int[] { Color.WHITE };// 每个序列的颜色设置
				PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };// 每个序列中点的形状设置
				XYMultipleSeriesRenderer renderer = MainFragment.buildRenderer(colors, styles);// 调用AbstractDemoChart中的方法设置renderer.

				int length = renderer.getSeriesRendererCount();
				for (int i = 0; i < length; i++) {
					((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
							.setFillPoints(true);// 设置图上的点为实心
				}

				// renderer, String title, String xTitle, String yTitle, double xMin,
				// double xMax, double yMin, double yMax, int axesColor, int labelsColor
				MainFragment.setChartSettings(renderer, "空气污染趋势", "时间", "等级", 0, 10, 0, 200,
						Color.LTGRAY, Color.LTGRAY);
				renderer.setXLabels(28);// 设置x轴显示12个点,根据setChartSettings的最大值和最小值自动计算点的间
				renderer.setYLabels(5);// 设置y轴显示10个点,根据setChartSettings的最大值和最小值自动计算点的间
				renderer.setShowGrid(true);// 是否显示网格
				renderer.setXLabelsAlign(Align.RIGHT);// 刻度线与刻度标注之间的相对位置关系
				renderer.setYLabelsAlign(Align.RIGHT);// 刻度线与刻度标注之间的相对位置关系

				renderer.setZoomButtonsVisible(true);// 是否显示放大缩小按钮
				renderer.setPanLimits(new double[] { 0, 28, 0, 500 });// 设置拖动时X轴Y轴允许的最大值最小值.
				renderer.setZoomLimits(new double[] { 0, 28, 0, 500 });// 设置放大缩小时X轴Y轴允许的最大最小值.


				View lineView = ChartFactory.getLineChartView(MyApplication.getContext(),
				MainFragment.buildDataset(titles, x, values), renderer);
				// 趋势图的绘制结束
				lineView.setBackgroundColor(Color.BLACK);
				setContentView(lineView);

	}*/
}
