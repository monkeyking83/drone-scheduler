package com.walmart.drone.scheduler;

/**
 * General drone schedule exception that can be handled in one spot
 * 
 * @author Christophe Santini
 *
 */
public class DroneSchedulerException extends RuntimeException {

	private static final long serialVersionUID = -1322912100318817355L;

	public DroneSchedulerException() {

	}

	public DroneSchedulerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DroneSchedulerException(String message) {
		super(message);
	}

	public DroneSchedulerException(Throwable cause) {
		super(cause);
	}

}
