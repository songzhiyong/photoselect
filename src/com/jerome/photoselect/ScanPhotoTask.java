package com.jerome.photoselect;

import java.util.ArrayList;

import com.jerome.photoselect.MainActivity.CustomAdapter;
import com.jerome.photoselect.beans.CategoryInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

public class ScanPhotoTask extends
		AsyncTask<Void, Void, ArrayList<CategoryInfo>> {
	private ProgressDialog dialog;
	private Context context;

	public ScanPhotoTask(Context context, ArrayList<CategoryInfo> mDatas,
			CustomAdapter adapter) {
		super();
		this.context = context;
	}

	@Override
	protected ArrayList<CategoryInfo> doInBackground(Void... params) {
		ArrayList<CategoryInfo> data = getPhotoes();
		return data;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "", "扫描中……");
	}

	@Override
	protected void onPostExecute(ArrayList<CategoryInfo> result) {
		super.onPostExecute(result);
		dialog.dismiss();
		((MainActivity) context).notifyDataSetChanged(result);
	}

	private ArrayList<CategoryInfo> getPhotoes() {
		Cursor cursor = null;
		ArrayList<CategoryInfo> infos = new ArrayList<CategoryInfo>();
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA };
		cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, "",
				null, "");
		int folderIdColumn = cursor
				.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
		int folderColumn = cursor
				.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		int fileNameColumn = cursor
				.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
		int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		while (cursor.moveToNext()) {
			CategoryInfo categoryInfo = new CategoryInfo(
					cursor.getInt(folderIdColumn),
					cursor.getString(folderColumn));
			if (!infos.contains(categoryInfo)) {
				categoryInfo.setPhotoNum(1);
				categoryInfo.setThumbnailPath(cursor.getString(pathColumn));
				categoryInfo.addPhoto(cursor.getString(pathColumn));
				infos.add(categoryInfo);
			} else {
				infos.get(infos.indexOf(categoryInfo)).addPhotoNum();
				infos.get(infos.indexOf(categoryInfo)).addPhoto(
						cursor.getString(pathColumn));
			}
			System.out.println(cursor.getString(folderColumn) + "==="
					+ cursor.getString(fileNameColumn) + "==="
					+ cursor.getString(pathColumn));
		}
		if (cursor != null) {
			cursor.close();
		}
		return infos;

	}

}
