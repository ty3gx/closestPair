/*
 * File: Traversal.java
 * Author: Tianchang Yang
 * ------------------------------------------------------------
 * Implement the dfs, bfs, and dijkstra methods below 
 * according to the specifications in the lab handout.
 */

import java.util.*;

public class Traversal {

    /**
     * Perform dfs on a given graph.
     * 
     * @param V input arraylist of all the vertices in the graph
     */
    public void dfs(ArrayList<Vertex> V) {
        if (V.size() == 0) {
            return;
        } //return if nothing is in the graph

        reset(V);

        int cc = 1;

        int n = V.size();
        for (int i = 0; i < n; i++) {
            V.get(i).setCCNum(0);
            //I'm not sure what ccNum is for in the Vertex class, and you didn't
            //specify anything in the handout. Since it's there, I used it as an
            //indicator as if the vertex has been visited
        }

        for (int i = 0; i < n; i++) {
            if (V.get(i).getCCNum() == 0) {
                explore(V, V.get(i), cc); //call recursive method to explore
            }
        }
    }

    /**
     * Recursive helper method to explore the graph using dfs.
     * 
     * @param V input arraylist of all the vertices in the graph
     * @param v current vertex exploring
     * @param cc number of edges count
     */
    private void explore(ArrayList<Vertex> V, Vertex v, int cc) {
        v.setCCNum(cc);
        v.setDist(v.getCCNum() - 1);
        cc++;

        int n = v.getNeighbors().size();
        for (int i = 0; i < n; i++) {
            if (v.getNeighbors().get(i).getCCNum() == 0) {
                v.getNeighbors().get(i).setPrev(v); //set prev
                explore(V, v.getNeighbors().get(i), cc);
            }
        }
    }

    /**
     * Perform bfs on a given graph.
     * 
     * @param V input arraylist of all the vertices in the graphs
     */
    public void bfs(ArrayList<Vertex> V) {
        if (V.size() == 0) {
            return;
        }

        reset(V);

        V.get(0).setDist(0);
        Queue<Vertex> Q = new LinkedList<Vertex>(); 
        //using queue interface and linkedlist implementation
        
        Q.add(V.get(0));
        while (!Q.isEmpty()) {
            Vertex u = Q.poll();
            int n = u.getNeighbors().size();
            for (int i = 0; i < n; i++) {
                if (u.getNeighbors().get(i).getDist()
                        == Double.MAX_VALUE) {
                    Q.add(u.getNeighbors().get(i));
                    u.getNeighbors().get(i).setDist(u.getDist() + 1);
                    u.getNeighbors().get(i).setPrev(u);
                }
            }
        }

    }

    /**
     * Perform dijkstra on a given graph.
     * 
     * @param V input arraylist of all the vertices in the graphs
     */
    public void dijkstra(ArrayList<Vertex> V) {
        if (V.size() == 0) {
            return;
        }
        
        reset(V);
        
        V.get(0).setDist(0);
        PriorityQueue<Vertex> H = 
                new PriorityQueue<Vertex>(V.size(), Vertex.compDist);
        //priorityqueue implementation is used here
        
        int n = V.size();
        for (int i = 0; i < n; i++) {
            H.add(V.get(i));
        }
        while (!H.isEmpty()) {
            Vertex u = (Vertex) H.remove();
            int nn = u.getNeighbors().size();
            for (int i = 0; i < nn; i++) {
                if (u.getNeighbors().get(i).getDist() > u.getDist() + 
                        u.getNeighbors().get(i).distTo(u)) {
                    u.getNeighbors().get(i).setDist(u.getDist() + 
                            u.getNeighbors().get(i).distTo(u));
                    u.getNeighbors().get(i).setPrev(u);
                    H.remove(u.getNeighbors().get(i));
                    H.add(u.getNeighbors().get(i)); //update Queue
                }
            }
        }
    }

    /**
     * Private helper method to reset the distances and prev pointers
     * 
     * @param V input arraylist of all the vertices in the graphs
     */
    private void reset(ArrayList<Vertex> V) {
        int n = V.size();
        for (int i = 0; i < n; i++) {
            V.get(i).setCCNum(0);
            V.get(i).setDist(Double.MAX_VALUE);
            V.get(i).setPrev(null);
        }
    }

}
