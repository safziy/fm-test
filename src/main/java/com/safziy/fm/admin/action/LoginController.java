package com.safziy.fm.admin.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.safziy.fm.admin.service.TestService;

@Controller
@RequestMapping("/admin/login")
public class LoginController {
	
	@Resource
	private TestService testServices;

	@RequestMapping("/login")
	public String login(){
		testServices.test();
		return "login";
	}
}
