package com.jjb.activity;

import static com.jjb.util.Constant.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.jjb.R;
import com.jjb.util.DBManager;
import com.jjb.util.Item;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private EditText item;
	private EditText price;
	private RadioGroup radio;
	private RadioButton rb0, rb1;
	private Spinner type;
	private DatePicker date;
	private TimePicker time;
	private Button ok, select;
	private List<String> spinnerList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	boolean isOut = true;
	int itemType = 0;
	Date itemDate;

	DBManager db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initial
		item = (EditText) findViewById(R.id.editText1);
		price = (EditText) findViewById(R.id.editText2);
		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		rb0 = (RadioButton) findViewById(R.id.radio0);
		rb1 = (RadioButton) findViewById(R.id.radio1);
		type = (Spinner) findViewById(R.id.spinner1);
		date = (DatePicker) findViewById(R.id.datePicker1);
		time = (TimePicker) findViewById(R.id.timePicker1);
		ok = (Button) findViewById(R.id.ok);
		select = (Button) findViewById(R.id.select);

		// initial db manager
		db = new DBManager(this);

		Bundle extras = getIntent() == null ? null : getIntent().getExtras();
		if (extras != null) {
			ArrayList<String> resultList = getIntent().getExtras()
					.getStringArrayList("resultList");
			StringBuilder sb = new StringBuilder();
			JSONObject jsonObject = null;
			String itemName = null;
			try {
				jsonObject = new JSONObject(resultList.get(0));
				jsonObject = new JSONObject(jsonObject.getString("json_res"));
				itemName = jsonObject.getString("parsed_text");
			} catch (Exception e) {
				e.printStackTrace();
			}
			ArrayList<String> words = new ArrayList<String>();
			if (itemName != null) {
				for (int i = 0;i < itemName.length();i++){
					if (itemName.charAt(i) != ' ') {
						words.add(new String() + itemName.charAt(i));
					}
				}
				//words = itemName.split(" ");
			}
			int size = words.size();
			int index = 0;
			for (int i = 0; i < size; ++i) {
				if (words.get(i).matches("[十九八七六五四三二一两万千百零块元毛角0-9]+")) {
					index= i;
					break;
				}
			}
			String tmp = "";
			for (int i = 0; i < index; ++i)
				tmp += words.get(i);
			item.setText(tmp);
			tmp = "";
			for (int i = index; i < size; ++i) {
				if (words.get(i).matches("[块点元]") && i +1 < size) {
				    tmp += ".";
				    for (int j = i + 1; j < size; ++j) {
				    	if (words.get(j).matches("[一1]")) {
				    		tmp += "1";
				    	} else if (words.get(j).matches("[二两2]")) {
				    		tmp += "2";
				    	} else if (words.get(j).matches("[三3]")) {
				    		tmp += "3";
				    	} else if (words.get(j).matches( "[四4]")) {
				    		tmp += "4";
				    	} else if (words.get(j).matches( "[五5]")) {
				    		tmp += "5";
				    	} else if (words.get(j).matches( "[六6]")) {
				    		tmp += "6";
				    	} else if (words.get(j).matches("[七7]")) {
				    		tmp += "7";
				    	} else if (words.get(j).matches("[八8]")) {
				    		tmp += "8";
				    	} else if (words.get(j).matches("[九9]")) {
				    		tmp += "9";
				    	}
				    }
				    break;
				} else {
					if (words.get(i).matches("[一1]")) {
			    		tmp += "1";
			    	} else if (words.get(i).matches("[二两2]")) {
			    		tmp += "2";
			    	} else if (words.get(i).matches("[三3]")) {
			    		tmp += "3";
			    	} else if (words.get(i).matches( "[四4]")) {
			    		tmp += "4";
			    	} else if (words.get(i).matches( "[五5]")) {
			    		tmp += "5";
			    	} else if (words.get(i).matches( "[六6]")) {
			    		tmp += "6";
			    	} else if (words.get(i).matches("[七7]")) {
			    		tmp += "7";
			    	} else if (words.get(i).matches("[八8]")) {
			    		tmp += "8";
			    	} else if (words.get(i).matches("[九9]")) {
			    		tmp += "9";
			    	} else if (words.get(i).matches("[十]") && i == index) {
			    		tmp += "1";
			    	}
				}
			}
			price.setText(tmp);
		}

		time.setIs24HourView(true);
		resizePikcer(date);// 调整datepicker大小
		resizePikcer(time);// 调整timepicker大小

		spinnerList.add("衣");
		spinnerList.add("食");
		spinnerList.add("住");
		spinnerList.add("行");

		// set Spinner
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(adapter);
		type.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				itemType = arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		type.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});

		// set date picker
		Calendar now = Calendar.getInstance();
		itemDate = now.getTime();
		date.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
				now.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String temp;
						// monthOfYear + 1
						temp = "" + year;
						temp += "-"
								+ (monthOfYear > 9 ? monthOfYear
										: ("0" + (monthOfYear + 1)));
						temp += "-"
								+ (dayOfMonth > 9 ? dayOfMonth
										: ("0" + dayOfMonth));

						try {
							Log.e("date", temp);
							itemDate = DATE_FORMAT.parse(temp);
						} catch (ParseException e) {
							Log.e("JJB",
									"ParseException occurred in DatePicker");
						}
					}
				});

		// set radio group
		radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {
				if (checkId == rb0.getId()) {
					isOut = true;
				}
				if (checkId == rb1.getId()) {
					isOut = false;
				}
			}

		});

		// set ok button
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String itemName = item.getText().toString();
				double itemPrice = Double.parseDouble(price.getText()
						.toString());
				db.addItem(new Item(USER_ID, itemName, itemPrice, isOut,
						itemType, itemDate));
				Toast.makeText(getApplicationContext(), "新建成功",
					     Toast.LENGTH_SHORT).show();
			}

		});
		
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SelectActivity.class);
				startActivity(intent);
			}
			
		});

	}

	/**
	 * 调整FrameLayout大小
	 * 
	 * @param tp
	 */
	private void resizePikcer(FrameLayout tp) {
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	/**
	 * 得到viewGroup里面的numberpicker组件
	 * 
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	/*
	 * 调整numberpicker大小
	 */
	private void resizeNumberPicker(NumberPicker np) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 0, 0);
		//np.setLayoutParams(params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return true;
	}
}
