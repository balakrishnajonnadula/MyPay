package com.mypay.clients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.mypay.banking.dto.Customer;
import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.document.DocumentGen;
import com.mypay.emil.MyEmail;
import com.mypay.metro.dao.MetroDao;
import com.mypay.metro.daoimpl.MetroDaoImpl;
import com.mypay.metro.dto.Metro;
import com.mypay.metro.exception.MetroBookingFailed;
import com.mypay.metro.exception.RoutesNotFound;
import com.mypay.queries.BankingQueries;
import com.mypay.banking.bankexception.BankingInsufficientFunds;
import com.mypay.banking.bankexception.InvalidCredentials;
import com.mypay.banking.bankexception.SourceNotFoundException;
import com.mypay.banking.daoimpl.BankingDaoImpl;

public class MClient {
	static MetroDao mDao = new MetroDaoImpl();
	static Scanner scanner = new Scanner(System.in);

	static Connection connection = null;
	static PreparedStatement pStatement = null;
	static ResultSet resultSet = null;

//	--------------------- Admin Metro---------------------

	public static void adminMetro() throws RoutesNotFound {
		while (true) {
			System.out.println("******Admin Metro*****");
			System.out.println("  1) Add Route   	  ");
			System.out.println("  2) Delete Route	  ");
			System.out.println("  3) Update Fare	  ");
			System.out.println("  4) View all routes  ");
			System.out.println("  5) Back			  ");
			System.out.println("**********************");
			switch (scanner.nextInt()) {
			case 1: {
				mDao.addRoute();
				break;
			}
			case 2: {
				System.out.println("Enter From station");
				String start = scanner.next();
				System.out.println("Enter to station");
				String end = scanner.next();
				mDao.deleteRoute(start, end);
				break;
			}
			case 3: {
				System.out.println("Enter From station");
				String start = scanner.next();
				System.out.println("Enter to station");
				String end = scanner.next();
				mDao.updateRoute(start, end);
				break;
			}
			case 4: {
				List<Metro> viewRoutes = mDao.viewRoutes();
				if (viewRoutes != null) {
					System.out.println("--------------------Welcome to Hyderabad MyMetro------------------");
					System.out.format("%-20s%-20s%-20s%-20s\n", "Route Id", "From ", "To ", "Fare");
					System.out.format("%-20s%-20s%-20s%-20s\n", "-----------------", "-----------------", "-----------",
							"-----------");
					for (Metro route : viewRoutes) {
						System.out.format("%-20s%-20s%-20s%-20s\n", route.getRouteId(), route.getStart(),
								route.getEnd(), route.getFare() + " ₹");
					}
				} else {
					throw new RoutesNotFound("Routes not found..!");
				}
				break;
			}
			case 5: {

				Client.adminOpps();

				break;
			}
			default: {
				System.err.println("Enter valid input..!");
				break;
			}
			}
		}
	}

//	--------------------- Customer Metro---------------------

	public static void custMetro()
			throws SourceNotFoundException, MetroBookingFailed, BankingInsufficientFunds, InvalidCredentials, RoutesNotFound {
		while (true) {
			System.out.println("*****Customer Metro*****");
			System.out.println("	1)View All Routes");
			System.out.println("	2) Book ticket		");
			System.out.println("	3) Back				");
			System.out.println("*************************");
			switch (scanner.nextInt()) {
			case 1: {
				List<Metro> viewRoutes = mDao.viewRoutes();
				if (viewRoutes != null) {
					System.out.println("--------------------Welcome to Hyderabad MyMetro------------------");
					System.out.format("%-20s%-20s%-20s%-20s\n", "Route Id", "From ", "To ", "Fare");
					System.out.format("%-20s%-20s%-20s%-20s\n", "-----------------", "-----------------", "-----------",
							"-----------");
					for (Metro route : viewRoutes) {
						System.out.format("%-20s%-20s%-20s%-20s\n", route.getRouteId(), route.getStart(),
								route.getEnd(), route.getFare() + " ₹");
					}
				} else {
					throw new RoutesNotFound("Routes not found..!");
				}
				break;
			}
			case 2: {
				System.out.println("Enter From station");
				String start = scanner.next();
				System.out.println("Enter to station");
				String end = scanner.next();
				double bookTicket = mDao.bookTicket(start, end);
				if (BankingDaoImpl.uName == null && BankingDaoImpl.pass == null) {
					BClient.bDao.login();
				}

				if (BankingDaoImpl.uName != null && BankingDaoImpl.pass != null) {
					Customer cus = BClient.bDao.viewMyDetails();
//					System.out.println(bookTicket);
//					System.out.println(cus.getBalence());

					if (bookTicket > 0 && bookTicket <= cus.getBalence()) {
						System.out.println("Press y or yes to debit amount from your bank account.");
						String input = scanner.next();
						if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
							try {
								connection = ConncetionUtilities.getConnection();
								pStatement = connection.prepareStatement(BankingQueries.WITHDRAW_AMOUNT);
								pStatement.setDouble(1, bookTicket);
								pStatement.setString(2, BankingDaoImpl.uName);
								pStatement.setString(3, BankingDaoImpl.pass);
								int executeUpdate = pStatement.executeUpdate();
								if (executeUpdate != 0) {
									String cusDetails = "Name : " + cus.getFirstName() + " " + cus.getLastName()
											+ "\nAddress : " + cus.getAddress() + "\nPhone : " + cus.getPhone()
											+ "\nGmail : " + cus.getEmail();
									String stations = "From  : " + start.toUpperCase() + " To : " + end.toUpperCase();
									String totalFare = "Toatal Fare : " + bookTicket;
									String path = "src/com/mypay/metro/metroticket/" + cus.getFirstName() + ".pdf";
									String subject = "MyMetro..!";
									DocumentGen.metroTicket(cusDetails, stations, totalFare, path);
									MyEmail.main(path, cus.getEmail(), subject);
									System.out.println("Booking conformed and mail sent to respected email..!");
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
//							cus.setBalence(cus.getBalence() - bookTicket);

						} else {
							throw new MetroBookingFailed("Booking Failed..!");
						}
					} else {
						throw new BankingInsufficientFunds("Enter valid sation points..!");
					}
				} else {
					throw new InvalidCredentials("Invalid Credentials");
				}
				break;
			}
			case 3: {
				Client.custOpps();
				break;
			}
			default: {
				System.err.println("Enter valid input");
				break;
			}
			}
		}

	}

}
