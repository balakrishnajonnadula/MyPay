package com.mypay.product.ProductException;

public class ProductNotFoudException extends Exception {
	String message;

	public ProductNotFoudException(String message) {
		super(message);
		this.message = message;
	}

}
