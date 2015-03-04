package com.shequ.baliu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shequ.baliu.R;
import com.shequ.baliu.holder.PersonInfo;

public class AdapterNeighbour extends BaseAdapter {

	private List<PersonInfo> mList;
	private Context mContext;

	private DisplayImageOptions mOptions;

	public AdapterNeighbour(Context context) {
		mContext = context;
		mList = new ArrayList<PersonInfo>();

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	public void notifyDataList(List<PersonInfo> list) {
		mList = list;
		this.notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.item_list_neighbour, null);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.neighbour_img);
		TextView name = (TextView) convertView
				.findViewById(R.id.neighbour_name);
		String img_url = mList.get(position).getPhoto();
		ImageLoader.getInstance().displayImage(img_url, img, mOptions);
		if (TextUtils.isEmpty(img_url)) {
			img.setImageResource(R.drawable.user_head_default);
		}
		name.setText(mList.get(position).getNickName());
		return convertView;
	}

}
