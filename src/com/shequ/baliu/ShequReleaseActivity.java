package com.shequ.baliu;

import com.shequ.baliu.fragment.ReleaseFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

	private ReleaseFragment mReleaseFragment;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

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
		mTextButton.setVisibility(View.INVISIBLE);
		mImageButton = mTitleLayout.findViewById(R.id.community_mode);
		mImageButton.setVisibility(View.INVISIBLE);

		mFragmentManager = getSupportFragmentManager();
		setChoiceFragment(0);
	}

	private void setChoiceFragment(int flag) {
		mFragmentTransaction = mFragmentManager.beginTransaction();
		hideAllFragment(mFragmentTransaction);
		switch (flag) {
		case 0:
			if (mReleaseFragment == null) {
				mReleaseFragment = new ReleaseFragment();
				mFragmentTransaction.add(R.id._content_main, mReleaseFragment);
			}
			mFragmentTransaction.show(mReleaseFragment);
			break;
		default:
			break;
		}
		mFragmentTransaction.commit();
	}

	private void hideAllFragment(FragmentTransaction transaction) {
		if (mReleaseFragment != null) {
			transaction.hide(mReleaseFragment);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id._return:
			this.onBackPressed();
			break;
		default:
			break;
		}
	}
}