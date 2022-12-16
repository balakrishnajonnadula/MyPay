package com.mypay.metro.exception;

public class RoutesNotFound extends Exception {
	String message;

	public RoutesNotFound(String message) {
		super(message);
		this.message = message;
	}

}
