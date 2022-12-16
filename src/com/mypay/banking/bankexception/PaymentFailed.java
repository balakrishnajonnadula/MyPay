package com.mypay.banking.bankexception;

public class PaymentFailed extends Exception {
	String message;

	public PaymentFailed(String message) {
		super(message);
		this.message = message;
	}

}
