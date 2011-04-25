package edu.berkeley.cs169.bricktionary.test;

import java.util.ArrayList;


import edu.berkeley.cs169.bricktionary.Piece;
import edu.berkeley.cs169.bricktionary.Position;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PieceTest extends TestCase {
	private int type;
	private int orientation;
	private Position center;
	private ArrayList <Position> vertices;
	private Piece p;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		type = 0;
		orientation = 0;
		center = new Position(0,0);
		vertices = new ArrayList<Position>();
		p = new Piece(type,center,orientation,vertices);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		p = null;
	}
	public void moveToTest(){
		p = new Piece(type,center);
		p.moveTo(5,5);
		assertTrue(p.getPos().getX() == 5);
		assertTrue(p.getPos().getY() == 5);
	}
	public void moveToTest2(){
		p = new Piece(type,center);
		p.moveTo(new Position(5,5));
		assertTrue(p.getPos().getX() == 5);
		assertTrue(p.getPos().getY() == 5);
	}
	public void rotateTest(){
		p = new Piece(type, center, orientation, vertices);
		p.rotate();
		assertTrue(p.getOrientation() == 1);
		p.rotate();
		assertTrue(p.getOrientation() == 2);
		p.rotate();
		assertTrue(p.getOrientation() == 3);
		p.rotate();
		assertTrue(p.getOrientation() == 0);
	}
	public void updateTest(){
		p.update();
		assertTrue(p.dirty == false);
	}
	public void setLocationTest(){
		p.setLocation(6);
		assertTrue(p.getLocation() == 6);
		p.setLocation(7);
		assertTrue(p.getLocation() == 7);
		p.setLocation(7);
		assertTrue(p.getLocation() == 8);
		
	}
	}
}