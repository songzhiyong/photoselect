package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import com.jerome.photoselect.beans.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 */
public class MultiPhotoSelectActivity extends Activity {

	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	private ImageLoader imageLoader;
	private Button btnOk;
	private int max;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);
		imageLoader = ImageLoader.getInstance();
		this.imageUrls = new ArrayList<String>();
		CategoryInfo categoryInfo = (CategoryInfo) getIntent()
				.getSerializableExtra("category");
		max = getIntent().getIntExtra("max", 3);
		for (PhotoInfo info : categoryInfo.getPhotoPaths()) {
			this.imageUrls.add(info.getPath());
		}
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.middle_img_default)
				.showImageForEmptyUri(R.drawable.middle_img_default)
				.cacheInMemory().cacheOnDisc().build();
		imageAdapter = new ImageAdapter(this, imageUrls);
		btnOk = (Button) findViewById(R.id.btn_ok);
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(imageAdapter);
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

	}

	/*
	 * private void startImageGalleryActivity(int position) { Intent intent =
	 * new Intent(this, ImagePagerActivity.class); intent.putExtra(Extra.IMAGES,
	 * imageUrls); intent.putExtra(Extra.IMAGE_POSITION, position);
	 * startActivity(intent); }
	 */

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
						}
						Toast.makeText(MultiPhotoSelectActivity.this,
								"超过图片最大数量", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(MultiPhotoSelectActivity.this,
								"超过图片最大数量", Toast.LENGTH_SHORT).show();
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