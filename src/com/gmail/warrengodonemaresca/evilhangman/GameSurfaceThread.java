package com.gmail.warrengodonemaresca.evilhangman;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

@SuppressLint("WrongCall")
public class GameSurfaceThread extends Thread {

	private SurfaceHolder threadSurfaceHolder;
	private GameSurfaceView threadSurfaceView;
	private boolean threadRunning;

	public GameSurfaceThread(SurfaceHolder sHolder, GameSurfaceView gsView){
		threadSurfaceHolder = sHolder;
		threadSurfaceView = gsView;
	}

	public void setRunning(boolean run){
		threadRunning = run;
	}

	@Override
	public void run() {
		while(threadRunning){

			threadSurfaceView.updateGallows();
			
			Canvas canvas = null;

			try {
				canvas = threadSurfaceHolder.lockCanvas(null);
				synchronized (threadSurfaceHolder){
					threadSurfaceView.onDraw(canvas);
				}
				
				sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   finally{
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (canvas != null) {
					threadSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		super.run();
	}
	
	public boolean isRunning(){
		return threadRunning;
	}



}
