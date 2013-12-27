/**
 * PhotoSorterActivity.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * --
 * 
 * Released under the MIT license (but please notify me if you use this code, so that I can give your project credit at
 * http://code.google.com/p/android-multitouch-controller ).
 * 
 * MIT license: http://www.opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.jerome.photoselect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.jerome.widgets.CustomPhotoSortrView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class PhotoSortrActivity extends Activity {

	CustomPhotoSortrView photoSorter;
	CustomPhotoSortrView photoSorter2;
	CustomPhotoSortrView photoSorter3;

	ProgressDialog progressDialog;
	DisplayMetrics metrics;
	private static int[] IMAGES = {
	// R.drawable.m74hubble
	// , R.drawable.catarina, R.drawable.tahiti,
	// R.drawable.sunset
	// ,
	// R.drawable.lake
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combine2);
		metrics = getResources().getDisplayMetrics();
		final View view = findViewById(R.id.layout_combine);
		ArrayList<String> photoes = getIntent().getStringArrayListExtra(
				"result");
		photoSorter = (CustomPhotoSortrView) findViewById(R.id.sort1);
		photoSorter.init(this, new String[] { photoes.get(0) },
				metrics.widthPixels, metrics.heightPixels / 2);
		photoSorter.setBackgroundColor(Color.RED);
		photoSorter2 = (CustomPhotoSortrView) findViewById(R.id.sort2);
		photoSorter2.init(this, new String[] { photoes.get(1) },
				metrics.widthPixels / 2, metrics.heightPixels / 2);
		photoSorter2.setBackgroundColor(Color.BLUE);
		photoSorter3 = (CustomPhotoSortrView) findViewById(R.id.sort3);
		photoSorter3.init(this, new String[] { photoes.get(2) },
				metrics.widthPixels / 2, metrics.heightPixels / 2);
		photoSorter3.setBackgroundColor(Color.GREEN);
		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				progressDialog.dismiss();
				Toast.makeText(PhotoSortrActivity.this, "save success",
						Toast.LENGTH_SHORT).show();
			};
		};
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(PhotoSortrActivity.this,
						"Save", "Saving Picture>>>", true, false);
				new Thread() {
					public void run() {
						saveThumbnail(view, new File("/mnt/sdcard/combine.png"));
						handler.sendEmptyMessage(1);
					};
				}.start();

			}
		});
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

	@Override
	protected void onResume() {
		super.onResume();
		photoSorter.loadImages(this, metrics.widthPixels,
				metrics.heightPixels / 2);
		photoSorter2.loadImages(this, metrics.widthPixels / 2,
				metrics.heightPixels / 2);
		photoSorter3.loadImages(this, metrics.widthPixels / 2,
				metrics.heightPixels / 2);
	}

	@Override
	protected void onPause() {
		super.onPause();
		photoSorter.unloadImages();
		photoSorter2.unloadImages();
		photoSorter3.unloadImages();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			photoSorter.trackballClicked();
			photoSorter2.trackballClicked();
			photoSorter3.trackballClicked();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}