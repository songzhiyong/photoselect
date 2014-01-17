/**
 * BitmapUtils.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * ClassName:BitmapUtils Function: TODO ADD FUNCTION
 * 
 * @author Administrator
 * @version
 * @Date 2013-12-25 上午9:54:59
 * 
 * @see
 */
public class BitmapUtils {
	
	public static Bitmap combinedTopBottomBmp(Bitmap topBmp, Bitmap bottomBmp) {
		if (topBmp == null) {
			return topBmp;
		}
		int gap = 1;
		int topWidth = topBmp.getWidth();
		int topHeight = topBmp.getHeight();
		int bottomHeight = bottomBmp.getHeight();
		//
		Bitmap combinedBmp = Bitmap.createBitmap(topWidth, topHeight
				+ bottomHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(combinedBmp);
		canvas.drawBitmap(topBmp, 0, 0, null);
		Paint paint1 = new Paint();
		canvas.drawBitmap(bottomBmp, 0, topHeight + gap, paint1);
		return combinedBmp;
	}

	public static Bitmap combinedLeftRightBmp(Bitmap leftBmp, Bitmap rightBmp) {
		if (leftBmp == null) {
			return leftBmp;
		}
		int gap = 1;
		int leftWidth = leftBmp.getWidth();
		int leftHeight = leftBmp.getHeight();
		int rightWidth = rightBmp.getWidth();
		//
		Bitmap combinedBmp = Bitmap.createBitmap(leftWidth + rightWidth,
				leftHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(combinedBmp);
		canvas.drawBitmap(leftBmp, 0, 0, null);
		Paint paint1 = new Paint();
		canvas.drawBitmap(rightBmp, leftWidth + gap, 0, paint1);
		return combinedBmp;
	}

	private void saveThumbnail(View view, File file) {
		final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		try {
			final OutputStream os = new FileOutputStream(file);
			try {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			} finally {
				os.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
			int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	public static void saveBitmap(CompressFormat format, Bitmap bmp,
			String path, int quality) throws IOException {
		File file = new File(path);
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream stream = new FileOutputStream(file);
		bmp.compress(format, quality, stream);
	}
}
