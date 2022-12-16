package com.mypay.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.queries.AdminQueries;

public class AdminLogin {
	Connection connection = null;
	PreparedStatement pStatement = null;
	ResultSet resultSet = null;
	public static String adminId;

	public int adminLogin(String userName, String password) {
		int count = 0;
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(AdminQueries.GET_ADMIN);
			pStatement.setString(1, userName);
			pStatement.setString(2, password);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				count++;
				adminId = resultSet.getString(1);
			}
//			System.out.println(adminId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
