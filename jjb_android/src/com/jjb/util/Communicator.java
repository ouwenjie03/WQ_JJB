package com.jjb.util;

import static com.jjb.util.Constant.HOST_NAME;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import android.util.Log;

public class Communicator {
	
	public static String sendGet(String method) throws SocketTimeoutException {
		String result = null;

		URL url = null;
		HttpURLConnection connection = null;
		InputStream in = null;
		try {
			url = new URL("http://" + HOST_NAME + method);
			Log.i("get url", "http://" + HOST_NAME + method);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setConnectTimeout(6000);
			connection.setDoOutput(true);
			in = connection.getInputStream();
			InputStreamReader r = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(r);
			String line = null;
			while ((line = br.readLine()) != null) {
				result = line + "\n";
			}
			if (result == null)
				Log.e("404", "No Response From Server!");
			r.close();
			connection.disconnect();
			return result;

		} catch (SocketTimeoutException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("get result", result);
		return result;

	}

	public static String sendPost(String method, String content) throws SocketTimeoutException {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL("http://" + HOST_NAME + method);
			Log.i("post url", "http://" + HOST_NAME + method + "?" + content);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Charset", "utf-8");
			OutputStream os = connection.getOutputStream();
			DataOutputStream dop = new DataOutputStream(os);
			byte[] buffer = content.getBytes();
			dop.write(buffer);
			dop.flush();
			dop.close();
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.i("post result", result);
		return result;
	}
}
