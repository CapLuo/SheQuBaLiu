package com.shequ.baliu.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shequ.baliu.R;
import com.shequ.baliu.holder.SecondHandGoods;
import com.shequ.baliu.view.CircularImage;

public class SecondGoodDetailFragment extends Fragment {

	private View mContentView;

	private CircularImage mHeadImage;
	private TextView mName;
	private TextView mTime;
	private TextView mTitle;
	private ImageView mBrowsingImage;
	private TextView mContent;
	private TextView mShequ;

	private SecondHandGoods mGood;

	private DisplayImageOptions mOptions;

	public void setGoodDetail(SecondHandGoods good) {
		mGood = good;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_secondgooddetail,
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
		StatService.onPageStart(getActivity(), "SecondGoodDetailFragment");
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "SecondGoodDetailFragment");
		super.onPause();
	}

	private void initView() {
		mHeadImage = (CircularImage) mContentView
				.findViewById(R.id.second_hand_head_img);
		mName = (TextView) mContentView.findViewById(R.id.second_hand_name);
		mTime = (TextView) mContentView.findViewById(R.id.second_hand_time);
		mTitle = (TextView) mContentView.findViewById(R.id.second_hand_title);
		mBrowsingImage = (ImageView) mContentView
				.findViewById(R.id.second_hand_browsing);
		mContent = (TextView) mContentView
				.findViewById(R.id.second_hand_summary);
		mShequ = (TextView) mContentView
				.findViewById(R.id.second_hand_location);
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	private void initData() {
		mHeadImage.setImageResource(R.drawable.user_head_default);
		ImageLoader.getInstance().displayImage(mGood.getHeadphoto(),
				mHeadImage, mOptions);
		mBrowsingImage.setImageResource(R.drawable.goods_loading);
		ImageLoader.getInstance().displayImage(mGood.getPhoto(),
				mBrowsingImage, mOptions);
		mName.setText(mGood.getNickname());
		fillTime();
		mTitle.setText(mGood.getTitle());
		fillContent();
		mShequ.setText(mGood.getGroupname());
	}

	private void fillTime() {
		String time = getActivity().getResources().getString(
				R.string.second_hand_detail_time);
		Date date = new Date(Long.parseLong(mGood.getUpdateTime()) * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		time = time + format.format(date);
		mTime.setText(time);
	}

	private void fillContent() {
		mContent.setText(Html.fromHtml(mGood.getContent()));
	}
}
