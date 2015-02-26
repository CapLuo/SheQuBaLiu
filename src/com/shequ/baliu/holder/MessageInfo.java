package com.shequ.baliu.holder;

public class MessageInfo {

	private String id;
	private String message;
	private String sendid;
	private String sendname;
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
		this.message = message;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}