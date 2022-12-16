package com.mypay.clients;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.mypay.banking.dto.Customer;
import com.mypay.metro.exception.InsufficientFund;
import com.mypay.metro.exception.SourceNotFoundException;
import com.mypay.banking.dao.BankingDao;
import com.mypay.banking.daoimpl.BankingDaoImpl;

public class BClient {
	static BankingDao bDao = new BankingDaoImpl();
	static Scanner scanner = new Scanner(System.in);

//	----------------- Admin Banking------------------------
	public static void adminOperators() throws SourceNotFoundException {
		while (true) {
			System.out.println("******ADMIN BANKING******");
			System.out.println(" 1) Add customer.        ");
			System.out.println(" 2) Delete customer.     ");
			System.out.println(" 3) View customer.  	 ");
			System.out.println(" 4) Update customer. 	 ");
			System.out.println(" 5) View all customers.	 ");
			System.out.println(" 6) Back.           	 ");
			System.out.println("*************************");
			switch (scanner.nextInt()) {
			case 1: {
				try {
					bDao.custRegister();
				} catch (InputMismatchException e) {
					System.err.println(e);
				}
				break;
			}
			case 2: {
				System.out.println("Enter account number..!");
				try {
					bDao.delecteCust(scanner.next());
				} catch (InputMismatchException e) {

				}
				break;
			}
			case 3: {
				System.out.println("Enter account number..!");
				Customer viewCus = null;
				try {
					viewCus = bDao.viewCustomer(scanner.next());
				} catch (InputMismatchException e) {
					System.err.println(e);
				}
				if (viewCus != null) {
					System.err.println("\tAccount Details");
					System.out.println("Account No \t: " + viewCus.getAccountNum() + "\nHolder Name \t: "
							+ viewCus.getFirstName().concat(" " + viewCus.getLastName()) + "\nHolder Phone \t: "
							+ viewCus.getPhone() + "\nAddress \t: " + viewCus.getAddress() + "\nHolder Email \t: "
							+ viewCus.getEmail() + "\nUsername \t: " + viewCus.getUserName() + "\nPassword \t: "
							+ viewCus.getPassword() + "\nBalence \t: " + viewCus.getBalence() + " ₹");
				} else {
					throw new SourceNotFoundException("Source not found..!");
				}
				break;
			}
			case 4: {
				System.out.println("Enter account number..!");
				bDao.updateCustData(scanner.next());

				break;
			}
			case 5: {
				List<Customer> viewAllCusomers = BClient.bDao.viewAllCusomers();
				if (!viewAllCusomers.isEmpty()) {
					System.out.format("%-15s%-20s%-18s%-18s%-33s%-15s%-15s%-15s\n", "Account No ", "Name ", "Phone",
							"Address", "Email", "Username ", " Password", "Current Balence");
					System.out.format("%-15s%-20s%-18s%-18s%-33s%-15s%-15s%-15s\n", "------------", "--------------",
							"---------------", "------------", "---------------------------", "---------", "---------",
							"----------------");
					for (Customer viewCus : viewAllCusomers) {
						System.out.format("%-15s%-20s%-18s%-18s%-33s%-15s%-15s%-15s\n", viewCus.getAccountNum(),
								viewCus.getFirstName().toUpperCase()
										.concat(" " + viewCus.getLastName().toUpperCase().charAt(0)),
								viewCus.getPhone(), viewCus.getAddress().toUpperCase(), viewCus.getEmail(),
								viewCus.getUserName(), viewCus.getPassword(), viewCus.getBalence() + " ₹");
					}
					System.out.println();
				} else {
					throw new SourceNotFoundException("Source not found Exception");
				}

				break;
			}
			case 6: {
				try {
					Client.adminOpps();
				} catch (Exception e) {
					System.out.println(e);
				}
				break;
			}
			}
		}
	}

//	----------------- Customer Banking------------------------

	public static void custFeatures() {
		while (true) {
			System.out.println("*******Customer Banking*******");
			System.out.println("	1) View my details.       ");
			System.out.println("	2) View bal.			  ");
			System.out.println("	3) Fund transfer.		  ");
			System.out.println("	4) Deposit. 			  ");
			System.out.println("	5) Withdraw. 			  ");
			System.out.println("	6) Back.				  ");
			System.out.println("******************************");
			switch (scanner.nextInt()) {
			case 1: {
				Customer my = bDao.viewMyDetails();
				if (my != null) {

					System.err.println("\tAccount Details");
					System.out.println("Account No \t: " + my.getAccountNum() + "\nHolder Name \t: "
							+ my.getFirstName().concat(" " + my.getLastName()) + ",\nHolder Phone \t: " + my.getPhone()
							+ "\nAddress \t: " + my.getAddress() + "\nHolder Email \t: " + my.getEmail()
							+ "\nUsername \t: " + my.getUserName() + "\nPassword \t: " + my.getPassword()
							+ "\nBalence \t: " + my.getBalence() + " ₹");
				} else {
					System.out.println("Source not found...!");
				}
				break;
			}
			case 2: {
				Customer viewBal = bDao.viewBal();
				if (viewBal != null) {
					System.out.println("Current balence is : " + viewBal.getBalence() + " ₹");
				} else {
					System.out.println("Source not found...!");
				}
				break;
			}
			case 3: {
				boolean fundTransfer = bDao.fundTransfer();
				if (fundTransfer) {
					System.out.println("Transaction successfull..!");
				} else {
					System.out.println("Transaction failed..!");
				}
				break;
			}
			case 4: {
				boolean diposit = bDao.diposit();
				if (diposit) {
					System.out.println("Dipsited successfully..!");
				} else {
					System.out.println("Transaction failed..!");
				}
				break;
			}
			case 5: {
				boolean withDraw = bDao.withDraw();
				if (withDraw) {
					System.out.println("withDrawn successfully..!");
				} else {
					System.out.println("Transaction failed..!");
				}
				break;
			}
			case 6: {
				Client.custOpps();
				break;
			}
			default: {
				System.err.println("Enter valid input..!");
			}
			}
		}
	}
}
