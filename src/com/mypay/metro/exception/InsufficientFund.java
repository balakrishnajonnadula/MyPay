package com.mypay.metro.exception;

public class InsufficientFund extends Exception {
	String message;

	public InsufficientFund(String message) {
		super(message);
		this.message = message;
	}

}
