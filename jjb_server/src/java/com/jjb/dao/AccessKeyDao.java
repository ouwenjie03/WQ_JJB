package com.jjb.dao;

import com.jjb.bean.AccessKey;

/**
 * AccessKey的数据库访问接口
 * @author Robert Peng
 */
public interface AccessKeyDao extends BaseDao {
	
	/**
	 * 查询用户的access key
	 * @param userId 用户id
	 * @return access key
	 */
	public AccessKey queryAccessKey(int userId);
	
	/**
	 * 设置用户的access key
	 * @param accessKey access key
	 * @return 设置成功则返回true，否则返回false
	 */
	public boolean setAccessKey(AccessKey accessKey);
	
}
