package com.shequ.baliu.holder;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.util.SqlHelper;
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
		final SecondHandGoods good = new SecondHandGoods();

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

		String grounpid = json.getString("groupid");
		if (Integer.valueOf(grounpid) == 0) {
			// String groupName = json.getString("groupname");
			good.setGroupname("博泰江滨");
		} else {
			SqlHelper.getRow(StaticVariableSet.SHEQU_GROUP,
					"groupid, groupname", "1", new JsonHttpResponseHandler(
							"UTF-8") {
						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							good.setGroupname("博泰江滨");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONArray errorResponse) {
							good.setGroupname("博泰江滨");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							good.setGroupname("博泰江滨");
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							try {
								for (int i = 0; i < response.length(); i++) {
									JSONObject json = response.getJSONObject(i);
									String name = json.getString("groupname");
									if (name != null && !name.equals("")) {
										good.setGroupname(name);
									}
								}
							} catch (JSONException e) {
								Log.e(StaticVariableSet.TAG, e.getMessage());
							}
						}

					});
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
