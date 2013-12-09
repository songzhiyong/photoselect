package com.jerome.photoselect.adapter;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerome.photoselect.R;
import com.jerome.photoselect.beans.CategoryInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomAdapter extends BaseAdapter {
	private List<CategoryInfo> mDatas;

	public CustomAdapter(List<CategoryInfo> mDatas) {
		super();
		setData(mDatas);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;
		if (row == null) {
			row = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_category, parent, false);
			holder = new ViewHolder(row);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		CategoryInfo categoryInfo = mDatas.get(position);
		holder.nameView.setText(categoryInfo.getName() + "("
				+ categoryInfo.getPhotoNum() + ")");

		if (TextUtils.isEmpty(categoryInfo.getThumbnailPath())) {
			holder.iconView.setImageResource(R.drawable.middle_img_default);
		} else {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.middle_img_default)
					.showImageForEmptyUri(R.drawable.middle_img_default)
					.showImageOnFail(R.drawable.middle_img_default).build();
			ImageLoader.getInstance().displayImage(
					"file://" + categoryInfo.getThumbnailPath(),
					holder.iconView, options);
		}
		return row;
	}

	class ViewHolder {
		TextView nameView;
		ImageView iconView;

		ViewHolder(View row) {
			nameView = (TextView) row.findViewById(R.id.tv_name);
			iconView = (ImageView) row.findViewById(R.id.iv_thumbnail);
		}
	}

	public void setData(List<CategoryInfo> data) {
		if (data != null) {
			mDatas = data;
		} else {
			mDatas = new ArrayList<CategoryInfo>();
		}
		notifyDataSetChanged();
	}
}