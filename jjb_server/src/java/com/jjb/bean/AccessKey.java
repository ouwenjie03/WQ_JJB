package com.jjb.bean;

import java.util.Date;

/**
 * 数据库中表AccessKey的bean类
 * @author Robert Peng
 */
public class AccessKey {
	private int userId;
	private String accessKey;
	private Date expiresTime;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public Date getExpiresTime() {
		return expiresTime;
	}
	public void setExpiresTime(Date expiresTime) {
		this.expiresTime = expiresTime;
	}
}
