package com.jjb.widget;

import android.content.Context;

public class SignUpTask extends SignInTask {
	public SignUpTask(Context context) {
		super(context);
		targetMethod = "signUp";
	}
}
