package com.jerome.photoselect.biz;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.jerome.photoselect.MultiPhotoSelectActivity.ImageAdapter;

public class ScanTask extends AsyncTask<Void, Void, ArrayList<String>> {
	private ProgressDialog dialog;
	private Context context;
	private String bucket_id;
	private ArrayList<String> data;
	private ImageAdapter mAdapter;

	public ScanTask(String path, Context context, ArrayList<String> imageUrls,
			ImageAdapter mAdapter) {
		super();
		this.bucket_id = path;
		this.context = context;
		this.data = imageUrls;
		this.mAdapter = mAdapter;
	}

	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		ArrayList<String> data = getPhotoes();
		return data;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "", "扫描中……");
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		dialog.dismiss();
		data.clear();
		data.addAll(result);
		mAdapter.notifyDataSetChanged();
	}

	private ArrayList<String> getPhotoes() {
		Log.i("imagePath", bucket_id);
		Cursor cursor = null;
		ArrayList<String> infos = new ArrayList<String>();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				MediaStore.Images.Media.BUCKET_ID + "=?",
				new String[] { bucket_id }, "");
		int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		while (cursor.moveToNext()) {
			infos.add(cursor.getString(pathColumn));
			Log.i("imagePath", cursor.getString(pathColumn));
		}
		if (cursor != null) {
			cursor.close();
		}
		return infos;

	}
}
