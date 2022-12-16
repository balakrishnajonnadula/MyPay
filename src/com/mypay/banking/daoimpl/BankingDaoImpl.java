package com.mypay.banking.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mypay.banking.dto.Customer;
import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.document.DocumentGen;
import com.mypay.emil.MyEmail;
import com.mypay.queries.BankingQueries;
import com.mypay.regex.RegExVal;
import com.mypay.banking.dao.BankingDao;

public class BankingDaoImpl implements BankingDao {
	List<Customer> customers = new ArrayList<>();
	Scanner scanner = new Scanner(System.in);
	public static String uName;
	public static String pass;
	static String key = "OJAS";
	static long accNum = 2022;

	static String cust = "CUS";
	static int custInc = 100;

	Connection connection = null;
	PreparedStatement pStatement = null;
	ResultSet resultSet = null;

	@Override
	public void custRegister() {
		System.out.println("Enter customer First name");
		String firstName = scanner.next();
		System.out.println("Enter customer Last name");
		String lastName = scanner.next();

		System.out.println("Enter customer phone");
		String phone = "";
		while (true) {
			String phoneNum = scanner.next();
			boolean phoneVal = RegExVal.phoneValidation(phoneNum);
			if (phoneVal) {
				phone = phoneNum;
				break;
			} else {
				System.out.println("Enter valid phone number..!");
			}
		}

		System.out.println("Enter customer email");
		String email = "";
		while (true) {
			String emailVal = scanner.next();
			boolean phoneVal = RegExVal.emailValidation(emailVal);
			if (phoneVal) {
				email = emailVal;
				break;
			} else {
				System.out.println("Enter valid Email Id");
			}
		}

		System.out.println("Enter customer address");
		String address = scanner.next();

		System.out.println("Enter opening Balence");
		double openingBal = 0;
		while (true) {
			double openingBalence = scanner.nextDouble();
			if (openingBalence >= 0) {
				openingBal = openingBalence;
				break;
			} else {
				System.out.println("Enter opening balence 0 or more than zero..!");
			}
		}
		System.out.println("Enter customer user name");
		String userName = scanner.next();
		System.out.println("Enter customer Password");
		String password = scanner.next();
		long accountNum = BankingDaoImpl.accNum;
		String accountNumber = key + accountNum;
		int num = BankingDaoImpl.custInc;
		String cutomerId = cust + num;
		BankingDaoImpl.accNum++;
		BankingDaoImpl.custInc++;
		Customer cus = new Customer(cutomerId, accountNumber, firstName, lastName, phone, address, openingBal, userName,
				password, email);
//		customers.add(cus);
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.ADD_CUSTOMER);
			pStatement.setString(1, cus.getCustoemrId());
			pStatement.setString(2, cus.getAccountNum());
			pStatement.setString(3, cus.getFirstName());
			pStatement.setString(4, cus.getLastName());
			pStatement.setString(5, cus.getPhone());
			pStatement.setString(6, cus.getEmail());
			pStatement.setString(7, cus.getAddress());
			pStatement.setDouble(8, cus.getBalence());
			pStatement.setString(9, cus.getUserName());
			pStatement.setString(10, cus.getPassword());
			int executeUpdate = pStatement.executeUpdate();
			if (executeUpdate != 0) {
				String custDetail = "Name : " + firstName + lastName + "\nAccount NO : " + accountNum + "\nPhone : "
						+ phone + "\nAddress : " + address + "\nopening bal : " + openingBal + "\nusername : "
						+ userName + "\npassword : " + password + "\nEmail : " + email;
				String fileName = "src/com/mypay/banking/custdetails/" + firstName + ".pdf";
				String subject = "MyBanking..!";
				DocumentGen.custReg(custDetail, fileName);
				MyEmail.main(fileName, email, subject);
				System.out.println(firstName + " added successfully and details sent to respected email..!");
			} else {
				System.out.println("Customer not added..");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Customer viewBal() {

		Customer customer = null;

		if (uName == null && pass == null) {
			login();
		}
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.VIEW_CUSTOMER_USING_UNAME_AND_PASSWORD);
			pStatement.setString(1, BankingDaoImpl.uName);
			pStatement.setString(2, BankingDaoImpl.pass);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String customerId = resultSet.getString(1);
				String accoutNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String phone = resultSet.getString(5);
				String email = resultSet.getString(6);
				String address = resultSet.getString(7);
				double balance = resultSet.getDouble(8);
				String userName = resultSet.getString(9);
				String password = resultSet.getString(10);

				customer = new Customer(customerId, accoutNumber, firstName, lastName, phone, address, balance,
						userName, password, email);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (!customers.isEmpty()) { for (Customer us : customers) { if
		 * (us.getUserName().equals(uName) && us.getPassword().equals(pass)) { return
		 * us; } } }
		 */
		return customer;
	}

