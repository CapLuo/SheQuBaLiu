package com.shequ.baliu.dialog;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.util.StaticVariableSet;

public class ShowAdDialog extends Dialog {


	private ImageView mAdView;

	public ShowAdDialog(Context context) {
		super(context);
		initView();
	}

	protected ShowAdDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initView();
	}

	public ShowAdDialog(Context context, int theme) {
		super(context, theme);
		initView();
	}

	private void initView() {
		this.setContentView(R.layout.dialog_ad_layout);
		this.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		mAdView = (ImageView) findViewById(R.id._dialog_image_view);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(StaticVariableSet.AD_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if(statusCode==200){  
					BitmapFactory factory=new BitmapFactory();  
					Bitmap bitmap=factory.decodeByteArray(responseBody, 0, responseBody.length);  
					mAdView.setImageBitmap(bitmap);  
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
				
			}
		});
	}
}
