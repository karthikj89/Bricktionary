package edu.berkeley.cs169.bricktionary.test;

import java.util.ArrayList;

import edu.berkeley.cs169.bricktionary.Position;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PositionTest extends TestCase {
	private Position pos;
	private ArrayList<Position> pArray;

	protected void setUp() throws Exception {
		super.setUp();
		pos = new Position(0,0);
		pArray = new ArrayList<Position>();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		pos = null;
		pArray = null;
	}
	
	public static TestSuite suite(){
		return new TestSuite(PositionTest.class);
	}
	
	public void testPositionInside(){
		//test Position.inside when position is a vertex
		pArray.add(pos);
		pArray.add(new Position(0,1));
		pArray.add(new Position(1,0));
		assertTrue(!pos.inside(pArray, false));
	}

}
