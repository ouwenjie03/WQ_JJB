package com.jjb.util;

import android.content.Context;
import android.widget.Toast;

public class Debugger {

	private static long startTime;
	private static boolean hasStartedTimer = false;

	public static void DisplayToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void startTimer(Context context) {
		startTime = System.currentTimeMillis();
		hasStartedTimer = true;
	}

	public static void stopTimer(Context context) {
		if (hasStartedTimer) {
			long endTime = System.currentTimeMillis();
			DisplayToast(context,
					"Time used: " + String.valueOf(endTime - startTime) + "ms");
			hasStartedTimer = false;
		} else {
			DisplayToast(context, "The timer didn't start yet.");
		}
	}
}
