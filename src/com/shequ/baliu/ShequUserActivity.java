package com.shequ.baliu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.fragment.AboutFragment;
import com.shequ.baliu.fragment.FeedbackFragment;
import com.shequ.baliu.fragment.LoginFragment;
import com.shequ.baliu.fragment.MessageFragment;
import com.shequ.baliu.fragment.RegisterFragment;

public class ShequUserActivity extends FragmentActivity implements
		OnClickListener {

	private View mTitleBar;
	private TextView mTitle;
	private View mReturn;
	private TextView mTextButton;

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	private LoginFragment mLoginFragment;
	private RegisterFragment mRegisterFragment;
	private MessageFragment mMessageFragment;
	private FeedbackFragment mFeedBackFragment;
	private AboutFragment mAboutFragment;

	private int mCurrentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		initView();
		initData();
	}

	@Override
	protected void onPause() {
		StatService.onPageEnd(this, "ShequUserActivity");
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onPageStart(this, "ShequUserActivity");
	}

	private void initView() {
		mTitleBar = findViewById(R.id._title_bar);
		mTitle = (TextView) mTitleBar.findViewById(R.id._text_title);
		mReturn = mTitleBar.findViewById(R.id._return);
		mTextButton = (TextView) mTitleBar.findViewById(R.id._text_button);
	}

	private void initData() {
		mReturn.setOnClickListener(this);
		mTextButton.setOnClickListener(this);

		mFragmentManager = getSupportFragmentManager();
		Intent intent = getIntent();
		int index = intent.getExtras().getInt("index", 0);
		setChoiceFragment(index);
	}

	public void setChoiceFragment(int index) {
		mTextButton.setVisibility(View.INVISIBLE);
		mCurrentIndex = index;
		mTransaction = mFragmentManager.beginTransaction();
		if (index == 0) {
			mTitle.setText(R.string.login_title);
			if (mLoginFragment == null) {
				mLoginFragment = new LoginFragment();
			}
			mTransaction.replace(R.id.content_main, mLoginFragment);
		}
		if (index == 1) {
			mTitle.setText(R.string.user_message);
			if (mMessageFragment == null) {
				mMessageFragment = new MessageFragment();
			}
			mTransaction.replace(R.id.content_main, mMessageFragment);
		}
		if (index == 2) {
			mTitle.setText(R.string.user_feedback);
			mTextButton.setVisibility(View.VISIBLE);
			mTextButton.setText(getResources().getString(
					R.string.feedback_submit));
			if (mFeedBackFragment == null) {
				mFeedBackFragment = new FeedbackFragment();
			}
			mTransaction.replace(R.id.content_main, mFeedBackFragment);
		}
		if (index == 3) {
			mTitle.setText(R.string.register_new);
			if (mRegisterFragment == null) {
				mRegisterFragment = new RegisterFragment();
			}
			mTransaction.replace(R.id.content_main, mRegisterFragment);
		}
		if (index == 4) {
			mTitle.setText(R.string.user_about_us);
			if (mAboutFragment == null) {
				mAboutFragment = new AboutFragment();
			}
			mTransaction.replace(R.id.content_main, mAboutFragment);
		}
		mTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._return:
			returnPage();
			break;
		case R.id._text_button:
			if (mFeedBackFragment != null) {
				mFeedBackFragment.getInputContent();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		returnPage();
	}

	private void returnPage() {
		if (mCurrentIndex == 3) {
			setChoiceFragment(0);
		} else {
			super.onBackPressed();
		}
	}
}
