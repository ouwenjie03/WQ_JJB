package com.jjb.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jjb.R;
import com.jjb.util.Constant;
import com.jjb.util.DBManager;
import com.jjb.util.Item;
import com.jjb.widget.MyDouble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class SelectActivity extends Activity {

	DBManager db;

	Button btn;
	DatePicker fromDp, toDp;
	TextView tv;

	private int fromYear;
	private int fromMonth;
	private int fromDay;

	private int toYear;
	private int toMonth;
	private int toDay;

	private void init() {
		Date curDate = new Date(System.currentTimeMillis());
		toYear = fromYear = Integer.parseInt(Constant.YEAR_FORMAT
				.format(curDate));
		toMonth = fromMonth = Integer.parseInt(Constant.MONTH_FORMAT
				.format(curDate));
		toDay = fromDay = Integer.parseInt(Constant.DAY_FORMAT.format(curDate));

		fromDp.init(fromYear, fromMonth - 1, fromDay,
				new OnDateChangedListener() {

					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						fromYear = year;
						fromMonth = monthOfYear + 1;
						fromDay = dayOfMonth;
					}

				});

		toDp.init(toYear, toMonth - 1, toDay, new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				toYear = year;
				toMonth = monthOfYear + 1;
				toDay = dayOfMonth;
			}

		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);

		db = new DBManager(this);

		fromDp = (DatePicker) findViewById(R.id.fromDatePicker);
		toDp = (DatePicker) findViewById(R.id.toDatePicker);
		btn = (Button) findViewById(R.id.button);
		init();

		// db.addItem(new ItemBean("abc", "abc", 1.234, true, 1, "2015-05-26"));

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String fromDate, toDate;
				fromDate = "" + fromYear;
				fromDate += "-"
						+ (fromMonth > 9 ? fromMonth : ("0" + fromMonth));
				fromDate += "-" + (fromDay > 9 ? fromDay : ("0" + fromDay));
				toDate = "" + toYear;
				toDate += "-" + (toMonth > 9 ? toMonth : ("0" + toMonth));
				toDate += "-" + (toDay > 9 ? toDay : ("0" + toDay));
				List<Item> li = db.listItemsByOccurredTime(Constant.USER_ID,
						fromDate, toDate);

				Log.e("from", fromDate);
				Log.e("to", toDate);

				int ss = li.size();
				if (ss == 0) {
					Toast.makeText(getApplicationContext(), "此段期间无消费记录",
							Toast.LENGTH_SHORT).show();
				} else {
					// look here !!!
					// write the intent !!!
					// then note the below !!!
					// String res = new String();
					// res += "------------------------------------\n";
					// res += "" + li.size() + "\n";
					// for (int i=0; i<li.size(); i++) {
					// res += li.get(i).toString()+"\n";
					// }
					// tv.setText(res);
					double clothesIn = 0.0;
					double dietIn = 0.0;
					double shelterIn = 0.0;
					double activityIn = 0.0;
					double clothesOut = 0.0;
					double dietOut = 0.0;
					double shelterOut = 0.0;
					double activityOut = 0.0;

					ArrayList<MyDouble> myDoublesOut = new ArrayList<MyDouble>();
					ArrayList<MyDouble> myDoublesIn = new ArrayList<MyDouble>();
					ArrayList<String> myTimeStrings = new ArrayList<String>();

					double tmpOut = 0.0, tmpIn = 0.0;
					String timeString = Constant.DATE_FORMAT.format(li.get(0)
							.getOccurredTime());
					boolean out = false;

					for (int i = 0; i < li.size(); ++i) {
						if (li.get(i).getClassify() == 0) {
							if (li.get(i).getIsOut()) {
								clothesOut += li.get(i).getPrice();
							} else {
								clothesIn += li.get(i).getPrice();
							}
						} else if (li.get(i).getClassify() == 1) {
							if (li.get(i).getIsOut()) {
								dietOut += li.get(i).getPrice();
							} else {
								dietIn += li.get(i).getPrice();
							}
						} else if (li.get(i).getClassify() == 2) {
							if (li.get(i).getIsOut()) {
								shelterOut += li.get(i).getPrice();
							} else {
								shelterIn += li.get(i).getPrice();
							}
						} else {
							if (li.get(i).getIsOut()) {
								activityOut += li.get(i).getPrice();
							} else {
								activityIn += li.get(i).getPrice();
							}
						}

						if (Constant.DATE_FORMAT.format(
								li.get(i).getOccurredTime()).equals(timeString)) {
							out = li.get(i).getIsOut();
							if (out) {
								tmpOut += li.get(i).getPrice();
							} else {
								tmpIn += li.get(i).getPrice();
							}
						} else {
							MyDouble myDoubleOut = new MyDouble();
							MyDouble myDoubleIn = new MyDouble();
							myDoubleOut.count = tmpOut;
							myDoubleIn.count = tmpIn;

							myDoublesOut.add(myDoubleOut);
							myTimeStrings.add(timeString);
							myDoublesIn.add(myDoubleIn);

							timeString = Constant.DATE_FORMAT.format(li.get(i)
									.getOccurredTime());
							out = li.get(i).getIsOut();
							if (out) {
								tmpOut = li.get(i).getPrice();
								tmpIn = 0.0;
							} else {
								tmpOut = 0.0;
								tmpIn = li.get(i).getPrice();
							}
						}
					}
					MyDouble myDoubleOut = new MyDouble();
					MyDouble myDoubleIn = new MyDouble();
					myDoubleOut.count = tmpOut;
					myDoubleIn.count = tmpIn;

					myDoublesOut.add(myDoubleOut);
					myDoublesIn.add(myDoubleIn);
					myTimeStrings.add(timeString);

					
					ArrayList<MyDouble> allMyDoublesOut = new ArrayList<MyDouble>();
					ArrayList<MyDouble> allMyDoublesIn = new ArrayList<MyDouble>();
					ArrayList<String> allMyTimeStrings = new ArrayList<String>();
					String lastDate = fromDate, reg = "-";
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					for (int i = 0; i < myTimeStrings.size(); i++) {
						String curDate = myTimeStrings.get(i);

						while (lastDate.compareTo(curDate) != 0) {
							allMyTimeStrings.add(lastDate);

							MyDouble myDoubleOut2 = new MyDouble();
							MyDouble myDoubleIn2 = new MyDouble();
							myDoubleOut2.count = 0;
							myDoubleIn2.count = 0;
							allMyDoublesOut.add(myDoubleOut2);
							allMyDoublesIn.add(myDoubleIn2);

							String[] f = lastDate.trim().split(reg);
							calendar.set(Integer.parseInt(f[0]),
									Integer.parseInt(f[1]) - 1,
									Integer.parseInt(f[2]));
							calendar.add(Calendar.DATE, 1);
							lastDate = sdf.format(calendar.getTime());
						}
						allMyTimeStrings.add(lastDate);
						allMyDoublesOut.add(myDoublesOut.get(i));
						allMyDoublesIn.add(myDoublesIn.get(i));

						String[] f = lastDate.trim().split(reg);
						calendar.set(Integer.parseInt(f[0]),
								Integer.parseInt(f[1]) - 1,
								Integer.parseInt(f[2]));
						calendar.add(Calendar.DATE, 1);
						lastDate = sdf.format(calendar.getTime());
					}
					String curDate = toDate;
					if (toDate.compareTo(myTimeStrings.get(myTimeStrings.size() - 1)) != 0) {
						while (lastDate.compareTo(curDate) != 0) {
							allMyTimeStrings.add(lastDate);

							MyDouble myDoubleOut2 = new MyDouble();
							MyDouble myDoubleIn2 = new MyDouble();
							myDoubleOut2.count = 0;
							myDoubleIn2.count = 0;
							allMyDoublesOut.add(myDoubleOut2);
							allMyDoublesIn.add(myDoubleIn2);

							String[] f = lastDate.trim().split(reg);
							calendar.set(Integer.parseInt(f[0]),
									Integer.parseInt(f[1]) - 1,
									Integer.parseInt(f[2]));
							calendar.add(Calendar.DATE, 1);
							lastDate = sdf.format(calendar.getTime());
						}
						allMyTimeStrings.add(lastDate);

						MyDouble myDoubleOut2 = new MyDouble();
						MyDouble myDoubleIn2 = new MyDouble();
						myDoubleOut2.count = 0;
						myDoubleIn2.count = 0;
						allMyDoublesOut.add(myDoubleOut2);
						allMyDoublesIn.add(myDoubleIn2);
					}

					Intent intent = new Intent(SelectActivity.this,
							XYChartActivity.class);
					intent.putExtra("clothesIn", clothesIn);
					intent.putExtra("dietIn", dietIn);
					intent.putExtra("shelterIn", shelterIn);
					intent.putExtra("activityIn", activityIn);
					intent.putExtra("clothesOut", clothesOut);
					intent.putExtra("dietOut", dietOut);
					intent.putExtra("shelterOut", shelterOut);
					intent.putExtra("activityOut", activityOut);
					
					intent.putExtra("allMyDoublesOut", allMyDoublesOut);
					intent.putExtra("allMyDoublesIn", allMyDoublesIn);
					intent.putExtra("allMyTimeStrings", allMyTimeStrings);
					startActivity(intent);
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}