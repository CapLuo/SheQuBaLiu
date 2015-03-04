package com.shequ.baliu.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.R;
import com.shequ.baliu.SheQuActivity;
import com.shequ.baliu.ShequFunActivity;
import com.shequ.baliu.adapter.AdapterCommunity;

public class CommunityFragment extends Fragment implements OnItemClickListener {

	private View mContentView;

	private ListView mCommunityListView;
	private AdapterCommunity mCommunityAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_community,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) (mContentView.getParent())).removeView(mContentView);
		}

		initView();
		return mContentView;
	}

	private void initView() {
		mCommunityListView = (ListView) mContentView
				.findViewById(R.id._listview_community);
		mCommunityAdapter = new AdapterCommunity(getActivity());
		mCommunityListView.setAdapter(mCommunityAdapter);
		mCommunityListView.setOnItemClickListener(this);
	}

	@Override
	public void onPause() {

		StatService.onPageEnd(getActivity(), "CommunityFragment");

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		StatService.onPageStart(getActivity(), "CommunityFragment");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String title = (String) mCommunityAdapter.getItem(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		intent.setClass(getActivity(), ShequFunActivity.class);
		getActivity()
				.startActivityForResult(intent, SheQuActivity.request_code);
	}
}
