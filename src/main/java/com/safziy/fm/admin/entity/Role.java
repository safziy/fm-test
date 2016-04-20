package com.safziy.fm.admin.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;


@Entity
@Table(name = "t_role")
@EntityListeners(BaseEnttiyListener.class)
public class Role extends IdEntity{

	private static final long serialVersionUID = 3163397682000458252L;
	private String name;
	private String description;
	private String authorityListStore;
	private Boolean isSystem;
	
	/**
	 * 共页面传值用 不持久化到数据库
	 */
	private List<String> authoritysList;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthorityListStore() {
		return authorityListStore;
	}
	public void setAuthorityListStore(String authorityListStore) {
		this.authorityListStore = authorityListStore;
	}
	public Boolean getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}
	
	
	
	/**
	 * 页面上传值过来的权限集合 权限框架中不使用
	 * @return
	 */
	@Transient
	public List<String> getAuthoritysList() {
		return authoritysList;
	}
	public void setAuthoritysList(List<String> authoritysList) {
		this.authoritysList = authoritysList;
	}

	/**
	 * 真正的权限集合
	 * @return
	 */
	@Transient
	public List<String> getAuthorityList() {
		if(StringUtils.isNotBlank(authorityListStore)){
			return ImmutableList.copyOf(StringUtils.split(authorityListStore, ","));
		}
		return null;
	}
	
}
