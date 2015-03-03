package com.shequ.baliu.view;

import com.shequ.baliu.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ConversationFragment extends Fragment implements OnClickListener {

	private View mContentView;

	private View mSendMessage;
	private EditText mEditText;
	private LinearLayout mContentMain;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_conversation,
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
		mSendMessage = mContentView.findViewById(R.id.conversation_commit);
		mSendMessage.setOnClickListener(this);

		mEditText = (EditText) mContentView
				.findViewById(R.id.conversation_edit);

		mContentMain = (LinearLayout) mContentView
				.findViewById(R.id.conversation_content_main);
	}

	private void initData() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.conversation_commit:
			TextView text = new TextView(getActivity());
			text.setBackgroundResource(R.drawable.chat_pop_send);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			int padding_right_in_dp = (int) getActivity().getResources()
					.getDimension(R.dimen.conversation_margin_left);
			int padding_top_in_dp = (int) getActivity().getResources()
					.getDimension(R.dimen.conversation_margin_top);
			final float scale = getResources().getDisplayMetrics().density;
			int padding_right_in_px = (int) (padding_right_in_dp * scale + 0.5f);
			int padding_top_in_px = (int) (padding_top_in_dp * scale + 0.5f);
			params.setMargins(0, padding_top_in_px, padding_right_in_px, 0);
			params.gravity = Gravity.RIGHT;
			text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity()
					.getResources()
					.getDimension(R.dimen.conversation_text_size));
			text.setText(mEditText.getText().toString());
			text.setLayoutParams(params);
			mContentMain.addView(text);
			mEditText.setText("");
			break;
		default:
			break;
		}
	}
}
