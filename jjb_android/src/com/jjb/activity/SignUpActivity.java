package com.jjb.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jjb.R;
import com.jjb.util.Debugger;
import com.jjb.widget.SignUpTask;

public class SignUpActivity extends BaseActivity {
	private TextView userNameView;
	private TextView passwordView;
	private TextView passwordConfirmView;
	
	private String userName;
	private String password;
	private String passwordConfirm;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		userNameView = (TextView) findViewById(R.id.username);
		passwordView = (TextView) findViewById(R.id.password);
		passwordConfirmView = (TextView) findViewById(R.id.passwordConfirm);
		
		findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userName = userNameView.getText().toString();
				password = passwordView.getText().toString();
				passwordConfirm = passwordConfirmView.getText().toString();
				
				Log.e("Signin userName", userName);
				Log.e("Signin userPassword", password);
			
				if (userName.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
					Debugger.DisplayToast(SignUpActivity.this, "上述信息不能为空");
					return;
				}
				
				if (!password.equals(passwordConfirm)) {
					Debugger.DisplayToast(SignUpActivity.this, "两次输入的密码不相同");
					return;
				}
				
				new SignUpTask(SignUpActivity.this).execute(userName, password);
			}
		});
	}
}
