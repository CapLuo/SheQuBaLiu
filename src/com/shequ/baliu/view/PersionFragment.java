package com.shequ.baliu.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shequ.baliu.ChoiceSequActivity;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.ShequUserActivity;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.DBManager;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class PersionFragment extends Fragment implements OnClickListener {

	private View mContentView;
	private RelativeLayout mUserInfoLayout;
	private Button inLogin;
	private ImageView mPersionFace;
	private TextView mUserName;
	private TextView mUserShequName;
	private Button mChangeCommunity;
	private View mMessageBox;
	private View mFeedback;
	private View mAboutUs;

	private DBManager mDBManager;

	private DisplayImageOptions mOptions;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_user, container,
					false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) (mContentView.getParent())).removeView(mContentView);
		}

		initView();
		return mContentView;
	}

	private void initView() {
		mUserInfoLayout = (RelativeLayout) mContentView
				.findViewById(R.id._user_info);

		inLogin = (Button) mContentView.findViewById(R.id._user_registr);
		inLogin.setOnClickListener(this);

		mPersionFace = (ImageView) mContentView.findViewById(R.id._user_img);
		mUserName = (TextView) mContentView.findViewById(R.id._user_name);
		mUserShequName = (TextView) mContentView
				.findViewById(R.id._user_shequ_name);
		mChangeCommunity = (Button) mContentView
				.findViewById(R.id._user_shequ_change);
		mChangeCommunity.setOnClickListener(this);

		mMessageBox = mContentView.findViewById(R.id._user_message);
		mMessageBox.setOnClickListener(this);
		mFeedback = mContentView.findViewById(R.id._user_feedback);
		mFeedback.setOnClickListener(this);
		mAboutUs = mContentView.findViewById(R.id._user_aboutus);
		mAboutUs.setOnClickListener(this);

	}

	private void initData() {
		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_head_default)
				.showImageOnFail(R.drawable.user_head_default)
				.cacheInMemory(false).cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		if (getActivity().getApplication() instanceof ShequApplication) {
			ShequApplication app = (ShequApplication) (getActivity()
					.getApplication());
			if (app.getLogin()) {
				mUserInfoLayout.setVisibility(View.VISIBLE);
				inLogin.setVisibility(View.INVISIBLE);
				mUserName.setText(app.getInfo().getNickName());
				String groupName = app.getInfo().getGroupName();
				if (groupName == null || groupName.equals("")) {
					mUserShequName.setText(getActivity().getResources()
							.getString(R.string.user_no_group));
				} else {
					mUserShequName.setText(app.getInfo().getGroupName());
				}
				ImageLoader.getInstance().displayImage(
						app.getInfo().getPhoto(), mPersionFace, mOptions);
			} else {
				inLogin.setVisibility(View.VISIBLE);
				mUserInfoLayout.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		StatService.onPageStart(getActivity(), "PersionFragment");

		initData();
		mDBManager = new DBManager(getActivity());
	}

	@Override
	public void onPause() {

		StatService.onPageStart(getActivity(), "PersionFragment");

		mDBManager.closeDB();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._user_registr:
			Intent intentRegistr = new Intent();
			intentRegistr.putExtra("index", 0);
			intentRegistr.setClass(getActivity(), ShequUserActivity.class);
			getActivity().startActivity(intentRegistr);
			break;
		case R.id._user_message:
			Intent intentMessage = new Intent();
			intentMessage.putExtra("index", 1);
			intentMessage.setClass(getActivity(), ShequUserActivity.class);
			getActivity().startActivity(intentMessage);
			break;
		case R.id._user_feedback:
			Intent intentFeedback = new Intent();
			intentFeedback.putExtra("index", 2);
			intentFeedback.setClass(getActivity(), ShequUserActivity.class);
			getActivity().startActivity(intentFeedback);
			break;
		case R.id._user_aboutus:
			Intent intentAbout = new Intent();
			intentAbout.putExtra("index", 4);
			intentAbout.setClass(getActivity(), ShequUserActivity.class);
			getActivity().startActivity(intentAbout);
			break;
		case R.id._user_shequ_change:
			Intent intentCommunityChange = new Intent();
			intentCommunityChange.setClass(getActivity(),
					ChoiceSequActivity.class);
			getActivity().startActivity(intentCommunityChange);
			break;
		default:
			break;
		}
	}

	public void loginOut(View v) {
		ShequTools tool = new ShequTools(getActivity());
		ShequApplication app = (ShequApplication) (getActivity()
				.getApplication());
		tool.writeSharedPreferences(StaticVariableSet.SHARE_USER, "");
		tool.writeSharedPreferences(StaticVariableSet.SHARE_PWD, "");
		mDBManager.deleteAllData(SqlHelper.MESSAGE_TABLE_NAME);
		app.setInfo(new PersonInfo());
		app.setLogin(false);
		initData();
		v.setVisibility(View.GONE);
	}

}
