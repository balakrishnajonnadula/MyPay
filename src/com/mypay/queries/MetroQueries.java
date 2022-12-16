package com.mypay.queries;

public interface MetroQueries {

	public String ADD_ROUTE = "insert into metro values(?,?,?,?,?);";

	public String VIEWALL_ROUTES = "SELECT * FROM metro;";

	public String GET_METRO_ROUTE_ID = "select metro_id from metro where from_station = ? AND to_station = ?";

	public String DELETE_ROUTE = "delete from metro where metro_id = ?";

	public String UPDATE_FARE = "update metro set  fare =? where metro_id =?;";

	public String GET_FARE_AND_ROUTEID = "select fare,metro_id from metro where from_station = ? and to_station = ?;";

}
