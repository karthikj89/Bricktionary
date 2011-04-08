package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;
import java.util.Iterator;

public class Piece {
	private int type;
	private int orientation;
	private Position pos;
	private ArrayList <Position> vertices, xvertices;
	private BoundingBox bb, xbb;
	boolean dirty; //dirty flag to show if xvertices/xbb (transformed vertices/bb) changed
	
	public Piece (int type, Position pos, int orientation, ArrayList <Position> vertices) {
		this.type = type;
		this.pos = new Position(pos);	
		this.orientation = orientation;
		this.vertices = vertices;
		initialize();
	}
	
	public Piece (int type, Position pos) {
		this.type = type;
		this.pos = new Position(pos);
		
		//TODO: get orientation and vertices from database according to type
		//use a dummy 2x4 triangle defined with center at 0,0 for now
		orientation = 0;
		vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		
		initialize();
	}
	
	private void initialize(){
		xvertices = new ArrayList<Position>();
		//copy vertices to xvertices
		for(int i = 0; i < vertices.size(); i++)
			xvertices.add(new Position(vertices.get(i)));
		
		bb = new BoundingBox(vertices); //create bounding box
		xbb = new BoundingBox(bb); //create transformed bounding box
		if(orientation!=0 || !(pos.x==0 && pos.y==0))
			update();
	}
	
	public void moveTo(Position newpos){
		dirty = true;
		pos = newpos;
	}
	
	public void rotate(){
		dirty = true;
		if(orientation < 3)
			orientation++;
		else
			orientation = 0;
	}
	
	//new class
	/* Checks if vertices inside the polygon described by vertices */
	public boolean inside(ArrayList<Position> vertices2){
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(!xvitr.next().inside(vertices2, true))
				return false;
		}
		return true;
	}
	
	//new class
	/* Checks for overlapping pieces
	 * currently only checks if vertices are inside Piece p2 (not counting edges),
	 * not a perfect check of overlap,
	 * more useful if used while placing pieces
	 * idea from here: http://gpwiki.org/index.php/Polygon_Collision
	 */
	public boolean overlap(Piece p2){
		//first check if BoundingBox overlaps
		BoundingBox xbb = getXBB();
		BoundingBox xbb2 = p2.getXBB();
		xbb.overlap(xbb2);
		
		//check if xvertices inside Piece p2 (not counting edges)
		ArrayList<Position> vertices2 = p2.getXVertices();
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(xvitr.next().inside(vertices2, false))
				return true;
		}
		return false;
	}
	
	//new class
	/* returns 2 times the area of this piece */
	public int area2x(){
		return Position.area2x(vertices);
	}
	
	/*updates (transforms) xvertices to correct orientation and positions*/
	public void update(){
		if(dirty){
			//update xvertices
			for(int i = 0; i < xvertices.size(); i++){
				Position xVertex = xvertices.get(i);
				xVertex.set(vertices.get(i));
				xVertex.rotate(orientation);
				xVertex.add(pos);
			}
			//update xbb
			Position xbbMax = xbb.getMax();
			xbbMax.set(bb.getMax());
			xbbMax.rotate(orientation);
			xbbMax.add(pos);
			Position xbbMin = xbb.getMin();
			xbbMin.set(bb.getMin());
			xbbMin.rotate(orientation);
			xbbMin.add(pos);
			//update flag
			dirty = false;
		}
	}
	
	//new classes: getters and setters
	public int getOrientation(){
		return orientation;
	}
	public Position getPos(){
		return pos;
	}
	public ArrayList<Position> getVertices(){
		return vertices;
	}
	/*Returns updated transformed vertices for Piece (correct orientation and position)*/
	public ArrayList<Position> getXVertices(){
		update();
		return xvertices;
	}
	public BoundingBox getBB(){
		return bb;
	}
	/*Returns updated transformed bb for Piece (correct orientation and position)*/
	public BoundingBox getXBB(){
		update();
		return xbb;
	}
}