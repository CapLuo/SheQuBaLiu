package com.shequ.baliu.fragment;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BussinessFragment extends Fragment {

	private View mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_business,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView();
		return mContentView;
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "BussinessFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "BussinessFragment");
	}

	private void initView() {

	}

}
