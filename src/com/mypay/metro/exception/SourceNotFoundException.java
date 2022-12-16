package com.mypay.metro.exception;

public class SourceNotFoundException extends Exception {
	String message;

	public SourceNotFoundException(String message) {
		super(message);
		this.message = message;
	}

}
