package com.homeloan.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.SavingAccountService;
import com.homeloan.app.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SavingAccountService savingAccountService;
	
	@GetMapping("/")
	public String homePage(Model model,HttpSession session) {
		String username = (String) session.getAttribute("username");
		Users currentUser = userService.findByUsername(username);
		SavingAccount userAcc = savingAccountService.findAccountByUserId(currentUser.getUserId());
		model.addAttribute("user",currentUser);
		model.addAttribute("account",userAcc);
		return "home";
	}
}
