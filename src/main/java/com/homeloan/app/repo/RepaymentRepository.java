package com.homeloan.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homeloan.app.model.Repayment;

public interface RepaymentRepository extends JpaRepository<Repayment, Integer> {
	
	
	Repayment findRepaymentByAccountNo(Long accountNo);
}
