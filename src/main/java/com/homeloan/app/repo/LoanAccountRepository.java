package com.homeloan.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.model.SavingAccount;

public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {
	
	List<LoanAccount> findLoanAccountByAccountNo(Long accountNo);
	
	List<LoanAccount> findByLoanAccId(int loanAccId); 
	
//	@Query("SELECT a FROM SavingAccount a WHERE a.user.userId = :userid")
	

}
