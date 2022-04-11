package com.homeloan.app.service;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.repo.SavingAccountRepository;

@Service
public class SavingAccountService {
	
	@Autowired
	private SavingAccountRepository repo;
	
	public Long getLastAccountNo() {
		return  repo.findAll().stream()
				.sorted(Comparator.comparing(SavingAccount::getAccountno).reversed())
				.findFirst().get().getAccountno();
	}
	
	public void saveAccount(SavingAccount acc) {
		repo.save(acc);
	}
	
	public SavingAccount findAccountByUserId(Integer id) {
		return repo.findSavingAccountByUserid(id);
	}
}
