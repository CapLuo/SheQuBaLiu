package com.shequ.baliu.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.adapter.AdapterMessage;
import com.shequ.baliu.holder.MessageInfo;
import com.shequ.baliu.util.DBManager;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class MessageFragment extends Fragment implements OnItemClickListener {

	private View mContentView;

	private ListView mMessageListView;
	private AdapterMessage mAdapter;

	private DBManager mDBManager;

	private List<MessageInfo> mMessages;

	private String mLastAddTime = "0";

	private BroadcastReceiver mBroadCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				if (mLastAddTime.equals("0")) {
					initData();
				} else {
					updateDataFromNet();
				}
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_message,
					container, false);
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
		mDBManager = new DBManager(getActivity());

		mMessageListView = (ListView) mContentView
				.findViewById(R.id.message_list);
		mAdapter = new AdapterMessage(getActivity());
		mMessageListView.setAdapter(mAdapter);
	}

	private void initData() {
		mMessages = mDBManager.queryMessage();
		mLastAddTime = mDBManager.lastAddTime;
		if (mMessages.isEmpty()) {
			updateDataFromNet();
		} else {
			mAdapter.notifyDataSetListAll(mMessages);
		}

		mMessageListView.setOnItemClickListener(this);
	}

	private void updateDataFromNet() {
		String touserid = ((ShequApplication) (getActivity().getApplication()))
				.getInfo().getUserId();
		SqlHelper.getMessage(StaticVariableSet.USER_MESSAGE,
				StaticVariableSet.USER_INFO, "userid", "touserid = " + touserid
						+ " AND `" + StaticVariableSet.USER_MESSAGE
						+ "`.addtime > " + mLastAddTime + " ORDER BY `"
						+ StaticVariableSet.USER_MESSAGE + "`.addtime",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							List<MessageInfo> messages = new ArrayList<MessageInfo>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								MessageInfo info = MessageInfo.parserJson(json,
										false);
								mLastAddTime = info.getTime();
								messages.add(info);
							}
							if (mMessages.isEmpty()) {
								mMessages = messages;
								mAdapter.notifyDataSetListAll(mMessages);
							} else {
								mMessages.addAll(messages);
								mAdapter.notifyDataSetList(messages);
							}
							mDBManager.addMessages(mMessages);
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mAdapter.setIndex(position);
		mAdapter.notifyDataSetChanged();
	}
}
