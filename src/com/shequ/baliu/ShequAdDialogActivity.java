package com.shequ.baliu;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.shequ.baliu.adapter.AdapterHomeAdvertImage;
import com.shequ.baliu.view.CirclePageIndicator;
import com.shequ.baliu.view.ShequViewPage;

public class ShequAdDialogActivity extends FragmentActivity implements
		OnGestureListener {

	private ShequViewPage mAd;
	private AdapterHomeAdvertImage mAdadapter;
	private GestureDetector mDetector;

	private float distance = 100;

	private int index = 0;
	private int[] ads = { R.drawable.ad_1, R.drawable.ad_2, R.drawable.ad_3,
			R.drawable.ad_4, R.drawable.ad_5 };

	private CirclePageIndicator mPageIndicator;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activity_show_ad_dialog);

		mDetector = new GestureDetector(this);
		mAd = (ShequViewPage) findViewById(R.id.show_ad_image_viewpage);
		mAdadapter = new AdapterHomeAdvertImage(addImageView());
		mAd.setAdapter(mAdadapter);

		mPageIndicator = (CirclePageIndicator) findViewById(R.id.show_ad__image_indicator);
		mPageIndicator.setViewPager(mAd);
	}

	private ArrayList<ImageView> addImageView() {
		ArrayList<ImageView> list = new ArrayList<ImageView>();
		for (int i = 0; i < ads.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(ads[i]);
			iv.setScaleType(ScaleType.FIT_XY);
			list.add(iv);
		}
		return list;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if (e1.getX() - e2.getX() > distance) {
			// mAd.setInAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_left_in));
			// mAd.setOutAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_left_out));
			index = index + 1;
			if (index == ads.length) {
				index = ads.length - 1;
				ShequAdDialogActivity.this.finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
				return true;
			}
			mAd.setCurrentItem(index);
			return true;
		} else if (e1.getX() - e2.getX() < (0 - distance)) {
			index = index - 1;
			if (index < 0) {
				index = 0;
			}
			mAd.setCurrentItem(index);
			return true;
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		if (index == ads.length - 1) {
			ShequAdDialogActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return this.mDetector.onTouchEvent(ev);
	}

}
