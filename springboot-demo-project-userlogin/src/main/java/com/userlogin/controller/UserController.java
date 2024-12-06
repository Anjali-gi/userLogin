package com.userlogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/SignupPage")  //mapping Signup from index
	public String openSigninPage(){
		return "signup" ; //signup html
	}
	
}
