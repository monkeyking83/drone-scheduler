package com.walmart.drone.order;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderDeliverySchedule {

	private OrderData orderData;
	private Optional<LocalDateTime> departureTime;
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

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
