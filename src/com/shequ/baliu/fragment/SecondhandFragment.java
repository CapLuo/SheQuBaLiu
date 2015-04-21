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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
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

	private Button mTransfer, mBuy, mGive, mExchange;

	private OnClickGoodDetail mClickGoodDetail = new OnClickGoodDetail() {

		@Override
		public void setGoodDetailClick(SecondHandGoods good) {
		}
	};

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

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "SecondhandFragment");
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "SecondhandFragment");
		super.onPause();
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

		mTransfer = (Button) mContentView
				.findViewById(R.id.second_good_summary_transfer);
		mTransfer.setBackgroundDrawable(getActivity().getResources()
				.getDrawable(R.drawable.second_button_background_left_blue));
		mTransfer.setOnClickListener(this);
		mBuy = (Button) mContentView.findViewById(R.id.second_good_summary_buy);
		mBuy.setOnClickListener(this);
		mGive = (Button) mContentView
				.findViewById(R.id.second_good_summary_give);
		mGive.setOnClickListener(this);
		mExchange = (Button) mContentView
				.findViewById(R.id.second_good_summary_exchange);
		mExchange.setOnClickListener(this);
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

	public void setOnClickGoodDetail(OnClickGoodDetail goodDetailListen) {
		mClickGoodDetail = goodDetailListen;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SecondHandGoods good = (SecondHandGoods) mAdatper.getItem(position - 1);
		mClickGoodDetail.setGoodDetailClick(good);
	}

	public interface OnClickGoodDetail {
		public void setGoodDetailClick(SecondHandGoods good);
	}

	/*
	 * 修改所有按钮背景 except 参数button
	 */
	private void onReadySummaryButton(View button) {
		mTransfer.setBackgroundDrawable(getActivity().getResources()
				.getDrawable(R.drawable.second_button_background_left_white));
		mBuy.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.second_button_background_white));
		mGive.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.second_button_background_white));
		mExchange.setBackgroundDrawable(getActivity().getResources()
				.getDrawable(R.drawable.second_button_background_right_white));
		switch (button.getId()) {
		case R.id.second_good_summary_transfer:
			button.setBackgroundDrawable(getActivity().getResources()
					.getDrawable(R.drawable.second_button_background_left_blue));
			break;
		case R.id.second_good_summary_buy:
			button.setBackgroundDrawable(getActivity().getResources()
					.getDrawable(R.drawable.second_button_background_blue));
			break;
		case R.id.second_good_summary_give:
			button.setBackgroundDrawable(getActivity().getResources()
					.getDrawable(R.drawable.second_button_background_blue));
			break;
		case R.id.second_good_summary_exchange:
			button.setBackgroundDrawable(getActivity()
					.getResources()
					.getDrawable(R.drawable.second_button_background_right_blue));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		onReadySummaryButton(view);
	}
}
