package com.homeloan.app.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.repo.LoanAccountRepository;
import com.homeloan.app.repo.SavingAccountRepository;

@Service
public class LoanAccountService {
	
	@Autowired
	private LoanAccountRepository repo;
	
	@Autowired
	private SavingAccountRepository savingAccRepo;
	
	public LoanAccount saveAppliedLoan( LoanAccount obj) {

		return repo.save(obj);
	}
	
	public List<LoanAccount> getLoanDetails(Integer id) {

		return repo.findByLoanAccId(id);
	}
	
	public List<LoanAccount> getListOfLoanApplied(){
		return repo.findAll();
	}
	
	public List<LoanAccount> getDetailsOfSpecifiedAccountNo( Long accountNo ) {

		return repo.findLoanAccountByAccountNo(accountNo);
	}
	
	public SavingAccount getSavingAccByLoanIdAndAccNo(Long accountNo) {
		return savingAccRepo.findSavingAccountByAccountno(accountNo);
	}
}
