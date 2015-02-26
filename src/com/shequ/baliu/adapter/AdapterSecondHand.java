package com.shequ.baliu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.shequ.baliu.holder.SecondHandGoods;

public class AdapterSecondHand extends BaseAdapter {

	private Context mContext;
	private List<SecondHandGoods> goods;
	private DisplayImageOptions mOptions;

	public AdapterSecondHand(Context context) {
		mContext = context;
		goods = new ArrayList<SecondHandGoods>();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	public void notifyDataListAll(List<SecondHandGoods> good) {
		goods = good;
		this.notifyDataSetChanged();
	}

	public void notifyDataList(List<SecondHandGoods> good) {
		goods.addAll(good);
		this.notifyDataSetChanged();
	}

	public void notifyDataList(SecondHandGoods good) {
		goods.add(good);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return goods.size();
	}

	@Override
	public Object getItem(int position) {
		return goods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_secondhand, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id._item_secondhand_img);
			// holder.time = (TextView)
			// convertViews.findViewById(R.id._item_secondhand_time);
			holder.title = (TextView) convertView
					.findViewById(R.id._item_secondhand_title);
			holder.price = (TextView) convertView
					.findViewById(R.id._item_secondhand_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(goods.get(position).getPhoto(),
				holder.img, mOptions);
		holder.title.setText(goods.get(position).getTitle());

		/*
		 * Date date = new
		 * Date(Long.parseLong(goods.get(position).getAddTime())); Date nowDate
		 * = new Date(); long timelong = nowDate.getTime() - date.getTime(); if
		 * (timelong < 60 * 60 * 24 * 1000) { SimpleDateFormat sdf = new
		 * SimpleDateFormat("HH:mm");
		 * sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		 * holder.time.setText(sdf.format(date)); } else { SimpleDateFormat sdf
		 * = new SimpleDateFormat("yyyy-MM-dd");
		 * sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		 * holder.time.setText(sdf.format(date)); }
		 */
		holder.price.setText(mContext.getResources().getString(R.string.money)
				+ goods.get(position).getPrice());

		return convertView;
	}

	static class ViewHolder {
		public ImageView img;
		// public TextView time;
		public TextView title;
		public TextView price;
	}
}
