package com.jjb.activity;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.jjb.R;
import com.jjb.util.Communicator;
import com.jjb.util.Constant;
import com.jjb.util.DBManager;
import com.jjb.util.Debugger;
import com.jjb.util.Item;
import com.jjb.util.LogUtil;
import com.jjb.widget.SinkingView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 主页Activity，包含一个波浪球和一个百度云语音，以及一个与服务器同步的动作
 * @author Robert Peng
 */
public class IndexActivity extends BaseActivity {
	private SharedPreferences preferences = null;
	private BaiduASRDigitalDialog mDialog = null;
	private DialogRecognitionListener mRecognitionListener;
	
	private String lastSync;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		preferences = getSharedPreferences(Constant.PREF_USER_INFO, MODE_PRIVATE);
		
		
		final Button recordButton = (Button) findViewById(R.id.addRecord);
		final Button textButton = (Button) findViewById(R.id.addTextRecord);
		
		final SinkingView sinkingView = (SinkingView) findViewById(R.id.sinking);
		// TODO 设定小球的百分比
		sinkingView.setPercent(0.56f);
		
		// 百度云语音的回调Listener
		mRecognitionListener = new DialogRecognitionListener() {
			public void onResults(Bundle bundle) {
				Bundle startBundle = new Bundle();
				Intent intent = new Intent(IndexActivity.this, MainActivity.class);
				startBundle.putStringArrayList(
								"resultList",
								bundle != null ? bundle.getStringArrayList(RESULTS_RECOGNITION) : null);
				intent.putExtras(startBundle);
				startActivity(intent);
			}
		};
		
		textButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		recordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 打开百度云语音
				if (mDialog != null) {
					mDialog.dismiss();
				}
				Bundle params = new Bundle();
				
