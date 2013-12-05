/**
 * ImagePreviewActivity.java
 * com.jerome.photoselect
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-5 		Administrator
 *
 * Copyright (c) 2013, JEROME All Rights Reserved.
 */

package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ClassName:ImagePreviewActivity<br>
 * Function: 所选照片预览
 * 
 * @author Administrator
 * @version
 * @Date 2013-12-5 下午2:02:43
 * 
 * @see
 */
public class ImagePreviewActivity extends Activity {
	private ArrayList<String> selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selected = getIntent().getStringArrayListExtra("selected");
		setContentView(R.layout.layout_preview);
		TextView result = (TextView) findViewById(R.id.tv_result);
		if (selected != null) {
			StringBuilder builder = new StringBuilder();
			for (String path : selected) {
				builder.append(path);
				builder.append("\n");
			}
			result.setText(builder);
		}
	}
}
