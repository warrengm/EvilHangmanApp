package com.gmail.warrengodonemaresca.evilhangman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private GameSurfaceThread thread;

	private Gallows gallows;

	private Paint paint;//TODO

	private int guesses;

	public GameSurfaceView(Context context) {
		super(context);

		start();
	}

	public GameSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);

		start();
	}

	public GameSurfaceView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		start();
	}

	private void start() {
		getHolder().addCallback(this);
		thread = new GameSurfaceThread(getHolder(), this);

		setFocusable(true);//TODO Remove?

		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHoder) {
		gallows = new Gallows(this);
		try {
			thread.start();
		} catch(IllegalThreadStateException e){
			thread = new GameSurfaceThread(getHolder(), this);
			thread.start();
			
			//invalidate();
		}
		
		thread.setRunning(true);
		paint.setStrokeWidth((int)(this.getHeight()/70));
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			}
			catch (InterruptedException e){}
		}
	}

	@Override
	protected void onDraw(Canvas canvas){
		if(thread.isRunning()){
			canvas.drawColor(getResources().getColor(R.color.white));
			paint.setColor(getResources().getColor(R.color.background_color));

			switch(guesses){
			case -1: case 0:
				gallows.drawRope(canvas, paint);
			case 1:
				paint.setStyle(Paint.Style.STROKE);
				gallows.drawLeftLeg(canvas, paint);
			case 2:
				gallows.drawRightLeg(canvas, paint);
			case 3:
				gallows.drawLeftArm(canvas, paint);
			case 4:
				gallows.drawRightArm(canvas, paint);
			case 5:
				gallows.drawTorso(canvas, paint);
			case 6:
				//draw eyes
			case 7:
				gallows.drawHead(canvas, paint);
			case 8: 
				paint.setStyle(Paint.Style.FILL);//TODO REMOVE REPLICATION
				gallows.drawSupportBeam(canvas, paint);
			case 9: 
				paint.setStyle(Paint.Style.FILL);
				gallows.drawHorizontalBeam(canvas, paint);
			case 10: 
				paint.setStyle(Paint.Style.FILL);
				gallows.drawVerticalBeam(canvas, paint);
			}
		}
	}

	public void updateGallows(){
		if(guesses < 0){
			gallows.update();	
		}
	}
	
	public void resetGallows(){
		gallows.reset();
	}

	public void setGuesses(int guesses){
		this.guesses = guesses;
		
		invalidate();
	}
	

	public void pause(){
		thread.setRunning(false);
	}
	
	public void resume(){
		thread.setRunning(true);
	}

}
