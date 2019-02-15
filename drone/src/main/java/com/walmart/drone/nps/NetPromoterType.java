package com.walmart.drone.nps;

public enum NetPromoterType {
	PROMOTER(9,10),
	NEUTRAL(7,8),
	DETRACTOR(0,6),
	UNKNOWN(-1, -1);
	
	private static final Integer MIN_SCORE = 0;
	private static final Integer MAX_SCORE = 10;
	
	private Integer min;
	private Integer max;
	
	private NetPromoterType(Integer min, Integer max) {
		this.min = min;
		this.max = max;
	}
	
	
	
	public Integer getMin() {
		return min;
	}



	public Integer getMax() {
		return max;
	}

	public static NetPromoterType getType(int score) {
		// adjust score to valid range:
		int adjustedScore = score > MAX_SCORE ? MAX_SCORE : score;
		adjustedScore = adjustedScore < MIN_SCORE ? MIN_SCORE : adjustedScore;
		NetPromoterType nps = UNKNOWN;
		
		for (NetPromoterType netPromoterType : NetPromoterType.values()) {
			if (adjustedScore >= netPromoterType.getMin() && adjustedScore <= netPromoterType.getMax()) {
				nps  = netPromoterType;
			}
		}
		return nps;
		
	}
	
	
}
