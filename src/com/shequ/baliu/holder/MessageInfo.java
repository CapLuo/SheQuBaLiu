package com.shequ.baliu.holder;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.text.TextUtils;

public class MessageInfo {

	private String id;
	private String message;
	private String sendid;
	private String receiveid;
	private String sendname;
	private String type;
	private String time;

	public String getId() {
		return id;
	}

	public void setID(String _id) {
		this.id = _id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		String contentStr = Html.fromHtml(message).toString();
		this.message = contentStr;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getReceiveid() {
		return receiveid;
	}

	public void setReceiveid(String receiveid) {
		this.receiveid = receiveid;
	}

	public String getSendname() {
		if (TextUtils.isEmpty(sendname)) {
			return "";
		}
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public static MessageInfo parserJson(JSONObject json, boolean isConversation)
			throws JSONException {
		MessageInfo info = new MessageInfo();
		info.setID(json.getString("messageid"));
		info.setSendid(json.getString("userid"));
		info.setReceiveid(json.getString("touserid"));
		info.setMessage(json.getString("content"));
		info.setTime(json.getString("addtime"));
		info.setType(json.getString("type"));
		if (!isConversation) {
			info.setSendname(json.getString("nickname"));
		}
		return info;
	}
}