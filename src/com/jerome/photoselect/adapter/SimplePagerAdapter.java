/**
 * SimplePagerAdapter.java
 * com.jerome.photoselect.adapter
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-5 		Administrator
 *
 * Copyright (c) 2013, JEROME All Rights Reserved.
 */

package com.jerome.photoselect.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * ClassName:SimplePagerAdapter Function: TODO ADD FUNCTION
 * 
 * @author Administrator
 * @version
 * @Date 2013-12-5 下午4:31:43
 * 
 * @see
 */
public class SimplePagerAdapter extends PagerAdapter {
	private ArrayList<View> views = new ArrayList<View>();

	@Override
	public int getItemPosition(Object object) {
		int index = views.indexOf(object);
		if (index == -1)
			return POSITION_NONE;
		else
			return index;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = views.get(position);
		container.addView(v);
		return v;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public int addView(View v) {
		return addView(v, views.size());
	}

	public int addView(View v, int position) {
		views.add(position, v);
		return position;
	}

	public int removeView(ViewPager pager, View v) {
		return removeView(pager, views.indexOf(v));
	}

	public int removeView(ViewPager pager, int position) {
		pager.setAdapter(null);
		views.remove(position);
		pager.setAdapter(this);

		return position;
	}

	public View getView(int position) {
		return views.get(position);
	}

}
