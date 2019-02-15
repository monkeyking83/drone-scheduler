package com.walmart.drone.scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.walmart.drone.DeliveryRoute;
import com.walmart.drone.GridCoordinates;
import com.walmart.drone.OrderData;
import com.walmart.drone.OrderDeliverySchedule;

@Component
public class DroneDeliveryScheduleCreator {

	private Logger logger = LoggerFactory.getLogger(DroneDeliveryScheduleCreator.class);
	
	@Value("#{ T(java.time.LocalTime).parse('${scheduler.operation.start}')}")
	private LocalTime operationStartTime;

	@Value("#{T(java.time.LocalTime).parse('${scheduler.operation.end}')}")
	private LocalTime operationEndTime;

	@Value("${scheduler.npsBasedOnStart:false}")
	private boolean npsBasedOnStart;

	public DeliveryRoute generateDeliverySchedule(List<OrderData> orderDataList) {
		DeliveryRoute route = new DeliveryRoute();
		OrderDeliverySchedule deliverySchedule;

		for (OrderData orderData : orderDataList) {
			deliverySchedule = new OrderDeliverySchedule();
			deliverySchedule.setOrderData(orderData);
			deliverySchedule.setEstimatedTravelTime(calculateTravelTime(orderData));
			deliverySchedule.setDistanceFromWharehouse(getDistanceFromWharehouse(orderData));
			deliverySchedule.setInitialTimeToDelivery(calculateInitialTimeToDelivery(orderData));
			route.addDeliverySchedule(deliverySchedule);
		}

		// order the list based on initialTimeToDelivery
		route.getDeliverySchedules().sort((OrderDeliverySchedule o1, OrderDeliverySchedule o2) -> o1
				.getInitialTimeToDelivery().compareTo(o2.getInitialTimeToDelivery()));

		LocalDateTime departureTime = operationStartTime.until(getCurrentTime(), ChronoUnit.MINUTES) > 0
				? getCurrentTime()
				: LocalDateTime.now().with(operationStartTime);
				
		LocalDateTime droneReturnTime = LocalDateTime.from(departureTime);
		
		for (OrderDeliverySchedule orderSchedule : route.getDeliverySchedules()) {
			droneReturnTime = departureTime.plus(orderSchedule.getInitialTimeToDelivery() * 2, ChronoUnit.SECONDS);
			
			// We can't schedule anything that would cause the drone to be flying after hours
			if (droneReturnTime.toLocalTime().isBefore(operationEndTime)) {
				orderSchedule.setDepartureTime(Optional.of(departureTime));
				route.getNetPromoterScore().addScore(calculateCurrentOrderScore(orderSchedule));
			} else {
				logger.info(
						"Unable to schedule order {} for delivery as it would cause the drone to fly beyond the allowed hours.",
						orderSchedule.getOrderData().getOrderId());
				route.getNetPromoterScore().addScore(0);
			}
			departureTime = LocalDateTime.from(droneReturnTime);
		}
		return route;
	}

	/**
	 * Calculates the existing NPS based on estimated travel time + the difference
	 * between max(orderTime, 06:00) and now
	 * 
	 * @param orderData
	 * @return
	 */
	private Integer calculateCurrentOrderScore(OrderDeliverySchedule orderSchedule) {

		Integer travelTime = orderSchedule.getEstimatedTravelTime() + Long.valueOf(orderSchedule.getOrderData()
				.getOrderDate().until(orderSchedule.getDepartureTime().get(), ChronoUnit.SECONDS)).intValue();

		if (npsBasedOnStart) {
			Long startToOrderSeconds = operationStartTime.until(orderSchedule.getOrderData().getOrderDate(), ChronoUnit.SECONDS);
			if (startToOrderSeconds > 0) {
				travelTime = travelTime.intValue() - Long.valueOf(startToOrderSeconds).intValue();
			}
		}

		Integer travelHours = travelTime / 3600;
		
		Integer nps = 10 - Math.min(10, travelHours);

		return nps;
	}

	
	/**
	 * Calculates the number of minutes that would have elapsed between the time the
	 * order was placed and if the order were to leave for delivery right now
	 * 
	 * @param orderData
	 * @return
	 */
	private Integer calculateInitialTimeToDelivery(OrderData orderData) {
		return (calculateTravelTime(orderData) + getElapsedSecondsSinceOrder(orderData));
	}

	/**
	 * 
	 * @param orderData
	 * @return The number of minutes since the order was placed
	 */
	public Integer getElapsedSecondsSinceOrder(OrderData orderData) {

		LocalDateTime orderTime = orderData.getOrderDate();
		return orderTime.isBefore(getCurrentTime()) ? Long.valueOf(orderData.getOrderDate().until(getCurrentTime(), ChronoUnit.SECONDS))
				.intValue() : 0;
	}

	/**
	 * 
	 * @param orderData
	 * @return The estimated travel time in seconds
	 */
	private Integer calculateTravelTime(OrderData orderData) {
		Double distance = getDistanceFromWharehouse(orderData);

		int minutes = distance.intValue();
		Double seconds = (distance % minutes) * 60;

		int travelTime = (minutes * 60) + Long.valueOf(Math.round(seconds)).intValue();

		return travelTime;
	}

	private Double getDistanceFromWharehouse(OrderData orderData) {
		GridCoordinates coordinates = orderData.getCoordinates();
		Double distance = Math.sqrt(Math.pow(coordinates.getX(), 2) + Math.pow(coordinates.getY(), 2));
		return distance;
	}

	// perhaps move this to a more widely available class
	public LocalDateTime getCurrentTime() {
		return LocalDateTime.now();
	}

}
