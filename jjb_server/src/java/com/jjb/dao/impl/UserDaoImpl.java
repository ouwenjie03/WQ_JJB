package com.jjb.dao.impl;

import com.jjb.bean.User;
import com.jjb.dao.UserDao;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	public User queryUser(String username) {
		return (User) getSession().createQuery("From User u where u.username='" + username + "'")
							.uniqueResult();
	}

	public boolean insertUser(User user) {
		try {
			int id = (Integer) getSession().save(user);
			user.setUserId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
