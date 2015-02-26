package com.shequ.baliu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ShequScrollView extends ScrollView {

	public ShequScrollView(Context context) {
		super(context);
	}

	public ShequScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShequScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

}
