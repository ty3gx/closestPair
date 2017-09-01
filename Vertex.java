/*
 * File: Vertex.java
 * Author: Sara Krehbiel
 * ----------------------------------------------------------
 * Each Vertex object stores the information associated with 
 * a single point: right now that is simply the x and y 
 * coordinates, but we will modify this in future labs.
 * 
 * Coordinates are set upon construction; you can retrieve 
 * them with public get methods but do not modify them.
 * 
 * You may find the public distTo method useful when
 * computing the closest points.
 * 
 * The two public static comparators may be used by Pairs
 * to sort the Vertex objects by either coordinate.
 * 
 * You may also add private ivars and public methods to help
 * you implement shortest pairs in the allowed runtime.
 * You do not have to do this; if you do, you should upload
 * your changes to this class along with Pairs.java.
 * Do not remove any functionality from this class.
 */

import java.util.*;

public class Vertex {

    /* ivars */
    // display coordinates
    private final int x;
    private final int y;

    /* public methods */
    // construct new object, specifying coordinates
    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
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
        double dX = v.getX() - x;
        double dY = v.getY() - y;
        return Math.sqrt(dX * dX + dY * dY);
    }

    /* public static comparators */
    public static Comparator<Vertex> compX = new Comparator<Vertex>() {
        public int compare(Vertex p1, Vertex p2) {
            return p1.getX() - p2.getX();
        }
    };

    public static Comparator<Vertex> compY = new Comparator<Vertex>() {
        public int compare(Vertex p1, Vertex p2) {
            return p1.getY() - p2.getY();
        }
    };

}
