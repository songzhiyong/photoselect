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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

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
	private TextView tvCount;

	private int currentPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selected = getIntent().getStringArrayListExtra("selected");
		setContentView(R.layout.layout_preview);
		initTop();
		initPager();
		initBtns();
		notifyCountChange();
	}

	private void initTop() {
		tvCount = (TextView) findViewById(R.id.tv_num);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putStringArrayListExtra("result", selected);
				setResult(RESULT_FIRST_USER, data);
				finish();
			}
		});
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private void initBtns() {
		findViewById(R.id.btn_del).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selected.remove(currentPos);
				mAdapter.removeView(mPager, currentPos);
				mAdapter.notifyDataSetChanged();
				mIndicator.notifyDataSetChanged();
				notifyCountChange();
			}
		});
		findViewById(R.id.btn_confirm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent data = new Intent();
						data.putStringArrayListExtra("result", selected);
						setResult(RESULT_OK, data);
						finish();
					}
				});
	}

	private void notifyCountChange() {
		int count = selected.size();
		if (count > 0) {
			tvCount.setText(String.valueOf(count));
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
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
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				currentPos = pos;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}
}
