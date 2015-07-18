package com.jjb.dao;

import com.jjb.bean.Money;

/**
 * Money类的数据库接入接口
 * @author Robert Peng
 */
public interface MoneyDao extends BaseDao {

	public Money queryMoney(int userId);
	
	public boolean setMoney(Money money);
	
}