				// API Key
				params.putString(BaiduASRDigitalDialog.PARAM_API_KEY,
						Constant.BAIDU_API_KEY);
				params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY,
						Constant.BAIDU_SECRET_KEY);
				// 设定对话框主题
				params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME,
						Constant.BAIDU_DIALOG_THEME);
				// 设定识别领域
				params.putInt(BaiduASRDigitalDialog.PARAM_PROP,
						Constant.BAIDU_PROP);
				// 设定语言
				params.putString(
						BaiduASRDigitalDialog.PARAM_LANGUAGE,
						Constant.BAIDU_LANGUAGE);
				// 设定提示音播放
				params.putBoolean(
						BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE,
						Constant.BAIDU_START_TONE_ENABLE);
				params.putBoolean(
						BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE,
						Constant.BAIDU_END_TONE_ENABLE);
				params.putBoolean(
						BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE,
						Constant.BAIDU_TIP_TONE_ENABLE);
				// 设定语义识别
				params.putBoolean(
						BaiduASRDigitalDialog.PARAM_NLU_ENABLE,
						Constant.BAIDU_NLU_ENABLE);

				// 生成对话框对象，设置回调函数
				mDialog = new BaiduASRDigitalDialog(IndexActivity.this, params);
				mDialog.setDialogRecognitionListener(mRecognitionListener);
				// 启动对话框
				mDialog.show();
			}
		});
		
		// 与服务器同步数据
		lastSync = preferences.getString(Constant.PREF_LAST_SYNC, null);
		LogUtil.w("Getting last sync date from SharedPreferences: " + lastSync);
		new AsyncTask<Void, Void, Void>() {
			DBManager db = new DBManager(IndexActivity.this);
			
			@Override
			protected Void doInBackground(Void... params) {
				String rawResponse = null;
				JSONObject response = null;
				JSONArray resultArr = new JSONArray();
				try {
					rawResponse = Communicator.sendPost("syncFromServer", "userId="
							+ Constant.USER_ID + "&accessKey=" + Constant.ACCESS_KEY
							+ "&fromDateTime=" + (lastSync == null ? "" : lastSync));
					response = new JSONObject(rawResponse);
					resultArr = response.getJSONArray("items");
				} catch (SocketTimeoutException e) {
					Debugger.DisplayToast(IndexActivity.this, "连接超时！");
				} catch (JSONException e) {
					LogUtil.e("Malformed response from server when trying to sync from Server: " + rawResponse);
				}

				JSONObject currentObject = null;
				Item iteratePointer = null;
				for (int i = 0; resultArr != null && i < resultArr.length(); i++) {
					
					try {
						currentObject = new JSONObject(resultArr.getString(i));
						iteratePointer = new Item();
						iteratePointer.setItemId(currentObject.getInt("itemId"));
						iteratePointer.setUserId(currentObject.getInt("userId"));
						iteratePointer.setName(currentObject.getString("name"));
						iteratePointer.setPrice(currentObject.getDouble("price"));
						iteratePointer.setIsOut(currentObject.getBoolean("isOut"));
						iteratePointer.setClassify(currentObject.getInt("classify"));
						iteratePointer.setOccurredTime(Constant.DATETIME_FORMAT.parse(currentObject.getString("occurredTime")));
						iteratePointer.setModifiedTime(Constant.DATETIME_FORMAT.parse(currentObject.getString("modifiedTime")));
						
						if (lastSync == null  || iteratePointer.getModifiedTime().after(Constant.DATETIME_FORMAT.parse(lastSync)))
							lastSync = Constant.DATETIME_FORMAT.format(iteratePointer.getModifiedTime());
						
						long result = db.addItem(iteratePointer);
						if (result == -1) { // error occurred when inserting, (probably results from duplicate id), try update
							db.updateItem(iteratePointer);
						}
					} catch (JSONException e) {
						LogUtil.e("Malfromed record in server response: index " + i + ", auto skipping.");
						LogUtil.e("Failed to parse the JSON Response from server");
						LogUtil.e("    server response is: " + rawResponse);
						e.printStackTrace();
					} catch (ParseException e) {
						LogUtil.e("Malfromed record in server response: index " + i + ", auto skipping.");
						LogUtil.e("Failed to parse the DateTime from server");
						LogUtil.e("    server response is: " + rawResponse);
						e.printStackTrace();
					} catch (Exception e) {
						LogUtil.e("Failed to sync from server due to unknown error.");
						e.printStackTrace();
					}
				}
				
				// 向服务器上传自lastSync以后更新过的本地数据
				List<Item> itemsInDB =
						db.listItemsByModifiedTime(Constant.USER_ID, lastSync, Constant.DATETIME_FORMAT.format(new Date()));
				resultArr = new JSONArray();
				for (Item item : itemsInDB) {
					try {
						currentObject = new JSONObject();
						currentObject.put("userId", item.getUserId());
						currentObject.put("itemId", item.getItemId());
						currentObject.put("name", item.getName());
						currentObject.put("price", item.getPrice());
						currentObject.put("isOut", item.getIsOut());
						currentObject.put("classify", item.getClassify());
						currentObject.put("occurredTime", Constant.DATETIME_FORMAT.format(item.getOccurredTime()));
						currentObject.put("modifiedTime", Constant.DATETIME_FORMAT.format(item.getModifiedTime()));
						resultArr.put(currentObject);
					} catch (JSONException e) {
						LogUtil.e("Malformed record in SQLite database, auto skiping: " + item.toString());
						e.printStackTrace();
					}
				}
				// 有item需要上传
				if (resultArr.length() > 0) {
					Date now = new Date();
					// 发送到服务器
					rawResponse = null;
					try {
						rawResponse = Communicator.sendPost("syncToServer", "items=" + resultArr.toString() + "&userId=" + Constant.USER_ID + "&accessKey=" + Constant.ACCESS_KEY);
					} catch (SocketTimeoutException e) {
						LogUtil.e("Socket timeouted when try to upload items");
						return null;
					}
					int successCount = 0;
					try {
						response = new JSONObject(rawResponse);
						successCount = response.getInt("syncCount");
					} catch (JSONException e) {
						LogUtil.e("Malformed response from server: " + rawResponse);
					}
					// 所有item同步成功
					if (successCount == resultArr.length()) {
						// 更新lastSync
						Editor editor = preferences.edit();
						editor.putString(Constant.PREF_LAST_SYNC, Constant.DATETIME_FORMAT.format(now));
						editor.commit();
					}
				}
				return null;
			}
			
		}.execute();
		
	}
	
}
