package com.shequ.baliu.view;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BussinessFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_business, container, false);
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

}
