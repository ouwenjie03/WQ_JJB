package com.jjb.dao;

import com.jjb.bean.User;

/**
 * User类的数据库接入接口
 * @author Robert Peng
 */
public interface UserDao extends BaseDao {

	public User queryUser(String username);
	
	public boolean insertUser(User user);
	
}
