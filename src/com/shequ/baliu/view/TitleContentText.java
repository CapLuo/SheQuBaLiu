package com.shequ.baliu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleContentText extends ViewGroup {

	private Context mContext;

	private int mLeftPadding;
	private int mRightPadding;

	private int mSpacing;

	private List<View> mChildren;

	public TitleContentText(Context context) {
		super(context);
		init(context);
	}

	public TitleContentText(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public TitleContentText(Context context, AttributeSet attr, int sytle) {
		super(context, attr, sytle);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mChildren = new ArrayList<View>();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
	}

	public void setTitle(String title, OnClickListener l) {
		TextView title_tv = new TextView(mContext);
		title_tv.setText(title);
		title_tv.setSingleLine(true);
		if (null != l) {
			title_tv.setOnClickListener(l);
		}
		mChildren.add(title_tv);
		this.addView(title_tv);
	}

	public void setImage(Bitmap bm) {
		ImageView img = new ImageView(mContext);
		img.setImageBitmap(bm);
		mChildren.add(img);
		this.addView(img);
	}

	public void setText(String text) {
		TextView tv = new TextView(mContext);
		tv.setText(text);
		mChildren.add(tv);
		this.addView(tv);
	}

	public void removeAll() {
		mChildren.clear();
		this.removeAll();
	}
}
