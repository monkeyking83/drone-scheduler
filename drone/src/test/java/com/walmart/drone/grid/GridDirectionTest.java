package com.walmart.drone.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GridDirectionTest {

	
	@Test
	public void testGridDirection() throws Exception {
		
		assertEquals(GridDirection.NORTH, GridDirection.getGridDirection("n"));
		assertEquals(GridDirection.NORTH, GridDirection.getGridDirection("N"));
		assertEquals(GridDirection.SOUTH, GridDirection.getGridDirection("s"));
		assertEquals(GridDirection.SOUTH, GridDirection.getGridDirection("S"));
		assertEquals(GridDirection.EAST, GridDirection.getGridDirection("e"));
		assertEquals(GridDirection.EAST, GridDirection.getGridDirection("E"));
		assertEquals(GridDirection.WEST, GridDirection.getGridDirection("w"));
		assertEquals(GridDirection.WEST, GridDirection.getGridDirection("W"));
		assertEquals(GridDirection.UNKNOWN, GridDirection.getGridDirection("North"));
	
	}


	
}
