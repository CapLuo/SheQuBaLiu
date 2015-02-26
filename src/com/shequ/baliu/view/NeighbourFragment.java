package com.shequ.baliu.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.adapter.AdapterNeighbour;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class NeighbourFragment extends Fragment {

	private View mContentView;

	private ListView mNeighbourListview;
	private AdapterNeighbour mAdapter;
	private View mMoreView;

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

	private void initView(LayoutInflater inflater) {
		mNeighbourListview = (ListView) mContentView
				.findViewById(R.id.list_neighbour);
		mAdapter = new AdapterNeighbour(getActivity());
		mNeighbourListview.setAdapter(mAdapter);
		mMoreView = inflater.inflate(R.layout.item_list_load, null);
		mNeighbourListview.addFooterView(mMoreView);
	}

	private void initData() {
		loadMoreData();
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
					+ groupid + " LIMIT 0, 15", new JsonHttpResponseHandler(
					"UTF-8") {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response) {
					List<PersonInfo> people = new ArrayList<PersonInfo>();
					try {

						for (int i = 0; i < response.length(); i++) {
							JSONObject json = response.getJSONObject(i);
							PersonInfo info = PersonInfo.parseJson(json);
							if (info != null) {
								people.add(info);
							}
						}
						mAdapter.notifyDataList(people);
					} catch (JSONException exception) {
						Log.e(StaticVariableSet.TAG, exception.getMessage());
					}
				}
			});
		}
	}
}
