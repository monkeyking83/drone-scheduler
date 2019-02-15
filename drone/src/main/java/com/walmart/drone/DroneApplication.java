package com.walmart.drone;

import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walmart.drone.scheduler.DroneScheduler;


@SpringBootApplication
public class DroneApplication implements ApplicationRunner {

	private static final String ORDERS = "input";
	
	@Value("${default.output.path:./schedules/drone_schedule.txt}")
	private String outputPath;
	
	@Autowired
	private DroneScheduler droneScheduler;
	
	private PrintStream out = System.out;
	
	public static void main(String[] args) {
		SpringApplication.run(DroneApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		if (args.containsOption(ORDERS) && args.getOptionValues(ORDERS).size() == 1) {
			String orderFilePath =  args.getOptionValues(ORDERS).get(0);
			
			String results = droneScheduler.generateDeliverySchedule(orderFilePath, outputPath);
			
			out.println(results);
			
		} else {
			out.println("Missing input file");
		}
		
	}

	
	
	

}

