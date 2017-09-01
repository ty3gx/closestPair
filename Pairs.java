/*
 * File: Pairs.java
 * Author: Tianchang Yang
 * ------------------------------------------------------------
 * Implement the closestPair method below so that it takes
 * an ArrayList of Vertex objects specifying n points
 * and computes and returns the Pair of closest points.
 * Your writeup should include pseudocode for your 
 * implementation and careful justification of its runtime,
 * which should be asymptotically faster than n^2.
 * 
 * A simple Pair class is defined for you - you can 
 * add to it, but you should retain the getP1 and getP2
 * methods, which are used by the GUI.
 * 
 * Add to this file whatever private instance variables 
 * and private helper methods you need to implement 
 * the closestPair functionality.
 */

import java.util.*;

public class Pairs {

    /* Pair class stores two vertices and distance between them */
    class Pair {

        private Vertex p1;
        private Vertex p2;
        private double d;

        public Pair(Vertex p1, Vertex p2) {
            this.p1 = p1;
            this.p2 = p2;
            d = p1.distTo(p2);
        }

        public double dist() {
            return d;
        }

        public Vertex getP1() {
            return p1;
        }

        public Vertex getP2() {
            return p2;
        }
    }
    
    /*
    * given an array of points, return the closest Pair, or null if none exists
    */
    public Pair closestPair(ArrayList<Vertex> P) {
        ArrayList<Vertex> Px = new ArrayList(P);
        //sort array in nlog time instead of in recursive calls
        ArrayList<Vertex> Py = new ArrayList(P);
        Collections.sort(Px, Vertex.compX); //call helper method
        Collections.sort(Py, Vertex.compY);
        
        return closestPair(Px, Py);
    }

    /*  */
    private Pair closestPair(ArrayList<Vertex> Px, ArrayList<Vertex> Py) {
        //Base cases
        if (Px.size() <= 3) {
            if (Px.size() < 2) {
                return null;
            }

            if (Px.size() == 2) {
                return new Pair(Px.get(0), Px.get(1));
            }

            //brute force approach
            if (Px.get(0).distTo(Px.get(1)) < Px.get(0).distTo(Px.get(2))
                    && Px.get(0).distTo(Px.get(1)) < Px.get(1).distTo(Px.get(2))) {
                return new Pair(Px.get(0), Px.get(1));
            }
            if (Px.get(0).distTo(Px.get(2)) < Px.get(0).distTo(Px.get(1))
                    && Px.get(0).distTo(Px.get(2)) < Px.get(1).distTo(Px.get(2))) {
                return new Pair(Px.get(0), Px.get(2));
            }
            if (Px.get(1).distTo(Px.get(2)) < Px.get(0).distTo(Px.get(1))
                    && Px.get(1).distTo(Px.get(2)) < Px.get(0).distTo(Px.get(2))) {
                return new Pair(Px.get(1), Px.get(2));
            }
        }

        //partition
        int mid = Px.size() / 2;
        
        ArrayList<Vertex> leftArrx = new ArrayList(mid);
        ArrayList<Vertex> rightArrx = new ArrayList(Px.size() - mid);
        
        ArrayList<Vertex> leftArry = new ArrayList(mid);
        ArrayList<Vertex> rightArry = new ArrayList(Px.size() - mid);

        for (int i = 0; i < mid; i++) {
            leftArrx.add(Px.get(i));
        }

        for (int i = mid; i < Px.size(); i++) {
            rightArrx.add(Px.get(i));
        }
        
        //find the midpoint coordinate
        double midPt = (leftArrx.get(mid - 1).getX() + 
                rightArrx.get(0).getX())
                / 2;
        
        //generate sorted leftArry and rightArry in lineaar time
        for (int i = 0; i < Px.size(); i++) {
            if (Py.get(i).getX() < midPt) { //!!!might cause exception!!!
                leftArry.add(Py.get(i));
            } else {
                rightArry.add(Py.get(i));
            }
        }

        //recursively find closest pair for the left and right halves
        Pair leftPair = closestPair(leftArrx, leftArry);
        Pair rightPair = closestPair(rightArrx, rightArry);
        Pair tempPair; //store result pair

        //finding if any closer pairs in the middle
        double d;
        if (leftPair.dist() < rightPair.dist()) {
             tempPair = leftPair;
             d = leftPair.dist();
        } else {
            tempPair = rightPair;
            d = rightPair.dist();
        }
        
        ArrayList<Vertex> YArr = new ArrayList();
        for (int i = 0; i < Py.size(); i++) {
            if (Py.get(i).getX() < midPt + d && Py.get(i).getX() > midPt - d) {
                YArr.add(Py.get(i));
            }
        }

        //find if any closer pairs exist in the middle part
        for (int i = 0; i < YArr.size() - 1; i++) {
            int j = i + 1;
            while (j < YArr.size() 
                    && YArr.get(i).distTo(YArr.get(j)) < tempPair.dist()) {
                tempPair = new Pair(YArr.get(i), YArr.get(j));
                j++;
                //max 6 iterations

            }
        }

        return tempPair;
        
    }

}
