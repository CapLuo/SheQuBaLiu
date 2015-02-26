package com.shequ.baliu.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class AdapterHomeAdvertImage extends PagerAdapter {

	private List<ImageView> mPageViews;

	public AdapterHomeAdvertImage(List<ImageView> list) {
		mPageViews = list;
	}

	@Override
	public int getCount() {
		return mPageViews.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mPageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mPageViews.get(arg1));
		return mPageViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

}
