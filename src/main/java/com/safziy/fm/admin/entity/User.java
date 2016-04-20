package com.safziy.fm.admin.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.common.collect.Lists;

@Entity
@Table(name = "t_user")
@EntityListeners(BaseEnttiyListener.class)
public class User extends IdEntity {

	private static final long serialVersionUID = -1393400171376159505L;
	private String loginName;
	private String plainPassword;
	private String password;
	private String salt;
	private String loginIp;
	private String department;
	private String name;
	private String email;
	private Boolean isAccountEnabled;
	private Date loginTime;

	private BigDecimal balance;

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	private List<Role> roleList = new ArrayList<Role>();

	// 多对多定义
	@ManyToMany
	// 中间表定义,表名采用默认命名规则
	@JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	public String getRoleString() {
		StringBuffer roles = new StringBuffer("");
		if (roleList != null && roleList.size() > 0) {
			int i = 0;
			for (Role role : roleList) {
				roles.append(role.getName());
				if (i < (roleList.size() - 1)) {
					roles.append(",");
				}
				i++;
			}
		}
		return roles.toString();
	}

	@Transient
	public List<String> getRoleIds() {
		List<String> roles = null;
		if (roleList != null && roleList.size() > 0) {
			roles = Lists.newArrayList();
			for (Role role : roleList) {
				roles.add(String.valueOf(role.getId()));
			}
		}
		return roles;
	}

	public Boolean getIsAccountEnabled() {
		return isAccountEnabled;
	}

	public void setIsAccountEnabled(Boolean isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 安全性考虑 密码不转换json形式到页面
	 * 
	 * @autor liuf
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	// 不持久化到数据库，也不显示在Restful接口的属性.
	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}