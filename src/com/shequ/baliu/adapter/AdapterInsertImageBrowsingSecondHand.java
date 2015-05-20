package com.shequ.baliu.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shequ.baliu.R;
import com.shequ.baliu.util.ShequTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterInsertImageBrowsingSecondHand extends BaseAdapter {
	private Context mContext;

	private ArrayList<String> mList;
	private DisplayImageOptions mOptions;

	public AdapterInsertImageBrowsingSecondHand(Context context) {
		mContext = context;

		mList = new ArrayList<String>();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	public void notifyDataList(ArrayList<String> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		int size = mList.size();
		if (size >= 1) {
			return size;
		}
		return mList.size() + 1;
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
		if (position == mList.size()) {
			((ImageView) view).setImageResource(R.drawable.add_release_pic);
			view.setTag("");
		} else {
			String photo_url = mList.get(position);
			if (photo_url.startsWith("content")) {
				ImageLoader.getInstance().displayImage(mList.get(position),
						((ImageView) view), mOptions);
			} else {
				ImageView view_image = (ImageView) view;
				view_image
						.setImageBitmap(ShequTools.getLoacalBitmap(photo_url));
			}
			view.setTag(mList.get(position));
		}
		return view;
	}
}