package com.shequ.baliu.holder;

import org.json.JSONException;
import org.json.JSONObject;

import com.shequ.baliu.util.StaticVariableSet;

import android.graphics.drawable.Drawable;

public class PersonInfo {

	private String mUserId;
	private String mUsername;
	private String mNickName;
	private String mRealname;
	private String mEmail;
	private String mBirthday;
	private String mSex;
	private String mGroupId;
	private String mGroupName;
	private String mPhone;
	private String mCity;
	private String mSigned;
	private String mAbout;
	private String mAddress;
	private String mISMerchant;
	private String mPhoto;

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		this.mUsername = username;
	}

	public String getNickName() {
		if (mNickName != null && !mNickName.equals("")) {
			return mNickName;
		} else if (mRealname != null && !mRealname.equals("")) {
			return mRealname;
		} else {
			return mUsername;
		}
	}

	public void setNickName(String name) {
		this.mNickName = name;
	}

	public String getRealname() {
		return mRealname;
	}

	public void setRealname(String realname) {
		this.mRealname = realname;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}

	public String getBirthday() {
		return mBirthday;
	}

	public void setBirthday(String birthday) {
		this.mBirthday = birthday;
	}

	public String getGroupId() {
		return mGroupId;
	}

	public void setGroupId(String groupId) {
		this.mGroupId = groupId;
	}

	public String getGroupName() {
		return mGroupName;
	}

	public void setGroupName(String groupName) {
		this.mGroupName = groupName;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		this.mPhone = phone;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public String getSex() {
		return mSex;
	}

	public void setSex(String sex) {
		this.mSex = sex;
	}

	public String getSigned() {
		return mSigned;
	}

	public void setSigned(String signed) {
		this.mSigned = signed;
	}

	public String getAbout() {
		return mAbout;
	}

	public void setAbout(String about) {
		this.mAbout = about;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		this.mAddress = address;
	}

	public String getMerchant() {
		return mISMerchant;
	}

	public void setMerchant(String isMerchant) {
		this.mISMerchant = isMerchant;
	}

	public void setPhoto(String photo) {
		this.mPhoto = photo;
	}

	public String getPhoto() {
		return mPhoto;
	}

	public static PersonInfo parseJson(JSONObject json) throws JSONException {
		PersonInfo info = new PersonInfo();
		String userid = json.getString("userid");
		if (userid == null) {
			return null;
		} else {
			info.setUserId(userid);
		}
		String username = json.getString("username");
		if (username == null) {
			return null;
		} else {
			info.setUsername(username);
		}
		String nickname = json.getString("nickname");
		info.setNickName(nickname == null ? "" : nickname);
		String realname = json.getString("realname");
		info.setRealname(realname == null ? "" : realname);
		String email = json.getString("email");
		info.setEmail(email == null ? "" : email);
		String groupid = json.getString("groupid");
		info.setGroupId(groupid == null ? "0" : groupid);
		String groupname = json.getString("groupname");
		info.setGroupName(groupname);
		String isMerchant = json.getString("ismerchant");
		info.setMerchant(isMerchant == null ? "0" : isMerchant);
		String path = json.getString("path");
		String face = json.getString("face");
		info.setPhoto(StaticVariableSet.IMG_URL + "/Upload/face/" + path + face);
		return info;
	}
}
