package com.shequ.baliu.fragment;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.dialog.ShequDialog;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class RegisterFragment extends Fragment implements OnClickListener,
		OnFocusChangeListener {

	private View mContentView;

	private EditText mUser;
	private EditText mEmail;
	private EditText mPassword;
	private EditText mRepeatPassword;

	private Button mRegister;

	private String mUsernameText;
	private String mEmailText;
	private String mPasswordText;
	private String mSaltText;

	private ShequDialog mDialog;

	private TextView mAgreementText;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_register,
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
		StatService.onPageStart(getActivity(), "RegisterFragment");
	}

	@Override
	public void onPause() {
		StatService.onPageEnd(getActivity(), "RegisterFragment");
		super.onPause();
	}

	private void initView() {
		mUser = (EditText) mContentView.findViewById(R.id.register_user);
		mEmail = (EditText) mContentView.findViewById(R.id.register_email);
		mPassword = (EditText) mContentView
				.findViewById(R.id.register_password);
		mRepeatPassword = (EditText) mContentView
				.findViewById(R.id.register_repeat_password);

		mUser.setOnFocusChangeListener(this);
		mEmail.setOnFocusChangeListener(this);
		mPassword.setOnFocusChangeListener(this);
		mRepeatPassword.setOnFocusChangeListener(this);

		mAgreementText = (TextView) mContentView
				.findViewById(R.id.register_agreement);

		mRegister = (Button) mContentView.findViewById(R.id.register_button);

		mDialog = new ShequDialog(getActivity()) {

			@Override
			public void onBackPressed() {
				// 屏蔽返回建
			}

		};
		View dialogContent = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_login_layout, null);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(dialogContent);
	}

	private void initData() {
		mRegister.setOnClickListener(this);

		String argment_gray = getActivity().getResources().getString(
				R.string.register_agreement_gray);
		String argment_blue = getActivity().getResources().getString(
				R.string.register_agreement_blue);
		SpannableStringBuilder builder = new SpannableStringBuilder(
				argment_gray + argment_blue);
		ForegroundColorSpan graySpan = new ForegroundColorSpan(getActivity()
				.getResources().getColor(R.color.edit_stroke_gray));
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(getActivity()
				.getResources().getColor(R.color.title_blue));
		builder.setSpan(graySpan, 0, argment_gray.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(blueSpan, argment_gray.length(), argment_gray.length()
				+ argment_blue.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mAgreementText.setText(builder);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_button:
			getRegister();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.register_user:
			if (!hasFocus) {
				if (isUserTextNull()) {
					return;
				}
				getUsernameIsAlreadyRegister(false);
			}
			return;
		case R.id.register_email:
			if (!hasFocus) {
				if (isEmailNull()) {
					return;
				}
				if (isEmailWrong()) {
					return;
				}
				getEmailIsAlreadyRegister(false);
			}
			return;
		case R.id.register_password:
			if (!hasFocus) {
				if (isPwdNull()) {
					return;
				}
			}
			return;
		case R.id.register_repeat_password:
			if (!hasFocus) {
				String repeatPasswordText = mRepeatPassword.getText()
						.toString();
				if (isRePwdNull(repeatPasswordText)) {
					return;
				}
				if (!isPwdSame(repeatPasswordText)) {
					return;
				} else {
					return;
				}
			}
		default:
			break;
		}
	}

	private void getRegister() {
		mDialog.show();
		if (isUserTextNull()) {
			mUser.requestFocus();
			dismiss();
			return;
		} else {
			getUsernameIsAlreadyRegister(true);
		}

	}

	private void gotoRegisterForNet() {
		mSaltText = ShequTools.getRandomString(6);
		String md5 = ShequTools.md5(mSaltText + mPasswordText);
		insertUserMain(mUsernameText, mEmailText, md5, mSaltText);
		insertUserInfo(mUsernameText, mEmailText,
				String.valueOf(new Date().getTime()));
	}

	private boolean isUserTextNull() {
		mUsernameText = mUser.getText().toString();
		if (mUsernameText == null || mUsernameText.equals("")) {
			return true;
		}
		return false;
	}

	private boolean isEmailNull() {
		mEmailText = mEmail.getText().toString();
		if (mEmailText == null || mEmailText.equals("")) {
			return true;
		}
		return false;
	}

	private boolean isEmailWrong() {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (!pattern.matcher(mEmailText).matches()) {
			Toast.makeText(getActivity(), R.string.register_email_wrong,
					Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	private boolean isPwdNull() {
		mPasswordText = mPassword.getText().toString();
		if (mPasswordText == null || mPasswordText.equals("")) {
			return true;
		}
		return false;
	}

	private boolean isRePwdNull(String repeatPasswordText) {
		if (repeatPasswordText == null || repeatPasswordText.equals("")) {
			return true;
		}
		return false;
	}

	private boolean isPwdSame(String repeatPasswordText) {
		if (!repeatPasswordText.equals(mPasswordText)) {
			Toast.makeText(getActivity(), R.string.register_password_not_same,
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private void getUsernameIsAlreadyRegister(final boolean isClickRegister) {
		SqlHelper.getRow(StaticVariableSet.USER_MAIN, "COUNT(*)",
				"username = '" + mUsernameText + "'",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								int count = Integer.parseInt(json
										.getString("COUNT(*)"));
								if (count == 0) {
									if (isClickRegister) {
										if (isEmailNull()) {
											mEmail.requestFocus();
											dismiss();
											return;
										}
										if (isEmailWrong()) {
											mEmail.requestFocus();
											dismiss();
											return;
										}
										getEmailIsAlreadyRegister(true);
									}
								} else {
									Toast.makeText(getActivity(),
											R.string.register_already,
											Toast.LENGTH_SHORT).show();
									if (isClickRegister) {
										mUser.requestFocus();
									}
									dismiss();
								}
							}
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});
	}

	private void getEmailIsAlreadyRegister(final boolean isClickRegister) {
		SqlHelper.getRow(StaticVariableSet.USER_MAIN, "COUNT(*)", "email = '"
				+ mEmailText + "'", new JsonHttpResponseHandler("UTF-8") {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				dismiss();
				Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				dismiss();
				Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				dismiss();
				Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
				Toast.makeText(getActivity(), R.string.login_error_net,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject json = response.getJSONObject(i);
						int count = Integer.parseInt(json.getString("COUNT(*)"));
						if (count == 0) {
							if (isClickRegister) {
								if (isPwdNull()) {
									mPassword.requestFocus();
									dismiss();
									return;
								}
								String mRepwd = mRepeatPassword.getText()
										.toString();
								if (isRePwdNull(mRepwd)) {
									mRepeatPassword.requestFocus();
									dismiss();
									return;
								}
								if (isPwdSame(mRepwd)) {
									gotoRegisterForNet();
								} else {
									mRepeatPassword.requestFocus();
									dismiss();
								}
							}
						} else {
							Toast.makeText(getActivity(),
									R.string.register_email_register,
									Toast.LENGTH_SHORT).show();
							if (isClickRegister) {
								mEmail.requestFocus();
							}
							dismiss();
						}
					}
				} catch (JSONException e) {
					Log.e(StaticVariableSet.TAG, e.getMessage());
				}
			}

		});
	}

	private void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	private void insertUserMain(String username, String email, String pwd,
			String salt) {
		SqlHelper.add(StaticVariableSet.USER_MAIN,
				"(`username`, `pwd`, `salt`, `email`) VALUES ('" + username
						+ "', '" + pwd + "', '" + salt + "', '" + email + "')");
	}

	private void insertUserInfo(final String username, final String email,
			final String addtime) {
		SqlHelper.getRow(StaticVariableSet.USER_MAIN, "userid, salt",
				"username = '" + username + "'", new JsonHttpResponseHandler(
						"UTF-8") {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dismiss();
						Log.e(StaticVariableSet.TAG, "网络连接不稳定！");
						Toast.makeText(getActivity(), R.string.login_error_net,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							JSONObject object = response.getJSONObject(0);
							String userid = object.getString("userid");
							String salt = object.getString("salt");
							PersonInfo info = new PersonInfo();
							info.setUserId(userid);
							info.setUsername(username);
							info.setEmail(email);
							SqlHelper.add(StaticVariableSet.USER_INFO,
									"(`userid`, `username`, `email`, `addtime`) VALUES ('"
											+ userid + "', '" + username
											+ "', '" + email + "', '" + addtime
											+ "')");
							ShequTools tools = new ShequTools(getActivity());
							tools.writeSharedPreferences(
									StaticVariableSet.SHARE_USER, userid);
							String pwd = ShequTools.md5(salt + userid);
							tools.writeSharedPreferences(
									StaticVariableSet.SHARE_PWD, pwd);
							ShequApplication app = (ShequApplication) (getActivity()
									.getApplication());
							app.setInfo(info);
							app.setLogin(true);
							dismiss();
							getActivity().setResult(201);
							getActivity().finish();
						} catch (JSONException e) {
							getActivity().setResult(401);
							getActivity().finish();
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});
	}
}
