package com.ramo.air.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.ramo.air.application.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ImageManageUtil {
	public static Drawable RToDrawable(int r) {
		Resources resources = MyApplication.getContext().getResources();
		Drawable drawable = resources.getDrawable(r);
		return drawable;
	}

	public static Bitmap RToBitmap(int r) {
		Resources res = MyApplication.getContext().getResources();
		InputStream is = res.openRawResource(r);
		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		return bmpDraw.getBitmap();
	}

	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/*
	 * 2 Resources r = this.getContext().getResources(); Inputstream is =
	 * r.openRawResource(R.drawable.my_background_image); BitmapDrawable bmpDraw
	 * = new BitmapDrawable(is); Bitmap bmp = bmpDraw.getBitmap();
	 * 
	 * 3、
	 * 
	 * Bitmap bmp=BitmapFactory.decodeResource(r, R.drawable.icon);
	 * 
	 * Bitmap newb = Bitmap.createBitmap( 300, 300, Config.ARGB_8888 );
	 * 
	 * 4、
	 * 
	 * InputStream is = getResources().openRawResource(R.drawable.icon);
	 * 
	 * Bitmap mBitmap = BitmapFactory.decodeStream(is);
	 */
	public static Bitmap StrToBitmap(String report_img) {
		byte[] bytes = Base64.decode(report_img, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

}
