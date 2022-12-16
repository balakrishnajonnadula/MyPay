package com.mypay.banking.bankexception;

public class SourceNotFoundException extends Exception {
	String message;

	public SourceNotFoundException(String message) {
		super(message);
		this.message = message;
	}

}
