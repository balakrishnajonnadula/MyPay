package com.mypay.product.ProductException;

public class ProductSourceNotFound extends Exception {
	String message;

	public ProductSourceNotFound(String message) {
		super(message);
		this.message = message;
	}

}
