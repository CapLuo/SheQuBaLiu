package com.shequ.baliu.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhjchen on 14/11/7.
 */
@MessageTag(value = "RC:GroupInvationMsg", flag = MessageTag.ISPERSISTED |  MessageTag.ISCOUNTED)
public class GroupInvitationNotification extends RongIMClient.MessageContent {

    private String groupId;
    private String message;


    public GroupInvitationNotification(String groupId, String message) {
        this.groupId = groupId;
        this.message = message;
    }

    public GroupInvitationNotification(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            setGroupId(jsonObj.getString("groupId"));
            setMessage(jsonObj.getString("message"));

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

    }


    @Override
    public byte[] encode() {

        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("groupId", groupId);
            jsonObj.put("message", message);


        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
