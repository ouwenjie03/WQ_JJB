package com.jjb.util;

import static com.jjb.util.Constant.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 14/12/12.
 * Modified by Robert Peng on 15/06/14.
 */
public class DBManager {
	private MyDatabaseHelper helper;
	private SQLiteDatabase db;
	
	private static String BY_USERID = "userid=?";
	private static String BY_ITEMID = "itemid=?";

	public DBManager(Context context) {
		helper = new MyDatabaseHelper(context, null); // 传入null使用默认factory初始化
		db = helper.getWritableDatabase();
	}

	/**
	 * 添加Item条目
	 * @param Item 要添加的条目
	 */
	public long addItem(Item item) {
		return db.insert(TABLE_NAME, "null", parseToCV(item));
	}

	/**
	 * 根据ItemId删除条目
	 * @param itemId item id
	 */
	public void deleteItem(int itemId) {
		String[] whereArgs = { Integer.toString(itemId) };
		db.delete(TABLE_NAME, BY_ITEMID, whereArgs);
	}

	/**
	 * 列出用户的item列表
	 * 
	 * @param userid
	 */
	public List<Item> listItems(int userId) {
		String[] whereArgs = new String[] { String.valueOf(userId) };
		Cursor c = db.query(TABLE_NAME, null, BY_USERID, whereArgs, null, null, null);

		return parseMultipleItems(c);
	}

	/**
	 * 查询某时间段内发生的item列表
	 * 
	 * @param userId 用户id
	 * @param fromDateTime 起始时间
	 * @param toDateTime 终止时间
	 */
	public List<Item> listItemsByOccurredTime(int userId, String fromDateTime, String toDateTime) {
		String[] whereArgs = new String[] { String.valueOf(userId), fromDateTime, toDateTime };
		Cursor c = db
				.query(TABLE_NAME, null,
						BY_USERID + " and date(occurredTime)>=date(?) and date(occurredTime)<=date(?)",
						whereArgs, null, null, "occurredTime");

		return parseMultipleItems(c);
	}
	
	/**
	 * 查询某时间段内修改的item列表
	 * 
	 * @param userId 用户id
	 * @param fromDateTime 起始时间
	 * @param toDateTime 终止时间
	 */
	public List<Item> listItemsByModifiedTime(int userId, String fromDateTime, String toDateTime) {
		String[] whereArgs = new String[] { String.valueOf(userId), fromDateTime, toDateTime };
		StringBuilder whereClause = new StringBuilder("userid=" + String.valueOf(userId));
		if (fromDateTime != null) {
			whereClause.append(" and datetime(modifiedTime)>=datetime(\"" + fromDateTime + "\")");
		}
		if (toDateTime != null) {
			whereClause.append(" and datetime(modifiedTime)<=datetime(\"" + toDateTime + "\")");
		}
		Cursor c = db
				.query(TABLE_NAME, null,
						whereClause.toString(), null, null, null, "datetime(modifiedTime)");

		return parseMultipleItems(c);
	}

	/**
	 * 根据itemId查询单条记录，找不到则返回null
	 * @param itemId item id
	 * @return 对应的单条记录，找不到则返回null
	 */
	public Item selectItem(int itemId) {
		String[] whereArgs = new String[] { String.valueOf(itemId) };
		Cursor c = db.query(TABLE_NAME, null, BY_ITEMID, whereArgs, null, null, null);

		return parseSingleItem(c);
	}

	/**
	 * 
	 */
	public void updateItem(Item item) {
		String[] whereArgs = { String.valueOf(item.getItemId()) };
		db.update(TABLE_NAME, parseToCV(item), BY_ITEMID, whereArgs);
	}

	private ContentValues parseToCV(Item item) {
		ContentValues cv = new ContentValues();
		
		//cv.put("itemid", item.getItemId());
		cv.put("userid", item.getUserId());
		cv.put("name", item.getName());
		cv.put("price", item.getPrice());
		cv.put("isout", item.getIsOut());
		cv.put("classify", item.getClassify());
		cv.put("occurredTime", DATETIME_FORMAT.format(item.getOccurredTime()));
		cv.put("modifiedTime", DATETIME_FORMAT.format(new Date()));
		
		return cv;
	}
	
	private List<Item> parseMultipleItems(Cursor c) {
		List<Item> res = new LinkedList<Item>();
		Item tempItem;
		while (!c.isAfterLast()) {
			tempItem = parseSingleItem(c);
			if (tempItem != null) {
				Log.e("item", tempItem.toString());
				res.add(tempItem);
			}
		}
		return res;
	}

	private Item parseSingleItem(Cursor c) {
		Item res = null;
		if (c.moveToNext()) {
			res = new Item();
			try {
				res.setItemId(c.getInt(0));
				res.setUserId(c.getInt(c.getColumnIndex("userid")));
				res.setName(c.getString(c.getColumnIndex("name")));
				res.setPrice(c.getDouble(c.getColumnIndex("price")));
				res.setIsOut(c.getInt(c.getColumnIndex("isout")) == 1);
				res.setClassify(c.getInt(c.getColumnIndex("classify")));
				String tempStr = c.getString(c.getColumnIndex("occurredTime"));
				res.setOccurredTime((Date) Constant.DATETIME_FORMAT.parse(tempStr));
				tempStr = c.getString(c.getColumnIndex("modifiedTime"));
				res.setModifiedTime((Date) Constant.DATETIME_FORMAT.parse(tempStr));
			} catch (ParseException e) {
				Log.e("JJB",
						"Malformatted record in database, abandoning the record...");
				res = null;
			}
		}
		return res;
	}

	/*
	 * test 删除数据表 重新建表 测试的时候可以用
	 */
	public void refreshPlanTable() {
		db.execSQL("drop table plan_table");
		db.execSQL("CREATE TABLE `item_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `userid` VARCHAR(255), `name` NVARCHAR(255), `price` DOUBLE, `isout` BIT, `classify` INT, `time` DATETIME, `isfinish` INT);");
	}
}
