package com.shequ.baliu.holder;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shequ.baliu.util.StaticVariableSet;

/*
 * 存储的时间是 php的时间格式 10为long 转的string java 为13位 取出去要转为long *1000
 */
public class SecondHandGoods {

	private String userid;
	private String title;
	private String price;
	private String content;
	private String photo;
	private String updatetime;
	private String nickname;
	private String headphoto;
	private String groupname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadphoto() {
		return headphoto;
	}

	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getContent() {
		
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUpdateTime() {
		return updatetime;
	}

	public void setUpdateTime(String updatetime) {
		this.updatetime = updatetime;
	}

	public static SecondHandGoods parseJson(JSONObject json)
			throws JSONException {
		SecondHandGoods good = new SecondHandGoods();

		String userid = json.getString("userid");
		if (userid == null || userid.equals("")) {
			return null;
		} else {
			good.setUserid(userid);
		}

		String photo = json.getString("photo");
		if (photo == null || photo.equals("")) {
			return null;
		} else {
			if (!photo.startsWith("http")) {
				photo = StaticVariableSet.IMG_URL + photo;
			}
			good.setPhoto(photo);
		}

		String title = json.getString("title");
		if (title == null || title.equals("")) {
			return null;
		} else {
			good.setTitle(title);
		}

		String price = json.getString("price");
		if (price == null || price.equals("")) {
			return null;
		} else {
			good.setPrice(price);
		}

		String content = json.getString("content");
		if (content == null || content.equals("")) {
			return null;
		} else {
			good.setContent(content);
		}

		String updatetime = json.getString("updatetime");
		good.setUpdateTime(updatetime);

		String nickname = json.getString("nickname");
		if (TextUtils.isEmpty(nickname)) {
			nickname = json.getString("username");
		}
		good.setNickname(nickname);

		String groupName = json.getString("groupname");
		good.setGroupname(groupName);

		String path = json.getString("path");
		String face = json.getString("face");
		if (TextUtils.isEmpty(path + face)) {
			good.setHeadphoto("");
		} else {
			good.setHeadphoto(StaticVariableSet.IMG_URL + "/Upload/face/"
					+ path + face);
		}

		return good;
	}
}
