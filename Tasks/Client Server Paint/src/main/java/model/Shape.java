package model;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public abstract class Shape<E> extends JPanel{

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private Color color;
	private int height;
	private int width;
	private String imageLocation;

	public Shape(int startX, int startY) {
		this.startX = startX; 
		this.startY = startY;
		endX = startX;
		endY = startY;
		this.color = Color.BLACK;
		imageLocation = "";
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color newColor) {
		color = newColor;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartX(int newStartX) {
		startX = newStartX;
	}

	public void setStartY(int newStartY) {
		startY = newStartY;
	}

	public void setEndX(int newEndX) {
		endX = newEndX;
	}

	public void setEndY(int newEndY) {
		endY = newEndY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int newHeight) {
		height = newHeight;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int newWidth) {
		width = newWidth;
	}

	public void setImage(String image) {
		this.imageLocation = image;
	}

	public String getImage() {
		return imageLocation;
	}

	abstract public E drawShape(int newX, int newY);

	abstract public void lastForm(int newX, int newY);

	abstract public void draw(Graphics2D g2);
}