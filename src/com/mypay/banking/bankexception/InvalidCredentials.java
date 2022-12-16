package com.mypay.banking.bankexception;

public class InvalidCredentials extends Exception {
	String message;

	public InvalidCredentials(String message) {
		super(message);
		this.message = message;
	}

}
