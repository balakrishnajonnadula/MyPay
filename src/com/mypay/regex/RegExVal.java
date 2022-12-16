package com.mypay.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExVal {

//	 Email validation
	public static boolean emailValidation(String email) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9.]+@gmail[.]com");
		Matcher matcher = pattern.matcher(email);
		if (matcher.find() && matcher.group().equals(email)) {
			return true;
		}
		return false;
	}

//	 phone validation
	public static boolean phoneValidation(String phone) {
		Pattern pattern = Pattern.compile("(0|91)?[6-9][0-9]{9}");
		Matcher matcher = pattern.matcher(phone);
		if (matcher.find() && matcher.group().equals(phone)) {
			return true;
		}
		return false;
	}

}
