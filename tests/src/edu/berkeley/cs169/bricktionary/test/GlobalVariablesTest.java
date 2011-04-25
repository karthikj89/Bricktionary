package edu.berkeley.cs169.bricktionary.test;

import edu.berkeley.cs169.bricktionary.GlobalVariables;
import junit.framework.TestCase;

public class GlobalVariablesTest extends TestCase {
	GlobalVariables gv;

	protected void setUp() throws Exception {
		super.setUp();
		gv = new GlobalVariables();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		gv = null;
	}
	
	public void testInit() {
		assertEquals(1,gv.getCurrentLevel());
		assertEquals(0,gv.getHighScore());
	}
	
	public void testSetGet() {
		gv.setCurrentLevel(1);
		gv.setHighScore(2);
		assertEquals(1,gv.getCurrentLevel());
		assertEquals(2,gv.getHighScore());
	}

}
