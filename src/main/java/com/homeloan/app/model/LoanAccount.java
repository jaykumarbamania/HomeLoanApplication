package com.homeloan.app.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "loan_account")
public class LoanAccount {
	
	@Id
	@Column(name = "loan_acc_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer loanAccId;
	
	@Column(name = "saving_acc_no")
	private Long accountNo;
	
	@Column(name="loan_amount")
	private Double amount;
	
	@Column(name="interest_rate")
	private Double interestRate;
	
	@Column(name="salary")
	private Double salary;
	
	@Column(name="tenure")
	private int tenure; //month or year
	
	@Lob  //Large Objects
	private String description;
	
//	@Transient
//	public static Map<Integer,String> statusTypes = Map.of(1,"Approved",2,"Ongoing",3,"Closed");
	
	@Column(name="status")
	private String status;
}
