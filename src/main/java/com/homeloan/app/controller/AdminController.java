package com.homeloan.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.LoanAccountService;
import com.homeloan.app.service.UserService;

@Controller
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoanAccountService loanAccService;
	
	@GetMapping("/admin/dashboard")
	public String getAdminDashoard(Model model,HttpSession session) {
		String username = (String) session.getAttribute("username");
		Users admin = userService.findByUsername(username);
		List<LoanAccount> loanAccs = loanAccService.getListOfLoanApplied();
		model.addAttribute("loanAccs", loanAccs);
		model.addAttribute("admin",admin);
		return "admindashboard";
	}
	
}
