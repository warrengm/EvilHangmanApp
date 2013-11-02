package com.gmail.warrengodonemaresca.evilhangman;
public class Pendulum extends Point{


	private double theta, length, angAcc, angVel;

	private int anchorX, anchorY;

	public Pendulum(){}

	/**
	 * Constructor which receives
	 * 
	 * @param swingX
	 * @param swingY
	 * @param anchorX
	 * @param anchorY
	 */
	public Pendulum(int swingX, int swingY, int anchorX, int anchorY){
		x = swingX;
		y = swingY;

		this.anchorX = anchorX;
		this.anchorY = anchorY;

		//XXX MIGHT HAVE TO REVIEW THE FOLLOWING CODE FOR SIGNS
		length = Math.sqrt(Math.pow(x - anchorX, 2) + Math.pow(y - anchorY, 2));
		
		theta = Math.asin((anchorX - x)/length);
	}
	
	public Pendulum(int swingX, int swingY, Point anchor){
		this(swingX, swingY, anchor.getX(), anchor.getY());
	}
	
	public Pendulum(Point swing, int anchorX, int anchorY){
		this(swing.getX(), swing.getY(), anchorX, anchorY);
	}
	
	public Pendulum(Point swing, Point anchor){
		this(swing.getX(), swing.getY(), anchor.getX(), anchor.getY());
	}

	/**
	 * 
	 * @param grav
	 * @param damping For realistic effects, select a value between .9 and 1
	 */
	public void updatePos(double grav, double damping){
		updatePos(grav);
		angVel *= damping;
	}
	
	public void updatePos(double grav){
		angAcc = grav*Math.sin(theta)/(length);
		angVel += angAcc;
		theta -= angVel;
		x = anchorX - (int)(length * Math.sin(theta));
		y = anchorY + (int)(length * Math.cos(theta));
	}
	
	public double getTheta(){
		return theta;
	}
	
	public String getInfo(){
		return theta + " | " + angAcc;
	}
	
	public double getAngVel(){
		return angVel;
	}
	
	public double getAngAcc(){
		return angAcc;
	}
	
	public void setNewAnchorPoint(int x, int y){
		anchorX = x;
		anchorY = y;
	}
	
	public void setNewAnchorPoint(Point anchor){
		anchorX = anchor.getX();
		anchorY = anchor.getY();
	}
	
	public int getAnchorX(){
		return anchorX;
	}
	
	public int getAnchorY(){
		return anchorY;
	}
}
