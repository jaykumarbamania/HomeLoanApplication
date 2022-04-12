package com.homeloan.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.homeloan.app.model.Repayment;

@Transactional(readOnly = true)
public interface RepaymentRepository extends JpaRepository<Repayment, Integer> {
	
	
	Repayment findRepaymentByAccountNo(Long accountNo);
}
