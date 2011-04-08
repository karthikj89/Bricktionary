package edu.berkeley.cs169.bricktionary.test;

import java.util.ArrayList;

import edu.berkeley.cs169.bricktionary.BoundingBox;
import edu.berkeley.cs169.bricktionary.Piece;
import edu.berkeley.cs169.bricktionary.Position;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundingBoxTest extends TestCase {
	private BoundingBox bb1;
	private BoundingBox bb2;
	private Piece piece;

	protected void setUp() throws Exception {
		super.setUp();
		bb1 = new BoundingBox();
		bb2 = new BoundingBox();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		bb1 = null;
		bb2 = null;
		piece = null;
	}
	
	public static TestSuite suite(){
		return new TestSuite(BoundingBoxTest.class);
	}
	
	public void testOverlapNone(){
		//test case of no overlap but touching edges
		bb1.expand(new Position(0,1));
		bb1.expand(new Position(-2,-1));
		bb2.expand(new Position(2,1));
		bb2.expand(new Position(0,-1));
		assertTrue(!bb1.overlap(bb2) && !bb2.overlap(bb1));
	}
	public void testOverlapVertIn(){
		//test case of corner inside each other
		bb1.expand(new Position(1,2));
		bb1.expand(new Position(-2,-1));
		bb2.expand(new Position(2,1));
		bb2.expand(new Position(-1,-2));
		assertTrue(bb1.overlap(bb2) && bb2.overlap(bb1));
	}
	public void testOverlapNoVertIn(){
		//test corner cases of overlapping even when no vertices inside each other
		bb1.expand(new Position(1,2));
		bb1.expand(new Position(-1,-2));
		bb2.expand(new Position(2,1));
		bb2.expand(new Position(-2,-1));
		assert(bb1.overlap(bb2) && bb2.overlap(bb1) && bb1.overlap(bb1));
	}
	public void testExpandPiece(){
		//test Piece and BoundingBox
		int orientation = 0;
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		piece = new Piece(0, new Position(2,1), orientation, vertices);
		piece.rotate();
		piece.rotate();
		piece.rotate();
		bb1.expand(piece);
		assertEquals(3,bb1.getMax().getX());
		assertEquals(3,bb1.getMax().getY());
		assertEquals(1,bb1.getMin().getX());
		assertEquals(-1,bb1.getMin().getY());
	}

}
