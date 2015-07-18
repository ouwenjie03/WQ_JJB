package com.jjb.dao;

import org.hibernate.Session;

/**
 * 数据库接入接口的父接口
 * @author Robert Peng
 */
public interface BaseDao {
	
	public void setSession(Session session);
	
	public Session getSession();
	
}
