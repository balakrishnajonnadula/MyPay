package com.mypay.banking.bankexception;

public class BankingInsufficientFunds extends Exception {
	String message;

	public BankingInsufficientFunds(String message) {
		super(message);
		this.message = message;
	}
	
}
