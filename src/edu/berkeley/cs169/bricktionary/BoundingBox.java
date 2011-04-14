package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;
import java.util.Iterator;

public class BoundingBox {
	private Position max, min; // max (top right), min (bottom right)
	private boolean valid;
	
	public BoundingBox(){
		valid = false;
	}
	
	public BoundingBox(ArrayList<Position> vertices){
		this();
		expand(vertices);
	}
	
	public BoundingBox(BoundingBox bb){
		this();
		max = new Position(bb.getMax());
		min = new Position(bb.getMin());
	}
	
	public void expand(Position vertex){
		if(valid){
			if(vertex.x > max.x)
				max.x = vertex.x;
			else if (vertex.x < min.x)
				min.x = vertex.x;
			if(vertex.y > max.y)
				max.y = vertex.y;
			else if (vertex.y < min.y)
				min.y = vertex.y;
		} else {
			max = new Position(vertex);
			min = new Position(vertex);
			valid = true;
		}
	
	}
	
	public void expand(ArrayList<Position> vertices){
		if (vertices!=null && vertices.size() > 0){ //has vertices
			Iterator<Position> vitr = vertices.iterator();
			while(vitr.hasNext()){
				expand(vitr.next());
			}
		}
	}
	
	public void expand(BoundingBox bb){
		expand(bb.getMax());
		expand(bb.getMin());
	}
	
	public void expand(Piece piece){
		expand(piece.getXBB());
	}
	
	public boolean inside(int x, int y){
		return x < max.x && y < max.y && x > min.x && y > min.y;
	}
	
	public boolean inside(Position vertex){
		return inside(vertex.x, vertex.y);
	}
	
	/* check if this BoundingBox overlaps BoundingBox bb*/
	public boolean overlap(BoundingBox bb){
		//check if this BoundingBox's vertices inside bb
		//check if bb's vertices inside this BoundingBox
		//check corner cases of overlapping even when no vertices inside each other
		return ((bb.inside(max) || bb.inside(min) || bb.inside(max.x, min.y) || bb.inside(min.x, max.y)) ||
			(inside(bb.getMax()) || inside(bb.getMin()) || inside(bb.getMax().x, bb.getMin().y) || inside(bb.getMin().x, bb.getMax().y)) ||
			(max.y >= bb.getMin().y && max.y <= bb.getMax().y && max.x >= bb.getMax().x && min.x <= bb.getMin().x) ||
			(bb.getMax().y >= min.y && bb.getMax().y <= max.y && max.x <= bb.getMax().x && min.x >= bb.getMin().x));
	}
	
	public Position getMax(){
		return max;
	}
	public Position getMin(){
		return min;
	}
	public boolean getValid(){
		return valid;
	}
}
