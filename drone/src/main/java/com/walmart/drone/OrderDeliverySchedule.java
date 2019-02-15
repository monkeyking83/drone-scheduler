package com.walmart.drone;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderDeliverySchedule {

	private OrderData orderData;
	private Optional<LocalDateTime> departureTime;
	private LocalDateTime estimatedDeliveryDate;
	private Integer estimatedTravelTime;
	private Integer initialTimeToDelivery;
	private Double distanceFromWharehouse;

	public OrderDeliverySchedule() {
		this.departureTime = Optional.empty();
	}
	
	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrderData(OrderData orderData) {
		this.orderData = orderData;
	}

	public Optional<LocalDateTime> getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Optional<LocalDateTime> departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDateTime getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryTime) {
		this.estimatedDeliveryDate = estimatedDeliveryTime;
	}

	public Integer getEstimatedTravelTime() {
		return estimatedTravelTime;
	}

	public void setEstimatedTravelTime(Integer estimatedTravelTime) {
		this.estimatedTravelTime = estimatedTravelTime;
	}

	public Integer getInitialTimeToDelivery() {
		return initialTimeToDelivery;
	}

	public void setInitialTimeToDelivery(Integer initialTimeToDelivery) {
		this.initialTimeToDelivery = initialTimeToDelivery;
	}

	public Double getDistanceFromWharehouse() {
		return distanceFromWharehouse;
	}

	public void setDistanceFromWharehouse(Double distanceFromWharehouse) {
		this.distanceFromWharehouse = distanceFromWharehouse;
	}

	
	
	
	
}
