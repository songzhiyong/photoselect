/**
 * CombineActivity.java
 * com.jerome.photoselect
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-25 		Administrator
 *
 * Copyright (c) 2013, JEROME All Rights Reserved.
 */

package com.jerome.photoselect;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * ClassName:CombineActivity Function: TODO ADD FUNCTION
 * 
 * @author Administrator
 * @version
 * @Date 2013-12-25 上午9:55:22
 * 
 * @see
 */
public class CombineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combine);
		ArrayList<String> photoes = getIntent().getStringArrayListExtra(
				"result");
		if (photoes != null && !photoes.isEmpty()) {
			Options options = new Options();
			options.inSampleSize = 4;
			Bitmap bitmap1 = BitmapFactory.decodeFile(photoes.get(0), options);
			Bitmap bitmap2 = BitmapFactory.decodeFile(photoes.get(1), options);
			Bitmap combine = BitmapUtils.combinedLeftRightBmp(bitmap1, bitmap2);
			bitmap1.recycle();
			bitmap2.recycle();
			Bitmap bitmap3 = BitmapFactory.decodeFile(photoes.get(2), options);
			Bitmap finalBitmap = BitmapUtils.combinedTopBottomBmp(combine,
					bitmap3);

			try {
				BitmapUtils.saveBitmap(CompressFormat.PNG, finalBitmap,
						"/mnt/sdcard/combine.png", 80);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageView iView = (ImageView) findViewById(R.id.iv_combined);
			iView.setImageBitmap(finalBitmap);
		}
	}
}
