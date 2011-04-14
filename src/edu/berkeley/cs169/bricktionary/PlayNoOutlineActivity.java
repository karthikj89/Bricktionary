package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayNoOutlineActivity extends Activity{
	TextView timer; //textview to display the countdown

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(this); 

		timer = new TextView(this);
		LinearLayout.LayoutParams timerLP = new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				0.0f);
		layout.addView(timer, timerLP); //add timer to layout

		//20000 is the starting number (in milliseconds)
		//1000 is the number to count down each time (in milliseconds)
		MyCount counter = new MyCount(20000,1000);
		counter.start();
		Panel myPanel = new Panel(this);
		layout.setOrientation(LinearLayout.VERTICAL); 
		layout.addView(myPanel, new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT, 
				1.0f));  

		//Play Now button
		Button playNowBtn = new Button(this); 
		playNowBtn.setText("Play Now!"); 
        playNowBtn.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		Intent i = new Intent().setClass(PlayNoOutlineActivity.this, PlayActivity.class);
        		startActivity(i);
            }
        });
		LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				0.0f);
		layout.addView(playNowBtn, btnLP); //add button to view
		Drawable bg = this.getResources().getDrawable(R.drawable.brick);
		layout.setBackgroundDrawable(bg);

		setContentView(layout); 

	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		Puzzle puzzle;
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);
			
			puzzle = new Puzzle(GlobalVariables.getCurrentLevel());
		}

		public void onDraw(Canvas canvas){
			BitmapDrawable background;
			background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.brick));
			background.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
			background.draw(canvas);
			
			Paint paint = new Paint();
			paint.setColor(Color.BLACK); 
			
			Display display = getWindowManager().getDefaultDisplay(); 
			int displayWidth = display.getWidth();
			int displayHeight = display.getHeight();

			//Draw puzzle in the background 
			Path puzzlePath = new Path();
			ArrayList<Position> puzzlePositions = puzzle.getSolution();
			for(int i = 0; i < puzzlePositions.size();i++){
				puzzlePath.lineTo(puzzlePositions.get(i).getX(), 
						puzzlePositions.get(i).getY());
			}
			puzzlePath.close();
			puzzlePath.offset(displayWidth/2-50, displayHeight/2-50);
			canvas.drawPath(puzzlePath, paint);
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		public void surfaceCreated(SurfaceHolder holder) {
			_thread.setRunning(true);
			_thread.start();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// simply copied from sample application LunarLander:
			// we have to tell thread to shut down & wait for it to finish, or
			// else
			// it might touch the Surface after we return and explode
			boolean retry = true;
			_thread.setRunning(false);
			while (retry) {
				try {
					_thread.join();
					retry = false;
				} catch (InterruptedException e) {
					// we will try it again and again...
				}
			}
		}
	}

	class ActionThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private Panel _panel;
		private boolean _run = false;

		public ActionThread(SurfaceHolder surfaceHolder, Panel panel) {
			_surfaceHolder = surfaceHolder;
			_panel = panel;
		}

		public void setRunning(boolean run) {
			_run = run;
		}

		public SurfaceHolder getSurfaceHolder() {
			return _surfaceHolder;
		}

		@Override
		public void run() {
			Canvas c;
			while (_run) {
				c = null;
				try {
					c = _surfaceHolder.lockCanvas(null);
					_panel.onDraw(c);
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						_surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

	//Timer class
	class MyCount extends CountDownTimer{
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
    		Intent i = new Intent().setClass(PlayNoOutlineActivity.this, PlayActivity.class);
    		startActivity(i);
		}
		@Override
		public void onTick(long millisUntilFinished) {
			timer.setText("Time Left: "+millisUntilFinished/1000);
		}
	}
}

