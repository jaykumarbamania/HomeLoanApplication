package com.homeloan.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homeloan.app.model.LoanAccount;

public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {
	
	List<LoanAccount> findLoanAccountByAccountNo(Long accountNo);
	
	List<LoanAccount> findByLoanAccId(int loanAccId); 
}
