package com.shequ.baliu.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterInsertImageBrowsing extends BaseAdapter {

	private Context mContext;

	private ArrayList<String> mList;
	private DisplayImageOptions mOptions;

	public AdapterInsertImageBrowsing(Context context) {
		mContext = context;
		mList = new ArrayList<String>();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	public void notifyDataList(ArrayList<String> list) {
		mList = list;
	}

	public void notifyDataList(String str) {
		mList = new ArrayList<String>();
		mList.add(str);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = new ImageView(mContext);
		}
		ImageLoader.getInstance().displayImage(mList.get(position),
				((ImageView) view), mOptions);
		view.setTag(mList.get(position));
		return view;
	}

}
