package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jerome.photoselect.beans.CategoryInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
	private int width;
	private Button btnPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_grid);
		results = getIntent().getStringArrayListExtra("selected");
		categoryInfo = (CategoryInfo) getIntent().getSerializableExtra(
				"category");
		width = getResources().getDisplayMetrics().widthPixels;
		initTop();
		initGridView();
	}

	private void initTop() {
		btnPreview = (Button) findViewById(R.id.btn_preview);
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent data = new Intent();
//				data.putExtra("result", results);
//				setResult(RESULT_OK, data);
//				finish();
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
		mAdapter = new CustomGridAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("position=" + position);
				String path = categoryInfo.getPhotoPaths().get(position);
				CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.cb_selected);
				if (results.contains(path)) {
					results.remove(path);
					checkBox.setChecked(false);
				} else {
					results.add(path);
					checkBox.setChecked(true);
				}
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

	class CustomGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return categoryInfo.getPhotoPaths().size();
		}

		@Override
		public Object getItem(int position) {
			return categoryInfo.getPhotoPaths().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.item_photoes, null);
				holder = new ViewHolder();
				LayoutParams params = new LayoutParams(width / 3, width / 3);
				convertView.setLayoutParams(params);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.cbSelected = (CheckBox) convertView
						.findViewById(R.id.cb_selected);
				holder.imageView.setScaleType(ScaleType.CENTER_CROP);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (results.contains(getItem(position))) {
				holder.cbSelected.setChecked(true);
			} else {
				holder.cbSelected.setChecked(false);
			}
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.middle_img_default)
					.showImageForEmptyUri(R.drawable.middle_img_default)
					.showImageOnFail(R.drawable.middle_img_default).build();
			ImageLoader.getInstance().displayImage(
					"file://" + getItem(position), holder.imageView, options);
			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			CheckBox cbSelected;
		}
	}
}
