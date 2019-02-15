package com.walmart.drone.order;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.walmart.drone.grid.GridCoordinates;

/**
 * Represents data from a single line in the input file
 * 
 * @author Christophe Santini
 *
 */
public class OrderData {

	private String orderId;
	
	private GridCoordinates coordinates;
	
	private LocalDateTime orderDate;
	
	public OrderData() {
		
	}

	
	public OrderData(String orderId, GridCoordinates coordinates, LocalDateTime orderDate) {
		this.orderId = orderId;
		this.coordinates = coordinates;
		this.orderDate = orderDate;
	}


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
