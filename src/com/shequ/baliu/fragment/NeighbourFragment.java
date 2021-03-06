package com.shequ.baliu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.R;

public class NeighbourFragment extends Fragment {

	private View mContentView;

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

		return mContentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "NeighbourFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "NeighbourFragment");
	}
}
