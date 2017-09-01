/*
 * File: Vertex.java
 * Author: Sara Krehbiel
 * ----------------------------------------------------------
 * Each Vertex object stores the information associated with 
 * a single point: for HW4, that includes the x and y 
 * coordinates, a neighbors list, a connected component number,
 * a distance, and a prev vertex.
 * 
 * Coordinates are set upon construction; you can retrieve 
 * them with public get methods but do not modify them.
 * 
 * You may find the public distTo method useful when
 * computing the closest points.
 * 
 * The three public static comparators may be used by Pairs
 * to sort the Vertex objects by either coordinate or 
 * by a priority queue used in your implementation of Dijkstra
 * to keep track of the min Vertex, where min is determined
 * by the dist field.
 * 
 * Upload this file with Traversal.java if you make changes.
 * Do not remove any functionality from this class.
 */

import java.util.*;

public class Vertex {

	/* ivars */
		
	// display coordinates, neighbors list,
	// connected component #, prev vertex, dist
	private final int x;
	private final int y;
	private ArrayList<Vertex> neighbors;
	private int ccnum;
	private Vertex prev;
	private double dist;
	
	/* public methods */
	
	// construct new object, specifying coordinates
	public Vertex(int x, int y) {
		// set the provided coordinates
		this.x=x;
		this.y=y;
		
		// initialize neighbors list, null prev pointer, and (infinite) dist
		neighbors = new ArrayList<Vertex>();
		ccnum = 0;
		prev = null;
		dist = Double.MAX_VALUE;
	}
	
	// retrieve coordinates
	public int getX() {
		return x;
	}	
	public int getY() {
		return y;
	}
		
	// u.distTo(v) returns the distance from vertex u to v 
	public double distTo(Vertex v) {
		double dX = v.getX()-x;
		double dY = v.getY()-y;
		return Math.sqrt(dX*dX+dY*dY);
	}
	
	// add to neighbors list
	public void addNeighbor(Vertex v) {
		neighbors.add(v);
	}
	
	// retrieve neighbors list
	public ArrayList<Vertex> getNeighbors() {
		return neighbors;
	}
	
	// set/get ccnum
	public void setCCNum(int c) {
		ccnum = c;
	}
	public int getCCNum() {
		return ccnum;
	}
	
	// set/get prev
	public void setPrev(Vertex v) {
		prev = v;
	}
	public Vertex getPrev() {
		return prev;
	}
	
	// set/get dist
	public void setDist(double d) {
		dist = d;
	}
	public double getDist() {
		return dist;
	}
	
	
	
	/* public static comparators */
	
	public static Comparator<Vertex> compX = new Comparator<Vertex>() {
		public int compare(Vertex p1, Vertex p2) {
			return p1.getX() - p2.getX();
		}
	};
	
	public static Comparator<Vertex> compY =  new Comparator<Vertex>() {
		public int compare(Vertex p1, Vertex p2) {
			return p1.getY() - p2.getY();
		}
	};
	
	public static Comparator<Vertex> compDist = new Comparator<Vertex>() {
		public int compare(Vertex p1, Vertex p2) {
			double delta = p1.getDist()-p2.getDist();
			if (delta>0) return 1;
			if (delta<0) return -1;
			return 0;
		}
	};
		
}