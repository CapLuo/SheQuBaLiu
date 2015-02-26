package com.shequ.baliu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shequ.baliu.R;
import com.shequ.baliu.holder.ShequEyeCityHolder;

public class AdpterEyeCity extends BaseAdapter {

	private Context mContext;
	private List<ShequEyeCityHolder> mList;

	public AdpterEyeCity(Context context) {
		mContext = context;
		mList = new ArrayList<ShequEyeCityHolder>();
	}

	public void addData(List<ShequEyeCityHolder> list, ListView listView) {
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(ShequEyeCityHolder holder, ListView listView) {
		mList.add(holder);
		notifyDataSetChanged();
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
				R.layout.item_list_eye_city, null);
		TextView text = (TextView) convertView.findViewById(R.id.content);
		text.setText(mList.get(position).getTitle());

		TextView line = (TextView) convertView.findViewById(R.id.line);
		if (position == getCount() - 1) {
			line.setVisibility(View.INVISIBLE);
		} else {
			line.setVisibility(View.VISIBLE);
		}

		convertView.setTag(mList.get(position));
		return convertView;
	}

	/*public void fixViewHeight(ListView listView) {
		// 如果没有设置数据适配器，则ListView没有子项，返回。
		int totalHeight = 0;
		for (int i = 0, len = getCount(); i < len; i++) {
			View listViewItem = getView(i, null, listView);
			// 计算子项View 的宽高 线性布局
			listViewItem.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += listViewItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (getCount()));
		listView.setLayoutParams(params);
	}*/
}
