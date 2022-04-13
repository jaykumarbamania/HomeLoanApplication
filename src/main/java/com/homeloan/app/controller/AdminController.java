package com.homeloan.app.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
	public String getAdminDashoard(Model model, HttpSession session) {
		String admin = (String) session.getAttribute("admin");
		if(admin == null || admin.equals("")) {
			return "redirect:/login";
		}
		Users adminUser = userService.findByUsername(admin);
		List<LoanAccount> loanAccs = loanAccService.getListOfLoanApplied();
		model.addAttribute("loanAccs", loanAccs);
		model.addAttribute("admin", adminUser);
		
		return "admindashboard";
	}

	@GetMapping("/downloadfile")
	public void downloadFile(@Param("id") Integer id, HttpSession session, Model model, HttpServletResponse response) throws IOException {

		LoanAccount obj = loanAccService.getLoanDetails(id).get(0);
		if (obj != null) {
			LoanAccount loanObj = obj;
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
