package com.shequ.baliu.view;

import com.shequ.baliu.R;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class ImageBrowsingLayout extends LinearLayout {

	private Context mContext;

	private ShowInsertImg mShowInsertImg = new ShowInsertImg() {

		@Override
		public void showImg(Uri uri, View v) {
		}
	};

	public ImageBrowsingLayout(Context context) {
		super(context);

		mContext = context;
	}

	public ImageBrowsingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
	}

	public void setShowImgInterface(ShowInsertImg img) {
		mShowInsertImg = img;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.removeAllViews();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);
			final float scale = getResources().getDisplayMetrics().density;
			int dWitdh = (int) mContext.getResources().getDimension(
					R.dimen.second_good_page_width);
			int witdh = (int) (dWitdh * scale + 0.5f);
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
					witdh, witdh);
			lparams.gravity = Gravity.CENTER_VERTICAL;
			lparams.leftMargin = 20;
			v.setLayoutParams(lparams);

			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					Uri uri = (Uri) view.getTag();
					mShowInsertImg.showImg(uri, view);
				}
			});
			((ImageView) v).setScaleType(ScaleType.CENTER_CROP);
			addView(v);
		}
	}

	public interface ShowInsertImg {
		public void showImg(Uri uri, View v);
	}
}
