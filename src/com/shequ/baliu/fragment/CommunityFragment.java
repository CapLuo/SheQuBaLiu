package com.shequ.baliu.fragment;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.UserInfo;
import io.rong.imlib.RongIMClient.ConnectCallback.ErrorCode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.SheQuActivity;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.ShequFunActivity;
import com.shequ.baliu.adapter.AdapterCommunity;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class CommunityFragment extends Fragment implements OnItemClickListener {

	private View mContentView;

	private ListView mCommunityListView;
	private GridView mCommunityGridView;

	private boolean mIsList = true;

	private Dialog mDialog;
	private String mUserId;
	private String mToken;
	private ShequTools mShequTools;
	private ArrayList<PersonInfo> mPersonInfos = new ArrayList<PersonInfo>();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_community,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) (mContentView.getParent())).removeView(mContentView);
		}

		initView();
		return mContentView;
	}

	private void initView() {
		mCommunityListView = (ListView) mContentView
				.findViewById(R.id.community_list);
		mCommunityGridView = (GridView) mContentView
				.findViewById(R.id.community_grid);
		mCommunityListView.setOnItemClickListener(this);
		mCommunityGridView.setOnItemClickListener(this);
		setModeLayout(mIsList);

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

		mShequTools = new ShequTools(getActivity());
	}

	@Override
	public void onPause() {

		StatService.onPageEnd(getActivity(), "CommunityFragment");

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		StatService.onPageStart(getActivity(), "CommunityFragment");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			ShequApplication app = (ShequApplication) getActivity()
					.getApplication();
			if (!app.getLogin()) {
				showToast(R.string.user_login_first);
				((SheQuActivity) getActivity()).setChoiceFragmentContent(3);
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
			return;
		}
		String title = (String) mCommunityListView.getAdapter().getItem(
				position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		intent.setClass(getActivity(), ShequFunActivity.class);
		getActivity()
				.startActivityForResult(intent, SheQuActivity.request_code);
	}

	public void setModeLayout(boolean isList) {
		mIsList = isList;
		if (isList) {
			mCommunityListView.setAdapter(new AdapterCommunity(getActivity(),
					true));
			mCommunityGridView.setVisibility(View.GONE);
			mCommunityListView.setVisibility(View.VISIBLE);
		} else {
			mCommunityGridView.setAdapter(new AdapterCommunity(getActivity(),
					false));
			mCommunityListView.setVisibility(View.GONE);
			mCommunityGridView.setVisibility(View.VISIBLE);
		}
	}

	private void loadMoreData() { // 加载更多数据
		String groupid = ((ShequApplication) (getActivity().getApplication()))
				.getInfo().getGroupId();
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
								mPersonInfos = people;
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
				// 此处处理连接成功。
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

				for (PersonInfo info : mPersonInfos) {
					RongIMClient.UserInfo user = new RongIMClient.UserInfo(info
							.getUserId(), info.getNickName(), info.getPhoto());
					list.add(user);
				}

				return list;
			}
		});
	}

	private UserInfo getUserInfoFromLocalCache(String userId) {
		PersonInfo person = null;
		for (PersonInfo personInfo : mPersonInfos) {
			if (personInfo.getUserId() == userId) {
				person = personInfo;
			}
		}
		if (person == null) {
			return null;
		}
		UserInfo info = new UserInfo(userId, person.getNickName(),
				person.getPhoto());
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
