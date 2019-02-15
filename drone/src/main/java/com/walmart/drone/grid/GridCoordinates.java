package com.walmart.drone.grid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This represents a point on the grid. Positive values on the X axis represent
 * an Eastward direction Negative values on the X axis represent a Westward
 * direction Positive values on the Y axis represent a Northward direction
 * Negative values on the Y axis represent a Southward direction
 * 
 * 
 * @author Christophe Santini
 *
 */
public class GridCoordinates {

	private Double x;

	private Double y;

	public GridCoordinates() {

	}

	public GridCoordinates(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
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
