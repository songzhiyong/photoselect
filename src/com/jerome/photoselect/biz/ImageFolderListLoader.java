package com.jerome.photoselect.biz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import com.jerome.photoselect.beans.CategoryInfo;

public class ImageFolderListLoader extends AsyncTaskLoader<List<CategoryInfo>> {

	List<CategoryInfo> mDatas;
	Context context;

	public ImageFolderListLoader(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public List<CategoryInfo> loadInBackground() {
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

	@Override
	public void deliverResult(List<CategoryInfo> categories) {
		if (isReset()) {
			if (categories != null) {
				onReleaseResources(categories);
			}
		}
		List<CategoryInfo> oldApps = categories;
		mDatas = categories;
		if (isStarted()) {
			super.deliverResult(categories);
		}

		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mDatas != null) {
			deliverResult(mDatas);
		}
		if (takeContentChanged() || mDatas == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(List<CategoryInfo> categories) {
		super.onCanceled(categories);
		onReleaseResources(categories);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (mDatas != null) {
			onReleaseResources(mDatas);
			mDatas = null;
		}

	}

	protected void onReleaseResources(List<CategoryInfo> categories) {
	}
}
