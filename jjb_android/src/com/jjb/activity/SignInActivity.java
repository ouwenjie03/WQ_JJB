package com.jjb.activity;

import com.jjb.R;
import com.jjb.util.Debugger;
import com.jjb.widget.SignInTask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SignInActivity extends BaseActivity {
	private TextView userNameView;
	private TextView passwordView;

	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		userNameView = (TextView) findViewById(R.id.username);
		passwordView = (TextView) findViewById(R.id.password);

		findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = userNameView.getText().toString();
				password = passwordView.getText().toString();

				Log.e("Signin userName", username);
				Log.e("Signin userPassword", password);

				if (username.equals("") || password.equals("")) {
					Debugger.DisplayToast(SignInActivity.this, "上述信息不能为空");
					return;
				}

				new SignInTask(SignInActivity.this).execute(username, password);
			}
		});

		findViewById(R.id.signup).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
	}
}
