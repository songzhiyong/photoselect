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
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.jerome.photoselect.adapter.SimplePagerAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

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
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private SimplePagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selected = getIntent().getStringArrayListExtra("selected");
		setContentView(R.layout.layout_preview);
		initTop();
		initPager();
	}

	private void initTop() {
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
			}
		});
	}

	private void initPager() {
		mPager = (ViewPager) findViewById(R.id.vp_content);
		mAdapter = new SimplePagerAdapter();
		for (int i = 0; i < selected.size(); i++) {
			ImageView iView = new ImageView(this);
			iView.setId(i + 10);
			ViewGroup.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			iView.setLayoutParams(params);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.middle_img_default)
					.showImageForEmptyUri(R.drawable.middle_img_default)
					.showImageOnFail(R.drawable.middle_img_default).build();
			ImageLoader.getInstance().displayImage("file://" + selected.get(i),
					iView, options);
			mAdapter.addView(iView);
		}
		mPager.setAdapter(mAdapter);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}
}
