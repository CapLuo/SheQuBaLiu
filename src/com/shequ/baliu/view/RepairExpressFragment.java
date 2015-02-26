package com.shequ.baliu.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shequ.baliu.R;
import com.shequ.baliu.ShequFunActivity;
import com.shequ.baliu.adapter.AdapterRepairExpress;
import com.shequ.baliu.adapter.AdapterRepairExpress.DataHolder;
import com.shequ.baliu.util.ShequFunEnum;

public class RepairExpressFragment extends Fragment implements
		OnItemClickListener {

	private View mContentView;

	private ListView mRepairExpressListView;
	private AdapterRepairExpress mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_express_repair,
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
		mRepairExpressListView = (ListView) mContentView
				.findViewById(R.id.list_repair_express);
		mAdapter = new AdapterRepairExpress(getActivity());
		mRepairExpressListView.setAdapter(mAdapter);
		mRepairExpressListView.setOnItemClickListener(this);
	}

	private void initData() {
		if (getActivity() instanceof ShequFunActivity) {
			ShequFunActivity funPage = (ShequFunActivity) getActivity();
			ShequFunEnum fun = funPage.getCurrentFunEnum();
			if (fun == ShequFunEnum.REPAIR) {
				mAdapter.notifyDataList(0);
			} else if (fun == ShequFunEnum.EXPRESS) {
				mAdapter.notifyDataList(1);
			} else if (fun == ShequFunEnum.TOGHTER) {
				mAdapter.notifyDataList(2);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DataHolder holder = (DataHolder) (view.getTag());
		String phone = holder.phone;
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:" + phone));
		getActivity().startActivity(intent);
	}

}
