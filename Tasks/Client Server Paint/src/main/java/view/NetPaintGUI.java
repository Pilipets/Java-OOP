package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import networking.AddShapeCommand;
import model.Line;
import model.Oval;
import model.Rectangle;
import model.Shape;

public class NetPaintGUI<E> extends JPanel {

	private JRadioButton line, rect, oval;
	private static String currentShapeName;
	private JScrollPane drawingPanel;
	private JPanel canvas = new Canvas();
	private static boolean isDrawing;
	private static int newX;
	private static int newY;
	private int startingY;
	private int startingX;
	private static Shape currentShape;
	private JColorChooser colorChooser;
	private static List<Shape> listOfShapes;
	private Logger logger = Logger.getLogger(NetPaintGUI.class.getName());
	private ObjectOutputStream output; // output stream to server
	private final int XLocation = 100;
	private final int YLocation = 0;
	private final int DX = 250;
	private final int DY = 150;

	public NetPaintGUI(){}

	public NetPaintGUI(ObjectOutputStream output) {

		this.output = output;

		listOfShapes = new ArrayList<Shape>();
		drawingPanel = new JScrollPane(canvas);

		isDrawing = false;

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		this.setSize(d.width - DX, d.height - DY);
		this.setVisible(true);
		this.setLocation(XLocation, YLocation);
		this.setLayout(new BorderLayout());
		this.add(drawingPanel, BorderLayout.CENTER);

		JPanel colors = new JPanel();
		colors.setLayout(new BorderLayout());
		colorChooser = new JColorChooser();
		colors.add(colorChooser, BorderLayout.CENTER);

		line = new JRadioButton("Line");
		rect = new JRadioButton("Rectangle");
		oval = new JRadioButton("Oval");

		JPanel radioButtons = new JPanel();
		radioButtons.setLayout(new FlowLayout());
		radioButtons.add(line);
		radioButtons.add(rect);
		radioButtons.add(oval);

		colors.add(radioButtons, BorderLayout.NORTH);
		this.add(colors, BorderLayout.SOUTH);

		registerListeners();
	}

	private void registerListeners() {

		line.addActionListener(new LineButtonListener());
		rect.addActionListener(new RectButtonListener());
		oval.addActionListener(new OvalButtonListener());

		MouseListener listener = new ListenToMouse();
		MouseMotionListener motionListener = new ListenToMouse();

		canvas.addMouseMotionListener(motionListener);
		canvas.addMouseListener(listener);
	}

	private class LineButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			line.setSelected(true);
			rect.setSelected(false);
			oval.setSelected(false);
			currentShapeName = Line.Name;

		}

	}

	private class RectButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			rect.setSelected(true);
			line.setSelected(false);
			oval.setSelected(false);
			currentShapeName = Rectangle.Name;
		}
	}

	private class OvalButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			oval.setSelected(true);
			line.setSelected(false);
			rect.setSelected(false);
			currentShapeName = Oval.Name;
		}

	}

	private class ListenToMouse implements MouseMotionListener, MouseListener {
		public void mouseClicked(MouseEvent evt) {

		}
		@Override
		public void mouseDragged(MouseEvent e) {

		}
		public void mouseMoved(MouseEvent evt) {

			if (isDrawing) {
				newX = evt.getX();
				newY = evt.getY();
				repaint();
			}
		}

		public void mousePressed(MouseEvent evt) {

			startingX = evt.getX();
			startingY = evt.getY();
			if (currentShapeName != null) {
				if (!isDrawing) {

					if (currentShapeName.equals(Line.Name)) {
						currentShape = new Line(startingX, startingY);
					}
					if (currentShapeName.equals(Oval.Name)) {
						currentShape = new Oval(startingX, startingY);
					}
					if (currentShapeName.equals(Rectangle.Name)) {
						currentShape = new Rectangle(startingX, startingY);
					}
					currentShape.setColor(colorChooser.getColor());
				} else {
					currentShape.lastForm(newX, newY);

					try {
						output.writeObject(new AddShapeCommand(currentShape));
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage());
					}
				}
				isDrawing = !isDrawing;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	}

	public static List<Shape> getListOfShapes() {
		return listOfShapes;
	}

	public static boolean getIsDrawing() {
		return isDrawing;
	}

	public static Shape getCurrentShape() {
		return currentShape;
	}

	public static int getNewX() {
		return newX;
	}

	public static int getNewY() {
		return newY;
	}

	public static String getCurrentShapeName() {
		return currentShapeName;
	}

	public void update(List<Shape> currentShapes) {
		this.listOfShapes = currentShapes;
		repaint();
	}
}
