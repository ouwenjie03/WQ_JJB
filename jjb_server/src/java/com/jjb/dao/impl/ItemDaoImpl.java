package com.jjb.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.jjb.bean.Item;
import com.jjb.dao.ItemDao;

public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {

	public boolean insertItem(Item item) {
		try {
			Item existItem = (Item)getSession().createQuery("From Item i where i.itemId='" + item.getItemId() + "'")
					.uniqueResult();
			if (existItem == null) {
				getSession().save(item);
			} else {
				existItem.setItemAll(item);
				getSession().update(existItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int insertItems(List<Item> items) {
		int successCounter = 0;
		for (Item item : items) {
			try {
				Item existItem = (Item)getSession().createQuery("From Item i where i.itemId='" + item.getItemId() + "'")
						.uniqueResult();
				if (existItem == null) {
					getSession().save(item);
				} else {
					existItem.setItemAll(item);
					getSession().update(existItem);
				}
			} catch (Exception e) {
				e.printStackTrace();
				successCounter--;
			}
			successCounter++;
		}
		return successCounter;
	}

	public List<Item> bulkQueryByTime(int userId, Date fromTime) {
		Criteria  criteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.eq("userId", userId));
		if (fromTime != null)
			criteria.add(Restrictions.ge("modifiedTime", fromTime));

		List<Item> resultArr = (List<Item>) criteria.list();
		for (Item item : resultArr) {
			item.setItemId(item.getItemId());
		}
		return resultArr;
	}

}
