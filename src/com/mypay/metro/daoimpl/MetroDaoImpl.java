package com.mypay.metro.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mypay.admin.AdminLogin;
import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.metro.dao.MetroDao;
import com.mypay.metro.dto.Metro;
import com.mypay.queries.MetroQueries;

public class MetroDaoImpl implements MetroDao {
	static int routeNum = 1;
	static String routeId = "M";
	List<Metro> routes = new ArrayList<Metro>();
	Scanner scanner = new Scanner(System.in);

	Connection connection = null;
	ResultSet resultSet = null;
	PreparedStatement pStatement = null;

//	-----------Add Route---------------
	@Override
	public void addRoute() {
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(MetroQueries.ADD_ROUTE);
			System.out.println("Enter start station.");
			String start = scanner.next();
			System.out.println("Enter end station");
			String end = scanner.next();
			System.out.println("Enter fare.");
			double fare = scanner.nextDouble();
			Metro route = new Metro((routeId + routeNum), start, end, fare, AdminLogin.adminId);
//			System.out.println(routeNum);
			MetroDaoImpl.routeNum++;
//			System.out.println(routeNum);
			pStatement.setString(1, route.getRouteId());
			pStatement.setString(2, route.getStart());
			pStatement.setString(3, route.getEnd());
			pStatement.setDouble(4, route.getFare());
			pStatement.setString(5, route.getAdminId());
			pStatement.addBatch();

			Metro route1 = new Metro((routeId + routeNum), start, end, fare, AdminLogin.adminId);
			pStatement.setString(1, route1.getRouteId());
			pStatement.setString(2, route1.getEnd());
			pStatement.setString(3, route1.getStart());
			pStatement.setDouble(4, route1.getFare());
			pStatement.setString(5, route1.getAdminId());
			pStatement.addBatch();
			int[] executeBatch = pStatement.executeBatch();
			if (executeBatch != null) {
				System.out.println("Route added successfully..!");
			} else {
				System.out.println("Route not added..!");
			}
			++routeNum;

//			routes.add(route);
//			routes.add(route1);
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
		 * System.out.println("Enter start station."); String start = scanner.next();
		 * System.out.println("Enter end station"); String end = scanner.next();
		 * System.out.println("Enter fare."); double fare = scanner.nextDouble(); Metro
		 * route = new Metro(start, end, fare, AdminLogin.adminId); Metro route1 = new
		 * Metro(end, start, fare, AdminLogin.adminId); routes.add(route);
		 * routes.add(route1); System.out.println("Route added successfully..!");
		 */
	}

//	--------------View Specific Route-----------
	@Override
	public List<Metro> viewRoutes() {
		List<Metro> metros = new ArrayList<Metro>();
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(MetroQueries.VIEWALL_ROUTES);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String routeId = resultSet.getString(1);
				String formStation = resultSet.getString(2);
				String toStation = resultSet.getString(3);
				double fare = resultSet.getDouble(4);
				String adminId = resultSet.getString(5);

				Metro metro = new Metro(routeId, formStation, toStation, fare, adminId);
				metros.add(metro);
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

		return metros;
		/*
		 * if (!routes.isEmpty()) { return routes; } return null;
		 */
	}

//	--------------Book Ticket-----------
	@Override
	public double bookTicket(String start, String end) {

		double fare = 0;
		String routeId = null;

		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(MetroQueries.GET_FARE_AND_ROUTEID);
			pStatement.setString(1, start);
			pStatement.setString(2, end);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				fare = resultSet.getDouble(1);
				routeId = resultSet.getString(2);
			}
			if (routeId != null && fare != 0) {
				System.out.println("1) Single trip");
				System.out.println("2) Double trip");
				switch (scanner.nextInt()) {
				case 1: {
					System.out.println("From : " + start + "\t To : " + end);
					System.out.println("Total fare is : " + fare + " ₹");
					return fare;
				}
				case 2: {
					System.out.println("From : " + start + "\t To : " + end);
					double disc = (10.0 / 100.0) * (fare * 2);
					System.out.println("Total fare is : " + (fare * 2) + " ₹");
					System.out.println("Discount  10 % : " + disc + " ₹");
					System.out.println("Net payable : " + ((fare * 2) - disc) + " ₹");
					return ((fare * 2) - disc);
				}
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
		 * if (!routes.isEmpty()) { for (Metro route : routes) { if
		 * (route.getStart().equalsIgnoreCase(start) &&
		 * route.getEnd().equalsIgnoreCase(end)) { k++;
		 * System.out.println("1) Single trip"); System.out.println("2) Double trip");
		 * switch (scanner.nextInt()) { case 1: { System.out.println("From : " +
		 * route.getStart() + "\t To : " + route.getEnd());
		 * System.out.println("Total fare is : " + route.getFare() + " ₹"); return
		 * route.getFare(); } case 2: { System.out.println("From : " + route.getStart()
		 * + "\t To : " + route.getEnd()); double disc = (10.0 / 100.0) *
		 * (route.getFare() * 2); System.out.println("Total fare is : " +
		 * (route.getFare() * 2) + " ₹"); System.out.println("Discount  10 % : " + disc
		 * + " ₹"); System.out.println("Net payable : " + ((route.getFare() * 2) - disc)
		 * + " ₹"); return ((route.getFare() * 2) - disc); } } } } } if (k == 0) {
		 * System.err.println("data not found..!"); }
		 */
		return 0;
	}

//	----------------Delete Route------------------
	@Override
	public void deleteRoute(String start, String end) {
		String routeIdOne = null;
		String routeIdTwo = null;

		try {
			connection = ConncetionUtilities.getConnection();

			pStatement = connection.prepareStatement(MetroQueries.GET_METRO_ROUTE_ID);
			pStatement.setString(1, start);
			pStatement.setString(2, end);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				routeIdOne = resultSet.getString(1);
			}

			pStatement = connection.prepareStatement(MetroQueries.DELETE_ROUTE);
			pStatement.setString(1, routeIdOne);
			int one = pStatement.executeUpdate();

			pStatement = connection.prepareStatement(MetroQueries.GET_METRO_ROUTE_ID);
			pStatement.setString(1, end);
			pStatement.setString(2, start);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				routeIdTwo = resultSet.getString(1);
			}

			pStatement = connection.prepareStatement(MetroQueries.DELETE_ROUTE);
			pStatement.setString(1, routeIdTwo);
			int two = pStatement.executeUpdate();

			if (one != 0 && two != 0) {
				System.out.println("Route deleted..!");
			} else {
				System.out.println("Route not deleted..!");
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
		 * int k = 0; if (!routes.isEmpty()) { for (Metro route : routes) { if
		 * (route.getStart().equalsIgnoreCase(start) &&
		 * route.getEnd().equalsIgnoreCase(end)) { k++; routes.remove(route); break; } }
		 * for (Metro route : routes) { if (route.getStart().equalsIgnoreCase(end) &&
		 * route.getEnd().equalsIgnoreCase(start)) { k++; routes.remove(route);
		 * System.out.println("route successfully removed..!"); break; } } } if (k == 0)
		 * { System.out.println("data not found..!"); }
		 */ }

//	----------------Update Route------------------
	@Override
	public void updateRoute(String start, String end) {
		String routeIdOne = null;
		String routeIdTwo = null;

		try {
			connection = ConncetionUtilities.getConnection();

			pStatement = connection.prepareStatement(MetroQueries.GET_METRO_ROUTE_ID);
			pStatement.setString(1, start);
			pStatement.setString(2, end);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				routeIdOne = resultSet.getString(1);
			}
//			System.out.println(routeIdOne);

			pStatement = connection.prepareStatement(MetroQueries.UPDATE_FARE);
			System.out.println("Enter Fare");
			float amt = scanner.nextFloat();
			if (amt > 0) {
				pStatement.setFloat(1, amt);
				pStatement.setString(2, routeIdOne);
			}
			int one = pStatement.executeUpdate();

			pStatement = connection.prepareStatement(MetroQueries.GET_METRO_ROUTE_ID);
			pStatement.setString(1, end);
			pStatement.setString(2, start);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				routeIdTwo = resultSet.getString(1);
			}
//			System.out.println(routeIdTwo);
			pStatement = connection.prepareStatement(MetroQueries.UPDATE_FARE);
			if (amt > 0) {
				pStatement.setFloat(1, amt);
				pStatement.setString(2, routeIdTwo);
			}
			int two = pStatement.executeUpdate();
			if (one != 0 && two != 0) {
				System.out.println("Fare updated successfull..!");
			} else {
				System.out.println("Fare not updated..!");
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
		 * int k = 0; if (!routes.isEmpty()) { double fare = 0; for (Metro route :
		 * routes) { if (route.getStart().equalsIgnoreCase(start) &&
		 * route.getEnd().equalsIgnoreCase(end)) { k++;
		 * System.out.println("Enter fare"); fare = scanner.nextDouble();
		 * route.setFare(fare); } } for (Metro route : routes) { if
		 * (route.getStart().equalsIgnoreCase(end) &&
		 * route.getEnd().equalsIgnoreCase(start)) { k++; if (fare > 0) {
		 * route.setFare(fare); System.out.println("Fare updated success fully..!"); } }
		 * } } if (k == 0) { System.out.println("data not found..!"); }
		 */

	}
}
