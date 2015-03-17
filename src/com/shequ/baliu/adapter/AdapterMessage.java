package com.shequ.baliu.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shequ.baliu.R;
import com.shequ.baliu.holder.MessageInfo;

public class AdapterMessage extends BaseAdapter {

	private Context mContext;
	private List<MessageInfo> mList;

	private int index = -1;

	public AdapterMessage(Context context) {
		mContext = context;
		mList = new ArrayList<MessageInfo>();
	}

	public void notifyDataSetListAll(List<MessageInfo> list) {
		mList = list;
		this.notifyDataSetChanged();
	}

	public void notifyDataSetList(List<MessageInfo> list) {
		mList.addAll(list);
		this.notifyDataSetChanged();
	}

	public void notifyDataSetList(MessageInfo info) {
		mList.add(info);
		this.notifyDataSetChanged();
	}

	public void setIndex(int index) {
		this.index = index;
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_message, null);
			holder.time = (TextView) convertView
					.findViewById(R.id.message_time);
			holder.name = (TextView) convertView
					.findViewById(R.id.message_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.message_contend);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Date date = new Date(
				Long.parseLong(mList.get(position).getTime()) * 1000);
		Date nowDate = new Date();
		long timelong = nowDate.getTime() - date.getTime();
		if (timelong < 60 * 60 * 24 * 1000) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			holder.time.setText(sdf.format(date));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			holder.time.setText(sdf.format(date));
		}

		String nickname = mList.get(position).getSendname();
		if (nickname == null || nickname.equals("") || nickname.equals("admin")) {
			holder.name.setText(R.string.user_admin);
		} else {
			holder.name.setText(mList.get(position).getSendname());
		}
		holder.name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		holder.content.setText(mList.get(position).getMessage());
		if (position == mList.size() - 1) {
			convertView.findViewById(R.id.message_list_line).setVisibility(
					View.INVISIBLE);
		}
		if (index == position) {
			// content.set
			convertView.setBackgroundResource(R.color.list_gray);
		} else {
			holder.content.setSingleLine();
			convertView.setBackgroundResource(R.color.white);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView time;
		TextView name;
		TextView content;
	}
}
