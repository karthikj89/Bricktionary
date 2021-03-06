package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;
import java.util.Iterator;

public class Piece {
	private int type;
	private int orientation;
	private Position center;
	private ArrayList <Position> vertices, xvertices;
	private BoundingBox bb, xbb;
	private boolean _active;
	private int location; // toolbox and board
	boolean dirty; //dirty flag to show if xvertices/xbb (transformed vertices/bb) changed


	/**
	 * 
	 * @param type
	 * @param pos
	 * @param orientation
	 * @param vertices
	 */
	public Piece (int type, Position pos, int orientation, ArrayList <Position> vertices) {
		this.type = type;
		this.center = new Position(pos);	
		this.orientation = orientation;
		this.vertices = vertices;
		this.location = toolbox;
		initialize();
	}
	/**
	 * 
	 * @param type
	 * @param pos
	 */
	public Piece (int type, Position pos) {

		this.type = type;
		this.center = new Position(pos);

		//TODO: get orientation and vertices from database according to type
		//use a dummy 2x4 triangle defined with center at 0,0 for now
		orientation = 0;
		if(type==square){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(0,0));
			vertices.add(new Position(0,40));
			vertices.add(new Position(40,40));
			vertices.add(new Position(40,0));
		}else if(type==smallTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(0,0));
			vertices.add(new Position(40,0));
			vertices.add(new Position(40,-40));
		}else if(type==mediumTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(0,0));
			vertices.add(new Position(50,0));
			vertices.add(new Position(50,-50));			
		}else if(type==largeTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(0,0));
			vertices.add(new Position(60,0));
			vertices.add(new Position(60,-60));			
		}else if(type==parallelogram){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(0,0));
			vertices.add(new Position(70,0));
			vertices.add(new Position(80,-40));
			vertices.add(new Position(10,-40));
		}
		this.location = toolbox;
		initialize();
	}

	public int getWidth(){
		return bb.getMax().getX()-bb.getMin().getX();
	}

	public int getHeight(){
		return bb.getMax().getY()-bb.getMin().getY();
	}

	private void initialize(){
		xvertices = new ArrayList<Position>();
		//copy vertices to xvertices
		for(int i = 0; i < vertices.size(); i++)
			xvertices.add(new Position(vertices.get(i)));

		bb = new BoundingBox(vertices); //create bounding box
		xbb = new BoundingBox(bb); //create transformed bounding box
		if(orientation!=0 || !(center.x==0 && center.y==0))
			update();
	}

	/**
	 * 
	 * @param newpos
	 */
	public void moveTo(Position newpos){
		dirty = true;
		center = newpos;	
	}

	public void moveTo(int x, int y){
		dirty = true;
		Position newPos = new Position(x,y);
		center = newPos;	
	}

	public void setVerticesOffset(Position newpos){
		int offsetX;
		int offsetY;
		for (Position p : vertices){
			offsetX = p.getX() - center.getX();
			offsetY = p.getY() - center.getY();
			p.set(newpos.getX() + offsetX, newpos.getY() + offsetY);
		}
		center = newpos;
		dirty = true;
	}

	public void rotate(){
		dirty = true;
		if(orientation < 3)
			orientation++;
		else
			orientation = 0;
	}

	/**new class
	 * Checks if vertices inside the polygon described by vertices 
	 * 
	 * @param vertices2
	 * @return
	 */
	public boolean inside(ArrayList<Position> vertices2){
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(!xvitr.next().inside(vertices2, true))
				return false;
		}
		return true;
	}

	/**new class
	 * Checks for overlapping pieces
	 * currently only checks if vertices are inside Piece p2 ,
	 * not a perfect check of overlap (perfect check would check edges),
	 * more useful if used while placing pieces
	 * idea from here: http://gpwiki.org/index.php/Polygon_Collision
	 */
	public boolean overlap(Piece p2){
		//first check if BoundingBox overlaps
		//if not, return false
		BoundingBox xbb = getXBB();
		BoundingBox xbb2 = p2.getXBB();
		if(!xbb.overlap(xbb2))
			return false;

		//check if xvertices inside Piece p2 (not checking by edge intersections)
		ArrayList<Position> vertices2 = p2.getXVertices();
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(xvitr.next().inside(vertices2, false))
				return true;
		}
		return false;
	}

	/**new class
	 * returns 2 times the area of this piece 
	 */
	public int area2x(){
		return Position.area2x(vertices);
	}

	/**updates (transforms) xvertices to correct orientation and positions
	 */
	public void update(){
		if(dirty){
			//update xvertices
			for(int i = 0; i < xvertices.size(); i++){
				Position xVertex = xvertices.get(i);
				xVertex.set(vertices.get(i));
				xVertex.rotate(orientation);
				xVertex.add(center);
			}
			//update xbb
			Position xbbMax = xbb.getMax();
			xbbMax.set(bb.getMax());
			xbbMax.rotate(orientation);
			xbbMax.add(center);
			Position xbbMin = xbb.getMin();
			xbbMin.set(bb.getMin());
			xbbMin.rotate(orientation);
			xbbMin.add(center);
			//update flag
			dirty = false;
		}
	}

	//new classes: getters and setters
	public int getLocation(){
		return location;

	}
	public void setLocation(int Location){
		this.location = Location;
	}
	public int getOrientation(){
		return orientation;
	}
	public Position getPos(){
		return center;
	}
	public ArrayList<Position> getVertices(){
		return vertices;
	}
	public void setXVertices(ArrayList<Position> vert){
		xvertices = vert;
	}
	/*Returns updated transformed vertices for Piece (correct orientation and position)*/
	public ArrayList<Position> getXVertices(){
		update();
		return xvertices;
	}
	public BoundingBox getBB(){
		return bb;
	}
	/**Returns updated transformed bb for Piece (correct orientation and position)*/
	public BoundingBox getXBB(){
		update();
		return xbb;
	}

	public boolean isActive(){
		return _active;
	}

	public void setActive(boolean b){
		_active = b;
	}

	public int getType(){
		return type;
	}


	public static final int
	square = 1,
	smallTriangle = 2,
	mediumTriangle = 3,
	largeTriangle = 4,
	parallelogram = 5;
	public static final int
	toolbox = 6;
	public static final int board = 7, pickedUp = 8;
}