package com.shequ.baliu.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.shequ.baliu.R;
import com.shequ.baliu.holder.ShequSortModelHolder;

public class AdapterShequChoice extends BaseAdapter implements SectionIndexer {
	private List<ShequSortModelHolder> list;
	private Context mContext;

	public AdapterShequChoice(Context context, List<ShequSortModelHolder> list) {
		this.list = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_choice, null);
			holder.title = (TextView) view
					.findViewById(R.id._lable_item_list_title);
			holder.text = (TextView) view
					.findViewById(R.id._lable_item_list_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText(list.get(position).getSortLetters());
			holder.title.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_gray));
		} else {
			holder.title.setVisibility(View.GONE);
		}

		holder.text.setText(this.list.get(position).getName());

		return view;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	public void updateListView(List<ShequSortModelHolder> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public final static class ViewHolder {
		public TextView title;
		public TextView text;
	}
}