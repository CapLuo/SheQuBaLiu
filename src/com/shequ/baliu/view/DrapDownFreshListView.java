package com.shequ.baliu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shequ.baliu.R;

public class DrapDownFreshListView extends ListView implements OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0;// 下拉过程的状态值
	private final static int PULL_To_REFRESH = 1; // 从下拉返回到不刷新的状态值
	private final static int REFRESHING = 2;// 正在刷新的状态值
	private final static int DONE = 3;
	private final static int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private static int MAX_COUNT = 4;

	private LayoutInflater inflater;
	// ListView头部下拉刷新的布局
	private LinearLayout headerView;
	private TextView lvHeaderTipsTv;
	private TextView lvHeaderLastUpdatedTv;
	private ImageView lvHeaderArrowIv;
	private ProgressBar lvHeaderProgressBar;

	// 定义头部下拉刷新的布局的高度
	private int headerContentHeight;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int startY;
	private int state;
	private boolean isBack;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	public DrapDownFreshListView(Context context) {
		super(context);
		init(context);
	}

	public DrapDownFreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

}
