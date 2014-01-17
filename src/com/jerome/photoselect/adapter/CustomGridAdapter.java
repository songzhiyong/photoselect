package com.jerome.photoselect.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jerome.photoselect.R;
import com.jerome.photoselect.beans.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class CustomGridAdapter extends BaseAdapter {
	private int width;
	private ArrayList<PhotoInfo> mData;

	public CustomGridAdapter(Context context, ArrayList<PhotoInfo> mData) {
		super();
		width = context.getResources().getDisplayMetrics().widthPixels;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position).getPath();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		PhotoInfo info = mData.get(position);
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
		holder.cbSelected.setChecked(info.isState());
		FadeInBitmapDisplayer displayer = new FadeInBitmapDisplayer(300) {
			@Override
			public void display(Bitmap bitmap, ImageAware imageAware,
					LoadedFrom loadedFrom) {
				if (loadedFrom != LoadedFrom.MEMORY_CACHE) {
					super.display(bitmap, imageAware, loadedFrom);
				} else {
					imageAware.setImageBitmap(bitmap);
				}
			}
		};
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.middle_img_default)
				.showImageForEmptyUri(R.drawable.middle_img_default)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.displayer(displayer)
				.showImageOnFail(R.drawable.middle_img_default).build();
		ImageAware imageAware = new ImageViewAware(holder.imageView, false);
		if (TextUtils.isEmpty(ImageLoader.getInstance().getLoadingUriForView(
				imageAware))) {
			ImageLoader.getInstance().displayImage(
					"file://" + getItem(position), imageAware, options);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		CheckBox cbSelected;
	}
}