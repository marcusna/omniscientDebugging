package de.unipaderborn.visuflow.graphdisplaycomponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.SinkAdapter;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

public class GraphManager implements Runnable {

	Graph graph;
	String styleSheet;
	private Viewer viewer;
	private ViewPanel view;

	ViewerPipe fromViewer;

	Container panel;
	JApplet applet;
	JButton zoomInButton, zoomOutButton, viewCenterButton, filterGraphButton;
	JToolBar settingsBar;
	JTextField attribute;
	JScrollPane scrollbar;
	
	double zoomInDelta, zoomOutDelta, maxZoomPercent, minZoomPercent;
	
	public GraphManager(String graphName, String styleSheet)
	{
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.zoomInDelta = .2;
		this.zoomOutDelta = .2;
		this.maxZoomPercent = .5;
		this.minZoomPercent = 2.0;
		this.styleSheet = styleSheet;
		createGraph(graphName);
		createUI();
	}

	public Container getApplet() {
		return applet.getRootPane();
	}

	void createGraph(String graphName)
	{
		graph = new MultiGraph(graphName);
		graph.addAttribute("ui.stylesheet", styleSheet);

		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		view = viewer.addDefaultView(false);

//		viewer.enableAutoLayout(new HierarchicalLayout());
		viewer.enableAutoLayout();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
	}

	private void createUI() {
		// TODO Auto-generated method stub
		createZoomControls();
		createViewListeners();
		createAttributeControls();
		createSettingsBar();
		createPanel();
		createAppletContainer();
	}

	private void createAppletContainer() {
		// TODO Auto-generated method stub
		applet = new JApplet();
		
		scrollbar = new JScrollPane(panel);
		applet.add(scrollbar);
	}

	private void createAttributeControls() {
		// TODO Auto-generated method stub
		attribute = new JTextField("ui.screenshot,C:/Users/Shashank B S/Desktop/image.png");
		filterGraphButton = new JButton("SetAttribute");
		
		filterGraphButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String[] newAttribute = attribute.getText().split(",");
				graph.setAttribute(newAttribute[0], newAttribute[1]);
			}
		});
	}

	private void createSettingsBar() {
		// TODO Auto-generated method stub
		settingsBar = new JToolBar("ControlsBar", JToolBar.HORIZONTAL);
		
		settingsBar.add(zoomInButton);
		settingsBar.add(zoomOutButton);
		settingsBar.add(viewCenterButton);
		settingsBar.add(filterGraphButton);
		settingsBar.add(attribute);
	}

	private void createPanel() {
		// TODO Auto-generated method stub
		panel = new JFrame().getContentPane();
		panel.add(view);
		panel.add(settingsBar, BorderLayout.PAGE_START);
	}

	private void createViewListeners() {
		// TODO Auto-generated method stub
		view.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				int rotationDirection = e.getWheelRotation();
				double viewPercent = view.getCamera().getViewPercent();
				if(rotationDirection > 0 && viewPercent > maxZoomPercent)
				{
					view.getCamera().setViewPercent(viewPercent - zoomInDelta);
				}
				else if(viewPercent < minZoomPercent)
				{
					view.getCamera().setViewPercent(viewPercent + zoomOutDelta);
				}
			}
		});
		
		view.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton() == 0)
				{
					Point dest = e.getPoint();
					System.out.println("dragged with button");
					System.out.println(dest);
				}
			}
		});
	}

	private void createZoomControls() {
		// TODO Auto-generated method stub
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		viewCenterButton = new JButton("reset");

		zoomInButton.setBackground(Color.gray);
		zoomInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				double viewPercent = view.getCamera().getViewPercent();
				if(viewPercent > maxZoomPercent)
					view.getCamera().setViewPercent(viewPercent - zoomInDelta);
			}
		});

		zoomOutButton.setBackground(Color.gray);
		zoomOutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				double viewPercent = view.getCamera().getViewPercent();
				if(viewPercent < minZoomPercent)
					view.getCamera().setViewPercent(viewPercent + zoomOutDelta);
			}
		});

		viewCenterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				view.getCamera().resetView();
			}
		});
	}

	void generateTestGraph()
	{
		graph.setStrict(false);
		graph.setAutoCreate( true );
		for (int i = 0; i < 50; i++) {
			String source = i + "";
			int temp = i + 1;
			String destination = temp + "";
			graph.addEdge(source+destination, source, destination);
			graph.addEdge(i+"", source, destination, true);
		}
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
	}

	/*void generateGraphFromGraphStructure(GraphStructure graphStructure)
	{
		Iterator<Entry<Unit, Integer>> graphNodesIterator = graphStructure.nodesMap.entrySet().iterator();
		Iterator<Entry<Unit, Integer>> graphEdgesIterator = graphStructure.nodesMap.entrySet().iterator();
		
		while(graphNodesIterator.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry) graphNodesIterator.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        graph.addNode(pair.getValue().toString());
//	        graphNodesIterator.remove(); // avoids a ConcurrentModificationException
		}
		while(graphEdgesIterator.hasNext())
		{
			Map.Entry pair = (Map.Entry) graphEdgesIterator.next();
			
		}
	}*/
	
	void generateGraphFromGenerator()
	{
		BaseGenerator gen  = new LobsterGenerator();
		gen.setDirectedEdges(true, false);
		gen.addNodeLabels(true);
		gen.addSink(graph);

		gen.begin();
		for (int i = 0; i < 100; i++) {
			gen.nextEvents();
		}
		gen.end();

		fromViewer = viewer.newViewerPipe();
		fromViewer.addSink(graph);

		fromViewer.addSink(new SinkAdapter(){
			@Override
			public void nodeAttributeAdded(String sourceId, long timeId, String nodeId, String attribute, Object value) {
				if(attribute.equals("ui.clicked")){
					toggleNode(nodeId);
				}
			}

			@Override
			public void nodeAttributeChanged(String sourceId, long timeId, String nodeId, String attribute, Object oldValue, Object newValue) {
				if(attribute.equals("ui.clicked")){
					toggleNode(nodeId);
				}
			}
		});
	}

	void toggleNode(String id){
		Node n  = graph.getNode(id);
		Object[] pos = n.getAttribute("xyz");
		Iterator<Node> it = n.getBreadthFirstIterator(true);
		if(n.hasAttribute("collapsed")){
			n.removeAttribute("collapsed");
			while(it.hasNext()){
				Node m  =  it.next();

				for(Edge e : m.getLeavingEdgeSet()) {
					e.removeAttribute("ui.hide");
				}
				m.removeAttribute("layout.frozen");
				m.setAttribute("x",((double)pos[0])+Math.random()*0.0001);
				m.setAttribute("y",((double)pos[1])+Math.random()*0.0001);

				m.removeAttribute("ui.hide");

			}
			n.removeAttribute("ui.class");

		} else {
			n.setAttribute("ui.class", "plus");
			n.setAttribute("collapsed");

			while(it.hasNext()){
				Node m  =  it.next();

				for(Edge e : m.getLeavingEdgeSet()) {
					e.setAttribute("ui.hide");
				}
				if(n != m) {
					m.setAttribute("layout.frozen");
					m.setAttribute("x", ((double) pos[0]) + Math.random() * 0.0001);
					m.setAttribute("y", ((double) pos[1]) + Math.random() * 0.0001);

					m.setAttribute("ui.hide");
				}

			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//		generateTestGraph();
		generateGraphFromGenerator();
		while(true)
			fromViewer.pump();
	}
}