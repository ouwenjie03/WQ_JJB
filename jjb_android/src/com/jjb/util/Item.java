package com.jjb.util;

import java.util.Date;

/**
 * 数据库中表Item的bean类
 * @author Robert Peng
 */
public class Item {
	
	private int itemId;
	private int userId;
	private String name;
	private double price;
	private boolean isOut;
	private int classify;
	private Date occurredTime;
	private Date modifiedTime;
	
	/**
	 * 返回该Item的JSON表示
	 * @return 该Item的JSON表示
	 */
	public String toString() {
		return new StringBuilder("{\"userId\":\"").append(userId)
				.append("\", \"itemId\":\"").append(itemId)
				.append("\", \"name\": \"").append(name)
				.append("\", \"price\": \"").append(price)
				.append("\", \"isOut\": \"").append(isOut)
				.append("\", \"classify\": \"").append(classify)
				.append("\", \"occurredTime\": \"").append(occurredTime)
				.append("\", \"modifiedTime\": \"").append(modifiedTime)
				.append("\"}").toString();
	}
	
	public Item() {}
	public Item(int userId, String name, double price, boolean isOut, int classify, Date occurredTime) {
		this.userId = userId;
		this.name = name;
		this.price = price;
		this.isOut = isOut;
		this.classify = classify;
		this.occurredTime = occurredTime;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean getIsOut() {
		return isOut;
	}
	public void setIsOut(boolean isOut) {
		this.isOut = isOut;
	}
	public int getClassify() {
		return classify;
	}
	public void setClassify(int classify) {
		this.classify = classify;
	}

	public Date getOccurredTime() {
		return occurredTime;
	}

	public void setOccurredTime(Date occurredTime) {
		this.occurredTime = occurredTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	
}
