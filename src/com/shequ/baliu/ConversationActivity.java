package com.shequ.baliu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shequ.baliu.view.ConversationFragment;

public class ConversationActivity extends FragmentActivity implements
		OnClickListener {

	private FragmentManager mManager;
	private FragmentTransaction mTransaction;

	private ConversationFragment mConversationPage;

	private View mTitleBar;
	private View mReturn;
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activity_conversation);

		initView();
		initData();
	}

	private void initView() {
		mManager = getSupportFragmentManager();

		mTitleBar = findViewById(R.id._title_bar);
		mReturn = mTitleBar.findViewById(R.id._return);
		mReturn.setOnClickListener(this);
		mTitle = (TextView) mTitleBar.findViewById(R.id._text_title);
	}

	private void initData() {
		setChoiceFragment();

		Intent intent = getIntent();
		String title = intent.getExtras().getString("Title");
		if (!TextUtils.isEmpty(title)) {
			mTitle.setText(title);
		}
	}

	private void setChoiceFragment() {
		mTransaction = mManager.beginTransaction();
		if (mConversationPage == null) {
			mConversationPage = new ConversationFragment();
		}
		mTransaction.replace(R.id._content_main, mConversationPage);
		mTransaction.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id._return:
			onBackPressed();
			break;
		default:
			break;
		}
	}
}
