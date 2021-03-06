package com.shequ.baliu.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
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
import com.shequ.baliu.ImageShowerActivity;
import com.shequ.baliu.R;
import com.shequ.baliu.holder.SecondHandGoods;
import com.shequ.baliu.view.ImageBrowsingLayout;
import com.shequ.baliu.view.ImageBrowsingLayout.ShowInsertImg;

public class AdapterSecondHand extends BaseAdapter {

	private Context mContext;
	private List<SecondHandGoods> goods;
	private DisplayImageOptions mOptions;

	private AdapterInsertImageBrowsing mImageAdapter;

	public AdapterSecondHand(Context context) {
		mContext = context;
		goods = new ArrayList<SecondHandGoods>();
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		mImageAdapter = new AdapterInsertImageBrowsing(context);
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
			holder.head = (ImageView) convertView
					.findViewById(R.id._item_secondhand_head_img);
			holder.name = (TextView) convertView
					.findViewById(R.id._item_secondhand_name);
			holder.groupname = (TextView) convertView
					.findViewById(R.id._item_secondhand_group);
			holder.imgLayout = (ImageBrowsingLayout) convertView
					.findViewById(R.id._item_secondhand_img_browsing);
			holder.time = (TextView) convertView
					.findViewById(R.id._item_secondhand_time);
			holder.title = (TextView) convertView
					.findViewById(R.id._item_secondhand_title);
			holder.oldPrice = (TextView) convertView
					.findViewById(R.id._item_second_old_price);
			holder.newPrice = (TextView) convertView
					.findViewById(R.id._item_second_price);
			holder.priceLayout = convertView
					.findViewById(R.id._item_secondhand_price_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.priceLayout.setVisibility(View.GONE);
		mImageAdapter.notifyDataList(goods.get(position).getPhoto());
		holder.imgLayout.setAdapter(mImageAdapter);
		holder.imgLayout.setShowImgInterface(new ShowInsertImg() {

			@Override
			public void showImg(String uri) {
				Intent intent = new Intent();
				intent.setClass(mContext, ImageShowerActivity.class);
				intent.putExtra("IMG_URI", uri);
				mContext.startActivity(intent);
			}
		});
		holder.head.setImageResource(R.drawable.user_head_default);
		if (!TextUtils.isEmpty(goods.get(position).getHeadphoto())) {
			ImageLoader.getInstance().displayImage(
					goods.get(position).getHeadphoto(), holder.head, mOptions);
		}
		holder.title.setText(goods.get(position).getTitle());
		holder.name.setText(goods.get(position).getNickname());
		holder.groupname.setText(goods.get(position).getGroupname());

		Date date = new Date(
				Long.parseLong(goods.get(position).getUpdateTime()) * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		holder.time.setText(format.format(date));

		return convertView;
	}

	static class ViewHolder {
		public ImageBrowsingLayout imgLayout;
		public ImageView head;
		public TextView name;
		public TextView groupname;
		public TextView time;
		public TextView title;
		public TextView newPrice;
		public TextView oldPrice;
		// price layout
		public View priceLayout;
	}

}
