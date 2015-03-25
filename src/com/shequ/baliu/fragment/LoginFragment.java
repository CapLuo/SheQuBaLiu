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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.ShequUserActivity;
import com.shequ.baliu.R;
import com.shequ.baliu.dialog.ShequDialog;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class LoginFragment extends Fragment {

	private View mContentView;

	private EditText mUserName;
	private EditText mPassword;
	private Button mLogin;
	private TextView mRegister;

	private ShequDialog mDialog;

	private TextView mDialogText;

	private String mUsernameText;
	private String mPasswordText;

	private PersonInfo mInfo;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_login, container,
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
		StatService.onPageEnd(getActivity(), "LoginFragment");
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onPageStart(getActivity(), "LoginFragment");
	}

	private void initView() {
		mUserName = (EditText) mContentView
				.findViewById(R.id.login_user_edittext);
		mPassword = (EditText) mContentView
				.findViewById(R.id.login_password_edittext);
		mLogin = (Button) mContentView.findViewById(R.id.login_button);
		mRegister = (TextView) mContentView.findViewById(R.id.login_register);

		mDialog = new ShequDialog(getActivity()) {

			@Override
			public void onBackPressed() {
				// 屏蔽换回建
			}

		};
		View dialogContent = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_login_layout, null);
		mDialogText = (TextView) dialogContent
				.findViewById(R.id.dialog_content);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(dialogContent);
	}

	private void initData() {
		mInfo = new PersonInfo();
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isInputNull()) {
					mDialog.show();
					onLogin();
				}
			}
		});
		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() instanceof ShequUserActivity) {
					ShequUserActivity activity = (ShequUserActivity) getActivity();
					activity.setChoiceFragment(3);
				}
			}
		});
	}

	private boolean isInputNull() {
		mUsernameText = mUserName.getText().toString();
		if (mUsernameText == null || mUsernameText.equals("")) {
			mUserName.requestFocus();
			return false;
		}
		mPasswordText = mPassword.getText().toString();
		if (mPasswordText == null || mPasswordText.equals("")) {
			mPassword.requestFocus();
			return false;
		}
		return true;
	}

	private void onLogin() {
		SqlHelper.get(StaticVariableSet.USER_MAIN, "`username` = '"
				+ mUsernameText + "'", new JsonHttpResponseHandler("UTF-8") {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				mDialog.dismiss();
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				mDialog.dismiss();
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				mDialog.dismiss();
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == 200) {
					try {

						if (response.length() == 0) {
							// 失败 用户名不存在
							mDialog.dismiss();
							Toast.makeText(getActivity(),
									R.string.login_error_username_none,
									Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = response.getJSONObject(0);
							String saltText = object.getString("salt");
							String pwdText = object.getString("pwd");
							String userid = object.getString("userid");
							if (pwdText.equals(ShequTools.md5(saltText
									+ mPasswordText))) {
								parseInfo(object);
								saveUserInfo(saltText, userid);
								setAppLoginInfo();
								loginSucess();
							} else {
								// 用户名密码错误
								mDialog.dismiss();
								Toast.makeText(getActivity(),
										R.string.login_error_password_wrong,
										Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						Log.e(StaticVariableSet.TAG, e.getMessage());
						mDialog.dismiss();
					}
				}
			}

		});
	}

	public void loginSucess() {
		SqlHelper.getAllInfo("`" + StaticVariableSet.USER_MAIN
				+ "`.`username` = '" + mUsernameText + "'",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						mDialog.dismiss();
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						mDialog.dismiss();
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						mDialog.dismiss();
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						if (statusCode == 200) {
							if (response.length() == 0) {
								mDialog.dismiss();
								getActivity().setResult(201);
								getActivity().finish();
								return;
							}
							try {
								JSONObject object = response.getJSONObject(0);
								mInfo = PersonInfo.parseJson(object);
								setAppLoginInfo();
								mDialog.dismiss();
								getActivity().setResult(201);
								getActivity().finish();
							} catch (JSONException e) {
								Log.e(StaticVariableSet.TAG, e.getMessage());
							}
						}
					}

				});
	}

	private void saveUserInfo(String salt, String userid) {
		ShequTools tool = new ShequTools(getActivity());
		tool.writeSharedPreferences(StaticVariableSet.SHARE_USER, userid);
		String pwd = ShequTools.md5(salt + userid);
		tool.writeSharedPreferences(StaticVariableSet.SHARE_PWD, pwd);
	}

	private void parseInfo(JSONObject object) throws JSONException {
		if (mInfo == null) {
			mInfo = new PersonInfo();
		}
		mInfo.setUserId(object.getString("userid"));
		mInfo.setUsername(object.getString("username"));
		mInfo.setEmail(object.getString("email"));
	}

	private void setAppLoginInfo() {
		ShequApplication app = (ShequApplication) (getActivity()
				.getApplication());
		app.setInfo(mInfo);
		app.setLogin(true);
	}
}
