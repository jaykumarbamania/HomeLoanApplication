package com.homeloan.app.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.Repayment;
import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.EmailService;
import com.homeloan.app.service.LoanAccountService;
import com.homeloan.app.service.RepaymentService;
import com.homeloan.app.service.SavingAccountService;
import com.homeloan.app.service.StorageService;
import com.homeloan.app.service.UserService;

@Controller
//@MultipartConfig(maxFileSize = 10737418240L, maxRequestSize = 10737418240L, fileSizeThreshold = 52428800)
public class LoanController {

	@Autowired
	private UserService userService;

	@Autowired
	private SavingAccountService savingAccountService;

	@Autowired
	private LoanAccountService loanAccService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RepaymentService repaymentService;

	@GetMapping("/apply/loan")
	public String getAdminDashoard(Model model, HttpSession session) {

		return "loan";
	}

	@RequestMapping(value = "/applyLoan", method = RequestMethod.POST, consumes = "multipart/form-data", produces = {
			MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String applyForLoan(LoanAccount loanAcc, HttpSession session, @RequestParam("docUrl") MultipartFile file) {
		String username = (String) session.getAttribute("username");
		Users currUser = userService.findByUsername(username);
		SavingAccount userAccount = savingAccountService.findAccountByUserId(currUser.getUserId());
		loanAcc.setAccountNo(userAccount.getAccountno());
		loanAcc.setStatus("Pending");
		loanAcc.setApprovedAmt((double) 0);
		loanAcc.setEligibleAmt(calculateEligibleLoanAmt(loanAcc.getSalary()));
//		storageService.store(file);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		loanAcc.setDocName(fileName);
		try {
			loanAcc.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		loanAccService.saveAppliedLoan(loanAcc);

		return "redirect:/";
	}

	@RequestMapping(value = "/acceptLoan", method = RequestMethod.GET)
	public String acceptLoan(@Param("id") Integer id, HttpSession session, Model model) {
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		updateLoanAcc.setStatus("Ongoing");
		updateLoanAcc.setLoanAccId(id);
		loanAccService.saveAppliedLoan(updateLoanAcc);
		model.addAttribute("accepted", "true");
//		if( session.getAttribute("accepted") == "true" ) {
//			System.out.println(model.getAttribute("accepted"));
			Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
	
//			model.addAttribute("principal",repayment.getPrinciple());
			model.addAttribute("repayment",repayment);
			model.addAttribute("viewColumn" , "true");
			
//		}

		return "redirect:/";
	}

	@RequestMapping(value = "/approveLoan", method = RequestMethod.GET)
	public String approveLoan(
//			@RequestParam("approvedAmt") Double approvedAmt , 
			@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {

		Integer id = Integer.parseInt(str_id);
		Repayment repayment = new Repayment();
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());

		updateLoanAcc.setLoanAccId(id);
		updateLoanAcc.setStatus("Approved");
//		double approvedAmt = calculateEligibleLoanAmt(updateLoanAcc.getSalary(), updateLoanAcc.getAmount());
		updateLoanAcc.setApprovedAmt(updateLoanAcc.getAmount());
		loanAccService.saveAppliedLoan(updateLoanAcc);

		repayment.setAccountNo(savingAcc.getAccountno());
		repayment.setPrinciple(updateLoanAcc.getApprovedAmt());
		repayment.setYear(updateLoanAcc.getTenure());
		repayment.setNoEmiPaid((double) 0);
		double emi = calculateEmi(updateLoanAcc.getApprovedAmt(), updateLoanAcc.getInterestRate(),
				updateLoanAcc.getTenure());
		repayment.setEmi(emi);
		repayment.setRate(updateLoanAcc.getInterestRate());
		repayment.setInterest(calculateInterest(updateLoanAcc.getApprovedAmt(), emi, updateLoanAcc.getTenure()));
		repayment.setStatus("Ongoing");
		repayment.setOutstanding(updateLoanAcc.getApprovedAmt());
		repaymentService.saveRepayment(repayment);
		emailService.sendOnApprove(savingAcc.getUser().getEmail(), "COngratulations , Your Loan is Approved");
//		model.addAttribute("loanApproved",true);
		return "redirect:/admin/dashboard";
	}

	@RequestMapping(value = "/rejectLoan", method = RequestMethod.GET)
	public String rejectLoan(@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {

		Integer id = Integer.parseInt(str_id);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		updateLoanAcc.setLoanAccId(id);
		updateLoanAcc.setStatus("Closed");
		loanAccService.saveAppliedLoan(updateLoanAcc);
		emailService.sendOnReject("loanhome349@gmail.com", "Unfortunately , Your Loan is Not Approved");
		return "redirect:/admin/dashboard";
	}
	
	@RequestMapping(value = "/viewLoan", method = RequestMethod.GET)
	public String viewLoan(@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {

		Integer id = Integer.parseInt(str_id);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		String username = (String) session.getAttribute("username");
		Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
		model.addAttribute("repayment",repayment);
		model.addAttribute("username",username);
		return "viewloan";
	}

//	private static double calculateEligibleLoanAmt(double salary,double amount) {
//		double approvedAmt = amount - salary * 50;
//		if(approvedAmt < 0 ) approvedAmt = approvedAmt*(-1);
//		return approvedAmt;
//	}

	private static double calculateEligibleLoanAmt(double salary) {
		return salary * 50;
	}

	private static double calculateEmi(double principle, double interestRate, int years) {
		int months = years * 12;
		interestRate = interestRate / (12 * 100);
		double emi = (principle * interestRate * Math.pow((1 + interestRate), months))
				/ (Math.pow((1 + interestRate), months) - 1);

		return emi;
	}

	private static double calculateInterest(double principal, double emi, int years) {

		double simple_interest = (years * 12 * emi) - principal;

		return simple_interest;
	}

}
