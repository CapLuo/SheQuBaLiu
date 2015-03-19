package com.shequ.baliu.fragment;

import java.util.Date;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FeedbackFragment extends Fragment {

	private View mContentView;

	private EditText mEditContent;
	private EditText mEditContact;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_feedback,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView();
		initData();

		return mContentView;
	}

	private void initView() {
		mEditContent = (EditText) mContentView
				.findViewById(R.id.feedback_content_edittext);
		mEditContact = (EditText) mContentView
				.findViewById(R.id.feedback_contact_edittext);
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "FeedbackFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "FeedbackFragment");
	}

	private void initData() {

	}

	public void getInputContent() {
		String content = mEditContent.getText().toString();
		if (content == null || content.equals("")) {
			mEditContent.requestFocus();
			return;
		}

		String contact = mEditContact.getText().toString();
		if (contact == null || contact.equals("")) {
			mEditContact.requestFocus();
			return;
		}

		Date date = new Date();
		ShequApplication app = (ShequApplication) (getActivity()
				.getApplication());
		String name = app.getInfo().getUsername();
		if (name == null || name.equals("")) {
			name = "游客";
		}
		SqlHelper
				.add(StaticVariableSet.FEED_BACK,
						"(`title`, `phone`, `content`, `name`, `updatetime`, `creattime`) VALUES ('来自手机APP端', '"
								+ contact
								+ "', '"
								+ content
								+ "', '"
								+ name
								+ "', '"
								+ date.getTime()
								+ "', '"
								+ date.getTime() + "')");
		getActivity().onBackPressed();
	}
}
