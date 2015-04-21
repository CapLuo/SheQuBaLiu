package com.shequ.baliu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShequReleaseActivity extends FragmentActivity implements
		OnClickListener {

	private View mTitleLayout;
	private TextView mTitleText;
	private TextView mTextButton;
	private View mReturn;
	private View mImageButton;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activity_release_second);

		initView();
	}

	private void initView() {
		mTitleLayout = findViewById(R.id._title_bar);
		mReturn = mTitleLayout.findViewById(R.id._return);
		mReturn.setOnClickListener(this);
		mTitleText = (TextView) mTitleLayout.findViewById(R.id._text_title);
		mTitleText.setText(R.string.release_title);
		mTextButton = (TextView) mTitleLayout.findViewById(R.id._text_button);
		mTextButton.setVisibility(View.VISIBLE);
		mTextButton.setText(R.string.release_text);
		mTextButton.setOnClickListener(this);
		mImageButton = mTitleLayout.findViewById(R.id.community_mode);
		mImageButton.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id._return:
			this.onBackPressed();
			break;
		case R.id._text_button:
			break;
		default:
			break;
		}
	}
}