package com.walmart.drone.nps;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetPromoterScoreTest {

	private static final List<Integer> netPromoter =  Arrays.asList(10, 10, 10, 10, 8);
	private static final List<Integer> negativeNps = Arrays.asList(0, 0, 0, 0, 5);
	private static final List<Integer> boundariesNps = Arrays.asList(-5, 15, 12, 14); 
	
	@Test
	public void testPromoter() throws Exception {
		testExpectedNps(netPromoter, 80);
		testExpectedNps(negativeNps, -100);
		testExpectedNps(boundariesNps, 50);
		
	}

	private void testExpectedNps(List<Integer> scores, int expectedNps) {
		NetPromoteScore nps = new NetPromoteScore();
		scores.forEach(n -> nps.addScore(n));
		assertEquals(expectedNps, nps.getScore());
	}
	
}
