package com.shequ.baliu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.adapter.AdapterShequChoice;
import com.shequ.baliu.holder.ShequSortModelHolder;
import com.shequ.baliu.util.CharacterParser;
import com.shequ.baliu.util.DBManager;
import com.shequ.baliu.util.PinyinComparator;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;
import com.shequ.baliu.view.SlideBar;
import com.shequ.baliu.view.SlideBar.OnTouchLetterChangeListenner;

public class ChoiceSequActivity extends Activity implements
		OnItemClickListener, OnClickListener {

	private ListView mListView;
	private SlideBar mSlideBar;
	private AdapterShequChoice mAdapter;
	private List<ShequSortModelHolder> mList;

	private EditText mEditText;

	private DBManager mDBManager;

	private ImageView mReturn;
	private ImageView mClose;

	private boolean isNeedRefresh = false;
	private Dialog mDialog;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_choice_shequ);

		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		mSlideBar = (SlideBar) findViewById(R.id._shequ_choice_slideBar);
		// 设置右侧触摸监听
		mSlideBar
				.setOnTouchLetterChangeListenner(new OnTouchLetterChangeListenner() {

					@Override
					public void onTouchLetterChange(MotionEvent event, String s) {
						// 该字母首次出现的位置
						int position = mAdapter.getPositionForSection(s
								.charAt(0));
						if (position != -1) {
							mListView.setSelection(position);
						}
					}
				});
		mListView = (ListView) findViewById(R.id._shequ_choice_list);

		mList = new ArrayList<ShequSortModelHolder>();

		mEditText = (EditText) findViewById(R.id._shequ_choice_edit);
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
				if (TextUtils.isEmpty(s.toString())) {
					mClose.setVisibility(View.INVISIBLE);
				} else {
					mClose.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mReturn = (ImageView) findViewById(R.id._return);
		mReturn.setOnClickListener(this);
		mClose = (ImageView) findViewById(R.id._shequ_choice_cancel);
		mClose.setOnClickListener(this);
		mClose.setVisibility(View.INVISIBLE);

		mDialog = new Dialog(this) {
			@Override
			public void onBackPressed() {
				// 屏蔽换回建
			}
		};
		TextView mDialogText = new TextView(this);
		mDialogText.setText(getResources().getString(
				R.string.fresh_another_data));
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(mDialogText);

		mDBManager = new DBManager(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 根据a-z进行排序源数据
		mList = mDBManager.queryGroups();
		if (mList.size() == 0) {
			isNeedRefresh = true;
			mDialog.show();
		} else {
			isNeedRefresh = false;
		}
		mAdapter = new AdapterShequChoice(this, mList);
		mListView.setAdapter(mAdapter);
		mListView.requestFocus();
		mListView.setOnItemClickListener(this);
		getShequListCount();
	}

	private void getShequListCount() {
		SqlHelper.getRow(StaticVariableSet.SHEQU_GROUP, "COUNT(*)", "1",
				new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								int count = Integer.parseInt(json
										.getString("COUNT(*)"));
								if (count != mDBManager.getGroupsCount()) {
									mDBManager
											.deleteAllData(SqlHelper.GROUP_TABLE_NAME);
									getShequList();
								}
							}
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dismiss();
					}

				});
	}

	/**
	 * 网络获取 社区名信息
	 */
	private void getShequList() {
		SqlHelper.getRow(StaticVariableSet.SHEQU_GROUP, "groupid, groupname",
				"1", new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dismiss();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						try {
							List<ShequSortModelHolder> holder = new ArrayList<ShequSortModelHolder>();
							for (int i = 0; i < response.length(); i++) {
								JSONObject json = response.getJSONObject(i);
								String groupid = json.getString("groupid");
								String name = json.getString("groupname");
								if (groupid != null && !groupid.equals("")
										&& name != null && !name.equals("")) {
									ShequSortModelHolder model = new ShequSortModelHolder();
									model.setGroupid(groupid);
									model.setName(name);
									String pinyin = characterParser
											.getSelling(name);
									String sortString = pinyin.substring(0, 1)
											.toUpperCase();
									if (sortString.matches("[A-Z]")) {
										model.setSortLetters(sortString);
									} else {
										model.setSortLetters("#");
									}
									holder.add(model);
								}
							}
							mDBManager.addGroups(holder);
							if (isNeedRefresh) {
								mList = holder;
								filterData("");
								mListView.requestFocus();
								dismiss();
							}
						} catch (JSONException e) {
							Log.e(StaticVariableSet.TAG, e.getMessage());
						}
					}

				});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<ShequSortModelHolder> filterDateList = new ArrayList<ShequSortModelHolder>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = mList;
		} else {
			filterDateList.clear();
			for (ShequSortModelHolder sortModel : mList) {
				String name = sortModel.getName();
				if (name.toUpperCase().indexOf(
						filterStr.toString().toUpperCase()) != -1
						|| characterParser.getSelling(name).toUpperCase()
								.startsWith(filterStr.toString().toUpperCase())) {
					filterDateList.add(sortModel);
				}
			}
		}
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		mAdapter.updateListView(filterDateList);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShequTools tools = new ShequTools(ChoiceSequActivity.this);
		Object model = mAdapter.getItem(position);
		if (model instanceof ShequSortModelHolder) {
			ShequSortModelHolder modelHolder = (ShequSortModelHolder) model;
			updateGroupFromNet(modelHolder.getId(), modelHolder.getName());
		}
		ChoiceSequActivity.this.finish();
	}

	private void updateGroupFromNet(String groupid, String groupname) {
		ShequApplication app = (ShequApplication) getApplication();
		String userid = app.getInfo().getUserId();
		app.getInfo().setGroupId(groupid);
		app.getInfo().setGroupName(groupname);
		if (!TextUtils.isEmpty(userid)) {
			SqlHelper.update(StaticVariableSet.SHEQU_GROUP_RELATION, "`groupid` = "
					+ groupid, "`userid` = " + userid);
		}
	}

	@Override
	public void finish() {
		mDBManager.closeDB();
		super.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._return:
			this.finish();
			break;
		case R.id._shequ_choice_cancel:
			mEditText.setText("");
			v.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	private void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
