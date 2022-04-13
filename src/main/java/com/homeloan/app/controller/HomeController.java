package com.homeloan.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.Repayment;
import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.LoanAccountService;
import com.homeloan.app.service.RepaymentService;
import com.homeloan.app.service.SavingAccountService;
import com.homeloan.app.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SavingAccountService savingAccountService;
	
	@Autowired
	private LoanAccountService loanAccService;
	
	@Autowired
	private RepaymentService repaymentService;
	
	@GetMapping("/")
	public String homePage(Model model,HttpSession session) {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Users currentUser = userService.findByUsername(username);
		SavingAccount userAcc = savingAccountService.findAccountByUserId(currentUser.getUserId());
		List<LoanAccount> listAcc =   loanAccService.getListOfLoanApplied().stream()
													.filter(acc -> acc.getAccountNo().equals(userAcc.getAccountno()))
													.collect(Collectors.toList());
		if(!listAcc.isEmpty()) {
			model.addAttribute("appliedLoans",listAcc);
		}
		
		List<LoanAccount> updateLoanAccList = loanAccService.getDetailsOfSpecifiedAccountNo(userAcc.getAccountno());
		LoanAccount updateLoanAcc;
		if(!updateLoanAccList.isEmpty()){
			updateLoanAcc = updateLoanAccList.get(0);
			if(updateLoanAcc.getStatus().equals("Ongoing")) {
				Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
				
//				model.addAttribute("principal",repayment.getPrinciple());
				model.addAttribute("repayment",repayment);
				model.addAttribute("viewColumn" , "true");
					
			}
		}
		
		
		model.addAttribute("user",currentUser);
		model.addAttribute("account",userAcc);
		
		return "home";
	}
}
