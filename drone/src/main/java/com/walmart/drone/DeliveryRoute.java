package com.walmart.drone;

import java.util.ArrayList;
import java.util.List;

import com.walmart.drone.nps.NetPromoteScore;

public class DeliveryRoute {
	private List<OrderDeliverySchedule> deliverySchedules;
	private NetPromoteScore netPromoterScore;

	public DeliveryRoute() {
		deliverySchedules = new ArrayList<>();
		netPromoterScore = new NetPromoteScore();
	}
	
	public List<OrderDeliverySchedule> getDeliverySchedules() {
		return deliverySchedules;
	}

	public void setDeliverySchedules(List<OrderDeliverySchedule> deliverySchedules) {
		this.deliverySchedules = deliverySchedules;
	}
	
	public void addDeliverySchedule(OrderDeliverySchedule deliverySchedule) {
		this.deliverySchedules.add(deliverySchedule);
	}
	

	public NetPromoteScore getNetPromoterScore() {
		return netPromoterScore;
	}

	public void setNetPromoterScore(NetPromoteScore netPromoterScore) {
		this.netPromoterScore = netPromoterScore;
	}

}
