package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jerome.photoselect.beans.CategoryInfo;
import com.jerome.photoselect.biz.ScanTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 */
public class MultiPhotoSelectActivity extends Activity {
	private static final int REQUEST_CODE_PRE = 1;

	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	private ImageLoader imageLoader;
	private Button btnOk, btnBack;
	private int max;
	private Toast toast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);
		toast = Toast.makeText(MultiPhotoSelectActivity.this, "超过图片最大数量",
				Toast.LENGTH_SHORT);
		imageLoader = ImageLoader.getInstance();
		this.imageUrls = new ArrayList<String>();
		CategoryInfo categoryInfo = (CategoryInfo) getIntent()
				.getSerializableExtra("category");
		String path = categoryInfo.getId() + "";

		max = getIntent().getIntExtra("max", 5);
		// for (PhotoInfo info : categoryInfo.getPhotoPaths()) {
		// this.imageUrls.add(info.getPath());
		// }
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.middle_img_default)
				.showImageForEmptyUri(R.drawable.middle_img_default)
				.cacheInMemory().cacheOnDisc().build();
		imageAdapter = new ImageAdapter(this, imageUrls);
		btnOk = (Button) findViewById(R.id.btn_ok);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(imageAdapter);
		ScanTask task = new ScanTask(path, this, imageUrls, imageAdapter);
		task.execute();
	}

	@Override
	protected void onStop() {
		imageLoader.stop();
		super.onStop();
	}

	public void btnChoosePhotosClick(View v) {

		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
		Toast.makeText(MultiPhotoSelectActivity.this,
				"Total photos selected: " + selectedItems.size(),
				Toast.LENGTH_SHORT).show();
		Log.d(MultiPhotoSelectActivity.class.getSimpleName(),
				"Selected Items: " + selectedItems.toString());
	}

	public void btnPreviewClick(View v) {
		Intent data = new Intent(MultiPhotoSelectActivity.this,
				ImagePreviewActivity.class);
		data.putExtra("selected", imageAdapter.getCheckedItems());
		startActivityForResult(data, REQUEST_CODE_PRE);
	}
	public void btnPreview2Click(View v) {
		Intent data = new Intent(MultiPhotoSelectActivity.this,
				ImagePreviewActivity2.class);
		data.putExtra("selected", imageAdapter.getCheckedItems());
		startActivityForResult(data, REQUEST_CODE_PRE);
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
			ArrayList<String> selected = data.getStringArrayListExtra("result");
			imageAdapter.setCheckedItems(selected);
			imageAdapter.notifyDataSetChanged();
		}
	}

	public class ImageAdapter extends BaseAdapter {

		ArrayList<String> mList;
		LayoutInflater mInflater;
		Context mContext;
		SparseBooleanArray mSparseBooleanArray;

		public ImageAdapter(Context context, ArrayList<String> imageList) {
			// TODO Auto-generated constructor stub
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
			mSparseBooleanArray = new SparseBooleanArray();
			mList = new ArrayList<String>();
			this.mList = imageList;

		}

		public ArrayList<String> getCheckedItems() {
			ArrayList<String> mTempArry = new ArrayList<String>();

			for (int i = 0; i < mList.size(); i++) {
				if (mSparseBooleanArray.get(i)) {
					mTempArry.add(mList.get(i));
				}
			}
			return mTempArry;
		}

		public void setCheckedItems(ArrayList<String> items) {
			mSparseBooleanArray.clear();
			for (int i = 0; i < items.size(); i++) {
				mSparseBooleanArray.put(mList.indexOf(items.get(i)), true);
			}
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_multiphoto_item,
						null);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox checkBox = (CheckBox) v
							.findViewById(R.id.checkBox1);
					int selectedSize = imageAdapter.getCheckedItems().size();
					if (selectedSize >= max) {
						if (checkBox.isChecked()) {
							checkBox.setChecked(!checkBox.isChecked());
						} else {
							toast.show();
						}
					} else {
						checkBox.setChecked(!checkBox.isChecked());
					}
				}
			});
			final CheckBox mCheckBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			final ImageView imageView = (ImageView) convertView
					.findViewById(R.id.imageView1);

			imageLoader.displayImage("file://" + imageUrls.get(position),
					imageView, options, new SimpleImageLoadingListener() {
						public void onLoadingComplete(Bitmap loadedImage) {
							Animation anim = AnimationUtils.loadAnimation(
									MultiPhotoSelectActivity.this,
									R.anim.fade_in);
							imageView.setAnimation(anim);
							anim.start();
						}
					});
			mCheckBox.setTag(position);
			mCheckBox.setChecked(mSparseBooleanArray.get(position));
			mCheckBox.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (((CheckBox) v).isChecked()) {
						return false;
					}
					if (max <= imageAdapter.getCheckedItems().size()) {
						toast.show();
						return true;
					}
					return false;
				}
			});
			mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
			return convertView;
		}

		OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mSparseBooleanArray.put((Integer) buttonView.getTag(),
						isChecked);
				int selectedSize = imageAdapter.getCheckedItems().size();
				btnOk.setText("OK(" + selectedSize + ")");
			}
		};
	}

}