package com.shequ.baliu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ShequGoodsBrowsing extends LinearLayout {

	public ShequGoodsBrowsing(Context context) {
		super(context);
	}

	public ShequGoodsBrowsing(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAdapter(BaseAdapter adapter) {
		int count = adapter.getCount();

		for (int i = 0; i < count; i++) {

		}
	}
}
