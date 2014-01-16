package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.jerome.photoselect.adapter.CustomGridAdapter;
import com.jerome.photoselect.beans.CategoryInfo;
import com.jerome.photoselect.beans.PhotoInfo;

/**
 * 
 * ClassName:GridSelectActivity <br>
 * Function:照片选择Grid<br>
 * 
 * @author Administrator
 * @version
 * @Date 2013 2013-12-5 上午10:54:31
 * 
 * @see
 */
public class GridSelectActivity extends Activity {
	private static final int REQUEST_CODE_PRE = 1;

	private GridView mGridView;
	private CustomGridAdapter mAdapter;
	private CategoryInfo categoryInfo;
	private ArrayList<String> results;

	private Button btnPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_grid);
		results = getIntent().getStringArrayListExtra("selected");
		categoryInfo = (CategoryInfo) getIntent().getSerializableExtra(
				"category");
		initTop();
		initGridView();
	}

	private void initTop() {
		btnPreview = (Button) findViewById(R.id.btn_preview);
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent(GridSelectActivity.this,
						ImagePreviewActivity.class);
				data.putExtra("selected", results);
				startActivityForResult(data, REQUEST_CODE_PRE);
			}
		});
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent(GridSelectActivity.this,
						ImagePreviewActivity.class);
				data.putExtra("selected", results);
				startActivityForResult(data, REQUEST_CODE_PRE);
			}
		});
	}

	private void initGridView() {
		mGridView = (GridView) findViewById(R.id.gd_photoes);
		// mGridView.setDrawSelectorOnTop(true);
		// mGridView.setSelector(getResources().getDrawable(
		// R.drawable.grid_selector));
		mAdapter = new CustomGridAdapter(this, categoryInfo.getPhotoPaths());
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhotoInfo itemInfo = categoryInfo.getPhotoPaths().get(position);
				System.out.println("position=" + position);
				String path = itemInfo.getPath();
				// CheckBox checkBox = (CheckBox) view
				// .findViewById(R.id.cb_selected);
				if (itemInfo.isState()) {
					results.remove(path);
					itemInfo.setState(false);
					// checkBox.setChecked(false);
				} else {
					itemInfo.setState(true);
					results.add(path);
					// checkBox.setChecked(true);
				}
				mAdapter.notifyDataSetChanged();
				int resultSize = results.size();
				btnPreview.setEnabled(resultSize != 0);
				btnPreview.setText(getResources().getString(R.string.preview)
						+ ((resultSize == 0) ? "" : "(" + results.size() + ")"));
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(resultCode, data);
			finish();
		} else if (resultCode == RESULT_CANCELED) {
			setResult(RESULT_CANCELED);
			finish();
		} else if (resultCode == RESULT_FIRST_USER) {
			results.clear();
			results.addAll(data.getStringArrayListExtra("result"));
			mAdapter.notifyDataSetChanged();
		}
	}

}
