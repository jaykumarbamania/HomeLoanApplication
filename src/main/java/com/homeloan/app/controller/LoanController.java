package com.homeloan.app.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.Repayment;
import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.service.EmailService;
import com.homeloan.app.service.LoanAccountService;
import com.homeloan.app.service.RepaymentService;
import com.homeloan.app.service.SavingAccountService;
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
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
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
	
	//user will accept loan
	@RequestMapping(value = "/acceptLoan", method = RequestMethod.GET)
	public String acceptLoan(@Param("id") Integer id, HttpSession session, Model model) throws UnsupportedEncodingException, MessagingException {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		updateLoanAcc.setStatus("Ongoing");
		updateLoanAcc.setLoanAccId(id);
		loanAccService.saveAppliedLoan(updateLoanAcc);
		SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());
		double newBal = savingAcc.getBalance() + updateLoanAcc.getAmount();
		savingAcc.setSequenceId(savingAcc.getSequenceId());
		savingAcc.setBalance(newBal);
		savingAccountService.saveAccount(savingAcc);
		model.addAttribute("accepted", "true");
//		if( session.getAttribute("accepted") == "true" ) {
//			System.out.println(model.getAttribute("accepted"));
			Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
	
//			model.addAttribute("principal",repayment.getPrinciple());
			model.addAttribute("repayment",repayment);
			model.addAttribute("viewColumn" , "true");
			emailService.sendEmail(savingAcc.getUser().getEmail(), "You have accepted loan", "Loan Accepted", "homeloan@bankapp.com");
//		}

		return "redirect:/";
	}
	
	//admin will approve loan
	@RequestMapping(value = "/approveLoan", method = RequestMethod.GET)
	public String approveLoan(
//			@RequestParam("approvedAmt") Double approvedAmt , 
			@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String admin = (String) session.getAttribute("admin");
		if(admin == null || admin.equals("")) {
			return "redirect:/login";
		}
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
		emailService.sendEmail(savingAcc.getUser().getEmail(), "Congratulations , Your Loan is Approved","Loan is Approved","loanhome349@gmail.com");

		return "redirect:/admin/dashboard";
	}
	
