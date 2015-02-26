package com.shequ.baliu.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shequ.baliu.util.StaticVariableSet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	// log的标签
	public static final String LOCATION_URL = "http://www.google.com/loc/json";
	public static final String LOCATION_HOST = "maps.google.com";

	/**
	 * 获取地理位置
	 * 
	 * @throws Exception
	 */
	public static String getLocation(String latitude, String longitude)
			throws Exception {
		String resultString = "";

		/** 这里采用get方法，直接将参数加到URL上 */
		String urlString = String.format(
				"http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", latitude,
				longitude);
		Log.i(StaticVariableSet.TAG, "Util: getLocation: URL: " + urlString);

		/** 新建HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** 采用GET方法 */
		HttpGet get = new HttpGet(urlString);
		try {
			/** 发起GET请求并获得返回数据 */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			/** 解析JSON数据，获得物理地址 */
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark")
						.toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString(
							"address");
				}
			}
		} catch (Exception e) {
			throw new Exception("获取物理位置出现错误:" + e.getMessage());
		} finally {
			get.abort();
			client = null;
		}

		return resultString;
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvaliable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) (context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		return !(networkinfo == null || !networkinfo.isAvailable());
	}

	/**
	 * 判断网络类型 wifi 3G
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiNetwrokType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if (info != null && info.isAvailable()) {
			if (info.getTypeName().equalsIgnoreCase("wifi")) {
				return true;
			}
		}
		return false;
	}
}