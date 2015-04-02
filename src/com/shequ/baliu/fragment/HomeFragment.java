package com.shequ.baliu.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shequ.baliu.R;
import com.shequ.baliu.SheQuActivity;
import com.shequ.baliu.ShequFunActivity;
import com.shequ.baliu.ShequShowActivity;
import com.shequ.baliu.adapter.AdapterHomeAdvertImage;
import com.shequ.baliu.adapter.AdapterHomeBusiness;
import com.shequ.baliu.adapter.AdpterEyeCity;
import com.shequ.baliu.holder.AdvertHomeInfo;
import com.shequ.baliu.holder.ShequEyeCityHolder;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;
import com.shequ.baliu.view.CirclePageIndicator;

public class HomeFragment extends Fragment {

	private View mContentView;

	private GridView mBusinessGridView;
	private AdapterHomeBusiness mBusinessAdpater;

	private int currentItem = 0;
	private View mImageLayout;
	private ViewPager mImagePagerView;
	private AdapterHomeAdvertImage mAdvertAdapter;
	private CirclePageIndicator mImageIndicator;

	private ScheduledExecutorService scheduledExecutorService;

	private ListView mDynamicListView;
	private AdpterEyeCity mDynamicAdapter;

	private DisplayImageOptions mOptions;

	private SheQuActivity mActivity;

	public HomeFragment(SheQuActivity activity) {
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_home, container,
					false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) (mContentView.getParent())).removeView(mContentView);
		}

		initView();
		initData();

		return mContentView;
	}

	private void initView() {
		// mScrollView = (ScrollView) mContentView
		// .findViewById(R.id._scrollview_home);
		/*
		 * mScrollView.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * 
		 * return false; } });
		 */

		mBusinessGridView = (GridView) mContentView
				.findViewById(R.id._gridview_home_business);
		mBusinessAdpater = new AdapterHomeBusiness(getActivity(),
				mBusinessGridView);
		mBusinessGridView.setAdapter(mBusinessAdpater);
		mBusinessGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 5) {
					if (getActivity() instanceof SheQuActivity) {
						SheQuActivity activity = (SheQuActivity) getActivity();
						activity.setChoiceFragmentContent(1);
					}
				} else {
					String title = (String) (mBusinessAdpater.getItem(position));
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putInt("position", position);
					intent.putExtras(bundle);
					intent.setClass(getActivity(), ShequFunActivity.class);
					getActivity().startActivityForResult(intent,
							SheQuActivity.request_code);
				}
			}
		});

		mImageLayout = mContentView.findViewById(R.id._home_image_layout);
		mImageLayout.setVisibility(View.INVISIBLE);
		mImagePagerView = (ViewPager) mContentView
				.findViewById(R.id._home_image_viewpage);
		ShequTools shequTools = new ShequTools(getActivity());
		LayoutParams lp = (LayoutParams) mImagePagerView.getLayoutParams();
		lp.height = shequTools.getDisplayMetricsWidth() / 2;
		lp.width = shequTools.getDisplayMetricsWidth();
		mImagePagerView.setLayoutParams(lp);
		mImageIndicator = (CirclePageIndicator) mContentView
				.findViewById(R.id._home_image_indicator);

		mDynamicListView = (ListView) mContentView
				.findViewById(R.id._listview_activity);
		mDynamicAdapter = new AdpterEyeCity(getActivity());
		mDynamicListView.setAdapter(mDynamicAdapter);
		mDynamicListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ShequShowActivity.class);
				intent.putExtra("PATH",
						((ShequEyeCityHolder) view.getTag()).getUrl());
				// getActivity().startActivity(intent);
				// 暂时不做跳转
			}

		});
		getEyeCityDataFromNet();
	}

	private void initData() {
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();
		getHomeAdvertImages();

	}

	@Override
	public void onPause() {
		scheduledExecutorService.shutdown();
		scheduledExecutorService = null;

		StatService.onPageEnd(getActivity(), "HomeFragment");

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		StatService.onPageStart(getActivity(), "HomeFragment");

		if (scheduledExecutorService == null) {
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
		}
		if (mAdvertAdapter != null && mAdvertAdapter.getCount() > 0) {
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1,
					3, TimeUnit.SECONDS);
		}
	}

	private void getEyeCityDataFromNet() {
		SqlHelper.get(StaticVariableSet.ADVERT_INFO,
				" ads_id = 6 ORDER BY id DESC LIMIT 0, 6",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						if (200 == statusCode) {
							try {
								List<ShequEyeCityHolder> list = new ArrayList<ShequEyeCityHolder>();
								for (int i = 0; i < response.length(); i++) {
									JSONObject json = response.getJSONObject(i);
									ShequEyeCityHolder holder = new ShequEyeCityHolder();
									String title = json.getString("title");
									String url = json.getString("url");
									if (title != null && !title.equals("")
											&& url != null && !url.equals("")) {
										holder.setTitle(title);
										holder.setUrl(url);
										list.add(holder);
									}
								}
								mDynamicAdapter.addData(list, mDynamicListView);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

				});
	}

	private void getHomeAdvertImages() {
		SqlHelper.getHomeAdvertImages(new JsonHttpResponseHandler("UTF-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					ArrayList<ImageView> listView = (ArrayList<ImageView>) parserImageUrl(response);
					if (listView.size() <= 0) {
						return;
					}
					if (scheduledExecutorService == null) {
						scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
					}
					mAdvertAdapter = new AdapterHomeAdvertImage(listView);
					mImagePagerView.setAdapter(mAdvertAdapter);
					scheduledExecutorService.scheduleAtFixedRate(
							new ScrollTask(), 1, 3, TimeUnit.SECONDS);
					mImageLayout.setVisibility(View.VISIBLE);
					mImageIndicator.setViewPager(mImagePagerView);
				} catch (JSONException e) {
					Log.e(StaticVariableSet.TAG, e.getMessage());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				mImageLayout.setVisibility(View.GONE);
				Log.e(StaticVariableSet.TAG, throwable.getMessage());
			}

		});
	}

	private List<ImageView> parserImageUrl(JSONArray response)
			throws JSONException {
		List<ImageView> adverts = new ArrayList<ImageView>();
		for (int i = 0; i < response.length(); i++) {
			AdvertHomeInfo info = new AdvertHomeInfo();
			JSONObject object = response.getJSONObject(i);
			String title = object.getString("title");
			String photo = object.getString("photo");
			String url = object.getString("url");
			info.setTitle(title);
			info.setUrl(url);
			info.setDrawable(StaticVariableSet.IMG_URL + photo);
			ImageView view = new ImageView(mActivity);
			view.setScaleType(ScaleType.FIT_XY);
			ImageLoader.getInstance().displayImage(info.getDrawable(), view,
					mOptions);
			// view.setTag(info);
			adverts.add(view);
		}
		return adverts;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mImagePagerView.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	/**
	 * 换行切换任务
	 * 
	 * @author luo
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mImagePagerView) {
				if (mAdvertAdapter != null) {
					currentItem = (currentItem + 1) % mAdvertAdapter.getCount();
					handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
				}
			}
		}

	}

}
