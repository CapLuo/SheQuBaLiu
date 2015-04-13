package com.shequ.baliu.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ShequTools {

	private Context mContext;

	private int mDisplayMetricsWidth;
	private int mDisplayMetricsHeight;
	private float mDensity;

	public ShequTools(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext = context;
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mDisplayMetricsHeight = dm.heightPixels;
		mDisplayMetricsWidth = dm.widthPixels;
		mDensity = dm.densityDpi;
		createAppDir();
	}

	public float getDensity() {
		return mDensity;
	}

	public int getDisplayMetricsWidth() {
		return mDisplayMetricsWidth;
	}

	public int getDisplayMetricsHeight() {
		return mDisplayMetricsHeight;
	}

	public void writeSharedPreferences(String key, String value) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				StaticVariableSet.SHARE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getSharePreferences(String key, String default_value) {
		String value;
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				StaticVariableSet.SHARE_NAME, Context.MODE_PRIVATE);
		value = sharedPreferences.getString(key, default_value);
		return value;
	}

	public Bitmap decodeBitmap(String path, int displayWidth, int displayHeight) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op);
		int width = (int) Math.ceil(op.outWidth / (float) displayWidth);
		int height = (int) Math.ceil(op.outHeight / (float) displayHeight);
		if (width > 1 && height > 1) {
			if (width > height) {
				op.inSampleSize = width;
			} else {
				op.inSampleSize = height;
			}
			op.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(path, op);
		}
		return Bitmap
				.createScaledBitmap(bmp, displayWidth, displayHeight, true);
	}

	public Bitmap decodeBitmap(String path, int maxImageSize) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op);
		int scale = 1;
		if (op.outWidth > maxImageSize || op.outHeight > maxImageSize) {
			scale = (int) Math.pow(
					2,
					(int) Math.round(Math.log(maxImageSize
							/ (double) Math.max(op.outWidth, op.outHeight))
							/ Math.log(0.5)));
		}
		op.inJustDecodeBounds = false;
		op.inSampleSize = scale;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}

	public boolean isLoginSuccess(String password, String username) {
		SqlHelper.get(StaticVariableSet.USER_MAIN, " username = '" + username
				+ "'", new JsonHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject json = response.getJSONObject(i);
						String passwordMD5 = json.getString("pwd");
						String salt = json.getString("salt");
					}
				} catch (JSONException e) {

				}
			}

		});
		return false;
	}

	/*
	 * MD5
	 * 
	 * @param string: 随即字串 + 登入密码
	 */
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	// 获取SDCard 路径
	public static String getExternalStoragePath() {
		try {
			Class<?> klass = Class
					.forName("android.os.Environment$UserEnvironment");

			Method method = klass
					.getDeclaredMethod("getExternalStorageDirectory");
			Object object = method.invoke(klass.getConstructor(int.class)
					.newInstance(new Object[] { 0 }));

			if (object != null) {
				return object.toString();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	private void createAppDir() {
		String sdcard = getExternalStoragePath();
		File sd = new File(sdcard);
		File baliuDir = new File(sd, StaticVariableSet.TAG);
		if (!baliuDir.exists()) {
			baliuDir.mkdir();
		}
	}

	// 获得随机字符串对密码加密
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void parserHtmlContent() {

	}

}