package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import model.Line;
import model.Shape;

public class Canvas extends JPanel {

	private final int Width = 5000;
	private final int Height = 4000;
	private final Color backgroundColor = Color.WHITE;

	public Canvas() {
		Dimension maxSize = new Dimension(Width, Height);
		this.setPreferredSize(maxSize);
		this.setBackground(backgroundColor);
		this.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		List<Shape> listOfShapes = NetPaintGUI.getListOfShapes();
		for (Shape s : listOfShapes) {
			s.draw(g2);
		}
		
		if (NetPaintGUI.getIsDrawing()) {
			
			String currentShapeName = NetPaintGUI.getCurrentShapeName();
			Shape currentShape = NetPaintGUI.getCurrentShape();
			int newX = NetPaintGUI.getNewX();
			int newY = NetPaintGUI.getNewY();
			
			g2.setColor(currentShape.getColor());
			
			if (currentShape != null && !currentShapeName.equals(Line.Name)) {
				g2.fill((java.awt.Shape) currentShape.drawShape(newX, newY));
				
			} else if(currentShapeName.equals(Line.Name)) {
				g2.draw((java.awt.Shape) currentShape.drawShape(newX, newY));
			}
		}
	}
}
