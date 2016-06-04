package com.yzl.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	@RequestMapping("/getUser")
	public String getUsers(ModelMap map) {
		System.out.println("111111111111");
		map.addAttribute("msg", "hello world!");
		return "/hello.jsp";
	}
	
	public UserController() {
		System.out.println("UserController()");
	}
}
