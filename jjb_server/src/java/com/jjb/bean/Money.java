package com.jjb.bean;

/**
 * 数据库中表Money的bean类
 * @author Robert Peng
 */
public class Money {
	private int userId;
	private double totalMoney;
	
	public Money() {}
	
	public Money(int userId, double totalMoney) {
		this.userId = userId;
		this.totalMoney = totalMoney;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
}
