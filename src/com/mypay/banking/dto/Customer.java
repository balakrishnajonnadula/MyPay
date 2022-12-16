package com.mypay.banking.dto;

public class Customer {
	private String custoemrId;
	private String accountNum;
	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	private double balence;
	private String userName;
	private String password;
	private String email;

	public Customer() {
		super();
	}

	public Customer(String custoemrId, String accountNum, String firstName, String lastName, String phone,
			String address, double balence, String userName, String password, String email) {
		super();
		this.custoemrId = custoemrId;
		this.accountNum = accountNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
		this.balence = balence;
		this.userName = userName;
		this.password = password;
		this.setEmail(email);
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getBalence() {
		return balence;
	}

	public void setBalence(double balence) {
		this.balence = balence;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustoemrId() {
		return custoemrId;
	}

	public void setCustoemrId(String custoemrId) {
		this.custoemrId = custoemrId;
	}

}
