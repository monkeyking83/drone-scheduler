package com.walmart.drone.scheduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.walmart.drone.order.OrderData;
import com.walmart.drone.order.OrderReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroneScheduleCreatorTest {

	@Mock
	TimeProvider timeProvider;
	
	@Autowired
	OrderReader reader;

	@InjectMocks
	@Autowired
	DroneDeliveryScheduleCreator scheduleCreator;

	@Value("classpath:challenge_input.txt")
	Resource challengeInput;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidInput() throws Exception {
		when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now().with(LocalTime.parse("05:00:00")));
		List<OrderData> orderData = reader.readOrderData(challengeInput.getFile());
		DeliveryRoute route = scheduleCreator.generateDeliverySchedule(orderData);
		int nps = route.getNetPromoterScore().getScore();
		assertEquals(75, nps);
		
	}
	
	@Test
	public void testWithValidInputLateInDay() throws Exception {
		when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now().with(LocalTime.parse("19:55:00")));
		List<OrderData> orderData = reader.readOrderData(challengeInput.getFile());
		DeliveryRoute route = scheduleCreator.generateDeliverySchedule(orderData);
		int nps = route.getNetPromoterScore().getScore();
		assertEquals(-100, nps);
		
	}

	

}
