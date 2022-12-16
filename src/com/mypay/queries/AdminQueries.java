package com.mypay.queries;

public interface AdminQueries {
	public String GET_ADMIN= "select admin_id from admin where username = ? and passoword = ?;";
}	