//	user can reject loan
	@RequestMapping(value = "/rejectLoan", method = RequestMethod.GET)
	public String rejectLoan(@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Integer id = Integer.parseInt(str_id);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		updateLoanAcc.setLoanAccId(id);
		updateLoanAcc.setStatus("Closed");
		loanAccService.saveAppliedLoan(updateLoanAcc);
		SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());
		emailService.sendEmail(savingAcc.getUser().getEmail(), "Unfortunately , Your Loan is Not Approved","Loan is Cancelled","loanhome349@gmail.com");
		return "redirect:/admin/dashboard";
	}
	
	//only user can view loan details
	@RequestMapping(value = "/viewLoan", method = RequestMethod.GET)
	public String viewLoan(@RequestParam("id") String str_id, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Integer id = Integer.parseInt(str_id);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(id).get(0);
		Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
		model.addAttribute("loan_id",id);
		model.addAttribute("repayment",repayment);
		model.addAttribute("username",username);
		return "viewloan";
	}
	
	@RequestMapping(value = "/prepayment", method = RequestMethod.GET)
	public String prePayment(@RequestParam("loan_id") String str_loanid, HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Integer loan_id = Integer.parseInt(str_loanid);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(loan_id).get(0);
		Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
		
		model.addAttribute("repayment",repayment);
		model.addAttribute("username",username);
		model.addAttribute("loan_id",loan_id);
		return "prepayment";
	}
	
	@RequestMapping(value = "/prepayment", method = RequestMethod.POST)
	public String checkPrePayment(@RequestParam("preEmi") Double preEmi, 
			@RequestParam("loan_id") String str_loanid, 
			@RequestParam("emi1") Double emi,
			HttpSession session, Model model) throws UnsupportedEncodingException, MessagingException {

		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		Integer loan_id = Integer.parseInt(str_loanid);
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(loan_id).get(0);
		Repayment repayment = repaymentService.getRepaymentByAccountNo(updateLoanAcc.getAccountNo());
		

		model.addAttribute("username",username);
		model.addAttribute("loan_id",loan_id);
		
		if(preEmi >= (emi *3) ) {
			Double newOutStanding = repayment.getOutstanding() - preEmi;
			System.out.println(newOutStanding);
			repayment.setRepaymentid(repayment.getRepaymentid());
			repayment.setOutstanding(newOutStanding);
			SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());
			double newBal = savingAcc.getBalance() - preEmi;
			savingAcc.setSequenceId(savingAcc.getSequenceId());
			savingAcc.setBalance(newBal);
			savingAccountService.saveAccount(savingAcc);
			repaymentService.saveRepayment(repayment);
			model.addAttribute("successMsg","Pre-Payment is Successfull ");
			model.addAttribute("repayment",repayment);
			
			emailService.sendEmail(savingAcc.getUser().getEmail(), "Congratualaion... Yur prepaymnet was successful ","Your Prepayment is successful","homeloan@bankapp.com");
			
			return "prepayment";
		}else {
			model.addAttribute("errorMsg","Emi should be minimum "+(emi * 3));
			model.addAttribute("repayment",repayment);
			return "prepayment";
		}
	}
	
	@RequestMapping(value = "/payAmount/{loan_id}/{repaymentId}/{ppe}/{pie}", method = RequestMethod.GET)
	public String payAmount(@PathVariable("repaymentId") Integer repaymentId,
			@PathVariable("loan_id") Integer loan_id,
			@PathVariable("ppe") Double ppe,
			@PathVariable("pie") Double pie,
			HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
	
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(loan_id).get(0);
		Repayment repayment = repaymentService.getRepaymentById(repaymentId);
		double currNoOfEmiPaid = repayment.getNoEmiPaid();
		double newOut = repayment.getOutstanding()-ppe;
		double newInterst = repayment.getInterest() - pie;
		repayment.setRepaymentid(repaymentId);
		repayment.setOutstanding(newOut);
		repayment.setInterest(newInterst);
		repayment.setNoEmiPaid(currNoOfEmiPaid+1);
		SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());
		double newBal = savingAcc.getBalance() - ppe - pie;
		savingAcc.setSequenceId(savingAcc.getSequenceId());
		savingAcc.setBalance(newBal);
		savingAccountService.saveAccount(savingAcc);
		emailService.sendEmail(savingAcc.getUser().getEmail(), "EMI DEDUCTED  ","EMI DEDUCTED","homeloan@bankapp.com");
		repaymentService.saveRepayment(repayment);
		model.addAttribute("loan_id",updateLoanAcc.getLoanAccId());
		model.addAttribute("repayment",repayment);
		model.addAttribute("username",username);
		return "redirect:/viewLoan?id="+updateLoanAcc.getLoanAccId();
	}
	
	@RequestMapping(value = "/foreClosure/{loan_id}/{repaymentId}/{outstanding}", method = RequestMethod.GET)
	public String foreClosure(@PathVariable("repaymentId") Integer repaymentId,
			@PathVariable("loan_id") Integer loan_id,
			@PathVariable("outstanding") Double outstanding,
			HttpSession session, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}

		model.addAttribute("loan_id",loan_id);
		model.addAttribute("outstanding",outstanding);
		model.addAttribute("repaymentId",repaymentId);
		return "foreclosure";
	}
	
	@RequestMapping(value = "/closeForeClosing", method = RequestMethod.POST)
	public String closeForeClosing(@RequestParam("repaymentid") Integer repaymentid,
			@RequestParam("loan_id") Integer loan_id,
			@RequestParam("outstanding") Double outstanding,
			HttpSession session, Model model) throws UnsupportedEncodingException, MessagingException{

		String username = (String) session.getAttribute("username");
		if(username == null || username.equals("")) {
			return "redirect:/login";
		}
		LoanAccount updateLoanAcc = loanAccService.getLoanDetails(loan_id).get(0);
		updateLoanAcc.setLoanAccId(loan_id);
		updateLoanAcc.setStatus("Closed");
		loanAccService.saveAppliedLoan(updateLoanAcc);
//		String username = (String) session.getAttribute("username");
		SavingAccount savingAcc = loanAccService.getSavingAccByLoanIdAndAccNo(updateLoanAcc.getAccountNo());
		Repayment repayment = repaymentService.getRepaymentById(repaymentid);
		double newBal = savingAcc.getBalance() - outstanding;
		savingAcc.setSequenceId(savingAcc.getSequenceId());
		savingAcc.setBalance(newBal);
		savingAccountService.saveAccount(savingAcc);
		repayment.setOutstanding((double) 0);
		repayment.setStatus("Closed");
		repaymentService.saveRepayment(repayment);
		model.addAttribute("loan_id",loan_id);
		model.addAttribute("outstanding",outstanding);
		model.addAttribute("repaymentId",repaymentid);
		
		emailService.sendEmail(savingAcc.getUser().getEmail(), "Your Loan is Closed", "Your loan is Closed!!!", "homeloan@bankapp.com");
		return "redirect:/";
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
