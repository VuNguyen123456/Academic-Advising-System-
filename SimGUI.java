//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************

import java.lang.reflect.*;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.*;

import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

import org.apache.commons.collections15.*;
import java.awt.geom.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;

/**
 *  GUI for forest interactions.
 *  
 *  @author Katherine (Raven) Russell
 */
class SimGUI {
	/**
	 *  Frame for the GUI.
	 */
	private JFrame frame;
	
	/**
	 *  The panel containing the load buttons.
	 */
	private JPanel buttonPanel = null;
	
	/**
	 *  Graph to start the display.
	 */
	private ThreeTenGraph<String> graph = null;
	
	/**
	 *  The initial starting node.
	 */
	private String startNode = null;
	
	/**
	 *  The last output of the topological sort.
	 */
	private LinkedList<String> sortedOrder = null;
	
	/**
	 *  Graph visualization server.
	 */
	private VisualizationViewer<String, Destination<String>> visServer;
	
	/**
	 *  Sort visualization server.
	 */
	private VisualizationViewer<String, Destination<String>> sortVisServer;
	
	/**
	 *  Graph visualization server controls.
	 */
	private EditingModalGraphMouse gm = null;
	
	/**
	 *  Node internals display.
	 */
	private ImagePanel imagePanel = null;
	
	/**
	 *  If a new sort has been created.
	 */
	private boolean newSort = false;
	
	/**
	 *  Load up the GUI.
	 *  @param graph the default graph to load
	 *  @param startNode the default starting node
	 */
	public SimGUI(ThreeTenGraph<String> graph, String startNode) {
		this.graph = graph;
		this.startNode = startNode;
		
		frame = new JFrame("Topological Sorting");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		frame.getContentPane().setLayout(new BorderLayout());
		
		resetDisplay();
		
		frame.setVisible(true);
	}
	
	/**
	 *  Load a new simulation.
	 */
	public void resetDisplay() {
		frame.getContentPane().removeAll();
		
		JPanel panel = new JPanel(new BorderLayout());
		addGraphAndSort(panel);
		frame.add(panel, BorderLayout.CENTER);
		
		frame.getContentPane().repaint();
		frame.revalidate();
		
		//menu needs to go after gm is set (if any)
		frame.setJMenuBar(null);
		frame.setJMenuBar(makeMenu(panel));
		makeBottomButtons(panel);
		
	}
	
	/**
	 *  Makes the menu for the simulation.
	 *  
	 *  @param panel the panel to reset when clicking
	 *  @return the menu created
	 */
	public JMenuBar makeMenu(JPanel panel) {
		JMenuBar menuBar = new JMenuBar();
		
		//exit option
		JMenu simMenu = new JMenu("Simulation");
		simMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		simMenu.add(exit);
		menuBar.add(simMenu);
		
		//graph editing options
		JMenu modeMenu = gm.getModeMenu();
		modeMenu.setText("Mode");
		modeMenu.setIcon(null); // I'm using this in a main menu
		modeMenu.setPreferredSize(new Dimension(50,20)); // Change the size 
		modeMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				addOneArray("", null, panel);
			}

			@Override
			public void menuDeselected(MenuEvent e) { }

