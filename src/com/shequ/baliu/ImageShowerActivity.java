package com.shequ.baliu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shequ.baliu.dialog.ImageLoadingDialog;
import com.shequ.baliu.util.ShequTools;

public class ImageShowerActivity extends FragmentActivity {

	private ImageView mImageView;
	private ImageLoadingDialog mDialog;

	private DisplayImageOptions mOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageshower);

		mImageView = (ImageView) findViewById(R.id.image_show_content);
		ShequTools tools = new ShequTools(this);
		LayoutParams lp = mImageView.getLayoutParams();
		lp.height = tools.getDisplayMetricsWidth();
		mImageView.setLayoutParams(lp);

		String str = getIntent().getStringExtra("IMG_URI");
		if (TextUtils.isEmpty(str)) {
			finish();
		}
		str = str.replace("small", "big");

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisk(false).bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		ImageLoader.getInstance().displayImage(str, mImageView, mOptions);
		mDialog = new ImageLoadingDialog(this);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDialog.dismiss();
			}
		}, 1000 * 2);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
