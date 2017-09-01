/*
 * File: GraphProgram.java
 * Author: Sara Krehbiel
 * -----------------------------------------------------------------
 * This GUI wrapper will be modified throughout the semester.
 * When the user clicks "Closest Pair," it displays n random points 
 * (the user decides n), calls your HW3 D&Q closest pair algorithm, 
 * implemented in Pairs.java, and highlights the returned pair.
 * The algorithms for this and subsequent labs will run on graphs,
 * so a new button "Get Graph" generates a random graph in which
 * each edge is present with independent user-specified probability.
 * This functionality has already been implemented for you.
 * 
 * Three new buttons call the algorithms you should implement in
 * Traversal.java. You should implement each of these to treat the 
 * first randomly generated vertex as the source and compute a 
 * distance from the source to each vertex while keeping track of 
 * the previous vertex on each vertex's path. 
 * 
 * DO NOT MODIFY THIS FILE.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

public class GUI extends JFrame {

	/* private ivars for random vertices and edges */
	private ArrayList<Vertex> V = new ArrayList<Vertex>();
	private int nNodes=18;
	private double edgeProb=0.1;
	private Random rand = new Random();

	/* private ivars for GUI */
	private GraphPanel panel = new GraphPanel();
	private JPanel menu = new JPanel(new FlowLayout());
	private JTextField userN = new JTextField(""+nNodes,NCOLS);
	private JTextField userPr = new JTextField(""+edgeProb,NCOLS);
	private JButton cpButton = new JButton("Closest Pair");
	private JButton graphButton = new JButton("Get Graph");
	private JButton dfsButton = new JButton("DFS");
	private JButton bfsButton = new JButton("BFS");
	private JButton dijkstraButton = new JButton("Dijkstra");
	private boolean drawPaths = false;
	
	/* private ivars for outputs from your algorithms */
	private Pairs.Pair closestPair;

	/* constants for display */

	// dimensions of frame
	private static final int WIDTH=800;
	private static final int HEIGHT=600;
	// min dist between any point and the boundary of the frame
	private static final int BORDER=10; 
	// size of textfield
	private static final int NCOLS=3; 
	// size of point
	private static final int DIAM=12;

	public static void main(String[] args) {
		new GUI();
	}

	/*
	 * Set up components in the frame; add panel as action listener
	 */
	public GUI() {
		menu.add(new JLabel("n:"));
		menu.add(userN);
		menu.add(new JLabel("edge prob:"));
		menu.add(userPr);
		menu.add(cpButton);
		menu.add(graphButton);
		menu.add(dfsButton);
		menu.add(bfsButton);
		menu.add(dijkstraButton);
		cpButton.addActionListener(panel);
		graphButton.addActionListener(panel);
		dfsButton.addActionListener(panel);
		bfsButton.addActionListener(panel);
		dijkstraButton.addActionListener(panel);

		setLayout(new BorderLayout());
		add(menu, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);

		setTitle("Closest Pairs");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
	}

	/*
	 * Generate nNodes Vertex objects with random coordinates, 
	 * stored in ArrayList<Vertex> ivar points
	 */
	private void generateRandomVertices() {
		V.clear();
		closestPair=null;
		// set up vertices with random display coordinates
		for (int i=0; i<nNodes; i++) {
			V.add(new Vertex(rand.nextInt(panel.getWidth()-2*BORDER-DIAM)+BORDER+DIAM/2,
					rand.nextInt(panel.getHeight()-2*BORDER-DIAM)+BORDER+DIAM/2));
		}
	}

	/*
	 * Adds each possible edge with independent probability p
	 */
	private void generateRandomEdges() {
		for (int i=0; i<V.size(); i++) {
			for (int j=i+1; j<V.size(); j++) {
				if (Math.random()<edgeProb) {
					V.get(i).addNeighbor(V.get(j));
					V.get(j).addNeighbor(V.get(i));
				}
			}
		}
	}
	
	/*
	 * Uses DFS to identify all connected components and throws 
	 * out all vertices and adjacent edges except those in the largest. 
	 */
	private void retainOnlyMaxComponent() {
		// label vertices in each connected component 1...k,
		// keeping track of size and ccnum of largest cc
		int cc = 1;
		int maxSize = 0;
		int maxCCNum=1;
		for (Vertex u : V) {
			if (u.getCCNum()==0) { 
				int size = explore(u,cc);
				if (size > maxSize) {
					maxSize = size;
					maxCCNum = cc;
				}
				cc++;
			}
		}

		// iterate through vertices, discarding all but maxCC
		for (int i=0; i<V.size(); i++) {
			Vertex v = V.get(i);
			if (v.getCCNum() != maxCCNum) {
				V.remove(i);
				i--;
			}
		}
	}
	
	/*
	 * Label all vertices reachable from u with cc,
	 * and return the number of vertices visited
	 */
	private int explore(Vertex u, int cc) {
		u.setCCNum(cc);
		int size = 1;
		for (Vertex v : u.getNeighbors()) {
			if (v.getCCNum()==0) {
				size += explore(v,cc);
			}
		}
		return size;
	}


	/* GraphPanel extends JPanel; paints vertices as circles 
	 * and edges as line segments.
	 * NOTE: THIS WILL CHANGE IN SUBSEQUENT LABS.
	 */
	class GraphPanel extends JPanel implements ActionListener {

		/*
		 * Listens for a button click from JFrame.
		 * Calls relevant graph algorithm and repaints.
		 */
		public void actionPerformed(ActionEvent e) {
			// when "Closest Pair" is clicked, call Pairs.closestpair
			if (e.getSource()==cpButton) {
				nNodes=Integer.parseInt(userN.getText());
				generateRandomVertices();
				closestPair = new Pairs().closestPair(V); 
			}
			// when "Get Graph" is clicked, construct graph with user params
			if (e.getSource()==graphButton) {
				nNodes=Integer.parseInt(userN.getText());
				edgeProb=Double.parseDouble(userPr.getText());
				generateRandomVertices();
				generateRandomEdges();
				retainOnlyMaxComponent();
			}
			if (e.getSource()==dfsButton) {
				new Traversal().dfs(V);
				drawPaths = true;
			}
			if (e.getSource()==bfsButton) {
				new Traversal().bfs(V);
				drawPaths = true;
			}
			if (e.getSource()==dijkstraButton) {
				new Traversal().dijkstra(V);
				drawPaths = true;
			}
			// clear old contents and paint new/modified graph
			removeAll();
			repaint();
			revalidate();
		}

		/*
		 * The painting portion of any button click
		 */
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.BLACK);
			// draw all points
			for (Vertex v : V) {
				drawNode(g,v);
			}
			// draw all edges
			for (Vertex u : V) {
				for (Vertex v : u.getNeighbors()) {
					drawEdge(g,u,v);
				}
			}
			// color closest pair red
			if (closestPair != null) {
				g.setColor(Color.RED);
				drawNode(g,closestPair.getP1());
				drawNode(g,closestPair.getP2());
			}
			if (drawPaths) {
				labelDistsDrawRoutes(g);
				drawPaths=false;
			}
		}

		/*
		 * Draws a node in the JPanel as a circle 
		 */
		private void drawNode(Graphics g, Vertex v) {
			Ellipse2D.Double node = new Ellipse2D.Double(v.getX()-DIAM/2,v.getY()-DIAM/2,DIAM,DIAM);
			((Graphics2D) g).fill(node);			
		}
		
		/*
		 * Draws an edge in the JPanel as a line segment
		 */
		private void drawEdge(Graphics g, Vertex u, Vertex v) {
			if (u==null || v==null) return;
			Line2D.Double line = new Line2D.Double(u.getX(),u.getY(),v.getX(),v.getY());
			((Graphics2D) g).draw(line);
		}
		
		/*
		 * Labels each vertex with the value of its dist field 
		 * and colors edges specified by prev pointers red
		 */
		private void labelDistsDrawRoutes(Graphics g) {
			g.setColor(Color.RED);
			for (Vertex v : V) {
				double dist = v.getDist();
				String dstr = ""+(int)dist;
				if (dist==Double.MAX_VALUE) dstr = ""+'\u221e';
				g.drawString(dstr,v.getX()+DIAM/2,v.getY()-DIAM/2);
				drawEdge(g,v.getPrev(),v);
			}
			g.setColor(Color.BLACK);
			g.drawString("Length of red line segments: " + (int)calculateTreeWeight(), BORDER, BORDER);
		}
		
		/*
		 * Calculates the total length of all edges specified by prev pointers
		 */
		private double calculateTreeWeight() {
			double w = 0;
			for (Vertex u : V) {
				if (u.getCCNum()>0 && u.getPrev() != null) {
					w += u.distTo(u.getPrev());
				}
			}
			System.out.println(w);
			return w;
		}

	}

}

