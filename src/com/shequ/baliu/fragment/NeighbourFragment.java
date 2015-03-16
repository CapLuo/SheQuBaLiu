package com.shequ.baliu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.ConversationActivity;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.adapter.AdapterNeighbour;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class NeighbourFragment extends Fragment implements OnItemClickListener {

	private View mContentView;

	private PullToRefreshListView mListView;

	private AdapterNeighbour mAdapter;

	private String mUserId;
	private int mCountNeighbour = 0;
	private boolean isRefreshing = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_neighbour,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView(inflater);
		initData();

		return mContentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ShequApplication app = (ShequApplication) getActivity()
				.getApplication();
		mUserId = app.getInfo().getUserId();
	}

	private void initView(LayoutInflater inflater) {
		mListView = (PullToRefreshListView) mContentView
				.findViewById(R.id.list_neighbour);
		mAdapter = new AdapterNeighbour(getActivity());
		mListView.setAdapter(mAdapter);
		// mMoreView = inflater.inflate(R.layout.item_list_load, null);
		// mNeighbourListview.addFooterView(mMoreView);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnItemClickListener(this);
	}

	private void initData() {
		loadMoreData();

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!isRefreshing) {
					isRefreshing = true;
					loadMoreData();
				}
			}

		});
	}

	private void loadMoreData() { // 加载更多数据
		String groupid = ((ShequApplication) (getActivity().getApplication()))
				.getInfo().getGroupId();
		if (groupid != null && !groupid.equals("")) {
			SqlHelper.get("`" + StaticVariableSet.USER_INFO + "`.`userid`, `"
					+ StaticVariableSet.USER_INFO + "`.`username`, `"
					+ StaticVariableSet.USER_INFO + "`.`nickname`, `"
					+ StaticVariableSet.USER_INFO + "`.`realname`, `"
					+ StaticVariableSet.USER_INFO + "`.`email`, `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "`.`groupid`, `"
					+ StaticVariableSet.SHEQU_GROUP + "`.`groupname`, `"
					+ StaticVariableSet.USER_INFO + "`.`ismerchant`, `"
					+ StaticVariableSet.USER_INFO + "`.`path`, `"
					+ StaticVariableSet.USER_INFO + "`.`face`", "`"
					+ StaticVariableSet.USER_INFO + "` JOIN `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "` ON `"
					+ StaticVariableSet.USER_INFO + "`.`userid` = `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION
					+ "`.`userid` JOIN `" + StaticVariableSet.SHEQU_GROUP
					+ "` ON `" + StaticVariableSet.SHEQU_GROUP_RELATION
					+ "`.`groupid` = `" + StaticVariableSet.SHEQU_GROUP
					+ "`.`groupid`", "`"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "`.`groupid` = "
					+ groupid + " AND `" + StaticVariableSet.USER_INFO
					+ "`.`ismerchant` = 0 LIMIT " + mCountNeighbour + ", 15",
					new JsonHttpResponseHandler("UTF-8") {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							List<PersonInfo> people = new ArrayList<PersonInfo>();
							try {
								mCountNeighbour += 15;
								for (int i = 0; i < response.length(); i++) {
									JSONObject json = response.getJSONObject(i);
									PersonInfo info = PersonInfo
											.parseJson(json);
									if (info != null
											&& !info.getUserId()
													.equals(mUserId)) {
										people.add(info);
									}
								}
								mAdapter.notifyDataList(people);
							} catch (JSONException exception) {
								Log.e(StaticVariableSet.TAG,
										exception.getMessage());
							}
							isRefreshing = false;
							mListView.onRefreshComplete();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							isRefreshing = false;
							mListView.onRefreshComplete();
							Log.e(StaticVariableSet.TAG, throwable.getMessage());
						}
					});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		PersonInfo info = (PersonInfo) mAdapter.getItem(position - 1);
		Intent intent = new Intent();
		intent.putExtra("Title", info.getNickName());
		intent.putExtra("Id", info.getUserId());
		intent.setClass(getActivity(), ConversationActivity.class);
		getActivity().startActivity(intent);
	}
}
