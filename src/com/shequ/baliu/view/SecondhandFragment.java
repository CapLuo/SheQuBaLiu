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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

	private View refresh;

	private ListView mListView;
	private AdapterSecondHand mAdatper;

	private static final int PRINTSCREEN = 10;
	private int start = 0;

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
		refresh = mContentView.findViewById(R.id.secondhand_refresh_more);
		refresh.setOnClickListener(this);
		mListView = (ListView) mContentView.findViewById(R.id.list_secondhand);
		mAdatper = new AdapterSecondHand(getActivity());
		mListView.setAdapter(mAdatper);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
	}

	private void initData() {
		getDataFromNet();
	}

	private void getDataFromNet() {
		SqlHelper.get(StaticVariableSet.SECOND_MARKET,
				"photo is not null ORDER BY addtime LIMIT " + start + ", " + PRINTSCREEN,
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							List<SecondHandGoods> list = new ArrayList<SecondHandGoods>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject object = response.getJSONObject(i);
								SecondHandGoods good = parseJson(object);
								if (good != null) {
									list.add(good);
								}
							}
							mAdatper.notifyDataList(list);
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});
		start += PRINTSCREEN;

	}

	private SecondHandGoods parseJson(JSONObject json) throws JSONException {
		SecondHandGoods good = new SecondHandGoods();
		String userid = json.getString("userid");
		if (userid == null || userid.equals("")) {
			return null;
		} else {
			good.setUserid(userid);
		}
		String photo = json.getString("photo");
		if (photo == null || photo.equals("")) {
			return null;
		} else {
			if (!photo.startsWith("http")) {
				photo = StaticVariableSet.IMG_URL + photo;
			}
			good.setPhoto(photo);
		}
		String title = json.getString("title");
		if (title == null || title.equals("")) {
			return null;
		} else {
			good.setTitle(title);
		}
		String price = json.getString("price");
		if (price == null || price.equals("")) {
			return null;
		} else {
			good.setPrice(price);
		}
		String content = json.getString("content");
		if (content == null || content.equals("")) {
			return null;
		} else {
			good.setContent(content);
		}
		String addTime = json.getString("addtime");
		good.setAddTime(addTime);
		return good;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.secondhand_refresh_more:
			refreshMore();
			break;
		default:
			break;
		}
	}

	private void refreshMore() {
		getDataFromNet();
	}
}
