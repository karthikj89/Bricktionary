package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Bricktionary extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button playButton = (Button) findViewById(R.id.Button01);
        playButton.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		Intent i = new Intent().setClass(Bricktionary.this, OutlineActivity.class);
        		startActivity(i);
            }
        });
        
        Button editButton = (Button) findViewById(R.id.Button02);
        editButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent().setClass(Bricktionary.this, SettingsActivity.class);
        		startActivity(i);
			}
		});
        
        Button highScoreButton = (Button) findViewById(R.id.Button03);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent().setClass(Bricktionary.this, HighScoreActivity.class);
        		startActivity(i);
			}
		});
        
    }
    
}