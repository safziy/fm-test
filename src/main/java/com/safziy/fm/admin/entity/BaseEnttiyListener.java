package com.safziy.fm.admin.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 实体类监听器
 * @filename      : BaseEnttiyListener.java
 * @description   : 实体类监听器 统一修改数据的创建时间和修改时间
 * @author        : liuf
 * @create        : 2013-10-31 上午11:47:31
 * @copyright     : hileto Corporation 2013
 *
 * Modification History:
 * Date             Author       Version
 * --------------------------------------
 * 2013-10-31 上午11:47:31
 */
public class BaseEnttiyListener {

	
	/**
	 * 在持久化实体之前调用
	 * @autor liuf
	 * @param entity
	 */
	@PrePersist
	public void onSave(IdEntity entity){
		Date createDate = new Date();
		entity.setCreateDate(createDate);
		entity.setModifyDate(createDate);
	}
	
	
	/**
	 * 在update实体之前调用
	 * @autor liuf
	 * @param entity
	 */
	@PreUpdate
	public void onUpdate(IdEntity entity){
		Date modeifyDate = new Date();
		entity.setModifyDate(modeifyDate);
	}
}
