package com.shequ.baliu.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.shequ.baliu.net.HttpUtil;

public class SqlHelper extends SQLiteOpenHelper {

	/**
	 * 网络查询数据库
	 */
	public static void get(String tableName, String where,
			JsonHttpResponseHandler jsonHttpHandler) {
		RequestParams params = new RequestParams();
		String paramsValue;
		try {
			paramsValue = new String(Base64.encode(("SELECT * FROM `"
					+ tableName + "` WHERE " + where).getBytes("utf-8"), 0));
			params.put("r", paramsValue);
			HttpUtil.get(StaticVariableSet.DATA_URL, params, jsonHttpHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void get(String colum, String from, String where,
			JsonHttpResponseHandler handler) {
		String paramsValue = new String(Base64.encode(("SELECT " + colum
				+ " FROM " + from + " WHERE " + where).getBytes(), 0));
		get(paramsValue, handler);
	}

	public static void getHomeAdvertImages(JsonHttpResponseHandler handler) {
		String paramsValue = new String(
				Base64.encode(
						("SELECT `" + StaticVariableSet.ADVERT_INFO
								+ "`.* FROM `" + StaticVariableSet.ADVERT_INFO
								+ "` JOIN `" + StaticVariableSet.ADVERT_MAIN
								+ "` ON `" + StaticVariableSet.ADVERT_INFO
								+ "`.`ads_id` = `"
								+ StaticVariableSet.ADVERT_MAIN
								+ "`.`id` WHERE `"
								+ StaticVariableSet.ADVERT_MAIN + "`.`name` = 'index_advert_slide'")
								.getBytes(), 0));
		get(paramsValue, handler);
	}

	public static void getInfo(String where, JsonHttpResponseHandler handler) {
		String paramsValue = new String(Base64.encode(("SELECT * FROM `"
				+ StaticVariableSet.USER_MAIN + "` WHERE " + where).getBytes(),
				0));
		get(paramsValue, handler);
	}

	public static void getAllInfo(String where, JsonHttpResponseHandler handler) {
		String paramsValue = new String(Base64.encode(
				("SELECT `" + StaticVariableSet.USER_INFO + "`.`userid`, `"
						+ StaticVariableSet.USER_INFO + "`.`username`, `"
						+ StaticVariableSet.USER_INFO + "`.`nickname`, `"
						+ StaticVariableSet.USER_INFO + "`.`realname`, `"
						+ StaticVariableSet.USER_INFO + "`.`email`, `"
						+ StaticVariableSet.SHEQU_GROUP_RELATION
						+ "`.`groupid`, `" + StaticVariableSet.SHEQU_GROUP
						+ "`.`groupname`, `" + StaticVariableSet.USER_INFO
						+ "`.`ismerchant`, `" + StaticVariableSet.USER_INFO
						+ "`.`path`, `" + StaticVariableSet.USER_INFO
						+ "`.`face`, `" + StaticVariableSet.USER_MAIN
						+ "`.`salt` FROM `" + StaticVariableSet.USER_MAIN
						+ "` JOIN `" + StaticVariableSet.USER_INFO + "` ON `"
						+ StaticVariableSet.USER_MAIN + "`.`userid` = `"
						+ StaticVariableSet.USER_INFO + "`.`userid` JOIN `"
						+ StaticVariableSet.SHEQU_GROUP_RELATION + "` ON `"
						+ StaticVariableSet.USER_INFO + "`.`userid` = `"
						+ StaticVariableSet.SHEQU_GROUP_RELATION
						+ "`.`userid` JOIN `" + StaticVariableSet.SHEQU_GROUP
						+ "` ON `" + StaticVariableSet.SHEQU_GROUP_RELATION
						+ "`.`groupid` = `" + StaticVariableSet.SHEQU_GROUP
						+ "`.`groupid` WHERE " + where).getBytes(), 0));
		get(paramsValue, handler);
	}

	public static void getFleaDeal(String limit, JsonHttpResponseHandler handler) {
		String paramsValue = new String(Base64.encode(
				("SELECT `" + StaticVariableSet.SECOND_MARKET + "`.`userid`, `"
						+ StaticVariableSet.SECOND_MARKET + "`.`title`, `"
						+ StaticVariableSet.SECOND_MARKET + "`.`price`, `"
						+ StaticVariableSet.SECOND_MARKET + "`.`content`, `"
						+ StaticVariableSet.SECOND_MARKET + "`.`photo`, `"
						+ StaticVariableSet.SECOND_MARKET + "`.`updatetime`, `"
						+ StaticVariableSet.USER_INFO + "`.`username`, `"
						+ StaticVariableSet.USER_INFO + "`.`path`, `"
						+ StaticVariableSet.USER_INFO + "`.`face`, `"
						+ StaticVariableSet.USER_INFO + "`.`nickname`, `"
						+ StaticVariableSet.SHEQU_GROUP
						+ "`.`groupname` FROM `"
						+ StaticVariableSet.SECOND_MARKET + "` JOIN `"
						+ StaticVariableSet.USER_INFO + "` ON `"
						+ StaticVariableSet.SECOND_MARKET + "`.`userid` = `"
						+ StaticVariableSet.USER_INFO + "`.`userid` JOIN  `"
						+ StaticVariableSet.SHEQU_GROUP + "` ON `"
						+ StaticVariableSet.USER_INFO + "`.`groupid` = `"
						+ StaticVariableSet.SHEQU_GROUP
						+ "`.`groupid` WHERE 1 ORDER BY `"
						+ StaticVariableSet.SECOND_MARKET
						+ "`.`updatetime` LIMIT " + limit).getBytes(), 0));
		get(paramsValue, handler);
	}

	/**
	 * 联合查询
	 */
	public static void getMessage(String tableName, String uninoName,
			String unino, String where, JsonHttpResponseHandler handler) {
		String paramsValue = new String(Base64.encode(("SELECT `" + tableName
				+ "`.*, nickname FROM `" + tableName + "` JOIN `" + uninoName
				+ "` ON `" + tableName + "`." + unino + " = `" + uninoName
				+ "`." + unino + " WHERE " + where).getBytes(), 0));
		get(paramsValue, handler);
	}

	public static void getRow(String tableName, String row, String where,
			JsonHttpResponseHandler jsonHttpHandler) {
		String paramsValue = new String(Base64.encode(("SELECT " + row
				+ " FROM `" + tableName + "` WHERE " + where).getBytes(), 0));
		get(paramsValue, jsonHttpHandler);
	}

	private static void get(String paramsValue, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("r", paramsValue);
		HttpUtil.get(StaticVariableSet.DATA_URL, params, handler);
	}

	// 用户注册数据插入
	public static void add(String tableName, String values) {
		RequestParams params = new RequestParams();
		String paramsValue = new String(Base64.encode(("INSERT INTO "
				+ tableName + values).getBytes(), 0));
		params.put("r", paramsValue);
		HttpUtil.post(StaticVariableSet.DATA_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.i(StaticVariableSet.TAG,
								"---------> Http request login success.");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e(StaticVariableSet.TAG,
								"---------> Http request login failure.");
					}
				});
	}

	public static void add(String tableName, String values,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		String paramsValue = new String(Base64.encode(("INSERT INTO "
				+ tableName + values).getBytes(), 0));
		params.put("r", paramsValue);
		HttpUtil.post(StaticVariableSet.DATA_URL, params, responseHandler);
	}

	public static void update(String tableName, String values, String where) {
		RequestParams params = new RequestParams();
		String paramsValue = new String(Base64.encode(("UPDATE `" + tableName
				+ "` SET " + values + " WHERE " + where).getBytes(), 0));
		params.put("r", paramsValue);
		HttpUtil.post(StaticVariableSet.DATA_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.i(StaticVariableSet.TAG,
								"---------> Http request login success.");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e(StaticVariableSet.TAG,
								"---------> Http request login failure.");

					}
				});
	}

	public static void getImToken(Context context, JSONObject object,
			ResponseHandlerInterface responseHandler) throws JSONException,
			UnsupportedEncodingException {

		String timestamp = String.valueOf(new Date().getTime());
		String nonce = ShequTools.getRandomString(5);
		String echostr = ShequTools.getRandomString(4);
		String signature = SignUtil.getSignature(timestamp, nonce);

		object.put("signature", signature);
		object.put("timestamp", timestamp);
		object.put("nonce", nonce);
		object.put("echostr", echostr);

		HttpUtil.post(context, StaticVariableSet.IM_DATA_URL, object,
				"application/json", responseHandler);
	}

	public static void uploadPic(Context context, byte[] bytes, String ext,
			ResponseHandlerInterface responseHandler) throws JSONException,
			UnsupportedEncodingException {

		JSONObject object = new JSONObject();
		object.put("file_data", new String(Base64.encode(bytes, 0)));
		object.put("file_ext", ext);

		HttpUtil.post(context, StaticVariableSet.IMG_UPLOAD, object,
				"application/json", responseHandler);
	}

	/*
	 * 本地数据库查询
	 */
	// 数据库版本号
	private static final int DATABASE_VERSION = 2;

	// 数据库名
	private static final String DATABASE_NAME = "BaliuDB.db";

	public static final String PACKAGE_NAME = "com.shequ.baliu";

	// 数据表名，一个数据库中可以有多个表
	public static final String GROUP_TABLE_NAME = "Club_Group";

	public static final String _GroupID = "groupid";
	public static final String _GroupName = "groupname";
	public static final String _GroupPinyin = "grouppinyin";

	public static final String MESSAGE_TABLE_NAME = "Club_Message";

	public static final String _MessageID = "id";
	public static final String _MessageNetId = "messageid";
	public static final String _MessageSendId = "sendid";
	public static final String _MessageReceiveId = "receiveid";
	public static final String _MessageSendName = "sendname";
	public static final String _MessageContent = "content";
	public static final String _MessageType = "type";
	public static final String _MessageTime = "time";

	public static final String FRIEND_TABLE_NAME = "Club_friend";

	public static final String _FriendUserId = "userId";
	public static final String _FriendName = "name";
	public static final String _FriendPortraitUri = "portraitUri";

	/*
	 * SQLiteOpenHelper的构造函数参数： // context：上下文环境 // name：数据库名字 //
	 * factory：游标工厂（可选） // version：数据库模型版本号 //
	 * 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时 //
	 * CursorFactory设置为null,使用系统默认的工厂类
	 */
	public SqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(StaticVariableSet.TAG, "DatabaseHelper Constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 调用时间：数据库第一次创建时onCreate()方法会被调用

		// onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
		// 这个方法中主要完成创建数据库后对数据库的操作

		Log.d(StaticVariableSet.TAG, "DatabaseHelper onCreate");

		// 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("CREATE TABLE [" + GROUP_TABLE_NAME + "] (");
		sBuffer.append("[" + _GroupID + "] INTEGER NOT NULL PRIMARY KEY, ");
		sBuffer.append("[" + _GroupName + "] TEXT,");
		sBuffer.append("[" + _GroupPinyin + "] TEXT)");

		// 执行创建表的SQL语句
		db.execSQL(sBuffer.toString());

		// 创建第二张表
		StringBuffer buffer = new StringBuffer();

		buffer.append("CREATE TABLE [" + MESSAGE_TABLE_NAME + "] (");
		buffer.append("[" + _MessageID + "] INTEGER NOT NULL PRIMARY KEY, ");
		buffer.append("[" + _MessageNetId + "] TEXT,");
		buffer.append("[" + _MessageSendId + "] TEXT,");
		buffer.append("[" + _MessageReceiveId + "] TEXT,");
		buffer.append("[" + _MessageSendName + "] TEXT,");
		buffer.append("[" + _MessageContent + "] TEXT,");
		buffer.append("[" + _MessageType + "] TEXT,");
		buffer.append("[" + _MessageTime + "] TEXT)");

		db.execSQL(buffer.toString());
		// 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

		StringBuffer friend_buffer = new StringBuffer();

		friend_buffer.append("CREATE TABLE [" + FRIEND_TABLE_NAME + "] (");
		friend_buffer.append("[" + _FriendUserId
				+ "] TEXT NOT NULL PRIMARY KEY,");
		friend_buffer.append("[" + _FriendName + "] TEXT,");
		friend_buffer.append("[" + _FriendPortraitUri + "] TEXT)");

		db.execSQL(friend_buffer.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

		// onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
		// 这样就可以把一个数据库从旧的模型转变到新的模型
		// 这个方法中主要完成更改数据库版本的操作

		Log.d(StaticVariableSet.TAG, "DatabaseHelper onUpgrade");

		db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE_NAME);
		onCreate(db);
		// 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
		// 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

}
