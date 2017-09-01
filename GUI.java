/*
 * File: GraphProgram.java
 * Author: Sara Krehbiel
 * -----------------------------------------------------------------
 * This GUI wrapper will be modified throughout the semester.
 * Now it displays n random points (the user decides n), and 
 * when the user clicks "Closest pair," it calls the D&Q 
 * closest pair algorithm, which you should implement in Pairs.java, 
 * and it highlights the returned pair. DO NOT MODIFY THIS FILE.
 *  
 * You SHOULD implement the closest pair algorithm in Pairs.java, 
 * and then submit Pairs.java with your name in the author line
 * before the assignment deadline.  If you modify Vertex.java, you 
 * should submit this as well. See the lab handout for details.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

public class GUI extends JFrame {

    /* private ivars for random points */
    private ArrayList<Vertex> points = new ArrayList<Vertex>();
    private int nNodes = 18;
    private Random rand = new Random();

    /* private ivars for GUI */
    private GraphPanel panel = new GraphPanel();
    private JPanel menu = new JPanel(new FlowLayout());
    private JTextField userN = new JTextField("" + nNodes, NCOLS);
    private JButton startButton = new JButton("Get Points");
    private JButton cpButton = new JButton("Closest Pair");

    /* constants for display */
    // dimensions of frame
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // min dist between any point and the boundary of the frame
    private static final int BORDER = 10;
    // size of textfield
    private static final int NCOLS = 3;
    // size of point
    private static final int DIAM = 12;

    public static void main(String[] args) {
        new GUI();
    }

    /*
	 * Set up components in the frame; add panel as action listener
     */
    public GUI() {
        menu.add(new JLabel("n:"));
        menu.add(userN);
        menu.add(startButton);
        menu.add(cpButton);
        startButton.addActionListener(panel);
        cpButton.addActionListener(panel);

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
    private void generateRandomPoints() {
        points.clear();
        closestPair = null;
        // set up vertices with random display coordinates
        for (int i = 0; i < nNodes; i++) {
            points.add(new Vertex(rand.nextInt(panel.getWidth() - 2 * BORDER - DIAM) + BORDER + DIAM / 2,
                    rand.nextInt(panel.getHeight() - 2 * BORDER - DIAM) + BORDER + DIAM / 2));
        }
    }

    /* GraphPanel extends JPanel; paints points as circles.
	 * NOTE: THIS WILL CHANGE IN SUBSEQUENT LABS.
     */
    class GraphPanel extends JPanel implements ActionListener {

        /*
		 * Listens for a button click from JFrame.
		 * Calls relevant graph algorithm and repaints.
         */
        public void actionPerformed(ActionEvent e) {
            // when "Get Graph" is clicked, construct graph with user params
            if (e.getSource() == startButton) {
                nNodes = Integer.parseInt(userN.getText());
                generateRandomPoints();
            }
            // when "Closest Pair" is clicked, call Pairs.closestpair
            if (e.getSource() == cpButton) {
                closestPair = new Pairs().closestPair(points);
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
            for (Vertex v : points) {
                drawNode(g, v);
            }
            // color closest pair red
            if (closestPair != null) {
                g.setColor(Color.RED);
                drawNode(g, closestPair.getP1());
                drawNode(g, closestPair.getP2());
            }
        }

        private void drawNode(Graphics g, Vertex v) {
            Ellipse2D.Double node = new Ellipse2D.Double(v.getX() - DIAM / 2, v.getY() - DIAM / 2, DIAM, DIAM);
            ((Graphics2D) g).fill(node);
        }

    }

    private Pairs.Pair closestPair;

}
