package com.homeloan.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.homeloan.app.model.SavingAccount;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Integer>{
	
//	@Query("SELECT a FROM SavingAccount a ORDER BY sequence_id DESC LIMIT 1")
//	SavingAccount getLastAccountNumber();
	
	@Query("SELECT a FROM SavingAccount a WHERE a.user.userId = :userid")
	SavingAccount findSavingAccountByUserid(@Param("userid") Integer userid);
	
	SavingAccount findSavingAccountByAccountno(Long accountno);
}
