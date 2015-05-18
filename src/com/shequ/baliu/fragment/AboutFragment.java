package com.shequ.baliu.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class AboutFragment extends Fragment {

	private View mContentView;

	private TextView mVersion;
	private TextView mCheck;
	private int versionCode;

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

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "AboutFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "AboutFragment");
	}

	private void initView() {
		mVersion = (TextView) mContentView.findViewById(R.id.about_vesrion);
		mCheck = (TextView) mContentView.findViewById(R.id.about_version_check);
	}

	private void initData() {
		mVersion.setText("V" + getAppInfo());
		mCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SqlHelper.getVersionCode(new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int status, Header[] arg1, byte[] arg2) {
						if (arg2 != null) {
							try {
								String str = new String(arg2);
								JSONObject object = new JSONObject(str);
								int version = Integer.parseInt(object
										.getString("Version"));
								if (versionCode >= version) {
									Toast.makeText(getActivity(), "已是最新版本", Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(), "版本更新，请去下载最新版本", Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Log.e(StaticVariableSet.TAG, e.getMessage());
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e(StaticVariableSet.TAG, arg3.getMessage());
					}
				});
			}
		});
	}

	private String getAppInfo() {
		try {
			String pkName = getActivity().getPackageName();
			String versionName = getActivity().getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			versionCode = getActivity().getPackageManager().getPackageInfo(
					pkName, 0).versionCode;
			return versionName;
		} catch (Exception e) {
			Log.e(StaticVariableSet.TAG, e.getMessage());
		}
		return null;
	}
}
