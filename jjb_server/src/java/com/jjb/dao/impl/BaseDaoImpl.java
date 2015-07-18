package com.jjb.dao.impl;

import org.hibernate.Session;

import com.jjb.dao.BaseDao;

public abstract class BaseDaoImpl implements BaseDao {

	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}
	
}
