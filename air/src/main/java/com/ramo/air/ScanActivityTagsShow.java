package com.ramo.air;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.AndroidCaptureCropTags.tagview.LocalStatic;
import com.example.AndroidCaptureCropTags.tagview.TagsView;
import com.ramo.air.utils.BitmapUtil;

/**
 * Created by sreay on 14-11-25.
 */
public class ScanActivityTagsShow extends Activity {
	private TagsView tagsView;
	public Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tags_show);
		tagsView = (TagsView) findViewById(R.id.tagsView);
		bitmap = BitmapUtil.loadBitmap(LocalStatic.path);
		tagsView.setImage(bitmap);
		tagsView.setTagInfoModels(LocalStatic.tagInfoModels);
	}

	@Override
	public void onBackPressed() {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}

		super.onBackPressed();
	}
}
