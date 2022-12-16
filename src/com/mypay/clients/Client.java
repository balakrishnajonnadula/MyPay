package com.mypay.clients;

import java.util.Scanner;

import com.mypay.metro.exception.MetroBookingFailed;
import com.mypay.metro.exception.RoutesNotFound;
import com.mypay.metro.exception.SourceNotFoundException;
import com.mypay.product.ProductException.ProductNotFoudException;
import com.mypay.product.ProductException.ProductSourceNotFound;
import com.mypay.admin.AdminLogin;
import com.mypay.banking.bankexception.BankingInsufficientFunds;
import com.mypay.banking.bankexception.CusomerExceptions;
import com.mypay.banking.bankexception.InvalidCredentials;
import com.mypay.banking.bankexception.PaymentFailed;

public class Client {
	static AdminLogin aLogin = new AdminLogin();
	static Scanner scanner = new Scanner(System.in);

//	-------------------------- Main-----------------------------------

	public static void main(String[] args) {
		while (true) {
			System.out.println("******* Welcome to MyPay ********");
			System.out.println("	1) Admin Portal.");
			System.out.println("	2) Customer Portal.");
			System.out.println("	3) Customer Regestration.");
			System.out.println("	4) Exit.");
			System.out.println("*********************************");

			switch (scanner.nextInt()) {
			case 1: {
				System.out.println("Enter username");
				String name = scanner.next();
				System.out.println("Enter password");
				String pass = scanner.next();
				int adminLogin = aLogin.adminLogin(name, pass);
				if (adminLogin != 0) {
					adminOpps();
				} else {
					System.err.println("---Validation failed---");
				}
				/*
				 * if (userName.equals(name) && password.equals(pass)) { try { adminOpps(); }
				 * catch (Exception e) { System.out.println(e); } } else {
				 * System.err.println("---Validation failed---"); }
				 */
				break;
			}

			case 2: {
				custOpps();
				break;
			}
			case 3: {
				BClient.bDao.custRegister();
				System.out.println("Cusomer Portal providing \n1) Ojaskart.\n2) Banking.\n3) Metro");
				System.out.println("Press y or yes to enter customer portal");
				String input = scanner.next();
				if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
					custOpps();
				} else {
					System.err.println("Thankyou for be a part in ojas family.");
				}
				break;
			}
			case 4: {
				System.out.println("Thank you for using OjasKart..!");
				System.exit(1);
			}
			default: {
				System.out.println("Enter valid input..!");
			}
			}

		}
	}

//	--------------------- Admin Features---------------------

	public static void adminOpps() {
		while (true) {
			System.out.println("*****Admin*****");
			System.out.println("  1) Cart.     ");
			System.out.println("  2) Banking.  ");
			System.out.println("  3) Metro.    ");
			System.out.println("  4) Back.     ");
			System.out.println("***************");
			switch (scanner.nextInt()) {
			case 1: {
				try {
					PClient.adminProduct();
				} catch (ProductNotFoudException e) {
					System.out.println(e);
				} catch (ProductSourceNotFound e) {
					System.out.println(e);
				}
				break;
			}
			case 2: {
				try {
					BClient.adminOperators();
				} catch (SourceNotFoundException e) {
					System.out.println(e);
				}
				break;
			}
			case 3: {
				try {
					MClient.adminMetro();
				} catch (RoutesNotFound e) {
					System.out.println(e);
				}
				break;
			}
			case 4: {
				main(null);
				break;
			}
			}
		}
	}

//	--------------------- Customer Features---------------------

	public static void custOpps() {
		while (true) {
			System.out.println("*****Customer Portal*****");
			System.out.println("     1) OjasKart.		 ");
			System.out.println("     2) Banking.		 ");
			System.out.println("     3) Metro.			 ");
			System.out.println("     4) Back.			 ");
			System.out.println("**************************");
			switch (scanner.nextInt()) {
			case 1: {
				try {
					PClient.custProduct();
				} catch (CusomerExceptions | InvalidCredentials | ProductNotFoudException | ProductSourceNotFound
						| PaymentFailed e) {
					System.out.println(e);
				}
				break;
			}
			case 2: {
				BClient.custFeatures();
				break;
			}
			case 3: {
				try {
					try {
						MClient.custMetro();
					} catch (RoutesNotFound e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (com.mypay.banking.bankexception.SourceNotFoundException e) {
					System.out.println(e);
				} catch (MetroBookingFailed e) {
					System.out.println(e);
				} catch (BankingInsufficientFunds e) {
					System.out.println(e);
				} catch (InvalidCredentials e) {
					System.out.println(e);
				}
				break;
			}
			case 4: {
				BClient.bDao.logout();
				main(null);
				break;
			}
			}
		}
	}

}
