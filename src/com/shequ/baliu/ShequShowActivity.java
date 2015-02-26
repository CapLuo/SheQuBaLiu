package com.shequ.baliu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class ShequShowActivity extends Activity implements OnClickListener {

	private WebView mWebView;
	private String mWebPath = "";

	private ImageView mReturnImg;
	private TextView mTitle;
	private TextView mTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_page);

		initView();

	}

	@SuppressLint("NewApi")
	private void initView() {
		mReturnImg = (ImageView) findViewById(R.id._img_return_show);
		mReturnImg.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id._title_show_page);
		mTimes = (TextView) findViewById(R.id._title_times_show);

		Intent intent = getIntent();
		Bundle bundle;
		if ((bundle = intent.getExtras()) != null) {
			parseIntent(bundle);
		}

		mWebView = (WebView) findViewById(R.id._web_content);

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setUseWideViewPort(true);// 關鍵點

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		//webSettings.setDisplayZoomControls(false);
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); // 支持缩放
		webSettings.setLoadWithOverviewMode(true);

		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomDensity = WebSettings.ZoomDensity.FAR;
			break;
		}
		webSettings.setDefaultZoom(zoomDensity);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});

		if (!mWebPath.equals("")) {
			mWebView.loadUrl(mWebPath);
		}
	}

	private void parseIntent(Bundle bundle) {
/*		String title = "";
		title = bundle.getString("TITLE");
		if (!title.equals("")) {
			mTitle.setText(title);
		}

		String time = "";
		time = bundle.getString("TIME");
		if (!time.equals("")) {
			mTimes.setText(time);
		}*/

		String path = "";
		path = bundle.getString("PATH");
		if (!path.equals("")) {
			mWebPath = path;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._img_return_show:
			this.finish();
			break;
		default:
			break;
		}
	}
}
