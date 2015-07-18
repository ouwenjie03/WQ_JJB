package com.jjb.dao.impl;

import com.jjb.bean.Money;
import com.jjb.dao.MoneyDao;

public class MoneyDaoImpl extends BaseDaoImpl implements MoneyDao {

	public Money queryMoney(int userId) {
		return (Money) getSession().load(Money.class, userId);
	}

	public boolean setMoney(Money money) {
		try {
			getSession().saveOrUpdate(money);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
