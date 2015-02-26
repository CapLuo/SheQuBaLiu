package com.shequ.baliu.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shequ.baliu.util.StaticVariableSet;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class CellLocationManager {

	private Context mContext;

	public CellLocationManager(Context context) {
		mContext = context;
	}

	/** 基站信息结构体 */
	public class SCell {
		public int MCC;
		public int MNC;
		public int LAC;
		public int CID;
	}

	/**
	 * 获取基站信息
	 * 
	 * @throws Exception
	 */
	private SCell getCellInfo() throws Exception {
		SCell cell = new SCell();

		/** 调用API获取基站信息 */
		TelephonyManager mTelNet = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
		if (location == null)
			throw new Exception("获取基站信息失败");

		String operator = mTelNet.getNetworkOperator();
		int mcc = Integer.parseInt(operator.substring(0, 3));
		int mnc = Integer.parseInt(operator.substring(3));
		int cid = location.getCid();
		int lac = location.getLac();

		/** 将获得的数据放到结构体中 */
		cell.MCC = mcc;
		cell.MNC = mnc;
		cell.LAC = lac;
		cell.CID = cid;

		return cell;
	}

	public String getLocationCell() {

		SCell cell = null;
		try {
			cell = getCellInfo();
		} catch (Exception e1) {
			Log.e(StaticVariableSet.TAG, "getLocationCell: getCellInfo: error: " + e1.getMessage());
			return null;
		}
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
			holder.put("radio_type", "gsm");
			holder.put("carrier", "HTC");

			JSONObject tower = new JSONObject();
			tower.put("mobile_country_code", cell.MCC);
			// Util.logi("getLocationCell: mobile_country_code = " + cell.MCC );
			tower.put("mobile_network_code", cell.MNC);
			// Util.logi("getLocationCell: mobile_network_code = " + cell.MNC );
			tower.put("cell_id", cell.CID);
			// Util.logi("getLocationCell: cell_id = " + cell.CID );
			tower.put("location_area_code", cell.LAC);
			// Util.logi("getLocationCell: location_area_code = " + cell.LAC );

			JSONArray towerarray = new JSONArray();
			towerarray.put(tower);
			holder.put("cell_towers", towerarray);

			StringEntity query = new StringEntity(holder.toString());
			Log.i(StaticVariableSet.TAG, "getLocationCell: holder: " + holder.toString());
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

			/** 解析返回的JSON数据获得经纬度 */
			JSONObject json = new JSONObject(strBuff.toString());
			JSONObject subjosn = new JSONObject(json.getString("location"));

			String latitude = subjosn.getString("latitude");
			String longitude = subjosn.getString("longitude");

			return NetUtil.getLocation(latitude, longitude);

		} catch (Exception e) {
			Log.e(StaticVariableSet.TAG, "getLocationCell: error: " + e.getMessage());
		} finally {
			post.abort();
			client = null;
		}

		return null;
	}

}
