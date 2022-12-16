package com.mypay.metro.dto;

public class Metro {
	private String routeId;
	private String start;
	private String end;
	private double fare;
	private String adminId;

	public Metro() {
		super();
	}

	public Metro(String routeId, String start, String end, double fare, String adminId) {
		super();
		this.routeId = routeId;
		this.start = start;
		this.end = end;
		if (fare > 0) {
			this.fare = fare;
		}
		this.adminId = adminId;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		if (fare > 0) {
			this.fare = fare;
		}
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
}
