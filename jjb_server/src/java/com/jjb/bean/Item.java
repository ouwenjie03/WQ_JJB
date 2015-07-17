package com.jjb.bean;

import java.util.Date;

/**
 * 数据库中表Item的bean类
 * @author Robert Peng
 */
public class Item {
	
	private String itemId;
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
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
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
	
	public void setItemAll(Item other) {
		this.classify = other.classify;
		this.isOut = other.isOut;
		this.itemId = other.itemId;
		this.modifiedTime = other.modifiedTime;
		this.name = other.name;
		this.occurredTime = other.occurredTime;
		this.price = other.price;
		this.userId = other.userId;
	}
}
