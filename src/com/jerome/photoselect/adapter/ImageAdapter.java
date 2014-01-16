package com.jerome.photoselect.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jerome.photoselect.R;
import com.jerome.photoselect.beans.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageAdapter extends BaseAdapter {
	private ArrayList<PhotoInfo> mData;
	LayoutInflater mInflater;
	int width;
	Context mContext;
	SparseBooleanArray mSparseBooleanArray;

	public ImageAdapter(Context context, ArrayList<PhotoInfo> mData) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mSparseBooleanArray = new SparseBooleanArray();
		width = context.getResources().getDisplayMetrics().widthPixels;
		this.mData = mData;
	}

	public ArrayList<String> getCheckedItems() {
		ArrayList<String> mTempArry = new ArrayList<String>();
		for (int i = 0; i < mData.size(); i++) {
			if (mSparseBooleanArray.get(i)) {
				mTempArry.add(mData.get(i).getPath());
			}
		}
		return mTempArry;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
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
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.middle_img_default)
				.showImageForEmptyUri(R.drawable.middle_img_default)
				.showImageOnFail(R.drawable.middle_img_default).build();
		ImageLoader.getInstance().displayImage("file://" + info.getPath(),
				holder.imageView, options);

		holder.cbSelected.setTag(position);
		holder.cbSelected.setChecked(mSparseBooleanArray.get(position));
		holder.cbSelected.setOnCheckedChangeListener(mCheckedChangeListener);

		return convertView;
	}

	OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
		}
	};
}

class ViewHolder {
	ImageView imageView;
	CheckBox cbSelected;
}
