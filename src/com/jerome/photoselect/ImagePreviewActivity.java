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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private TextView tvCount, tvTitle;
	private CheckBox cbSelected;

	private SimplePagerAdapter mAdapter;
	private ArrayList<String> selected;
	private SparseBooleanArray selectedArray;
	private int currentPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_preview);
		initTop();
		initPager();
		initBtns();
		notifyCountChange();
	}

	private void initTop() {
		tvCount = (TextView) findViewById(R.id.tv_num);
		tvTitle = (TextView) findViewById(R.id.textView1);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putStringArrayListExtra("result", getCheckedItems());
				setResult(RESULT_FIRST_USER, data);
				finish();
			}
		});
	}

	private void initBtns() {
		cbSelected = ((CheckBox) findViewById(R.id.cb_select));
		cbSelected.setChecked(true);
		cbSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				selectedArray.put(currentPos, isChecked);
				notifyCountChange();
			}
		});
		findViewById(R.id.btn_confirm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent data = new Intent();
						data.putStringArrayListExtra("result",
								getCheckedItems());
						setResult(RESULT_OK, data);
						finish();
					}
				});
	}

	public ArrayList<String> getCheckedItems() {
		ArrayList<String> mTempArry = new ArrayList<String>();

		for (int i = 0; i < selected.size(); i++) {
			if (selectedArray.get(i)) {
				mTempArry.add(selected.get(i));
			}
		}
		return mTempArry;
	}

	private void notifyCountChange() {
		int count = getCheckedItems().size();
		if (count > 0) {
			tvCount.setText(String.valueOf(count));
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	private void initPager() {
		selected = getIntent().getStringArrayListExtra("selected");
		selectedArray = new SparseBooleanArray();
		for (int i = 0; i < selected.size(); i++) {
			selectedArray.put(i, true);
		}
		tvTitle.setText(currentPos + 1 + "/" + selected.size());
		mPager = (ViewPager) findViewById(R.id.vp_content);
		mAdapter = new SimplePagerAdapter();
		for (int i = 0; i < selected.size(); i++) {
			ImageView iView = new ImageView(this);
			iView.setId(i + 10);
			ViewGroup.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			iView.setLayoutParams(params);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.middle_img_default)
					.showImageForEmptyUri(R.drawable.middle_img_default)
					.showImageOnFail(R.drawable.middle_img_default).build();
			ImageLoader.getInstance().displayImage("file://" + selected.get(i),
					iView, options);
			mAdapter.addView(iView);
		}
		mPager.setAdapter(mAdapter);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				currentPos = pos;
				tvTitle.setText(currentPos + 1 + "/" + selected.size());
				cbSelected.setChecked(selectedArray.get(pos));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putStringArrayListExtra("result", getCheckedItems());
		setResult(RESULT_FIRST_USER, data);
		super.onBackPressed();
	}
}
