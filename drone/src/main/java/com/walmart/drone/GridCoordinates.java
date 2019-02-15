package com.walmart.drone;

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

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

}
