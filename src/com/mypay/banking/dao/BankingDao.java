package com.mypay.banking.dao;

import java.util.List;

import com.mypay.banking.dto.Customer;

public interface BankingDao {
	public void custRegister();

	public Customer viewBal();

	public boolean withDraw();

	public boolean diposit();

	public boolean fundTransfer();

	public Customer viewMyDetails();

	public void login();

	public void delecteCust(String accountNum);

	public void updateCustData(String accNum);

	public void logout();

	public List<Customer> viewAllCusomers();

	public Customer viewCustomer(String accountNUm);
}
