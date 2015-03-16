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
import com.shequ.baliu.fragment.SecondhandFragment;
import com.shequ.baliu.util.ShequFunEnum;

public class ShequFunActivity extends FragmentActivity {

	public static int result_islogin = 200;
	public static int result_notlogin = 400;

	private View mTitleBar;
	private TextView mTitle;
	private View mReturn;

	private NeighbourFragment mNeighbourFragment;
	private SecondhandFragment mSecondhandFragment;
	private RepairExpressFragment mRepairExpressFragment;
	private BankFragment mBankFragment;

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
				Toast.makeText(this, "请先登入", Toast.LENGTH_SHORT).show();
				setResult(result_notlogin);
				onBackPressed();
				return;
			}
		}
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
			if (mSecondhandFragment == null) {
				mSecondhandFragment = new SecondhandFragment();
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
		case PROPERTY:
			mFun = ShequFunEnum.PROPERTY;
		case MEDIA:
			mFun = ShequFunEnum.MEDIA;
		case BANKING:
			mFun = ShequFunEnum.BANKING;
			if (mBankFragment == null) {
				mBankFragment = new BankFragment();
				transaction.add(R.id._content_main, mBankFragment);
			}
			transaction.show(mBankFragment);
			break;
		default:
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
	}
}