	@Override
	public boolean withDraw() {
		if (uName == null && pass == null) {
			login();
		}

		double balance = 0;

		try {
			connection = ConncetionUtilities.getConnection();

			pStatement = connection.prepareStatement(BankingQueries.VIEW_BALANCE);
			pStatement.setString(1, BankingDaoImpl.uName);
			pStatement.setString(2, BankingDaoImpl.pass);
			resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				balance = resultSet.getDouble(1);
			}

			System.out.println("Enter amount.");
			double amt = scanner.nextDouble();
			if (amt > 0 && amt <= balance) {
				pStatement = connection.prepareStatement(BankingQueries.WITHDRAW_AMOUNT);
				pStatement.setDouble(1, amt);
				pStatement.setString(2, BankingDaoImpl.uName);
				pStatement.setString(3, BankingDaoImpl.pass);

				int executeUpdate = pStatement.executeUpdate();
				if (executeUpdate != 0) {
					return true;
				}
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (!customers.isEmpty()) { for (Customer bank : customers) { if
		 * (bank.getUserName().equals(uName) && bank.getPassword().equals(pass)) {
		 * System.out.println("Enter amount."); double amt = scanner.nextDouble(); if
		 * (amt > 0 && amt <= bank.getBalence()) { bank.setBalence(bank.getBalence() -
		 * amt); return true; } } } }
		 */
		return false;
	}

	@Override
	public boolean diposit() {
		if (uName == null && pass == null) {
			login();
		}

		try {
			connection = ConncetionUtilities.getConnection();
			System.out.println("Enter amount.");
			double amt = scanner.nextDouble();
			if (amt > 0) {
				pStatement = connection.prepareStatement(BankingQueries.DIPOSIT_AMOUNT);
				pStatement.setDouble(1, amt);
				pStatement.setString(2, BankingDaoImpl.uName);
				pStatement.setString(3, BankingDaoImpl.pass);
				int executeUpdate = pStatement.executeUpdate();
				if (executeUpdate != 0) {
					return true;
				}
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (!customers.isEmpty()) { for (Customer bank : customers) { if
		 * (bank.getUserName().equals(uName) && bank.getPassword().equals(pass)) {
		 * System.out.println("Enter amount."); double amt = scanner.nextDouble(); if
		 * (amt > 0) { bank.setBalence(bank.getBalence() + amt); return true; } } } }
		 */
		return false;
	}

	@Override
	public boolean fundTransfer() {
		if (uName == null && pass == null) {
			login();
		}
		String cusAccNum = null;
		double firstAmt = 0;
		double secondAmount = 0;

		try {
			connection = ConncetionUtilities.getConnection();
			System.out.println("Enter to Account Number.");
			String accoNum = scanner.next();
			pStatement = connection.prepareStatement(BankingQueries.GET_ACCOUTNT_NUMBER);
			pStatement.setString(1, BankingDaoImpl.uName);
			pStatement.setString(2, BankingDaoImpl.pass);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				cusAccNum = resultSet.getString(1);
			}
			if (!cusAccNum.equals(accoNum)) {
				pStatement = connection.prepareStatement(BankingQueries.VIEW_BALANCE);
				pStatement.setString(1, BankingDaoImpl.uName);
				pStatement.setString(2, BankingDaoImpl.pass);
				resultSet = pStatement.executeQuery();
				while (resultSet.next()) {
					firstAmt = resultSet.getDouble(1);
				}
				System.out.println(firstAmt);

				pStatement = connection.prepareStatement(BankingQueries.VIEW_BALANCE_USING_ACCOUNT_NUM);
				pStatement.setString(1, accoNum);
				resultSet = pStatement.executeQuery();
				while (resultSet.next()) {
					secondAmount = resultSet.getDouble(1);
				}
				System.out.println(secondAmount);
				if (secondAmount != 0) {

					System.out.println("Enter amount");
					double amt = scanner.nextDouble();
					if (amt > 0 && amt <= firstAmt) {
						pStatement = connection.prepareStatement(BankingQueries.FUND_TRANSFER_CREDIT);
						pStatement.setDouble(1, amt);
						pStatement.setString(2, accoNum);
						int one = pStatement.executeUpdate();

						pStatement = connection.prepareStatement(BankingQueries.WITHDRAW_AMOUNT);
						pStatement.setDouble(1, amt);
						pStatement.setString(2, BankingDaoImpl.uName);
						pStatement.setString(3, BankingDaoImpl.pass);
						int two = pStatement.executeUpdate();

						if (one != 0 && two != 0) {
							return true;
						}
					}

				}
			} else {
				System.out.println("Sorry we don't process to your own account");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * if (!customers.isEmpty()) { for (Customer bank : customers) { if
		 * (bank.getUserName().equals(uName) && bank.getPassword().equals(pass)) {
		 * System.out.println("Enter to Account Number."); String accoNum =
		 * scanner.next(); List<Customer> viewAllCusomers = viewAllCusomers(); if
		 * (viewAllCusomers != null) { for (Customer cus : viewAllCusomers) { if
		 * (cus.getAccountNum().equals(accoNum)) { if
		 * (bank.getAccountNum().equals(accoNum)) {
		 * System.out.println("Sorry we don't process to your own account"); } else {
		 * System.out.println("Enter amount"); double amt = scanner.nextDouble(); if
		 * (amt > 0 && amt <= bank.getBalence()) { bank.setBalence(bank.getBalence() -
		 * amt); cus.setBalence(cus.getBalence() + amt); return true; } } } } } } } }
		 */

		return false;
	}

	@Override
	public Customer viewMyDetails() {

		Customer customer = null;

		if (uName == null && pass == null) {
			login();
		}
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.VIEW_CUSTOMER_USING_UNAME_AND_PASSWORD);
			pStatement.setString(1, BankingDaoImpl.uName);
			pStatement.setString(2, BankingDaoImpl.pass);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String customerId = resultSet.getString(1);
				String accoutNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String phone = resultSet.getString(5);
				String email = resultSet.getString(6);
				String address = resultSet.getString(7);
				double balance = resultSet.getDouble(8);
				String userName = resultSet.getString(9);
				String password = resultSet.getString(10);

				customer = new Customer(customerId, accoutNumber, firstName, lastName, phone, address, balance,
						userName, password, email);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		Customer customer = new Customer(cust, cust, uName, uName, cust, key, accNum, uName, pass, cust);
		/*
		 * if (!customers.isEmpty()) { for (Customer us : customers) { if
		 * (us.getUserName().equals(uName) && us.getPassword().equals(pass)) { return
		 * us; } } }
		 */
		return customer;
	}

	@Override
	public void delecteCust(String accountNum) {

		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.DELETE_CUSTOMER);
			pStatement.setString(1, accountNum);
			int executeUpdate = pStatement.executeUpdate();
			if (executeUpdate != 0) {
				System.out.println("Customer deleted..!");
				logout();
			} else {
				System.out.println("Customer not delted..!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * int k = 0; if (!customers.isEmpty()) { for (Customer us : customers) { if
		 * (us.getAccountNum().equals(accountNum)) { k++; customers.remove(us); break; }
		 * } if (k > 0) { logout();
		 * System.out.println("Customer successfully deleted."); } else {
		 * System.out.println("Customer not found..!"); } }
		 */
	}

	@Override
	public void updateCustData(String accNum) {

		try {
			System.out.println("update firstname");
			String firstName = scanner.next();
			System.out.println("update lastname");
			String lastName = scanner.next();
			System.out.println("Enter customer phone");
			String phone = "";
			while (true) {
				String phoneNum = scanner.next();
				boolean phoneVal = RegExVal.phoneValidation(phoneNum);
				if (phoneVal) {
					phone = phoneNum;
					break;
				} else {
					System.out.println("Enter valid phone number..!");
				}
			}

			System.out.println("Enter customer email");
			String email = "";
			while (true) {
				String emailVal = scanner.next();
				boolean phoneVal = RegExVal.emailValidation(emailVal);
				if (phoneVal) {
					email = emailVal;
					break;
				} else {
					System.out.println("Enter valid Email Id");
				}
			}
			System.out.println("update address");
			String address = scanner.next();

			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.UPDATE_CUSTOMER);
			pStatement.setString(1, firstName);
			pStatement.setString(2, lastName);
			pStatement.setString(3, phone);
			pStatement.setString(4, address);
			pStatement.setString(5, email);
			pStatement.setString(6, accNum);
			int executeUpdate = pStatement.executeUpdate();
			if (executeUpdate != 0) {
				System.out.println("Details updated successfully..!");
			} else {
				System.out.println("Details not updated..!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (!customers.isEmpty()) { for (Customer cus : customers) { if
		 * (cus.getAccountNum().equals(accNum)) {
		 * System.out.println("update firstname"); cus.setFirstName(scanner.next());
		 * System.out.println("update lastname"); cus.setLastName(scanner.next());
		 * System.out.println("update phone"); cus.setPhone(scanner.next());
		 * System.out.println("update address"); cus.setAddress(scanner.next());
		 * System.out.println("update email"); cus.setEmail(scanner.next());
		 * System.out.println("Details updated successfully..!"); } } }
		 */
	}

	@Override
	public List<Customer> viewAllCusomers() {

		List<Customer> cList = new ArrayList<Customer>();

		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.VIEW_ALL_CUSTOMERS);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String customerId = resultSet.getString(1);
				String accoutNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String phone = resultSet.getString(5);
				String email = resultSet.getString(6);
				String address = resultSet.getString(7);
				double balance = resultSet.getDouble(8);
				String userName = resultSet.getString(9);
				String password = resultSet.getString(10);

				Customer customer = new Customer(customerId, accoutNumber, firstName, lastName, phone, address, balance,
						userName, password, email);
				cList.add(customer);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			;
		}
		/*
		 * if (!customers.isEmpty()) { return customers; }
		 */
		return cList;
	}

