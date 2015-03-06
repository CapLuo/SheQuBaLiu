package com.shequ.baliu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shequ.baliu.R;
import com.shequ.baliu.util.StaticVariableSet;

public class AboutFragment extends Fragment {

	private View mContentView;

	private TextView mVersion;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_about, null,
					false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView();
		initData();
		return mContentView;
	}

	private void initView() {
		mVersion = (TextView) mContentView.findViewById(R.id.about_vesrion);
	}

	private void initData() {
		mVersion.setText("V" + getAppInfo());
	}

	private String getAppInfo() {
		try {
			String pkName = getActivity().getPackageName();
			String versionName = getActivity().getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
			Log.e(StaticVariableSet.TAG, e.getMessage());
		}
		return null;
	}
}
