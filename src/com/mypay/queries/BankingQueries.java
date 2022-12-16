package com.mypay.queries;

public interface BankingQueries {

	public String ADD_CUSTOMER = "insert into customer values(?,?,?,?,?,?,?,?,?,?);";

	public String GET_CUSTOMER_ID = "select customer_id from customer where username = ? and password = ?;";

	public String GET_USER_AND_PASSWORD = "select username,password from customer where  customer_id = ?;";

	public String VIEW_ALL_CUSTOMERS = "SELECT * FROM customer;";

	public String DELETE_CUSTOMER = "delete from  customer where account_number = ?;";

	public String VIEW_CUSTOMER = "select * from customer where account_number = ?;";

	public String UPDATE_CUSTOMER = "update customer set firstname = ?, lastname = ?, phone = ?, address = ?, email = ? where account_number = ?;";

	public String GET_ACCOUTNT_NUMBER = "SELECT account_number from customer where username = ? and password = ?;";

	public String VIEW_CUSTOMER_USING_UNAME_AND_PASSWORD = "SELECT * FROM customer where username = ? and password = ?;";

	public String VIEW_BALANCE = "SELECT balance from customer where username = ? and password = ?;";

	public String VIEW_BALANCE_USING_ACCOUNT_NUM = "SELECT balance from customer where account_number = ?;";

	public String WITHDRAW_AMOUNT = "update customer set balance = (balance - ?) where username = ? and password = ?;";

	public String DIPOSIT_AMOUNT = "update customer set balance = (balance + ?) where username = ? and password = ?;";

	public String FUND_TRANSFER_CREDIT = "update customer set balance = (balance + ?) where account_number = ?;";

}
