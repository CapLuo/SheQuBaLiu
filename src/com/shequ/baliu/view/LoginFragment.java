package com.shequ.baliu.view;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.ShequUserActivity;
import com.shequ.baliu.R;
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

	private Dialog mDialog;

	private TextView mDialogText;

	private String mUsernameText;
	private String mPasswordText;

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

	private void initView() {
		mUserName = (EditText) mContentView
				.findViewById(R.id.login_user_edittext);
		mPassword = (EditText) mContentView
				.findViewById(R.id.login_password_edittext);
		mLogin = (Button) mContentView.findViewById(R.id.login_button);
		mRegister = (TextView) mContentView.findViewById(R.id.login_register);

		mDialog = new Dialog(getActivity()) {

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
							String salt = object.getString("salt");
							String pwd = object.getString("pwd");
							if (pwd.equals(ShequTools.md5(salt + mPasswordText))) {
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
							try {
								JSONObject object = response.getJSONObject(0);
								PersonInfo info = PersonInfo.parseJson(object);
								String salt = object.getString("salt");
								ShequApplication app = (ShequApplication) (getActivity()
										.getApplication());
								app.setInfo(info);
								app.setLogin(true);
								ShequTools tool = new ShequTools(getActivity());
								tool.writeSharedPreferences(
										StaticVariableSet.SHARE_USER,
										info.getUserId());
								String pwd = ShequTools.md5(salt
										+ info.getUserId());
								tool.writeSharedPreferences(
										StaticVariableSet.SHARE_PWD, pwd);
								mDialog.dismiss();
								getActivity().finish();
							} catch (JSONException e) {
								Log.e(StaticVariableSet.TAG, e.getMessage());
							}
						}
					}

				});
	}
}
