package com.homeloan.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homeloan.app.model.Repayment;
import com.homeloan.app.repo.RepaymentRepository;

@Service
public class RepaymentService {
	
	@Autowired
	private RepaymentRepository repaymentRepo;
	
	public Repayment saveRepayment(Repayment repayment) {
		return repaymentRepo.save(repayment);
	}

}
