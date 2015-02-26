package com.shequ.baliu.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewControlFlying extends ScrollView {

	public enum SlideMode {
		NONE, VERTICAL, HORIZONTAL
	}

	private SlideMode mSlide = SlideMode.NONE;

	public ScrollViewControlFlying(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollViewControlFlying(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewControlFlying(Context context) {
		super(context);
	}

	public void setSlideMode(SlideMode mode) {
		mSlide = mode;
	}

	private PointF mDownPointF = new PointF();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownPointF.x = event.getX();
			mDownPointF.y = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 防止屏幕坏点
			if (0 == (int) mDownPointF.x) {
				mDownPointF.x = event.getX();
				mDownPointF.y = event.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
			float distanceX = Math.abs(event.getX() - mDownPointF.x);
			float distanceY = Math.abs(event.getY() - mDownPointF.y);
			mDownPointF.x = 0;
			mDownPointF.y = 0;
			if (distanceX > distanceY) {
				// 左右翻页滑动
			} else {
				// 上下滑动
			}
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

}
