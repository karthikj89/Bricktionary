package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;

import edu.berkeley.cs169.bricktionary.PlayActivity.Panel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
		layout.addView(timer, timerLP);

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

		//Submit button
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
		btnLP.leftMargin = 350; //set button position
		layout.addView(playNowBtn, btnLP);
		layout.setBackgroundColor(Color.WHITE);

		setContentView(layout); 

	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		public Panel(Context context) {
			super(context);
		}

		public void onDraw(Canvas canvas){
			canvas.drawColor(Color.WHITE);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}

	}

	//Timer class
	class MyCount extends CountDownTimer{
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
			timer.setText("done!");
		}
		@Override
		public void onTick(long millisUntilFinished) {
			timer.setText("Left: "+millisUntilFinished/1000);
		}
	}
}

