package com.shequ.baliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shequ.baliu.R;

public class AdapterHomeBusiness extends BaseAdapter {

	private String[] mBussiness;
	private Context mContext;

	public AdapterHomeBusiness(Context context, GridView view) {
		mContext = context;
		String[] bussinesses = context.getResources().getStringArray(
				R.array.shequ_business);
		mBussiness = new String[] { bussinesses[0], bussinesses[1],
				bussinesses[2], bussinesses[3], bussinesses[4],
				context.getString(R.string.home_business_more) };
		//fixViewHeight(view);
	}

	@Override
	public int getCount() {
		return mBussiness.length;
	}

	@Override
	public Object getItem(int position) {
		return mBussiness[position];
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
					R.layout.item_gridview_home, null);
			holder.name = (TextView) convertView.findViewById(R.id._item_text);
			holder.img = (ImageView) convertView.findViewById(R.id._item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mBussiness[position]);
		holder.img.setImageResource(choseDrawable(position));
		return convertView;
	}

	private int choseDrawable(int position) {
		String bussiness = mBussiness[position];
		if (bussiness.equals("邻里圈")) {
			return R.drawable.ic_llq;
		} else if (bussiness.equals("家政")) {
			return R.drawable.ic_jz;
		} else if (bussiness.equals("二手市场")) {
			return R.drawable.ic_essc;
		} else if (bussiness.equals("维修")) {
			return R.drawable.ic_wx;
		} else if (bussiness.equals("快递")) {
			return R.drawable.ic_kd;
		} else {
			return R.drawable.ic_more;
		}
	}

	static class ViewHolder {
		ImageView img;
		TextView name;
	}
/*	public void fixViewHeight(GridView gridview) {
		// 如果没有设置数据适配器，则ListView没有子项，返回。

		int totalHeight = 0;

		for (int i = 0, len = getCount() / gridview.get ; i < len; i++) {
			View itemview = getView(i, null, gridview);
			// 计算子项View 的宽高 线性布局
			itemview.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += itemview.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = gridview.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight;
		gridview.setLayoutParams(params);
	}*/
}
