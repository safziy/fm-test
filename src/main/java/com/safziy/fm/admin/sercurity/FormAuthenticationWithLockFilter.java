package com.safziy.fm.admin.sercurity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormAuthenticationWithLockFilter extends FormAuthenticationFilter {
	private long maxLoginAttempts = 2;

	public static ConcurrentHashMap<String, AtomicLong> accountLockMap = new ConcurrentHashMap<String, AtomicLong>();

	private static final Logger log = LoggerFactory.getLogger(FormAuthenticationWithLockFilter.class);

	/**
	 * 重写过滤器onAccessDenied方法
	 * 不跳转到上次请求url地址 直接跳转到后台首页
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		 if (isLoginRequest(request, response)) {
	            if (isLoginSubmission(request, response)) {
	                return executeLogin(request, response);
	            } else {
	                return true;
	            }
	            
	        } else {
	            
	            redirectToLogin(request, response);
	            return false;
	        }
	}
	
	/**
	 * 重写executeLogin方法
	 * 处理登录时的业务
	 */
	@Override
	protected boolean executeLogin(ServletRequest request,
			ServletResponse response) throws Exception {
		
		AuthenticationToken token = createToken(request, response);

		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}
//		if(log.isDebugEnabled())
//			log.debug("executeLogin user=" + getUsername(request)+ ";password="+getPassword(request));
		
		if (!doLogin(request, response, token)) {
//			resetAccountLock(getUsername(request));
			return false;
		}
//		if (checkIfAccountLocked(request)) {
//			
//			return onLoginFailure(token, new ExcessiveAttemptsException(),
//					request, response);
//			
//		}
//		else {
//			if (!doLogin(request, response, token)) {
//				resetAccountLock(getUsername(request));
//				return false;
//			}
//			return true;
//		}
		return true;
	}

	@SuppressWarnings("unused")
	private boolean checkIfAccountLocked(ServletRequest request) {
		
		String username = getUsername(request);
		if (accountLockMap.get((String) username) != null) {
			long remainLoginAttempts = accountLockMap.get((String) username)
					.get();
			request.setAttribute("remainLoginAttempts", remainLoginAttempts);
			if (remainLoginAttempts <= 0) {
				log.info(username + "登陆被锁定");
				return true;
			}
		}
		return false;
	}

	/**
	 * 登录验证操作
	 * @author:liufei
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private boolean doLogin(ServletRequest request, ServletResponse response,
			AuthenticationToken token) throws Exception {
		
		try {
			Subject subject = getSubject(request, response);
			subject.login(token);

			return onLoginSuccess(token, subject, request, response);
		} catch (IncorrectCredentialsException e) {
//			decreaseAccountLoginAttempts(request);
//			checkIfAccountLocked(request);

			return onLoginFailure(token, e, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		}
	}

	@SuppressWarnings("unused")
	private void decreaseAccountLoginAttempts(ServletRequest request) {
	
		AtomicLong initValue = new AtomicLong(maxLoginAttempts);
		AtomicLong remainLoginAttempts = accountLockMap.putIfAbsent(
				getUsername(request), new AtomicLong(maxLoginAttempts));
		if (remainLoginAttempts == null) {
			remainLoginAttempts = initValue;
		}
		remainLoginAttempts.getAndDecrement();
		accountLockMap.put(getUsername(request), remainLoginAttempts);
	}

	@SuppressWarnings("unused")
	private void resetAccountLock(String username) {
		accountLockMap.put(username, new AtomicLong(maxLoginAttempts));
	}

	public void setMaxLoginAttempts(long maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}
}
