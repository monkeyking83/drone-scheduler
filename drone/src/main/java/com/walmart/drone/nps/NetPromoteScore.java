package com.walmart.drone.nps;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class NetPromoteScore {

	private List<Integer> scores;

	public NetPromoteScore() {
		this.scores = new ArrayList<>();
	}
	
	public void addScore(Integer score) {
		this.scores.add(score);
	}
	
	public List<Integer> getScores() {
		return this.scores;
	}
	
	public int getScore() {

		Map<NetPromoterType, Integer> countByType = initCountByTypeMap();
		Integer count;
		NetPromoterType netPromoterType;
		
		for (Integer score : scores) {
			netPromoterType = NetPromoterType.getType(score);
			count = countByType.get(netPromoterType);
			countByType.put(netPromoterType, count + 1);
		}

		Double promoterScore = (countByType.get(NetPromoterType.PROMOTER).doubleValue() / scores.size()) * 100;
		Double detractorScore = (countByType.get(NetPromoterType.DETRACTOR).doubleValue() / scores.size()) * 100;

		return promoterScore.intValue() - detractorScore.intValue();
		
	}

	private static Map<NetPromoterType, Integer> initCountByTypeMap() {
		Map<NetPromoterType, Integer> countByType = new EnumMap<>(NetPromoterType.class);

		for (NetPromoterType netPromoterType : NetPromoterType.values()) {
			countByType.put(netPromoterType, Integer.valueOf(0));
		}
		return countByType;
	}

}
