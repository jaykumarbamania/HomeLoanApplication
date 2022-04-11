package com.homeloan.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homeloan.app.model.LoanAccount;

public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {

}
