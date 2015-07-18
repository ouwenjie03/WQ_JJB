package com.jjb.dao.impl;

import com.jjb.bean.AccessKey;
import com.jjb.dao.AccessKeyDao;

public class AccessKeyDaoImpl extends BaseDaoImpl implements AccessKeyDao {

	public AccessKey queryAccessKey(int userId) {
		return (AccessKey) getSession().get(AccessKey.class, userId);
	}

	public boolean setAccessKey(AccessKey accessKey) {
		try {
			getSession().saveOrUpdate(accessKey);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
