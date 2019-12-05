package model;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Line extends Shape{

	public final static String Name = "line";
	public Line(int startX, int startY) {
		super(startX, startY);
		
	}

	@Override
	public Line2D drawShape(int newX, int newY) {
		Line2D line = new Line2D.Double(getStartX(), getStartY(), newX, newY);
		return line;
		
	}

	public void lastForm(int newX, int newY) {
		setEndX(newX);
		setEndY(newY);
		
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(getColor());
		g2.drawLine(getStartX(),getStartY(), getEndX(),getEndY());
		
	}
}
