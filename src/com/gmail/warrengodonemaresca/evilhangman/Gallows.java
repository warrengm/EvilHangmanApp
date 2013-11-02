package com.gmail.warrengodonemaresca.evilhangman;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class Gallows {
	//private Point head, hips, handR, handL, footR, footL;
	private Pendulum rope;
	//Rope contains rope + head

	/**
	 * Base of the neck -- shoulder level.
	 */
	//private Point neck;

	private int headRadius;

	/**
	 * Height from the top of the noose's rope to the bottom of the man's leg's
	 * (which are at an angle).
	 */
	private int height, width;

	private int anchorX, anchorY;
	
	private int ropeX, ropeY, neck, torso, arm, legX, legY;
	
	SurfaceView view;
	
	public Gallows(SurfaceView view){
		this.view = view;
		
		this.height = view.getHeight();
		this.width = view.getWidth();

		init();
	}
	
	private void init(){
		headRadius = (int)(height * 1/15);
		
		anchorX = width * 1/2;
		anchorY = height * 3/20;
		
		rope = new Pendulum(anchorX + 50, anchorY + height/4, 
				anchorX, anchorY);
		
		ropeX = rope.getX();
		ropeY = rope.getY();
		
		neck = height/10;
		torso = (int)(height/4.5);
		
		arm = (int)((torso - neck) * Math.sin(Math.PI/4));
		
		legX = (int)((torso - headRadius) * Math.cos(Math.PI/3));
		legY = (int)((torso - headRadius) * Math.sin(Math.PI/3));
	}
	
	public void drawVerticalBeam(Canvas canvas, Paint paint){
		canvas.drawRect(height/20, 0, height/20 + height/13, height, paint);
	}
	
	public void drawHorizontalBeam(Canvas canvas, Paint paint){
		canvas.drawRect(0, height/10, width, height/10 + height/13, paint);
	}
	
	public void drawSupportBeam(Canvas canvas, Paint paint){
		float strokeWidth = paint.getStrokeWidth();
		paint.setStrokeWidth(height / 20);
		canvas.drawLine(height/10, height * 2/5, width/3, height/8, paint);
		paint.setStrokeWidth(strokeWidth);
	}
	
	public void drawRope(Canvas canvas, Paint paint){
		canvas.drawLine(anchorX, anchorY, rope.getX(), rope.getY(), paint);
	}
	
	public void drawHead(Canvas canvas, Paint paint){
		/*paint.setColor(Color.parseColor
				(view.getResources().getString(R.color.white)));*/
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(ropeX, ropeY, headRadius, paint);
		
		/*paint.setColor(Color.parseColor
				(view.getResources().getString(R.color.background_color)));*/
		paint.setStyle(Paint.Style.STROKE);
		//canvas.drawCircle(rope.getX(), rope.getY(), headRadius, paint);*/
	}
	
	public void drawTorso(Canvas canvas, Paint paint){
		canvas.drawLine(ropeX, ropeY , ropeX,
				ropeY + torso, paint);
	}
	
	/**
	 * Precondition: paint is set to stroke style.
	 */
	public void drawRightArm(Canvas canvas, Paint paint){
		canvas.drawLine(ropeX, ropeY + neck, ropeX - arm, ropeY + neck + arm, 
				paint);
	}
	
	public void drawLeftArm(Canvas canvas, Paint paint){
		canvas.drawLine(ropeX, ropeY + neck, ropeX + arm, ropeY + neck + arm, 
				paint);
	}
	
	public void drawRightLeg(Canvas canvas, Paint paint){
		canvas.drawLine(ropeX, ropeY + torso, ropeX - legX, 
				ropeY + torso + legY, paint);
	}
	
	public void drawLeftLeg(Canvas canvas, Paint paint){
		canvas.drawLine(ropeX, ropeY + torso, ropeX + legX, 
				ropeY + torso + legY, paint);
	}
	
	public void update(){
		rope.updatePos(.5, .9995);

		ropeX = rope.getX();
		ropeY = rope.getY();
	}
	
	public void reset(){
		init();
	}
	
}
