package com.shequ.baliu.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.shequ.baliu.util.StaticVariableSet;

public class WifiLocationManager {

	private Context mContext;
	private WifiManager wifiManager;

	public WifiLocationManager(Context context) {
		mContext = context;
		wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
	}

	public void getLocation(BroadcastReceiver receiver) {
		mContext.registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
	}

	public List<ScanResult> getWifiList() {
		return wifiManager.getScanResults();
	}

	public String getLocationWifi() {

		/** 采用Android默认的HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** 采用POST方法 */
		HttpPost post = new HttpPost(StaticVariableSet.LOCATION_URL);
		try {
			/** 构造POST的JSON数据 */
			JSONObject holder = new JSONObject();
			holder.put("version", "1.1.0");
			holder.put("host", StaticVariableSet.LOCATION_HOST);
			holder.put("address_language", "zh_CN");
			holder.put("request_address", true);

			JSONArray towerarray = new JSONArray();
			List<ScanResult> wifiList = getWifiList();
			for (int i = 0; i < wifiList.size(); i++) {
				JSONObject tower = new JSONObject();
				tower.put("mac_address", wifiList.get(i).BSSID);
				tower.put("ssid", wifiList.get(i).SSID);
				tower.put("signal_strength", wifiList.get(i).level);
				towerarray.put(tower);
			}

			holder.put("wifi_towers", towerarray);
			Log.d(StaticVariableSet.TAG, "holder.put: " + holder.toString());

			StringEntity query = new StringEntity(holder.toString());
			post.setEntity(query);

			/** 发出POST数据并获取返回数据 */
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			Log.d(StaticVariableSet.TAG, "result: " + strBuff.toString());

			/** 解析返回的JSON数据获得经纬度 */
			JSONObject json = new JSONObject(strBuff.toString());
			JSONObject subjosn = new JSONObject(json.getString("location"));

			String latitude = subjosn.getString("latitude");
			String longitude = subjosn.getString("longitude");

			return NetUtil.getLocation(latitude, longitude);

		} catch (ClientProtocolException e) {
			Log.e(StaticVariableSet.TAG, "ClientProtocolException : " + e.getMessage());
		} catch (IOException e) {
			Log.e(StaticVariableSet.TAG, "IOException : " + e.getMessage());
		} catch (Exception e) {
			Log.e(StaticVariableSet.TAG, "Exception : " + e.getMessage());
		} finally {
			post.abort();
			client = null;
		}
		return null;
	}
}