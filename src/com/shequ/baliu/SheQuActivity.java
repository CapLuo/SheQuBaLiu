package com.shequ.baliu;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.fragment.BussinessFragment;
import com.shequ.baliu.fragment.CommunityFragment;
import com.shequ.baliu.fragment.HomeFragment;
import com.shequ.baliu.fragment.PersionFragment;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class SheQuActivity extends FragmentActivity implements OnClickListener {

	public static int request_code = 101;

	private ShequTools mShequTools;

	private boolean isList = true;
	private View mTitleBar;
	private ImageView mCommunityMode;
	private ImageView mReturnView;
	private TextView mTitle;
	private TextView mTextButton;

	// private TextView mHomeText;
	// private TextView mCommunityText;
	// private TextView mBusinessText;
	// private TextView mLifeText;
	// private TextView mActivityText;
	// private TextView mCarsText;
	// private TextView mNewsText;
	// private List<TextView> mBusinessList = new ArrayList<TextView>();

	private HomeFragment mHome;
	private CommunityFragment mCommunity;
	private BussinessFragment mBussiness;
	private PersionFragment mPersion;

	private RelativeLayout mHomeLayout;
	private RelativeLayout mCommunityLayout;
	private RelativeLayout mBussinessLayout;
	private RelativeLayout mPersionLayout;

	private ImageView mHomeLayoutImage;
	private ImageView mCommunityLayoutImage;
	private ImageView mBussinessLayoutImage;
	private ImageView mPersionLayoutImage;

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	// the position for the fragment id
	private int mCurrentPosition = 0;

	private boolean isLogin = false;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		// mDataHolder.setBusinessControlListener(new
		// ControlBusinessHolderListener() {
		// @Override
		// public void chageBusinessView() {
		// setDataPager();
		// }

		// @Override
		// public void chageContentView(ShequBusinessEnum bussiness) {
		// if (mDataHolder.getCurrentBusiness() == bussiness) {
		// fillContentView(bussiness);
		// }
		// }
		// });

		mShequTools = new ShequTools(this);
		mFragmentManager = getSupportFragmentManager();

		initView();
		// mDataHolder.setShequBussiness(ShequBusinessEnum.HOME);
	}

	private void initView() {
		mTitleBar = findViewById(R.id._title_bar);
		mReturnView = (ImageView) mTitleBar.findViewById(R.id._return);
		mReturnView.setOnClickListener(this);
		mTitle = (TextView) mTitleBar.findViewById(R.id._text_title);
		mTextButton = (TextView) mTitleBar.findViewById(R.id._text_button);
		mCommunityMode = (ImageView) mTitleBar
				.findViewById(R.id.community_mode);
		mCommunityMode.setOnClickListener(this);

		mHomeLayout = (RelativeLayout) findViewById(R.id._layout_home);
		mHomeLayout.setOnClickListener(this);
		mCommunityLayout = (RelativeLayout) findViewById(R.id._layout_community);
		mCommunityLayout.setOnClickListener(this);
		mBussinessLayout = (RelativeLayout) findViewById(R.id._layout_bussiness);
		mBussinessLayout.setOnClickListener(this);
		mPersionLayout = (RelativeLayout) findViewById(R.id._layout_person);
		mPersionLayout.setOnClickListener(this);

		mHomeLayoutImage = (ImageView) findViewById(R.id._layout_home_image);
		mCommunityLayoutImage = (ImageView) findViewById(R.id._layout_community_image);
		mBussinessLayoutImage = (ImageView) findViewById(R.id._layout_bussiness_image);
		mPersionLayoutImage = (ImageView) findViewById(R.id._layout_person_image);

		// mHomeText = (TextView) mTitleBar.findViewById(R.id._lable_home);
		// mCommunityText = (TextView)
		// mTitleBar.findViewById(R.id._lable_community);
		// mBusinessText = (TextView)
		// mTitleBar.findViewById(R.id._lable_business);
		// mLifeText = (TextView) mTitleBar.findViewById(R.id._lable_life);
		// mActivityText = (TextView)
		// mTitleBar.findViewById(R.id._lable_activity);
		// mCarsText = (TextView) mTitleBar.findViewById(R.id._lable_cars);
		// mNewsText = (TextView) mTitleBar.findViewById(R.id._lable_news);
		// mBusinessList.clear();
		// mBusinessList.add(mHomeText);
		// mBusinessList.add(mCommunityText);
		// mBusinessList.add(mBusinessText);
		// mBusinessList.add(mLifeText);
		// mBusinessList.add(mActivityText);
		// mBusinessList.add(mCarsText);
		// mBusinessList.add(mNewsText);
		// for (TextView view : mBusinessList) {
		// view.setClickable(true);
		// view.setOnClickListener(this);
		// 定义business view宽度
		// LayoutParams params = view.getLayoutParams();
		// params.width = mShequTools.getDisplayMetricsWidth() >> 2;
		// view.setLayoutParams(params);
		// }
		// 初始化home页面onCreate

		mDialog = new Dialog(this, R.style.dialog);
		View dialogContent = LayoutInflater.from(this).inflate(
				R.layout.dialog_network, null);
		dialogContent.findViewById(R.id.dialog_ok).setOnClickListener(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(dialogContent);

		setChoiceFragmentContent(0);
	}

	// private void setDataPager() {
	// View view = null;
	// switch (mDataHolder.getCurrentBusiness()) {
	// case COMMUNITY:
	// view = LayoutInflater.from(this).inflate(
	// R.layout.activity_community, null);
	// break;
	// case BUSINESS:
	// view = LayoutInflater.from(this).inflate(
	// R.layout.activity_business, null);
	// break;
	// case LIFE:
	// view = LayoutInflater.from(this).inflate(R.layout.activity_life,
	// null);
	// mList = (ListView) view.findViewById(R.id._list_car_activity);
	// break;
	// case CARS:
	// view = LayoutInflater.from(this).inflate(R.layout.activity_cars,
	// null);
	// mList = (ListView) view.findViewById(R.id._list_car_activity);
	// break;
	// case ACTIVITY:
	// view = LayoutInflater.from(this).inflate(
	// R.layout.activity_activity, null);
	// break;
	// case NEWS:
	// view = LayoutInflater.from(this).inflate(R.layout.activity_news,
	// null);
	// break;
	// case HOME:
	// default:
	// view = LayoutInflater.from(this).inflate(R.layout.activity_home,
	// null);
	// break;
	// }
	// if (null != view) {
	// mScrollView.removeAllViews();
	// mScrollView.addView(view);
	// }
	// businessTextHighLight();
	// fitBusinessScroll();
	// }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// case R.id._lable_home:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.HOME);
		// break;
		// case R.id._lable_community:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.COMMUNITY);
		// break;
		// case R.id._lable_business:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.BUSINESS);
		// break;
		// case R.id._lable_life:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.LIFE);
		// break;
		// case R.id._lable_activity:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.ACTIVITY);
		// break;
		// case R.id._lable_cars:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.CARS);
		// break;
		// case R.id._lable_news:
		// mDataHolder.setShequBussiness(ShequBusinessEnum.NEWS);
		// break;
		case R.id._return:
			this.onBackPressed();
			break;
		case R.id._layout_home:
			setChoiceFragmentContent(0);
			break;
		case R.id._layout_community:
			setChoiceFragmentContent(1);
			break;
		case R.id._layout_bussiness:
			setChoiceFragmentContent(2);
			break;
		case R.id._layout_person:
			setChoiceFragmentContent(3);
			break;
		case R.id._text_button:
			if (mPersion != null) {
				mPersion.loginOut(view);
			}
			break;
		case R.id.community_mode:
			isList = !isList;
			if (isList) {
				mCommunity.setModeLayout(isList);
				mCommunityMode.setImageResource(R.drawable.mode_list);

			} else {
				mCommunity.setModeLayout(isList);
				mCommunityMode.setImageResource(R.drawable.mode_grid);
			}
			break;
		case R.id.dialog_ok:
			mDialog.dismiss();
			break;
		default:
			break;
		}
	}

	public void setChoiceFragmentContent(int position) {
		mTextButton.setVisibility(View.INVISIBLE);
		mCommunityMode.setVisibility(View.INVISIBLE);
		mTransaction = mFragmentManager.beginTransaction();
		mCurrentPosition = position;
		hideAllFragment(mTransaction);
		clearAllSelector();
		switch (position) {
		case 0:
			mTitle.setText(getResources().getString(R.string.title_home));
			mHomeLayoutImage
					.setImageResource(R.drawable.ic_tabbar_course_pressed);
			if (mHome == null) {
				mHome = new HomeFragment(this);
				mTransaction.add(R.id._content_main, mHome);
			}
			mTransaction.show(mHome);
			break;
		case 1:
			mTitle.setText(getResources().getString(R.string.title_shequ));
			mCommunityLayoutImage
					.setImageResource(R.drawable.ic_tabbar_found_pressed);
			if (mCommunity == null) {
				mCommunity = new CommunityFragment();
				mTransaction.add(R.id._content_main, mCommunity);
			}
			mCommunityMode.setVisibility(View.VISIBLE);
			mTransaction.show(mCommunity);
			break;
		case 2:
			mTitle.setText(getResources().getString(R.string.business));
			mBussinessLayoutImage
					.setImageResource(R.drawable.ic_tabbar_business_pressed);
			if (mBussiness == null) {
				mBussiness = new BussinessFragment();
				mTransaction.add(R.id._content_main, mBussiness);
			}
			mTransaction.show(mBussiness);
			break;
		case 3:
			mTitle.setText(getResources().getString(R.string.title_center));
			mPersionLayoutImage
					.setImageResource(R.drawable.ic_tabbar_settings_pressed);
			if (isLogin) {
				mTextButton.setVisibility(View.VISIBLE);
				mTextButton.setText("退出");
				mTextButton.setOnClickListener(this);
			}
			if (mPersion == null) {
				mPersion = new PersionFragment();
				mTransaction.add(R.id._content_main, mPersion);
			}
			mTransaction.show(mPersion);
			break;
		default:
			break;
		}
		mTransaction.commit();
	}

	private void clearAllSelector() {
		mHomeLayoutImage.setImageResource(R.drawable.ic_tabbar_course_normal);
		mCommunityLayoutImage
				.setImageResource(R.drawable.ic_tabbar_found_normal);
		mBussinessLayoutImage
				.setImageResource(R.drawable.ic_tabbar_business_normal);
		mPersionLayoutImage
				.setImageResource(R.drawable.ic_tabbar_settings_normal);
	}

	private void hideAllFragment(FragmentTransaction transaction) {
		if (mHome != null) {
			transaction.hide(mHome);
		}
		if (mCommunity != null) {
			transaction.hide(mCommunity);
		}
		if (mBussiness != null) {
			transaction.hide(mBussiness);
		}
		if (mPersion != null) {
			transaction.hide(mPersion);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		StatService.onPageStart(this, "SheQuActivity");

		if (!ShequTools.isNetworkAvailable(this)) {
			mDialog.show();
		}

		ShequApplication app = (ShequApplication) getApplication();
		isLogin = app.getLogin();
		if (!app.getLogin()) {
			String user = mShequTools.getSharePreferences(
					StaticVariableSet.SHARE_USER, "");
			String pwd = mShequTools.getSharePreferences(
					StaticVariableSet.SHARE_PWD, "");
			if (!user.equals("") && !pwd.equals("")) {
				getInfo(user, pwd);
			}
		} else {
			if (mCurrentPosition == 2) {
				mTextButton.setText("退出");
				mTextButton.setOnClickListener(this);
				mTextButton.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onPause() {

		StatService.onPageEnd(this, "SheQuActivity");

		super.onPause();
	}

	private void getInfo(String user, final String pwd) {
		SqlHelper.getAllInfo("`" + StaticVariableSet.USER_MAIN
				+ "`.`userid` = '" + user + "'", new JsonHttpResponseHandler(
				"UTF-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					JSONObject object = response.getJSONObject(0);
					PersonInfo info = PersonInfo.parseJson(object);
					String salt = object.getString("salt");
					ShequApplication app = (ShequApplication) getApplication();
					if (pwd.equals(ShequTools.md5(salt + info.getUserId()))) {
						app.setLogin(true);
						isLogin = true;
						app.setInfo(info);
					}
				} catch (JSONException e) {
					Log.e(StaticVariableSet.TAG, e.getMessage());
				}
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == 400) {
			setChoiceFragmentContent(3);
		}
	}

	@Override
	public void onBackPressed() {
		if (0 == mCurrentPosition) {
			super.onBackPressed();
		} else {
			setChoiceFragmentContent(0);
		}
	}

	// private void businessTextHighLight() {
	// int index = ShequBusinessEnum.getValues(mDataHolder
	// .getCurrentBusiness());
	// for (int i = 0; i < mBusinessList.size(); i++) {
	// if (index == i) {
	// mBusinessList.get(i).setTextColor(
	// getResources().getColor(R.color.red));
	// } else {
	// mBusinessList.get(i).setTextColor(
	// getResources().getColor(R.color.black));
	// }
	// }
	// }

	// private void fitBusinessScroll() {
	// int index = ShequBusinessEnum.getValues(mDataHolder
	// .getCurrentBusiness());
	// int scrollChildWidth = mHomeText.getLayoutParams().width;
	// mBusinessScrollView.smoothScrollTo((int) (scrollChildWidth * index),
	// mBusinessScrollView.getScrollY());
	// }

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// mDownPointF.x = event.getX();
	// mDownPointF.y = event.getY();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// // 防止屏幕坏点
	// if (0 == (int) mDownPointF.x) {
	// mDownPointF.x = event.getX();
	// mDownPointF.y = event.getY();
	// }
	// break;
	// case MotionEvent.ACTION_UP:
	// float distanceX = Math.abs(event.getX() - mDownPointF.x);
	// float distanceY = Math.abs(event.getY() - mDownPointF.y);
	// if (distanceX > distanceY) {
	// // 左右翻页滑动
	// if (distanceX > 100 && event.getX() > mDownPointF.x) {
	// /*
	// * ShequBusinessEnum businessEnum = ShequBusinessEnum
	// * .getIntToEnum(ShequBusinessEnum
	// * .getValues(mDataHolder.getCurrentBusiness()) - 1); if
	// * (businessEnum != ShequBusinessEnum.NONE) {
	// * mDataHolder.setShequBussiness(businessEnum); }
	// */
	// mCurrentPosition = ((mCurrentPosition - 1) < 0 ? 0
	// : mCurrentPosition - 1);
	// setChoiceFragmentContent(mCurrentPosition);
	// } else if (distanceX > 100 && event.getX() < mDownPointF.x) {
	// /*
	// * ShequBusinessEnum businessEnum = ShequBusinessEnum
	// * .getIntToEnum(ShequBusinessEnum
	// * .getValues(mDataHolder.getCurrentBusiness()) + 1); if
	// * (businessEnum != ShequBusinessEnum.NONE) {
	// * mDataHolder.setShequBussiness(businessEnum); }
	// */
	// mCurrentPosition = ((mCurrentPosition + 1) > 2 ? 2
	// : mCurrentPosition + 1);
	// setChoiceFragmentContent(mCurrentPosition);
	// }
	// } else {
	// // 上下滑动
	// }
	// mDownPointF.x = 0;
	// mDownPointF.y = 0;
	// break;
	// default:
	// break;
	// }
	// return super.dispatchTouchEvent(event);
	// }

	// private void fillContentView(ShequBusinessEnum business) {
	// switch (business) {
	// case COMMUNITY:
	// break;
	// case BUSINESS:
	// break;
	// case LIFE:
	// break;
	// case CARS:
	// mList.setAdapter(new AdapterCars(this,
	// mDataHolder.getDataCarsHolder().getCars()));
	// ((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
	// mList.setOnItemClickListener(new OnItemClickListener() {

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View view, int position,
	// long arg3) {
	// Intent intent = new Intent();
	// intent.setClass(SheQuActivity.this, ShequShowActivity.class);
	// SheQuActivity.this.startActivity(intent);
	// }
	// });
	// fixListViewHeight(mList);
	// break;
	// case ACTIVITY:
	// break;
	// case NEWS:
	// break;
	// case HOME:
	// default:
	// break;
	// }
	// }

}