	@Override
	public void login() {
		System.out.println("Enter user name.");
		String name = scanner.next();
		System.out.println("Enter user password.");
		String pass = scanner.next();

		String customerId = null;

		try {

			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.GET_CUSTOMER_ID);
			pStatement.setString(1, name);
			pStatement.setString(2, pass);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				customerId = resultSet.getString(1);
			}
//			System.out.println(customerId);
			if (customerId != null) {
				pStatement = connection.prepareStatement(BankingQueries.GET_USER_AND_PASSWORD);
				pStatement.setString(1, customerId);
				resultSet = pStatement.executeQuery();
				while (resultSet.next()) {
					BankingDaoImpl.uName = resultSet.getString(1);
					BankingDaoImpl.pass = resultSet.getString(2);
				}
			} else {
				System.out.println("Enter valid credentials..!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		/*
		 * if (!customers.isEmpty()) { for (Customer us : customers) { if
		 * (us.getUserName().equals(name) && us.getPassword().equals(pass)) {
		 * BankingDaoImpl.uName = name; BankingDaoImpl.pass = pass; } } }
		 */
	}

	public void logout() {
		uName = null;
		pass = null;
	}

	@Override
	public Customer viewCustomer(String accountNum) {

		Customer customer = null;

		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(BankingQueries.VIEW_CUSTOMER);
			pStatement.setString(1, accountNum);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String customerId = resultSet.getString(1);
				String accoutNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String phone = resultSet.getString(5);
				String email = resultSet.getString(6);
				String address = resultSet.getString(7);
				double balance = resultSet.getDouble(8);
				String userName = resultSet.getString(9);
				String password = resultSet.getString(10);

				customer = new Customer(customerId, accoutNumber, firstName, lastName, phone, address, balance,
						userName, password, email);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (customers != null) { for (Customer us : customers) { if
		 * (us.getAccountNum().equals(accountNUm)) { return us; } } }
		 */
		return customer;
	}

}
