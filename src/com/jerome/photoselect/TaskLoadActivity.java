package com.jerome.photoselect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.jerome.photoselect.adapter.CustomAdapter;
import com.jerome.photoselect.beans.CategoryInfo;
import com.jerome.photoselect.biz.ScanPhotoTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TaskLoadActivity extends Activity {
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
		mAdapter = new CustomAdapter(mDatas);
		mListView.setAdapter(mAdapter);
		ScanPhotoTask task = new ScanPhotoTask(this, mDatas, mAdapter);
		task.execute();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(TaskLoadActivity.this,
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

}
