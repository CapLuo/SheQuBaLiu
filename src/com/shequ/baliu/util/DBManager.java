package com.shequ.baliu.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shequ.baliu.holder.FriendInfo;
import com.shequ.baliu.holder.MessageInfo;
import com.shequ.baliu.holder.PersonInfo;
import com.shequ.baliu.holder.ShequSortModelHolder;

public class DBManager {

	private static SQLiteDatabase db;
	private Context mContext;

	public String lastAddTime = "0";

	// 单例模式
	public DBManager(Context context) {
		mContext = context;
		this.getInstance();
	}

	public SQLiteDatabase getInstance() {
		if (db != null) {
			return db;
		} else {
			db = new SqlHelper(mContext).getWritableDatabase();
			return db;
		}
	}

	public void deleteAllData(String tablename) {
		db.execSQL("DELETE FROM " + tablename);
	}

	public void addGroups(List<ShequSortModelHolder> groups) {
		db.beginTransaction();
		try {
			for (ShequSortModelHolder sortModel : groups) {
				db.execSQL("REPLACE INTO " + SqlHelper.GROUP_TABLE_NAME
						+ " VALUES(?,?,?)", new Object[] { sortModel.getId(),
						sortModel.getName(), sortModel.getSortLetters() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void deleteOldPerson(ShequSortModelHolder holder) {
		db.delete(SqlHelper.GROUP_TABLE_NAME, "groupid == ?",
				new String[] { String.valueOf(holder.getId()) });
	}

	public int getGroupsCount() {
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "
				+ SqlHelper.GROUP_TABLE_NAME, null);
		c.moveToFirst();
		int count = Integer.parseInt(c.getString(c.getColumnIndex("COUNT(*)")));
		c.close();
		return count;
	}

	public List<ShequSortModelHolder> queryGroups() {
		Cursor c = db.rawQuery("SELECT * FROM " + SqlHelper.GROUP_TABLE_NAME
				+ " ORDER BY " + SqlHelper._GroupPinyin, null);
		return parserCursor(c);
	}

	private List<ShequSortModelHolder> parserCursor(Cursor c) {
		List<ShequSortModelHolder> list = new ArrayList<ShequSortModelHolder>();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ShequSortModelHolder holder = new ShequSortModelHolder();
			holder.setGroupid(c.getString(c.getColumnIndex(SqlHelper._GroupID)));
			holder.setName(c.getString(c.getColumnIndex(SqlHelper._GroupName)));
			holder.setSortLetters(c.getString(c
					.getColumnIndex(SqlHelper._GroupPinyin)));
			list.add(holder);
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public void addMessage(MessageInfo info) {
		db.execSQL(
				"REPLACE INTO " + SqlHelper.MESSAGE_TABLE_NAME
						+ " VALUES(?,?,?,?,?,?,?,?)",
				new Object[] { null, info.getId(), info.getSendid(),
						info.getReceiveid(), info.getSendname(),
						info.getMessage(), info.getType(), info.getTime() });
	}

	public void addMessages(List<MessageInfo> messages) {
		if (messages.size() == 0) {
			return;
		}

		db.beginTransaction();
		try {
			for (MessageInfo info : messages) {
				db.execSQL(
						"REPLACE INTO " + SqlHelper.MESSAGE_TABLE_NAME
								+ " VALUES(?,?,?,?,?,?,?,?)",
						new Object[] { null, info.getId(), info.getSendid(),
								info.getReceiveid(), info.getSendname(),
								info.getMessage(), info.getType(),
								info.getTime() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void deleteOldMessage(String id) {
		db.delete(SqlHelper.MESSAGE_TABLE_NAME, "id == ?", new String[] { id });
	}

	public List<MessageInfo> queryMessage(String type) {
		Cursor c = db.rawQuery("SELECT * FROM " + SqlHelper.MESSAGE_TABLE_NAME
				+ " WHERE " + SqlHelper._MessageType + " = " + type
				+ " ORDER BY " + SqlHelper._MessageNetId, null);
		return parserMessageCursor(c);
	}

	private List<MessageInfo> parserMessageCursor(Cursor c) {
		List<MessageInfo> list = new ArrayList<MessageInfo>();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			MessageInfo info = new MessageInfo();
			info.setID(c.getString(c.getColumnIndex(SqlHelper._MessageNetId)));
			info.setSendid(c.getString(c
					.getColumnIndex(SqlHelper._MessageSendId)));
			info.setReceiveid(c.getString(c
					.getColumnIndex(SqlHelper._MessageReceiveId)));
			info.setSendname(c.getString(c
					.getColumnIndex(SqlHelper._MessageSendName)));
			info.setMessage(c.getString(c
					.getColumnIndex(SqlHelper._MessageContent)));
			info.setTime(lastAddTime = c.getString(c
					.getColumnIndex(SqlHelper._MessageTime)));
			list.add(info);
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public void addFriendInfo(PersonInfo info) {
		db.execSQL(
				"REPLACE INTO " + SqlHelper.FRIEND_TABLE_NAME
						+ " VALUES(?,?,?)",
				new Object[] { info.getUserId(), info.getNickName(),
						info.getPhoto() });
	}

	public void addFriendInfos(List<PersonInfo> infos) {
		if (infos.size() < 1) {
			return;
		}
		db.beginTransaction();
		try {
			for (PersonInfo info : infos) {
				db.execSQL("REPLACE INTO " + SqlHelper.FRIEND_TABLE_NAME
						+ " VALUES(?,?,?)", new Object[] { info.getUserId(),
						info.getNickName(), info.getPhoto() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void deleteOldFriend(String id) {
		db.delete(SqlHelper.FRIEND_TABLE_NAME, "id == ?", new String[] { id });
	}

	public ArrayList<FriendInfo> queryFriend(Context context) {
		if (db == null) {
			db = new SqlHelper(context).getWritableDatabase();
		}
		Cursor c = db.rawQuery("SELECT * FROM " + SqlHelper.FRIEND_TABLE_NAME,
				null);
		return parseFriend(c);
	}

	private ArrayList<FriendInfo> parseFriend(Cursor c) {
		ArrayList<FriendInfo> infos = new ArrayList<FriendInfo>();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			FriendInfo info = new FriendInfo();
			info.setUserid(c.getString(c
					.getColumnIndex(SqlHelper._FriendUserId)));
			info.setName(c.getString(c.getColumnIndex(SqlHelper._FriendName)));
			info.setPortraitUri(c.getString(c
					.getColumnIndex(SqlHelper._FriendPortraitUri)));
			infos.add(info);
			c.moveToNext();
		}
		c.close();
		return infos;
	}

	// 用完必须close
	public void closeDB() {
		db.close();
		db = null;
	}
}
