package com.shequ.baliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shequ.baliu.R;

public class AdapterCommunity extends BaseAdapter {

	private Context mContext;
	private String[] mCommunityBusiness;

	public AdapterCommunity(Context context) {
		mContext = context;

		mCommunityBusiness = mContext.getResources().getStringArray(
				R.array.shequ_business);
	}

	@Override
	public int getCount() {
		return mCommunityBusiness.length;
	}

	@Override
	public Object getItem(int position) {
		return mCommunityBusiness[position];
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
					R.layout.item_list_community, null);
			holder.img = (ImageView) convertView.findViewById(R.id._item_img);
			holder.name = (TextView) convertView.findViewById(R.id._item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setImageResource(choseDrawable(position));
		holder.name.setText(mCommunityBusiness[position]);
		return convertView;
	}

	private int choseDrawable(int position) {
		String bussiness = mCommunityBusiness[position];
		if (bussiness.equals("邻里圈")) {
			return R.drawable.ic_llq;
		} else if (bussiness.equals("二手市场")) {
			return R.drawable.ic_essc;
		} else if (bussiness.equals("维修")) {
			return R.drawable.ic_wx;
		} else if (bussiness.equals("快递")) {
			return R.drawable.ic_kd;
		} else if (bussiness.equals("家政")) {
			return R.drawable.ic_jz;
		} else if (bussiness.equals("缴费")) {
			return R.drawable.ic_jf;
		} else if (bussiness.equals("便利店")) {
			return R.drawable.ic_bld;
		} else if (bussiness.equals("果蔬")) {
			return R.drawable.ic_sg;
		} else if (bussiness.equals("健康")) {
			return R.drawable.ic_jk;
		} else if (bussiness.equals("教育")) {
			return R.drawable.ic_jy;
		} else {
			return R.drawable.ic_jr;
		}
	}

	static class ViewHolder {
		ImageView img;
		TextView name;
	}
}
