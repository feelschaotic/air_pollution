package amap.yuntu.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Util {
	public static double parseDouble(double d) {
		DecimalFormat df = new DecimalFormat("0.000000",
				new DecimalFormatSymbols(Locale.US));
		return Double.parseDouble(df.format(d));
	}

}
