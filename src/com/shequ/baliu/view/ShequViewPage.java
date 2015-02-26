package com.shequ.baliu.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ShequViewPage extends ViewPager {

	public ShequViewPage(Context context) {
		super(context);
	}

	public ShequViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return super.onTouchEvent(arg0);
	}

}
