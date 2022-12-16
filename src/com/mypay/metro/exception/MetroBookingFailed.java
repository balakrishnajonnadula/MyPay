package com.mypay.metro.exception;

public class MetroBookingFailed extends Exception {
	String message;

	public MetroBookingFailed(String message) {
		super(message);
		this.message = message;
	}

}
