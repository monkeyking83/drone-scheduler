package com.walmart.drone;

import java.time.LocalDateTime;

public class OrderData {

	private String orderId;
	
	private GridCoordinates coordinates;
	
	private LocalDateTime orderDate;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public GridCoordinates getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(GridCoordinates coordinates) {
		this.coordinates = coordinates;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	
}
