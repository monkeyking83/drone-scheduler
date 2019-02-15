package com.walmart.drone;

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

import com.walmart.drone.scheduler.DroneSchedulerException;

@Component
public class OrderReader {

	Logger logger = LoggerFactory.getLogger(OrderReader.class);
	
	private static int ORDER_ID_INDEX = 0;
	private static int  DESTINATION_COORD_INDEX = 1;
	private static int ORDER_TIME_INDEX = 2;
	/**
	 * This pattern assumes that North/South directions must come first
	 */
	private static Pattern coordinatePattern = Pattern.compile("([N|S])(\\d+)([E|W])(\\d+)");
	
	
	
	public List<OrderData> readOrderData(String orderListPath) {
		File orderListFile = new File(orderListPath);
		return readOrderData(orderListFile);
	}
	
	public List<OrderData> readOrderData(File  orderListFile) {
		List<OrderData> orderDataList = new ArrayList<>();
		try {
			List<String> orderLines = Files.readAllLines(orderListFile.toPath());
			int orderLineIndex = 1;
			Optional<OrderData> orderData = Optional.empty();
			
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

	private Optional<OrderData> extractOrderData(String orderLine, int orderLineIndex) {
		
		try {
			String[] orderComponents = orderLine.split("\\s");
			
			if (orderComponents.length != 3) {
				// order format not valid
			}
			
			String orderId = orderComponents[ORDER_ID_INDEX];
			String destinationCoordinates = orderComponents[DESTINATION_COORD_INDEX];
			String orderTime = orderComponents[ORDER_TIME_INDEX];
	
			// validate order id
			
			GridCoordinates coordinates = extractGridCoordinates(destinationCoordinates);
			
			// validate time
			
			OrderData orderData = new OrderData();
			orderData.setOrderId(orderId);
			orderData.setCoordinates(coordinates);
			orderData.setOrderDate(extractOrderDate(orderTime));
			 
			return Optional.of(orderData);
		}
		catch (Exception e) {
			logger.error(String.format("Unable to schedule delivery for order %s on line %s of input file", orderLine, orderLineIndex), e);
		}
		return Optional.empty();
	}
	
	
	private LocalDateTime extractOrderDate(String orderTimeInput) {
		LocalTime orderTime = LocalTime.parse(orderTimeInput); // TODO: handle bad date format
		return LocalDateTime.now().with(orderTime);
	}
	
	private GridCoordinates extractGridCoordinates(String coordinatesString) {
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
		
		throw new DroneSchedulerException("Unable to extract direction or distance from input coordinates: " + coordinatesString);
		
	}
	
	private Double extractDistance(String distance) {
		
		if (NumberUtils.isCreatable(distance)) {
			return NumberUtils.createDouble(distance);
		}
		
		throw new DroneSchedulerException("Unable to extract distance from input: " + distance);
	}

	
	
	
}
