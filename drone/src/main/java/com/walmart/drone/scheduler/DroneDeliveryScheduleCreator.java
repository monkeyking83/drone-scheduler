package com.walmart.drone.scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.walmart.drone.grid.GridCoordinates;
import com.walmart.drone.order.OrderData;
import com.walmart.drone.order.OrderDeliverySchedule;

@Component
public class DroneDeliveryScheduleCreator {

	private Logger logger = LoggerFactory.getLogger(DroneDeliveryScheduleCreator.class);

	@Value("#{ T(java.time.LocalTime).parse('${scheduler.operation.start}')}")
	private LocalTime operationStartTime;

	@Value("#{T(java.time.LocalTime).parse('${scheduler.operation.end}')}")
	private LocalTime operationEndTime;

	@Value("${scheduler.npsBasedOnStart:false}")
	private boolean npsBasedOnStart;

	@Autowired
	TimeProvider timeProvider;

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

		LocalDateTime departureTime = operationStartTime.until(timeProvider.getCurrentTime(), ChronoUnit.MINUTES) > 0
				? timeProvider.getCurrentTime()
				: LocalDateTime.now().with(operationStartTime);

		LocalDateTime droneReturnTime;

		for (OrderDeliverySchedule orderSchedule : route.getDeliverySchedules()) {
			droneReturnTime = departureTime.plus(orderSchedule.getInitialTimeToDelivery() * 2l, ChronoUnit.SECONDS);

			// We can't schedule anything that would cause the drone to be flying after
			// hours
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

		Integer travelTime = orderSchedule.getEstimatedTravelTime();
		
		if (orderSchedule.getDepartureTime().isPresent()) {
			travelTime += (int)orderSchedule.getOrderData()
				.getOrderDate().until(orderSchedule.getDepartureTime().get(), ChronoUnit.SECONDS);
		}
		
		if (npsBasedOnStart) {
			long startToOrderSeconds = operationStartTime.until(orderSchedule.getOrderData().getOrderDate(),
					ChronoUnit.SECONDS);
			if (startToOrderSeconds > 0) {
				travelTime = travelTime.intValue() - (int)startToOrderSeconds;
			}
		}

		Integer travelHours = travelTime / 3600;

		return 10 - Math.min(10, travelHours);
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
		return orderTime.isBefore(timeProvider.getCurrentTime())
				? (int) orderData.getOrderDate().until(timeProvider.getCurrentTime(), ChronoUnit.SECONDS)
				: 0;
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

		return (minutes * 60) + (int)(Math.round(seconds));
	}

	private Double getDistanceFromWharehouse(OrderData orderData) {
		GridCoordinates coordinates = orderData.getCoordinates();
		return Math.sqrt(Math.pow(coordinates.getX(), 2) + Math.pow(coordinates.getY(), 2));
	}

}
