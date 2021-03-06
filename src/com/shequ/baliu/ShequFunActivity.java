package com.shequ.baliu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.shequ.baliu.fragment.BankFragment;
import com.shequ.baliu.fragment.NeighbourFragment;
import com.shequ.baliu.fragment.RepairExpressFragment;
import com.shequ.baliu.fragment.SecondGoodDetailFragment;
import com.shequ.baliu.fragment.SecondhandFragment;
import com.shequ.baliu.fragment.SecondhandFragment.OnClickGoodDetail;
import com.shequ.baliu.holder.SecondHandGoods;
import com.shequ.baliu.util.ShequFunEnum;

public class ShequFunActivity extends FragmentActivity {

	public static int result_islogin = 200;
	public static int result_notlogin = 400;

	private View mTitleBar;
	private TextView mTitle;
	private View mReturn;
	private TextView mTextButton;

	private NeighbourFragment mNeighbourFragment;
	private SecondhandFragment mSecondhandFragment;
	private RepairExpressFragment mRepairExpressFragment;
	private BankFragment mBankFragment;
	private SecondGoodDetailFragment mSecondGoodDetailFragment;

	private FragmentManager mFragmentManager;

	private ShequFunEnum mFun = ShequFunEnum.NONE;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		this.setContentView(R.layout.activity_fun);

		initView();
		initViewData();
	}

	@Override
	protected void onPause() {
		StatService.onPageEnd(this, "ShequFunActivity");
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onPageStart(this, "ShequFunActivity");
	}

	private void initView() {
		mFragmentManager = getSupportFragmentManager();

		mTitleBar = findViewById(R.id._title_bar);
		mTitle = (TextView) mTitleBar.findViewById(R.id._text_title);
		mTextButton = (TextView) mTitleBar.findViewById(R.id._text_button);
		mReturn = findViewById(R.id._return);
		mReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShequFunActivity.this.onBackPressed();
			}
		});
	}

	private void initViewData() {
		Intent intent = getIntent();
		String titleName = intent.getExtras().getString("title");
		if (titleName != null && !titleName.equals("")) {
			mTitle.setText(titleName);
		}
		int position = intent.getExtras().getInt("position");

		choiceFragment(ShequFunEnum.getIntToEnum(position));
	}

	private void choiceFragment(ShequFunEnum fun) {
		if (fun == ShequFunEnum.NEIGHBOUR) {
			ShequApplication app = (ShequApplication) getApplication();
			if (!app.getLogin()) {
				setResult(result_notlogin);
				onBackPressed();
				return;
			}
		}
		mTextButton.setVisibility(View.INVISIBLE);
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideAllFragment(transaction);
		switch (fun) {
		case NEIGHBOUR:
			mFun = ShequFunEnum.NEIGHBOUR;
			if (mNeighbourFragment == null) {
				mNeighbourFragment = new NeighbourFragment();
				transaction.add(R.id._content_main, mNeighbourFragment);
			}
			transaction.show(mNeighbourFragment);
			break;
		case SECONDHAND:
			mFun = ShequFunEnum.SECONDHAND;
			mTextButton.setText(R.string.second_hand_release);
			mTextButton.setVisibility(View.VISIBLE);
			mTextButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					ShequApplication app = (ShequApplication) getApplication();
					if (!app.getLogin()) {
						setResult(result_notlogin);
						onBackPressed();
						return;
					}
					Intent intent = new Intent();
					intent.setClass(ShequFunActivity.this,
							ShequReleaseActivity.class);
					ShequFunActivity.this.startActivity(intent);
				}
			});
			if (mSecondhandFragment == null) {
				mSecondhandFragment = new SecondhandFragment();
				mSecondhandFragment
						.setOnClickGoodDetail(new OnClickGoodDetail() {
							@Override
							public void setGoodDetailClick(SecondHandGoods good) {
								FragmentTransaction transaction = mFragmentManager
										.beginTransaction();
								hideAllFragment(transaction);
								mFun = ShequFunEnum.SECONDDETAILS;
								if (mSecondGoodDetailFragment == null) {
									mSecondGoodDetailFragment = new SecondGoodDetailFragment();
									transaction.add(R.id._content_main,
											mSecondGoodDetailFragment);
								}
								mSecondGoodDetailFragment.setGoodDetail(good);
								transaction.addToBackStack(null);
								transaction.show(mSecondGoodDetailFragment);
								transaction.commit();
							}
						});
				transaction.add(R.id._content_main, mSecondhandFragment);
			}
			transaction.show(mSecondhandFragment);
			break;
		case REPAIR:
			mFun = ShequFunEnum.REPAIR;
			if (mRepairExpressFragment == null) {
				mRepairExpressFragment = new RepairExpressFragment();
				transaction.add(R.id._content_main, mRepairExpressFragment);
			}
			transaction.show(mRepairExpressFragment);
			break;
		case EXPRESS:
			mFun = ShequFunEnum.EXPRESS;
			if (mRepairExpressFragment == null) {
				mRepairExpressFragment = new RepairExpressFragment();
				transaction.add(R.id._content_main, mRepairExpressFragment);
			}
			transaction.show(mRepairExpressFragment);
			break;
		case TOGHTER:
			mFun = ShequFunEnum.TOGHTER;
			if (mRepairExpressFragment == null) {
				mRepairExpressFragment = new RepairExpressFragment();
				transaction.add(R.id._content_main, mRepairExpressFragment);
			}
			transaction.show(mRepairExpressFragment);
			break;
		case DISTRIBUTION:
			mFun = ShequFunEnum.DISTRIBUTION;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
		case PROPERTY:
			mFun = ShequFunEnum.PROPERTY;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
		case MEDIA:
			mFun = ShequFunEnum.MEDIA;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
		case BANKING:
			mFun = ShequFunEnum.BANKING;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
			break;
		default:
			mFun = ShequFunEnum.NONE;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
			break;
		}
		transaction.commit();
	}

	public ShequFunEnum getCurrentFunEnum() {
		return mFun;
	}

	private void hideAllFragment(FragmentTransaction transaction) {
		if (mNeighbourFragment != null) {
			transaction.hide(mNeighbourFragment);
		}
		if (mSecondhandFragment != null) {
			transaction.hide(mSecondhandFragment);
		}
		if (mRepairExpressFragment != null) {
			transaction.hide(mRepairExpressFragment);
		}
		if (mBankFragment != null) {
			transaction.hide(mBankFragment);
		}
		if (mSecondGoodDetailFragment != null) {
			transaction.hide(mSecondGoodDetailFragment);
		}
	}

	@Override
	public void onBackPressed() {
		if (ShequFunEnum.SECONDDETAILS == mFun) {
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			if (mSecondGoodDetailFragment != null) {
				transaction.remove(mSecondGoodDetailFragment);
				mSecondGoodDetailFragment = null;
			}
			transaction.commit();
		}
		super.onBackPressed();
	}

}
