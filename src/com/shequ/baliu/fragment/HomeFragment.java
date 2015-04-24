package com.shequ.baliu.fragment;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.UserInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shequ.baliu.R;
import com.shequ.baliu.SheQuActivity;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.ShequFunActivity;
import com.shequ.baliu.ShequShowActivity;
import com.shequ.baliu.adapter.AdapterHomeAdvertImage;
import com.shequ.baliu.adapter.AdapterHomeBusiness;
import com.shequ.baliu.adapter.AdpterEyeCity;
import com.shequ.baliu.holder.AdvertHomeInfo;
import com.shequ.baliu.holder.FriendInfo;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.holder.ShequEyeCityHolder;
import com.shequ.baliu.util.DBManager;
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

	private ShequTools mShequTools;
	private String mToken;
	private String mUserId;

	private Dialog mDialog;

	private DBManager mDBManager;

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
				} else if (position == 0) {
					ShequApplication app = (ShequApplication) getActivity()
							.getApplication();
					if (!app.getLogin()) {
						showToast(R.string.user_login_first);
						((SheQuActivity) getActivity())
								.setChoiceFragmentContent(3);
						return;
					}
					mDialog.show();
					mToken = mShequTools.getSharePreferences(
							StaticVariableSet.SHARE_TOKEN, "");
					if (TextUtils.isEmpty(mToken)) {
						try {
							getmToken();
						} catch (UnsupportedEncodingException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
							dismiss();
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					} else {
						try {
							loadMoreData();

							startSingleChat();
						} catch (Exception e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
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
		mShequTools = new ShequTools(getActivity());
		LayoutParams lp = (LayoutParams) mImagePagerView.getLayoutParams();
		lp.height = mShequTools.getDisplayMetricsWidth() / 2;
		lp.width = mShequTools.getDisplayMetricsWidth();
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

		mDialog = new Dialog(getActivity()) {
			@Override
			public void onBackPressed() {
				// 屏蔽返回建
			}
		};
		View dialogContent = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_login_layout, null);
		TextView content = (TextView) dialogContent
				.findViewById(R.id.dialog_content);
		content.setText(R.string.fresh_another_data);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(dialogContent);
	}

	private void initData() {
		mDBManager = new DBManager(getActivity());
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
								Log.e(StaticVariableSet.TAG, e.getMessage());
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

	private void loadMoreData() { // 加载更多数据
		String groupid = ((ShequApplication) (getActivity().getApplication()))
				.getInfo().getGroupId();
		if (TextUtils.isEmpty(groupid)) {
			showToast("加入小区才能看到邻里圈的朋友");
			dismiss();
			return;
		}
		mUserId = ((ShequApplication) (getActivity().getApplication()))
				.getInfo().getUserId();
		if (groupid != null && !groupid.equals("")) {
			SqlHelper.get("`" + StaticVariableSet.USER_INFO + "`.`userid`, `"
					+ StaticVariableSet.USER_INFO + "`.`username`, `"
					+ StaticVariableSet.USER_INFO + "`.`nickname`, `"
					+ StaticVariableSet.USER_INFO + "`.`realname`, `"
					+ StaticVariableSet.USER_INFO + "`.`email`, `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "`.`groupid`, `"
					+ StaticVariableSet.SHEQU_GROUP + "`.`groupname`, `"
					+ StaticVariableSet.USER_INFO + "`.`ismerchant`, `"
					+ StaticVariableSet.USER_INFO + "`.`path`, `"
					+ StaticVariableSet.USER_INFO + "`.`face`", "`"
					+ StaticVariableSet.USER_INFO + "` JOIN `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "` ON `"
					+ StaticVariableSet.USER_INFO + "`.`userid` = `"
					+ StaticVariableSet.SHEQU_GROUP_RELATION
					+ "`.`userid` JOIN `" + StaticVariableSet.SHEQU_GROUP
					+ "` ON `" + StaticVariableSet.SHEQU_GROUP_RELATION
					+ "`.`groupid` = `" + StaticVariableSet.SHEQU_GROUP
					+ "`.`groupid`", "`"
					+ StaticVariableSet.SHEQU_GROUP_RELATION + "`.`groupid` = "
					+ groupid + " AND `" + StaticVariableSet.USER_INFO
					+ "`.`ismerchant` = 0",
					new JsonHttpResponseHandler("UTF-8") {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							ArrayList<PersonInfo> people = new ArrayList<PersonInfo>();
							try {
								for (int i = 0; i < response.length(); i++) {
									JSONObject json = response.getJSONObject(i);
									PersonInfo info = PersonInfo
											.parseJson(json);
									if (info != null
											&& !info.getUserId()
													.equals(mUserId)) {
										people.add(info);
									}
								}
								mDBManager.addFriendInfos(people);
								setFrined();
								// 启动会话列表
								RongIM.getInstance().startConversationList(
										getActivity());
								dismiss();
							} catch (JSONException exception) {
								Log.e(StaticVariableSet.TAG,
										exception.getMessage());
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							Log.e(StaticVariableSet.TAG, throwable.getMessage());
							dismiss();
							showToast(R.string.home_connection_server_error);
						}
					});
		}
	}

	private void getmToken() throws UnsupportedEncodingException, JSONException {
		JSONObject object = new JSONObject();
		ShequApplication app = (ShequApplication) getActivity()
				.getApplication();
		PersonInfo info = app.getInfo();
		object.put("id", info.getUserId());
		object.put("name", info.getNickName());
		object.put("portraitUri", info.getPhoto());
		object.put("way", "Token");
		SqlHelper.getImToken(getActivity(), object,
				new JsonHttpResponseHandler("utf-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						Log.d(StaticVariableSet.TAG, "success object "
								+ statusCode + " " + response.toString());
						try {
							String object = response.getString("Token");
							JSONObject json = new JSONObject(object);
							mToken = json.getString("token");
							mShequTools.writeSharedPreferences(
									StaticVariableSet.SHARE_TOKEN, mToken);
							loadMoreData();
							startSingleChat();
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						} catch (Exception e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						Log.e(StaticVariableSet.TAG,
								"failure " + throwable.getMessage() + " "
										+ statusCode + " " + responseString);
						dismiss();
						showToast(R.string.home_connection_server_error);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						Log.e(StaticVariableSet.TAG, "failure array "
								+ throwable.getMessage() + " " + statusCode);
						dismiss();
						showToast(R.string.home_connection_server_error);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Log.e(StaticVariableSet.TAG, "failure object "
								+ throwable.getMessage() + " " + statusCode);
						dismiss();
						showToast(R.string.home_connection_server_error);
					}
				});
	}

	private void startSingleChat() throws Exception {

		RongIM.connect(mToken, new RongIMClient.ConnectCallback() {

			@Override
			public void onSuccess(String s) {
				// 此处处理连接成功。
				Log.d(StaticVariableSet.TAG, "Login successfully.");
			}

			@Override
			public void onError(ErrorCode errorCode) {
				// 此处处理连接不成功。
				Log.e(StaticVariableSet.TAG, "Login faile. " + errorCode);
				dismiss();
				showToast(R.string.home_connection_server_error);
			}
		});
	}

	private void setFrined() {
		RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {
			// App 返回指定的用户信息给 IMKit 界面组件。
			@Override
			public RongIMClient.UserInfo getUserInfo(String userId) {
				// 原则上 App
				// 应该将用户信息和头像在移动设备上进行缓存，每次获取用户信息的时候，就不用再通过网络获取，提高加载速度，提升用户体验。我们后续将提供用户信息缓存功能，方便您开发。
				return getUserInfoFromLocalCache(userId);
			}
		}, false);

		// 设置好友信息提供者。
		RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {
			@Override
			public ArrayList<RongIMClient.UserInfo> getFriends() {
				// 返回 App 的好友列表给 IMKit 界面组件，供会话列表页中选择好友时使用。
				ArrayList<RongIMClient.UserInfo> list = new ArrayList<RongIMClient.UserInfo>();
				if (mDBManager == null) {
					mDBManager = new DBManager(HomeFragment.this.getActivity());
				}
				ArrayList<FriendInfo> mPeopele = mDBManager
						.queryFriend(getActivity());
				for (FriendInfo info : mPeopele) {
					RongIMClient.UserInfo user = new RongIMClient.UserInfo(info
							.getUserid(), info.getName(), info.getPortraitUri());
					list.add(user);
				}

				return list;
			}
		});
	}

	private UserInfo getUserInfoFromLocalCache(String userId) {
		FriendInfo person = null;
		if (mDBManager == null) {
			mDBManager = new DBManager(HomeFragment.this.getActivity());
		}
		ArrayList<FriendInfo> mPeopele = mDBManager.queryFriend(getActivity());
		for (FriendInfo personInfo : mPeopele) {
			if (personInfo.getUserid().equals(userId)) {
				person = personInfo;
			}
		}
		if (person == null) {
			return null;
		}
		UserInfo info = new UserInfo(userId, person.getName(),
				person.getPortraitUri());
		return info;
	}

	private void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void showToast(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	private void showToast(int strId) {
		Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT).show();
	}
}
