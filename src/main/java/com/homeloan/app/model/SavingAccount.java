package com.homeloan.app.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "savingaccount")
public class SavingAccount {
	
	@Id
	@Column(name = "sequence_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer sequenceId;
	
	@Column(name="acc_no",unique = true)
	private Long accountno;
	
	@OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "user_id",referencedColumnName = "user_id")
	private Users user;
	
	@Column(name="curr_balance")
	private Double balance;
	
	@Column(name="created_at")
	private Date createdat;
	
	@Column(name="updated_at")
	private Date updatedat;

}
