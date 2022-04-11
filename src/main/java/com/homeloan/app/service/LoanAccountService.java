package com.homeloan.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.repo.LoanAccountRepository;

@Service
public class LoanAccountService {
	
	@Autowired
	private LoanAccountRepository repo;
	
	public LoanAccount saveAppliedLoan(LoanAccount obj) {
		return repo.save(obj);
	}
	
	public List<LoanAccount> getListOfLoanApplied(){
		return repo.findAll();
	}
}
