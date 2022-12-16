package com.mypay.banking.bankexception;

public class CusomerExceptions extends Exception {
	String message;

	public CusomerExceptions(String message) {
		super(message);
		this.message = message;

	}

}
