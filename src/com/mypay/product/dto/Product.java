package com.mypay.product.dto;

public class Product {
	private String productId;
	private String productName;
	private int Qty;
	private double price;
	private String adminId;

	public Product() {
		super();
	}

	public Product(String id, String productName, int qty, double price, String adminId) {
		super();
		this.productId = id;
		this.productName = productName;
		Qty = qty;
		this.price = price;
		this.setAdminId(adminId);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQty() {
		return Qty;
	}

	public void setQty(int qty) {
		Qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

}
