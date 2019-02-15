package com.walmart.drone.order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.walmart.drone.grid.GridCoordinates;
import com.walmart.drone.grid.GridDirection;
import com.walmart.drone.scheduler.DroneSchedulerException;

/**
 * Parses contents of an input file to produce a list of OrderData
 * 
 * @author Christophe Santini
 *
 */
@Component
public class OrderReader {

	Logger logger = LoggerFactory.getLogger(OrderReader.class);

	private static int orderIdIndex = 0;
	private static int destinationCoordIndex = 1;
	private static int orderTimeIndex = 2;
	private static String coordinatePatternString = "([N|S])(\\d+)([E|W])(\\d+)";
	private static String orderIdPatternString = "WM\\d{3,4}";

	/**
	 * This pattern assumes that North/South directions must come first
	 */
	private static Pattern coordinatePattern = Pattern.compile(coordinatePatternString);

	private static Pattern orderIdPattern = Pattern.compile(orderIdPatternString);

	public List<OrderData> readOrderData(String orderListPath) {
		File orderListFile = new File(orderListPath);
		return readOrderData(orderListFile);
	}

	public List<OrderData> readOrderData(File orderListFile) {
		List<OrderData> orderDataList = new ArrayList<>();
		try {
			List<String> orderLines = Files.readAllLines(orderListFile.toPath());
			int orderLineIndex = 1;
			Optional<OrderData> orderData;

			for (String orderLine : orderLines) {
				if (StringUtils.isNotEmpty(orderLine)) {
					orderData = extractOrderData(orderLine, orderLineIndex);
					orderData.ifPresent(o -> orderDataList.add(o));
				}
				orderLineIndex++;
			}

		} catch (IOException e) {
			throw new DroneSchedulerException("Unable to read from " + orderListFile.getName(), e);
		}

		return orderDataList;
	}

	Optional<OrderData> extractOrderData(String orderLine, int orderLineIndex) {

		try {
			String[] orderComponents = orderLine.split("\\s");

			if (orderComponents.length != 3) {
				throw new DroneSchedulerException(String.format("Incorrect number of arguments: %s", orderLine));
			}

			String orderId = orderComponents[orderIdIndex];
			String destinationCoordinates = orderComponents[destinationCoordIndex];
			String orderTime = orderComponents[orderTimeIndex];

			validateOrderId(orderId);

			GridCoordinates coordinates = extractGridCoordinates(destinationCoordinates);

			// validate time
			OrderData orderData = new OrderData();
			orderData.setOrderId(orderId);
			orderData.setCoordinates(coordinates);
			orderData.setOrderDate(extractOrderDate(orderTime));

			return Optional.of(orderData);
		} catch (Exception e) {
			logger.error(String.format("Unable to schedule delivery for order %s on line %s of input file", orderLine,
					orderLineIndex), e);
		}
		return Optional.empty();
	}

	LocalDateTime extractOrderDate(String orderTimeInput) {
		LocalTime orderTime = LocalTime.parse(orderTimeInput);
		return LocalDateTime.now().with(orderTime);
	}

	GridCoordinates extractGridCoordinates(String coordinatesString) {
		Matcher coordinateMatcher = coordinatePattern.matcher(coordinatesString);

		// first item should be a direction
		if (coordinateMatcher.matches()) {

			GridDirection latitude = GridDirection.getGridDirection(coordinateMatcher.group(1));
			Double latitudeDistance = extractDistance(coordinateMatcher.group(2));
			GridDirection longitude = GridDirection.getGridDirection(coordinateMatcher.group(3));
			Double longitudeDistance = extractDistance(coordinateMatcher.group(4));

			return new GridCoordinates(latitude.calculateDistance(latitudeDistance),
					longitude.calculateDistance(longitudeDistance));
		}

		throw new DroneSchedulerException(
				"Unable to extract direction or distance from input coordinates: " + coordinatesString);

	}

	Double extractDistance(String distance) {

		if (NumberUtils.isCreatable(distance)) {
			return NumberUtils.createDouble(distance);
		}

		throw new DroneSchedulerException("Unable to extract distance from input: " + distance);
	}

	void validateOrderId(String orderId) {
		if (!orderIdPattern.matcher(orderId).matches()) {
			throw new DroneSchedulerException(String.format("%s is not a valid order ID format", orderId));
		}
	}

}
