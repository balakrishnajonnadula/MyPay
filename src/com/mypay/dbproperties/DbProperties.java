package com.mypay.dbproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbProperties {
	public static String driver;
	public static String url;
	public static String userName;
	public static String password;

	static {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("src//com//mypay//dbproperties//db.properties"));
//			System.out.println(properties);
			driver = properties.getProperty("driver");
			url = properties.getProperty("url");
			userName = properties.getProperty("userName");
			password = properties.getProperty("password");
//			System.out.println(driver + " , " + url + " , " + userName + " , " + password);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
