package edu.berkeley.cs169.bricktionary.test;
import java.util.ArrayList;

import edu.berkeley.cs169.bricktionary.Piece;
import edu.berkeley.cs169.bricktionary.Position;
import edu.berkeley.cs169.bricktionary.Puzzle;
import junit.framework.TestCase;


public class PuzzleTest extends TestCase {
	private Puzzle puzzle;

	protected void setUp() throws Exception {
		super.setUp();
		puzzle = new Puzzle(0,new ArrayList<Position>(),new ArrayList<Piece>());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		puzzle = null;
	}
	
	public void testCorrectScore(){
		ArrayList<Position> solution = puzzle.getSolution();
		//use a single triangle solution
		solution.add(new Position(0,0));
		solution.add(new Position(0,2));
		solution.add(new Position(4,0));
		
		//use a single triangle piece
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		assertEquals(1111,puzzle.calculateScore());
	}

}
