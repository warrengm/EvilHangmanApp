package com.gmail.warrengodonemaresca.evilhangman;

/**
 * A point particle with position and velocity. This code does not provide
 * for external forces (gravity, friction, etc.).
 * 
 * @author Warren
 *
 */
public class Point {
	/**
	 * Incremental velocity components.
	 */
	private int dx, dy; // ints or doubles?
	
	/**
	 * x and y coordinates
	 */
	protected int x, y;
	
	
	/**
	 * Coefficient of restitution/elasticity. The energy lost in a collision.
	 */
	private double elasticity = 1;
	
	/**
	 * Default constructor that does nothing.
	 */
	public Point(){}
	
	/**
	 * Constructor which sets the coordinates.
	 */
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor which sets the coordinates and velocity components.
	 */
	public Point(int x, int y, int dx, int dy){
		this(x, y);
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Constructor which sets the coordinates, velocity, and elasticity.
	 */
	public Point(int x, int y, int dx, int dy, double elasticity){
		this(x, y, dx, dy);
		this.elasticity = elasticity;
	}
	
	/**
	 * Sets the x coordinate.
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**
	 * Returns the x coordinate.
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Sets the Y coordinate.
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * Returns the y coordinate.
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the x velocity component.
	 */
	public void setDX(int dx){
		this.dx = dx;
	}
	
	/**
	 * Returns the x velocity component
	 */
	public int getDX(){
		return dx;
	}
	
	/**
	 * Sets the y velocity component.
	 */
	public void setDY(int dy){
		this.dy = dy;
	}
	
	/**
	 * Returns the y velocity component
	 */
	public int getDY(){
		return dy;
	}
	
	/**
	 * Sets the coefficient of elasticity.
	 */
	public void setElasticity(double elasticity){
		this.elasticity = elasticity;
	}
	
	/**
	 * Returns the coefficient of elasticity/restitution.
	 */
	public double getElasticity(){
		return elasticity;
	}
	
	/**
	 * Reverses the velocity in the x dimension and reduces it according to
	 * the elasticity;
	 */
	public void bounceX(){
		dx *= -elasticity;
	}
	
	/**
	 * Reverses the velocity in the y dimension and reduces it according to
	 * the elasticity;
	 */
	public void bounceY(){
		dy *= -elasticity;
	}
	
	/**
	 * Updates the x coordinate based on dx
	 */
	public void updatePosX(){
		x += dx;
	}
	
	/**
	 * Updates the x coordinate based on dy
	 */
	public void updatePosY(){
		y += dy;
	}
} //end
