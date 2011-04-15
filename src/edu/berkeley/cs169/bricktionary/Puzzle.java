package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;
import java.util.Iterator;

public class Puzzle {
	int level;
	long startedTime; //changed to a long
	long endedTime; //changed to a long
	ArrayList<Position> solution;
	ArrayList<Piece> pieces;
	boolean outline;
	

	/**
	 * 
	 * @param level
	 * @param solution
	 * @param pieces
	 */

	public Puzzle(int level, ArrayList<Position> solution, ArrayList<Piece> pieces){

		this.level = level;
		//TODO: set startedTime and endedTime to currentTime
		startedTime = System.nanoTime();
		this.solution = solution;
		this.pieces = pieces;
		this.outline = true;
	}
	
	private ArrayList<ArrayList<Position>> getsolutionList(){
		ArrayList<ArrayList<Position>> solutionList = new ArrayList<ArrayList<Position>>();
		
		ArrayList<Position>solution1 = new ArrayList<Position>();
		solution1.add(new Position(0,0));
		solution1.add(new Position(50,-50));
		solution1.add(new Position(100,0));
		solution1.add(new Position(100,80));
		solution1.add(new Position(0,80));
		
		ArrayList<Position>solution2 = new ArrayList<Position>();
		solution2.add(new Position(0,0));
		solution2.add(new Position(50,-50));
		solution2.add(new Position(100,0));
		solution2.add(new Position(100,80));
		
		ArrayList<Position>solution3 = new ArrayList<Position>();
		solution3.add(new Position(0,0));
		solution3.add(new Position(50,-50));
		solution3.add(new Position(100,0));
		
		solutionList.add(solution1);
		solutionList.add(solution2);
		solutionList.add(solution3);
		
		return solutionList;
	}
	

	/**
	 * 
	 * @param level
	 */

	public Puzzle(int level){
		this(level,null,null);		
		//TODO: load solution and pieces from database according to level
		//use a dummy triangle solution for now
		
		int solutionIndex = level-1;
		ArrayList<ArrayList<Position>> solutionList = getsolutionList();
		solution = solutionList.get(solutionIndex);
		
		//pieces required for puzzle
		pieces = new ArrayList<Piece>();
		pieces.add(new Piece(1, new Position(0,0)));
		pieces.add(new Piece(2, new Position(0,0)));
		pieces.add(new Piece(3, new Position(0,0)));
		pieces.add(new Piece(4, new Position(0,0)));
		pieces.add(new Piece(5, new Position(0,0)));
	}
	
	private Piece addPiece(int type, Position pos){
		Piece piece = new Piece(type, pos);
		pieces.add(piece);
		return piece;
	}
	
	public void movePiece(Piece piece, Position pos){
		pieces.remove(piece);
		piece.moveTo(pos);
		pieces.add(piece);
	}
	
	public int calculateScore(){
		endedTime = System.nanoTime();
		Iterator <Piece> pitr = pieces.iterator();
		
		//calculate if pieces and solution same area
		int slnArea = Position.area2x(solution);
		int piecesArea = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			piecesArea += pitr.next().area2x();
		}
		boolean sameArea = false;
		if(slnArea == piecesArea)
			sameArea = true;
		
		//** return score=0 if pieces not the same area as solution
		if(!sameArea)
			return 0;
		
		BoundingBox sbb = new BoundingBox(solution); //bounding box of solution
		BoundingBox pbb = new BoundingBox(); //bounding box of pieces
		while(pitr.hasNext()){
			Piece piece = pitr.next();
			pbb.expand(piece);
		}
		
		//align solution BB to pieces BB according to min
		Position displaceSBB = new Position(pbb.getMin().x - sbb.getMin().x, pbb.getMin().y - sbb.getMin().y);
		Position alignedSBBMax = new Position(sbb.getMax().x+displaceSBB.x,
				sbb.getMax().y+displaceSBB.y);
		
		//check if bounding boxes match
		boolean bbsMatch = false;
		if(pbb.getMax().equals(alignedSBBMax))
			bbsMatch = true;
		
		//** return score=1 if at least area is the same, but bounding box doesn't match
		if(!bbsMatch)
			return 1;
		
		//if no outline, move solution vertices according to alignment
		if(!outline) {
			Iterator<Position> slnItr = solution.iterator();
			while(slnItr.hasNext()){
				slnItr.next().add(displaceSBB);
			}
		}
		
		//calculate if pieces inside solution
		boolean inside = true;
		int numPiecesInside = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			if(pitr.next().inside(solution)){
				numPiecesInside++;
			} else {
				inside = false;
			}
		}
		
		//** return score=2 to 8 depending on percentage of pieces inside solution
		if(!inside)
			return 1 + 7*(numPiecesInside/pieces.size());
		
		//calculate if pieces overlap
		//not needed here if done on the fly when placing pieces
		boolean overlap = false;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			Iterator<Piece> pitr2 = pieces.iterator();
			while(pitr2.hasNext()){
				if(pitr.next().overlap(pitr2.next())){
					overlap = true;
					break;
				}
			}
		}
		
		//** return score=10 if satisfies all criterion
		//else return score=9 if overlap detected
		if(!overlap)
			return 10;
		else
			return 9;
	}
	
	public void reset(){
		//TODO: set startedTime to currentTime
		//TODO: set solution and pieces back to initial state (pull from database);
	}
	
	//new classes: getters and setters
	public ArrayList<Position> getSolution(){
		return solution;
	}
	public ArrayList<Piece> getPieces(){
		return pieces;
	}
	//set if puzzle shows outline or not
	public void setOutline(boolean outline){
		this.outline = outline;
	}
	//get time elapsed in seconds since Puzzle created
	public double getElapsedTime(){
		return (double)(System.nanoTime()-startedTime)/1000000000.0;
	}
}
