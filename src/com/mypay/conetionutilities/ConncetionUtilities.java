package com.mypay.conetionutilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mypay.dbproperties.DbProperties;

public class ConncetionUtilities {

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DbProperties.driver);
		Connection connection = DriverManager.getConnection(DbProperties.url, DbProperties.userName,
				DbProperties.password);
		return connection;
	}

	public static void closeConnection(Connection con, PreparedStatement pre) throws SQLException {
		if (con != null) {
			con.close();
		}
		if (pre != null) {
			pre.close();
		}
	}

	public static void closeConnection(Connection con, PreparedStatement pre, ResultSet rset) throws SQLException {
		if (con != null) {
			con.close();
		}
		if (pre != null) {
			pre.close();
		}
		if (rset != null) {
			rset.close();
		}
	}
}
