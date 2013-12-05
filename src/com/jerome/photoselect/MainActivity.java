package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jerome.photoselect.beans.CategoryInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends Activity {
	private static final int REQUEST_CODE_SELECT_PHOTOES = 1;

	private ListView mListView;
	private CustomAdapter mAdapter;
	private ArrayList<CategoryInfo> mDatas;
	private ImageLoader loader;
	private ArrayList<String> selectedPhotoes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		selectedPhotoes = new ArrayList<String>();
		loader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = ImageLoaderConfiguration
				.createDefault(this);
		loader.init(config);
		setContentView(R.layout.activity_main);
		initListView();
	}

	private void initListView() {
		mDatas = new ArrayList<CategoryInfo>();
		mListView = (ListView) findViewById(R.id.lv_photoes);
		mAdapter = new CustomAdapter();
		mListView.setAdapter(mAdapter);
		ScanPhotoTask task = new ScanPhotoTask(this, mDatas, mAdapter);
		task.execute();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						GridSelectActivity.class);
				intent.putExtra("category", mDatas.get(position));
				intent.putExtra("selected", selectedPhotoes);
				startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTOES);
			}
		});
	}

	public void notifyDataSetChanged(ArrayList<CategoryInfo> data) {
		mDatas.clear();
		mDatas.addAll(data);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			selectedPhotoes.clear();
			selectedPhotoes.addAll(data.getStringArrayListExtra("result"));
			Log.i("result", "" + data.getSerializableExtra("result"));
			Toast.makeText(this, "" + data.getSerializableExtra("result"),
					Toast.LENGTH_SHORT).show();
		}
	}

	class CustomAdapter extends BaseAdapter {

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
				loader.displayImage(
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

	}
}
