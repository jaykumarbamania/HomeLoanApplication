package com.homeloan.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "apply_loan")
public class ApplyLoan {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name = "loan_acc")
	private Long loanAccount;
	
	@Column(name = "saving_acc")
	private Long savingAccount;
	
	@Column(name="loan_amount")
	private Double amount;
	
	@Column(name="interest_rate")
	private Double interestRate;
	
	@Column(name="tenure")
	private int tenure;
	
	@Column(name="status")
	private String status;
	
	@Lob  //Large Objects
	private String description;
	
	@Column(name="docUrl")
	private String docUrl;
}
