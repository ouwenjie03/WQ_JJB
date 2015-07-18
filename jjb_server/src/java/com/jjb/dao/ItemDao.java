package com.jjb.dao;

import java.util.Date;
import java.util.List;

import com.jjb.bean.Item;

/**
 * Item的数据库接入接口
 * @author Robert Peng
 */
public interface ItemDao extends BaseDao {
	
	/**
	 * 根据指定时间返回指定时间以后的该用户所有Item。fromTime = null则返回数据库中该用户所有的item
	 * @param userId 用户id
	 * @param fromTime 指定查询时间
	 * @return 查询时间后的所有item
	 */
	public List<Item> bulkQueryByTime(int userId, Date fromTime);
	
	/**
	 * 添加item
	 * @param item item
	 * @return 添加成功则返回true，否则返回false
	 */
	public boolean insertItem(Item item);
	
	/**
	 * 批量添加item
	 * @param items 要添加的item们
	 * @return 添加成功的item个数
	 */
	public int insertItems(List<Item> items);

}
