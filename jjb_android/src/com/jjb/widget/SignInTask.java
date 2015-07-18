package com.jjb.widget;

import static com.jjb.util.Constant.*;

import java.net.SocketTimeoutException;
import java.security.MessageDigest;

import org.json.JSONException;
import org.json.JSONObject;

import com.jjb.activity.IndexActivity;
import com.jjb.util.Communicator;
import com.jjb.util.Constant;
import com.jjb.util.Debugger;
import com.jjb.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class SignInTask extends AsyncTask<String, Void, String> {
	protected Context context;
	protected String targetMethod = "signIn";
	
	protected String accessKey;
	protected String expiresTime;
	protected int userId;
	
	private boolean isSuccess = false;
	private String message;
	
	public SignInTask(Context context) {
		this.context = context;
	}
	
	protected String encoderByMd5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		 
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		 
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		 
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	
	@Override
	protected String doInBackground(String... params) {
		String userID = null;
		String result = null;
		if (params.length < 2)
			return userID;
		try {
			result =
					Communicator.sendPost(targetMethod, "username=" + params[0] + "&password=" + encoderByMd5(params[1]));
			JSONObject response = new JSONObject(result);
			accessKey = response.getString("accessKey");
			expiresTime = response.getString("expiresTime");
			userId = response.getInt("userId");
			
			isSuccess = true;
		} catch (SocketTimeoutException e) {
			message = "连接超时！";
		} catch (JSONException e) {
			message = "啊哦，不知道怎么了~";
			LogUtil.e("Malformed response from server: " + result);
		}
		return userID;
	}
	
	protected void onPostExecute(String userID) {
		if (isSuccess) {
			SharedPreferences settings = context.getSharedPreferences(PREF_USER_INFO, Activity.MODE_PRIVATE);
			Editor editor = settings.edit();
			editor.putString(PREF_ACCESS_KEY, accessKey);
			editor.putString(PREF_EXPIRES_TIME, expiresTime);
			editor.putInt(PREF_USERID, userId);
			editor.commit();
			
			Constant.USER_ID = userId;
			Constant.ACCESS_KEY = accessKey;
			
			Intent intent = new Intent(context,IndexActivity.class);
			context.startActivity(intent);
		} else {
			Debugger.DisplayToast(context, message);
		}
	}

}
