package com.walmart.drone.scheduler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.drone.DeliveryRoute;
import com.walmart.drone.OrderData;
import com.walmart.drone.OrderDeliverySchedule;
import com.walmart.drone.OrderReader;

@Component
public class DroneScheduler {
	
	private DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());
	
	private Logger logger = LoggerFactory.getLogger(DroneScheduler.class);
	
	private static String orderDeliveryFormat = "%s %s";
	private static String npsFormat = "NPS %s";
	
	@Autowired
	private OrderReader orderReader;
	
	@Autowired
	private DroneDeliveryScheduleCreator scheduleCreator;
	
	public String generateDeliverySchedule(String ordersPath, String outputPath) {
		
		try {
			backupExistingOutput(outputPath);
		}
		catch (Exception e) {
			logger.error("Unable to backup an existing output path", e);
		}
		
		List<OrderData> orderDataList = orderReader.readOrderData(ordersPath);
		
		DeliveryRoute route = scheduleCreator.generateDeliverySchedule(orderDataList);
		
		try {
			Path output = printRouteToFile(route, outputPath);
			return output.toAbsolutePath().toString();
		}
		catch (Exception e) {
			logger.error("Error generating output", e);
			return "Unable to generate file. See logs for more details";
		}
		
	}
	
	private void backupExistingOutput(String outputPath) throws IOException {
		Path output = Paths.get(outputPath);
		if (Files.exists(output)) {
			BasicFileAttributes outputAttributes = Files.readAttributes(output, BasicFileAttributes.class);
			FileTime fileTime  = outputAttributes.lastModifiedTime();
			Files.copy(output, Paths.get(outputPath + "-" + fileFormatter.format(fileTime.toInstant())));
		}
	}
	
	private Path printRouteToFile(DeliveryRoute route, String outputPath) throws IOException {
		
		List<String> routeSchedule = new ArrayList<>();
		
		for (OrderDeliverySchedule delivery : route.getDeliverySchedules()) {
			if (delivery.getDepartureTime().isPresent()) {
				routeSchedule.add(printSchedule(delivery));
			}
		}
		
		routeSchedule.add(String.format(npsFormat, route.getNetPromoterScore().getScore()));
		Path output = Paths.get(outputPath);
		Files.write(output, routeSchedule, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		return output;
	}
		
	private String printSchedule(OrderDeliverySchedule deliverySchedule) {
		
		String orderId = deliverySchedule.getOrderData().getOrderId();
		String orderTime = deliverySchedule.getDepartureTime().get().format(DateTimeFormatter.ISO_LOCAL_TIME);
		
		return String.format(orderDeliveryFormat, orderId, orderTime);
	}
	
}
