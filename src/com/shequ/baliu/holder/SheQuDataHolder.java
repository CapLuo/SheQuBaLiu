package com.shequ.baliu.holder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;

public class SheQuDataHolder {

	private ControlBusinessHolderListener mControlBusinessHolderListener = new ControlBusinessHolderListener() {

		@Override
		public void chageBusinessView() {
		}
	};

	public interface ControlBusinessHolderListener {
		public void chageBusinessView();

	}

	public SheQuDataHolder() {
		initData();
	}

	public void setBusinessControlListener(ControlBusinessHolderListener l) {
		mControlBusinessHolderListener = l;
	}

	private void initData() {
	}

	private void getLifeInfo() {
		SqlHelper.get(StaticVariableSet.LIFE_LABLE, "parentid = 0",
				new JsonHttpResponseHandler("UTF-8"));
	}

	/*
	 * SELECT * FROM `club_article` WHERE isrecommend = 1 LIMIT 0 , 30 //limit 0
	 * - 30
	 */
	private void getCarsInfo() {
		SqlHelper.get(StaticVariableSet.CARS_MAIN,
				"isrecommend = 1 LIMIT 0, 5", new JsonHttpResponseHandler(
						"UTF-8"));

	}
}
