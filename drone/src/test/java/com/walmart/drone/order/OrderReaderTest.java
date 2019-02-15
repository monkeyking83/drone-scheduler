package com.walmart.drone.order;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.walmart.drone.grid.GridCoordinates;
import com.walmart.drone.scheduler.DroneSchedulerException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderReaderTest {

	@Autowired
	OrderReader orderReader;
	
	@Value("classpath:challenge_input.txt")
	Resource challengeInput;

	
	@Value("classpath:some_invalid_input.txt")
	Resource someInvalidInput;

	@Value("classpath:empty_input.txt")
	Resource emptyInput;
	
	
	@Test
	public void testChallengeInput() throws Exception {
		List<OrderData> orderDataList = orderReader.readOrderData(challengeInput.getFile());
		
		assertEquals(4, orderDataList.size());
		assertEquals(generateOrderData("WM001", 11, -5, "05:11:50"), orderDataList.get(0));
		assertEquals(generateOrderData("WM002", -3, 2, "05:11:55"), orderDataList.get(1));
		assertEquals(generateOrderData("WM003", 7, 50, "05:31:50"), orderDataList.get(2));
		assertEquals(generateOrderData("WM004", 11, 5, "06:11:50"), orderDataList.get(3));
		
	}
	
	@Test
	public void testSomeInvalidInput() throws Exception {
		List<OrderData> orderDataList = orderReader.readOrderData(someInvalidInput.getFile());
		assertEquals(2, orderDataList.size());
		assertEquals(generateOrderData("WM001", 11, -5, "05:11:50"), orderDataList.get(0));
		assertEquals(generateOrderData("WM004", 11, 5, "06:11:50"), orderDataList.get(1));
		
	}

	@Test
	public void testEmptyFile() throws Exception {
		List<OrderData> orderDataList = orderReader.readOrderData(emptyInput.getFile());
		assertEquals(0, orderDataList.size());
	}

	
	@Test(expected = DroneSchedulerException.class)
	public void testMissingFile() throws Exception {
		orderReader.readOrderData("I don't exist");
	}

	
	private OrderData generateOrderData(String orderId, double xCoordinate, double yCoordinate, String orderTime) {
		GridCoordinates coordinates = new GridCoordinates(xCoordinate, yCoordinate);
		LocalTime time = LocalTime.parse(orderTime);
		return new OrderData(orderId, coordinates, LocalDateTime.now().with(time));
	}
	
}
