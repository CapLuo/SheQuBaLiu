package com.shequ.baliu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shequ.baliu.R;

public class AdapterRepairExpress extends BaseAdapter {

	private Context mContext;

	private List<DataHolder> mList;
	private int mPosition;

	public AdapterRepairExpress(Context context) {
		mContext = context;
		mList = new ArrayList<DataHolder>();
	}

	public void notifyDataList(int position) {
		mPosition = position;
		mList.clear();
		switch (position) {
		case 0:
			String[] repairs = mContext.getResources().getStringArray(
					R.array.shequ_repair);
			String[] repairs_phones = mContext.getResources().getStringArray(
					R.array.shequ_repair_phone);
			for (int i = 0; i < repairs.length; i++) {
				DataHolder data = new DataHolder();
				data.name = repairs[i];
				data.phone = repairs_phones[i];
				mList.add(data);
			}
			break;
		case 1:
			String[] expresses = mContext.getResources().getStringArray(
					R.array.shequ_express);
			String[] expresses_phones = mContext.getResources().getStringArray(
					R.array.shequ_express_phone);
			for (int i = 0; i < expresses.length; i++) {
				DataHolder data = new DataHolder();
				data.name = expresses[i];
				data.phone = expresses_phones[i];
				mList.add(data);
			}
			break;
		default:
			break;
		}
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_repair, null);
		}
		ImageView img = (ImageView) convertView
				.findViewById(R.id.list_repair_img);
		switch (mPosition) {
		case 0:
			img.setImageResource(R.drawable.ic_wx);
			break;
		case 1:
			img.setImageResource(R.drawable.ic_kd);
			break;
		default:
			break;
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.list_repair_name);
		name.setText(mList.get(position).name);
		TextView phone = (TextView) convertView
				.findViewById(R.id.list_repair_phone);
		name.setText(mList.get(position).name);
		phone.setText(mList.get(position).phone);

		convertView.setTag(mList.get(position));
		return convertView;
	}

	public class DataHolder {
		public String name;
		public String phone;
	}
}
