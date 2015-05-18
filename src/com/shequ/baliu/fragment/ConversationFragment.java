package com.shequ.baliu.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.holder.MessageInfo;
import com.shequ.baliu.util.DBManager;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

	private String mTouserid;
	private String mUserid;
	private String mName;

	private DBManager mDBManager;

	private String mMessageId = "0";
	private boolean isNeedGetMessage;

	private BroadcastReceiver mBroadCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				if (isNeedGetMessage) {
					getReceiveMessageFromNet();
				}
			}
		}

	};

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "ConversationFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "ConversationFragment");
	}

	public ConversationFragment(String userid, String id, String name) {
		mTouserid = id;
		mUserid = userid;
		mName = name;
		isNeedGetMessage = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView();
		initData();

		return mContentView;
	}

	@Override
	public void onAttach(Activity activity) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		activity.registerReceiver(mBroadCast, filter);
		super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(mBroadCast);
		super.onDestroyView();
	}

	private void initView() {
	}

	private void initData() {
		mDBManager = new DBManager(getActivity());

		List<MessageInfo> mMessages = mDBManager.queryMessage("9");
		for (MessageInfo info : mMessages) {
			addChildeInContentMain(info.getMessage(),
					info.getSendid().equals(mUserid));
			mMessageId = info.getId();
		}
		getMessageForNet();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		default:
			break;
		}
	}

	private void insertMessageForNet(final MessageInfo info) {
		SqlHelper.add(
				StaticVariableSet.USER_MESSAGE,
				" (`userid`, `touserid`, `type`, `content`, `isread`, `addtime`) VALUES ('"
						+ mUserid + "', '" + mTouserid + "', '" + 9 + "', '"
						+ info.getMessage() + "', '" + 1 + "', '"
						+ info.getTime() + "')",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e(StaticVariableSet.TAG,
								"---------> Http request login sucess.");
						getInsertMessageId(info);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e(StaticVariableSet.TAG,
								"---------> Http request login failure.");
					}
				});
	}

	private void getMessageForNet() {
		SqlHelper.get(StaticVariableSet.USER_MESSAGE, " (`userid` = " + mUserid
				+ " OR `userid` = " + mTouserid + ") AND (`touserid` = "
				+ mTouserid + " OR `touserid` = " + mUserid + ") AND `type` = "
				+ 9 + " AND " + mMessageId
				+ " < `messageid` ORDER BY `messageid`",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							ArrayList<MessageInfo> infos = new ArrayList<MessageInfo>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								MessageInfo info = MessageInfo.parserJson(json,
										true);
								addChildeInContentMain(info.getMessage(), info
										.getSendid().equals(mUserid));
								infos.add(info);
								mMessageId = info.getId();
							}
							mDBManager.addMessages(infos);
							isNeedGetMessage = true;
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						isNeedGetMessage = true;
						Log.e(StaticVariableSet.TAG, throwable.getMessage());
					}

				});
	}

	private void getReceiveMessageFromNet() {
		SqlHelper.get(StaticVariableSet.USER_MESSAGE, " `userid` = "
				+ mTouserid + " AND `touserid` = " + mUserid + " AND `type` = "
				+ 9 + " AND " + mMessageId
				+ " < `messageid` ORDER BY `messageid`",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							ArrayList<MessageInfo> infos = new ArrayList<MessageInfo>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								MessageInfo info = MessageInfo.parserJson(json,
										true);
								addChildeInContentMain(info.getMessage(), info
										.getSendid().equals(mUserid));
								infos.add(info);
								mMessageId = info.getId();
							}
							mDBManager.addMessages(infos);
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});
	}

	private void getInsertMessageId(final MessageInfo info) {
		SqlHelper.get(
				"`messageid`",
				StaticVariableSet.USER_MESSAGE,
				" `userid` = " + mUserid + " AND `touserid` = " + mTouserid
						+ " AND `type` = " + 9 + " AND `content` = '"
						+ info.getMessage() + "' ORDER BY `messageid`",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						if (response.length() != 0) {
							for (int i = 0; i < response.length(); i++) {
								JSONObject json;
								try {
									json = response.getJSONObject(i);
									info.setID(json.getString("messageid"));
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}
						mDBManager.addMessage(info);
					}

				});
	}

	// 补全发送的 消息
	private MessageInfo fullSendMessage(String content) {
		MessageInfo info = new MessageInfo();
		info.setMessage(content);
		info.setReceiveid(mTouserid);
		info.setSendid(mUserid);
		info.setSendname(mName);
		long time = new Date().getTime();
		info.setTime(String.valueOf((long) (time / 1000)));
		return info;
	}

	private void addChildeInContentMain(String content, boolean isSend) {
		TextView text = new TextView(getActivity());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		int padding_right_in_dp = (int) getActivity().getResources()
				.getDimension(R.dimen.conversation_margin_left);
		int padding_top_in_dp = (int) getActivity().getResources()
				.getDimension(R.dimen.conversation_margin_top);
		final float scale = getResources().getDisplayMetrics().density;
		int padding_right_in_px = (int) (padding_right_in_dp * scale + 0.5f);
		int padding_top_in_px = (int) (padding_top_in_dp * scale + 0.5f);
		if (isSend) {
			params.setMargins(0, padding_top_in_px, padding_right_in_px, 0);
			params.gravity = Gravity.RIGHT;
			text.setBackgroundResource(R.drawable.chat_pop_send);
		} else {
			params.setMargins(padding_right_in_px, padding_top_in_px, 0, 0);
			params.gravity = Gravity.LEFT;
			text.setBackgroundResource(R.drawable.chat_pop_recieve_sel);
		}
		text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity()
				.getResources().getDimension(R.dimen.conversation_text_size));
		text.setText(content);
		text.setLayoutParams(params);
		mContentMain.addView(text);
	}
}
