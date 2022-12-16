package com.mypay.metro.dao;

import java.util.List;

import com.mypay.metro.dto.Metro;

public interface MetroDao {
	public void addRoute();

	public List<Metro> viewRoutes();

	public double bookTicket(String start, String end);

	public void deleteRoute(String start, String end);

	public void updateRoute(String start, String end);
}
