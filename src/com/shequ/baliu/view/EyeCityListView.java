package com.shequ.baliu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class EyeCityListView extends ListView {

	public EyeCityListView(Context context) {
		super(context);
	}

	public EyeCityListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EyeCityListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
