package com.shequ.baliu.fragment;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.shequ.baliu.R;
import com.shequ.baliu.adapter.AdapterSecondHand;
import com.shequ.baliu.holder.SecondHandGoods;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class SecondhandFragment extends Fragment implements
		OnItemClickListener, OnClickListener {

	private View mContentView;

	private PullToRefreshListView mListView;
	private AdapterSecondHand mAdatper;

	private static final int PRINTSCREEN = 10;
	private int start = 0;
	private boolean isRefreshing = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_secondhand,
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
		mListView = (PullToRefreshListView) mContentView
				.findViewById(R.id.list_secondhand);
		mAdatper = new AdapterSecondHand(getActivity());
		mListView.setAdapter(mAdatper);
		mListView.setOnItemClickListener(this);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
	}

	private void initData() {
		getDataFromNet();

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!isRefreshing) {
					isRefreshing = true;
					getDataFromNet();
				}
			}
		});
	}

	private void getDataFromNet() {
		SqlHelper.getFleaDeal(start + ", " + PRINTSCREEN,
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							List<SecondHandGoods> list = new ArrayList<SecondHandGoods>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject object = response.getJSONObject(i);
								SecondHandGoods good = SecondHandGoods
										.parseJson(object);
								if (good != null) {
									Log.e("@@@@", "sadasd");
									list.add(good);
								}
							}
							mAdatper.notifyDataList(list);
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
						isRefreshing = false;
						mListView.onRefreshComplete();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Log.e(StaticVariableSet.TAG, throwable.getMessage());
						isRefreshing = false;
						mListView.onRefreshComplete();
					}

				});
		start += PRINTSCREEN;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View v) {
	}

}
