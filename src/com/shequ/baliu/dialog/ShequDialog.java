package com.shequ.baliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ShequDialog extends Dialog {

	private static int showTimes = 10000;// ms

	private static int FLAG_DISMISS = 1;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == FLAG_DISMISS) {
				ShequDialog.this.dismiss();
			} else {
				super.handleMessage(msg);
			}
		}

	};

	public ShequDialog(Context context) {
		super(context);
	}

	public ShequDialog(Context context, int theme) {
		super(context, theme);
	}

	protected ShequDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	public void dismiss() {
		if (this.isShowing()) {
			super.dismiss();
		}
	}

	@Override
	public void show() {
		super.show();
		mHandler.sendEmptyMessageDelayed(FLAG_DISMISS, showTimes);
	}
}
