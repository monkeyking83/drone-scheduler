package com.walmart.drone.grid;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public enum GridDirection {
	
	NORTH("N", 1), 
	SOUTH("S", -1), 
	EAST("E", 1), 
	WEST("W", -1), 
	UNKNOWN(EMPTY, 0);

	private String direction;
	private int numericDirection;

	private static final Map<String, GridDirection> byDirection = Arrays.asList(GridDirection.values()).stream()
			.collect(Collectors.toMap(GridDirection::getDirection, Function.identity()));

	private GridDirection(String direction, int numericDirection) {
		this.direction = direction;
		this.numericDirection = numericDirection;
	}

	public String getDirection() {
		return direction;
	}

	public int getNumericDirection() {
		return numericDirection;
	}

	public Double calculateDistance(Double directionlessDistance) {
		return numericDirection * directionlessDistance;
	}
	
	public static GridDirection getGridDirection(String direction) {
		if (StringUtils.isEmpty(direction)) {
			return UNKNOWN;
		}

		GridDirection gridDirection = byDirection.get(direction.toUpperCase());

		return gridDirection != null ? gridDirection : UNKNOWN;

	}

}
