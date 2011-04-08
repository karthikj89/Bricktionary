package edu.berkeley.cs169.bricktionary.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.berkeley.cs169.bricktionary.BoundingBox;
import edu.berkeley.cs169.bricktionary.GlobalVariables;
import edu.berkeley.cs169.bricktionary.Piece;
import edu.berkeley.cs169.bricktionary.Position;

public class PlayActivityTest PositionTestCase {
	ArrayList<Position> pieces;
	
}

protected void setUp() throws Exception {
	super.setUp();
	pieces = new ArrayList<Position>();
}

protected void tearDown() throws Exception {
	super.tearDown();
	pieces.clear();
}

public static TestSuite suite(){
	return new TestSuite(PlayActivityTest.class);
}

public void testRecall() {
	pieces.add(new Piece(1, new Pos(1,1)));
	pieces.add(new Piece(1, new Pos(2,2)));
	pieces.add(new Piece(1, new Pos(3,3)));
	
	int i = 0;
	int posX;
	int posY;
	
	for (i = 0; i < pieces.size(); i ++){
		if (pieces.get(i).getLocation()!= 6){
			pieces.get(i).setLocation(6);
			posX = 10 + 60 * i;
			posY = 10;
			pieces.get(i).moveTo(posX,posY);
		}
		pieces.get(i).setActive(false);
	}
	for (i = 0; i < pieces.size(); i ++){
		assertTrue(pieces.get(i).getLocation == 6);
		assertTrue(pieces.get(i).isActive == false);
		assertTrue(pieces.get(i).getPos().getX() == 10 + 60 * i);
		assertTrue(pieces.get(i).getPos().getY() == 10);
	}
}

public void setActiveTest(){
	pieces.add(new Piece(1, new Pos(0,0)));
	pieces.add(new Piece(1, new Pos(0,0)));
	pieces.add(new Piece(1, new Pos(0,0)));
	Piece p = new Piece(1,new Pos(1,2));
	pieces.add(p);
	for (int i = 0; i < pieces.size(); i ++){
		if (pieces.get(i).getLocation() != 8 ){
			pieces.get(i).setActive(false);
		}
	}
	p.setActive(true);
	
	for (int i = 0; i < pieces.size(); i ++){
		if (pieces.get(i).getPos().getX() != 1){
			assertTrue(pieces.get(i).isActive == false);
		}
		else {
			assertTrue(pieces.get(i).isActive);
		}
	}
}

