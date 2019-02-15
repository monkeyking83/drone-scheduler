package com.walmart.drone.scheduler;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.walmart.drone.DeliveryRoute;
import com.walmart.drone.OrderData;
import com.walmart.drone.OrderReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroneDeliverySchedulerTest {

	@Autowired
	OrderReader reader;

	@Autowired
	DroneDeliveryScheduleCreator scheduleCreator;

	@Value("classpath:challenge_input.txt")
	Resource resourceFile;

	@Test
	public void testDelivery() throws Exception {
		List<OrderData> orderData = reader.readOrderData(resourceFile.getFile());
		
		DeliveryRoute route = scheduleCreator.generateDeliverySchedule(orderData);
		route.getNetPromoterScore().getScore();
	
		
	}

}
