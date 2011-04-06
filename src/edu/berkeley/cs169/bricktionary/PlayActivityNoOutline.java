package edu.berkeley.cs169.bricktionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayActivityNoOutline extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlinepuzzle1);
		
		Button outlineButton = (Button) findViewById(R.id.PlayOutline1Button);
        outlineButton.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		Intent i = new Intent().setClass(PlayActivityNoOutline.this, PlayActivity.class);
        		startActivity(i);
            }
        });
	}

}