			@Override
			public void menuCanceled(MenuEvent e) { }
		});
		menuBar.add(modeMenu);
		
		return menuBar;
	}
	
	/**
	 *  Creates one graph panel with an topological sort viewer at the bottom.
	 *  
	 *  @param panel the panel to add it to
	 */
	public void addGraphAndSort(JPanel panel) {
		addOneArray("", null, panel);
		addSort(panel);
		addGraph(panel);
	}
	
	/**
	 *  Creates one graph on the panel.
	 *  
	 *  @param panel the panel to add it to
	 */
	public void addGraph(JPanel panel) {
		if(visServer != null) {
			panel.add(visServer, BorderLayout.CENTER);
			return;
		}
		
		int width = frame.getWidth()-6;
		int height = frame.getHeight()/8*5;
		
		//make new ones
		Layout<String, Destination<String>> layout = new ISOMLayout<String, Destination<String>>(graph);
		layout.setSize(new Dimension(width, height));
		visServer = new VisualizationViewer<>(layout);
		visServer.setPreferredSize(new Dimension(width,height));
		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<String, Destination<String>> context = visServer.getRenderContext();
		
		addIteractions(panel);
		
		//label edges with toString()
		context.setEdgeLabelTransformer(
			new Transformer<Destination<String>,String>(){
				public String transform(Destination<String> e) {
					return ""+e.priority;
				}
			}
		);
		
		//make edge text bigger
		context.setEdgeFontTransformer(
			new Transformer<Destination<String>,Font>(){
				public Font transform(Destination<String> v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		//set edge line stroke to bolder
		context.setEdgeStrokeTransformer(
			new Transformer<Destination<String>,Stroke>(){
				public Stroke transform(Destination<String> e) {
					return new BasicStroke(3);
				}
			}
		);
		
		//make nodes bigger
		context.setVertexShapeTransformer(
			new Transformer<String,Shape>(){
				public Shape transform(String v) {
					int s = 100;
					return new Ellipse2D.Double(-s/2.0, -s/4.0, s, s/2);
				}
			}
		);
		
		//make lines straight
		//context.setEdgeShapeTransformer(new EdgeShape.Line<>());
		
		//put text in middle
		context.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(0.5, 0.5));
		
		//move edge labels off the lines
		context.setLabelOffset(-10);
		
		//label vertices with toString()
		context.setVertexLabelTransformer(
			new Transformer<String,String>(){
				public String transform(String v) {
					return v;
				}
			}
		);
		
		//color vertices white
		context.setVertexFillPaintTransformer(
			new Transformer<String,Paint>(){
				public Paint transform(String v) {
					return Color.WHITE;
				}
			}
		);
		
		//make node text bigger
		context.setVertexFontTransformer(
			new Transformer<String,Font>(){
				public Font transform(String v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		//Add user interactions
		gm = new EditingModalGraphMouse<>(context, getNodeFactory(), getEdgeFactory());
		gm.setMode(ModalGraphMouse.Mode.EDITING);
		visServer.setGraphMouse(gm);
		
		panel.add(visServer, BorderLayout.CENTER);
	}
	
	/**
	 * Adds picking interactions.
	 *  
	 * @param panel the panel to add it to
	 */
	private void addIteractions(JPanel panel) {
		visServer.getPickedVertexState().addItemListener(new ItemListener() {
			@SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String vertex = (String) e.getItem();
					
					try{
						Field field1 = ThreeTenGraph.class.getDeclaredField("adjHeap");
						field1.setAccessible(true);
						
						LinkedHashMap<String,MinHeap<Destination<String>>> map = (LinkedHashMap<String,MinHeap<Destination<String>>>) field1.get(graph);
						
						Field field2 = MinHeap.class.getDeclaredField("queue");
						field2.setAccessible(true);
						
						Comparable<Destination<String>>[] queue = (Comparable<Destination<String>>[]) field2.get(map.get(vertex));
						addOneArray(vertex, queue, panel);
					}
					catch(IllegalAccessException e2) {
						
					}
					catch(NoSuchFieldException e2) {
						
					}
				}
				else {
					addOneArray("", null, panel);
				}
			}
		});
	}
	
	/**
	 *  Creates one topological sort viewer.
	 *  
	 *  @param panel the panel to add it to
	 */
	public void addSort(JPanel panel) {
		if(newSort == false && sortVisServer != null) {
			panel.add(sortVisServer, BorderLayout.SOUTH);
			return;
		}
		else if(sortVisServer != null) {
			panel.remove(sortVisServer);
			panel.revalidate();
		}
		newSort = false;
		
		int width = frame.getWidth()-6;
		int height = frame.getHeight()/8*2;
		
		//make new ones
		ThreeTenGraph<String> sort = new ThreeTenGraph<>();
		if(sortedOrder != null && sortedOrder.size() > 0) {
			ListIterator<String> itr = sortedOrder.listIterator(0);
			while(itr.hasNext()) {
				sort.addVertex(itr.next());
			}
			
			itr = sortedOrder.listIterator(0);
			while(itr.hasNext()) {
				String nextNode = itr.next();
				if(itr.hasNext()) {
					String nextNode2 = itr.next();
					sort.addEdge(new Destination<String>(null, 1), nextNode, nextNode2);
					itr.previous();
				}
			}
		}
		else { sort.addVertex("Click Button For Sort"); }
		
		Layout<String, Destination<String>> layout = new DAGLayout<String, Destination<String>>(sort);
		layout.setSize(new Dimension(width, height));
		sortVisServer = new VisualizationViewer<>(layout);
		sortVisServer.setPreferredSize(new Dimension(width,height));
		
		sortVisServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<String, Destination<String>> context = sortVisServer.getRenderContext();
		
		//label edges with toString()
		context.setEdgeLabelTransformer(
			new Transformer<Destination<String>,String>(){
				public String transform(Destination<String> e) {
					return "then";
				}
			}
		);
		
		//set edge line stroke to bolder
		context.setEdgeStrokeTransformer(
			new Transformer<Destination<String>,Stroke>(){
				public Stroke transform(Destination<String> e) {
					return new BasicStroke(3);
				}
			}
		);
	
		//make edge text bigger
		context.setEdgeFontTransformer(
			new Transformer<Destination<String>,Font>(){
				public Font transform(Destination<String> v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		//make nodes bigger
		context.setVertexShapeTransformer(
			new Transformer<String,Shape>(){
				public Shape transform(String v) {
					int s = 100;
					return new Ellipse2D.Double(-s/2.0, -s/4.0, s, s/2);
				}
			}
		);
		
		//make lines straight
		context.setEdgeShapeTransformer(new EdgeShape.Line<>());
		
		//put text in middle
		context.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(0.5, 0.5));
		
		//move edge labels off the lines
		context.setLabelOffset(5);
		
		//label vertices with toString()
		context.setVertexLabelTransformer(
			new Transformer<String,String>(){
				public String transform(String v) {
					return v;
				}
			}
		);
		
		//color vertices white
		context.setVertexFillPaintTransformer(
			new Transformer<String,Paint>(){
				public Paint transform(String v) {
					return Color.WHITE;
				}
			}
		);
		
	
		//make node text bigger
		context.setVertexFontTransformer(
			new Transformer<String,Font>(){
				public Font transform(String v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		DefaultModalGraphMouse model = new DefaultModalGraphMouse();
		model.setMode(ModalGraphMouse.Mode.PICKING);
		sortVisServer.setGraphMouse(model);
		
		panel.add(sortVisServer, BorderLayout.SOUTH);
	}
	
	/**
	 *  Tracks the nodes made.
	 */ 
	static int nodeCount = 0;
	
	/**
	 *  Generates a new node with a (probably) unique id.
	 *  
	 *  @return a factory for new nodes
	 */
	public Factory<String> getNodeFactory() {
		return new Factory<String> () { 
			/**
			 *  Creates a new node.
			 *  @return new node string
			 */ 
			public String create() {
				return new String("Node"+nodeCount++);
			}
		};
	}
	
	/**
	 *  Generates a new edge (priority) with a random value from 1-10.
	 *  
	 *  @return a factory for new edges
	 */
	public Factory<Destination<String>> getEdgeFactory() {
		return new Factory<Destination<String>> () { 
			/**
			 *  Creates a new edge (destination object).
			 *  @return new edge
			 */ 
			public Destination<String> create() {
				return new Destination<String>(null, 1 + (int)(Math.random() * 10));
			}
		};
	}
	
	/**
	 *  Makes the panel containing the step, reset, and play buttons.
	 *  @param panel the panel to sorts to
	 */
	public void makeBottomButtons(JPanel panel) {
		if(buttonPanel != null) frame.remove(buttonPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		JButton chooseStartNode = new JButton("Choose Start");
		chooseStartNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				startNode = JOptionPane.showInputDialog("Enter a starting node:",startNode);
			}
		});
		buttonPanel.add(chooseStartNode);
		
		JButton runButton = new JButton("Run Topological Sort");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				while(!graph.containsVertex(startNode)) {
					startNode = JOptionPane.showInputDialog("Invalid Start Node. Enter a new node:",startNode);
				}
				try {
					sortedOrder = TopologicalSort.topologicalSort(graph,startNode);
					newSort = true;
					addSort(panel);
				}
				catch(IllegalArgumentException e) {
					JOptionPane.showMessageDialog(frame, "Graph Contains a Cycle. Remove it to do a topological sort!", "Bad :(", JOptionPane.ERROR_MESSAGE);
					sortedOrder = null;
				}
			}
		});
		buttonPanel.add(runButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.revalidate();
	}
		
	/**
	 * The image panel with the graphics.
	 */
	private static class ImagePanel extends JPanel {
		
		/**
		 *  The image itself.
		 */
		private BufferedImage image;
		
		/**
		 *  Creates an image panel from an image.
		 *  
		 *  @param image the image to use for drawing
		 */
		public ImagePanel(BufferedImage image) { setImage(image); }
		
		/**
		 * Set the image to be displayed.
		 * @param image the image to display
		 */
		public void setImage(BufferedImage image) {
			this.image = image;
			this.setAlignmentY(Component.TOP_ALIGNMENT);
			this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}
	
	/**
	 *  Creates one array panel.
	 *  
	 *  @param label the top label for the array
	 *  @param arr the array to draw
	 *  @param panel the panel to put it in
	 */
	@SuppressWarnings("unchecked")
	public void addOneArray(String label, Comparable<Destination<String>>[] arr, JPanel panel) {
		int imageHeight = frame.getHeight()/8*1;
		int imageWidth = frame.getWidth()-6;
		
		int arrayHeight = imageHeight/3*2;
		int arrayLine1 = imageHeight/3*1;
		
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		
		//setup graphics for drawing
		Graphics2D g = image.createGraphics();
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLACK);
		
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		//make the image white to start with
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, image.getWidth(), image.getHeight());
		
		//draw the outline of the array (gray with black box around)
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, arrayLine1, imageWidth, arrayHeight);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, arrayLine1, imageWidth, arrayHeight);
		
		//label them
		g.drawString(label, 4, arrayLine1 - 16);
		
		//draw the actual array?
		if(arr != null && arr.length > 0) {
			//how big should the boxes be?
			int colWidth = imageWidth / arr.length;
			if(colWidth > arrayHeight) colWidth = arrayHeight; //max = a square
			
			for(int currCol = 0; currCol < arr.length; currCol++) {
				g.setColor(Color.WHITE);
				g.fillRect(currCol * colWidth, arrayLine1, colWidth, arrayHeight);
				g.setColor(Color.BLACK);
				g.drawRect(currCol * colWidth, arrayLine1, colWidth, arrayHeight);
				
				g.setFont(new Font("SansSerif", Font.BOLD, 12));
				String dataString = ((arr[currCol] == null) ? "n" : ((Destination<String>)arr[currCol]).node);
				int dataStringWidth = g.getFontMetrics().stringWidth(dataString);
				g.drawString(dataString, currCol * colWidth + colWidth/2 - dataStringWidth/2, arrayLine1 + arrayHeight/2 + 6);
				
				g.setFont(new Font("SansSerif", Font.BOLD, 8));
				String indexString = ""+currCol;
				int indexStringWidth = g.getFontMetrics().stringWidth(indexString);
				g.drawString(indexString, currCol * colWidth + colWidth/2 - indexStringWidth/2, arrayLine1 + arrayHeight + 8);
			}
		}
		
		if(imagePanel == null) {
			imagePanel = new ImagePanel(image);
		}
		else {
			imagePanel.setImage(image);
		}
		
		imagePanel.repaint();
		panel.add(imagePanel, BorderLayout.NORTH);
		
		g.setStroke(oldStroke);
		
	}
}
