package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape{
	public final static String Name = "rect";

	public Rectangle(int startX, int startY) {
		super(startX, startY);
	}

	@Override
	public Rectangle2D drawShape(int newX, int newY) {

		Rectangle2D rect = null;
		
		// Quadrant 4
		if(newX > getStartX() && newY > getStartY()) {
			rect = new Rectangle2D.Double(getStartX(), getStartY(), newX - getStartX(), newY - getStartY());
		// Quadrant 3
		}else if(newX < getStartX() && newY > getStartY()) {
			rect = new Rectangle2D.Double(newX, getStartY(), getStartX() - newX, newY - getStartY());
		// Quadrant 2
		}else if(newX > getStartX() && newY < getStartY()) {
			rect = new Rectangle2D.Double(getStartX(), newY, newX - getStartX(), getStartY() - newY);
		// Quadrant 1
		}else{ //newX < getX() && newY < getY() 
			rect = new Rectangle2D.Double(newX, newY,getStartX() - newX, getStartY() - newY);
		}
		return rect;
	}

	public void lastForm(int newX, int newY) {
		// Quadrant 4
		if(newX > getStartX() && newY > getStartY()) {
			setWidth(newX - getStartX());
			setHeight(newY - getStartY());
			setStartX(getStartX());
			setStartY(getStartY());
		// Quadrant 3
		}else if(newX < getStartX() && newY > getStartY()) {
			setWidth(getStartX() - newX);
			setHeight(newY - getStartY());
			setStartX(newX);
			setStartY(getStartY());
		// Quadrant 2
		}else if(newX > getStartX() && newY < getStartY()) {
			setWidth(newX - getStartX());
			setHeight(getStartY() - newY);
			setStartX(getStartX());
			setStartY(newY);
		// Quadrant 1
		}else{ //newX < getX() && newY < getY() 
			setWidth(getStartX() - newX);
			setHeight(getStartY() - newY);
			setStartX(newX);
			setStartY(newY);
		}
		
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(getColor());
		g2.fillRect(getStartX(),getStartY(),getWidth(), getHeight());
		
	}

}
