package com.jjb.util;

import android.util.Log;

public class LogUtil {
	
	private static String getDefaultTag(StackTraceElement stackTraceElement) {
		String fileName = stackTraceElement.getFileName();
		String stringArray[] = fileName.split("\\.");
		String tag = stringArray[0];
		return tag;
	}
	
	public static void i(String content) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (content == null)
			content = "null";
		Log.i(getDefaultTag(stackTraceElement), content);
	}
	
	public static void w(String content) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (content == null)
			content = "null";
		Log.w(getDefaultTag(stackTraceElement), content);
	}
	
	public static void e(String content) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (content == null)
			content = "null";
		Log.e(getDefaultTag(stackTraceElement), content);
	}

}
