package com.homeloan.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.SavingAccount;
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
	public String getAdminDashoard(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Users admin = userService.findByUsername(username);
		List<LoanAccount> loanAccs = loanAccService.getListOfLoanApplied();
		model.addAttribute("loanAccs", loanAccs);
		model.addAttribute("admin", admin);
		
		return "admindashboard";
	}

	@GetMapping("/downloadfile")
	public void downloadFile(@Param("id") Integer id, HttpSession session, Model model, HttpServletResponse response) throws IOException {
		String username = (String) session.getAttribute("username");
		Users currUser = userService.findByUsername(username);
		SavingAccount account = userService.getSavingAccount(currUser.getUserId());
//		List<LoanAccount> obj = loanAccService.getDetailsOfSpecifiedAccountNo(account.getAccountno());
		Optional<LoanAccount> obj = loanAccService.getLoanDetails(id);
		if (obj != null) {
			LoanAccount loanObj = obj.get();
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + loanObj.getDocName();
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(loanObj.getFile());
			outputStream.close();
		}
	}

}
