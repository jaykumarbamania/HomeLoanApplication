package com.homeloan.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.LoanAccountService;
import com.homeloan.app.service.SavingAccountService;
import com.homeloan.app.service.UserService;


@Controller
public class LoanController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SavingAccountService savingAccountService;
	
	@Autowired
	private LoanAccountService loanAccService;
	
	@GetMapping("/apply/loan")
	public String getAdminDashoard(Model model,HttpSession session) {
//		model.addAttribute("statusTypes",LoanAccount.statusTypes.entrySet());
		return "loan";
	}
	
	@RequestMapping(value = "/applyLoan", method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
	        produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public String applyForLoan(LoanAccount loanAcc,HttpSession session) {
		String username = (String) session.getAttribute("username");
		Users currUser = userService.findByUsername(username);
		SavingAccount userAccount = savingAccountService.findAccountByUserId(currUser.getUserId());
		loanAcc.setAccountNo(userAccount.getAccountno());
		loanAcc.setStatus("Pending");
		loanAccService.saveAppliedLoan(loanAcc);
		
		return "redirect:/";
	}
}
