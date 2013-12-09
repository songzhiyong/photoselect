package com.jerome.photoselect;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.jerome.photoselect.adapter.CustomAdapter;
import com.jerome.photoselect.beans.CategoryInfo;
import com.jerome.photoselect.biz.ImageFolderListLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * LoderCursor.java
 * 
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-9 		Administrator
 *
 * Copyright (c) 2013, JEROME All Rights Reserved.
 */

/**
 * ClassName:LoderCursor<br>
 * Function: Demonstration of the use of a CursorLoader to load and display
 * images data in a fragment.
 * 
 * @author Jerome Song
 * @version
 * @Date 2013-12-9 下午2:39:23
 * 
 * @see
 */
public class LoaderLoadActivity extends FragmentActivity {
	private static final int REQUEST_CODE_SELECT_PHOTOES = 1;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ImageLoaderConfiguration config = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(config);
		
		FragmentManager fm = getSupportFragmentManager();
		setContentView(R.layout.activity_loader);
		FolderListFragment list = new FolderListFragment();
		fm.beginTransaction().add(R.id.content, list).commit();
	}
	

	public static class FolderListFragment extends ListFragment implements
			LoaderManager.LoaderCallbacks<List<CategoryInfo>> {

		CustomAdapter mAdapter;
		ArrayList<String> selectedPhotoes;
		String mCurFilter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setEmptyText("沒有照片");
			setHasOptionsMenu(true);
			selectedPhotoes = new ArrayList<String>();
			mAdapter = new CustomAdapter(new ArrayList<CategoryInfo>());
			setListAdapter(mAdapter);

			setListShown(false);

			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Intent intent = new Intent(getActivity(),
					GridSelectActivity.class);
			intent.putExtra("category", (CategoryInfo)mAdapter.getItem(position));
			intent.putExtra("selected", selectedPhotoes);
			startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTOES);
		}

		@Override
		public Loader<List<CategoryInfo>> onCreateLoader(int id, Bundle args) {
			return new ImageFolderListLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<CategoryInfo>> loader,
				List<CategoryInfo> data) {
			mAdapter.setData(data);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<CategoryInfo>> loader) {
			mAdapter.setData(null);
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (data != null) {
				selectedPhotoes.clear();
				selectedPhotoes.addAll(data.getStringArrayListExtra("result"));
				Log.i("result", "" + data.getSerializableExtra("result"));
				Toast.makeText(getActivity(), "" + data.getSerializableExtra("result"),
						Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
